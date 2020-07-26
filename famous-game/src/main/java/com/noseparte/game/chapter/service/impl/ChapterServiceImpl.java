package com.noseparte.game.chapter.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.AttrCode;
import com.noseparte.common.bean.ChapterBean;
import com.noseparte.common.bean.StateCode;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.resources.ChapterConf;
import com.noseparte.common.resources.GlobalVariableConf;
import com.noseparte.common.resources.OccupationConf;
import com.noseparte.game.chapter.entity.Chapter;
import com.noseparte.game.chapter.mongo.ChapterDao;
import com.noseparte.game.chapter.service.ChapterService;
import com.noseparte.game.item.HoldItem;
import com.noseparte.game.item.ItemMgr;
import com.noseparte.game.mission.service.MissionService;
import com.noseparte.game.occuption.service.OccupationService;
import com.noseparte.game.role.entity.Role;
import com.noseparte.game.role.service.RoleService;
import com.noseparte.game.school.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Resource
    private ChapterDao chapterDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private MissionService missionService;
    @Autowired
    private OccupationService occupationService;


    @Override
    public ErrorCode challenge(Long rid, Integer schooldId, Integer chapterId) {
        if (!ConfigManager.chapterConfMap.containsKey(chapterId)) {
            return ErrorCode.CHAPTER_ID_NOT_EXIST;
        }
        Chapter chapter = this.chapterDao.getChapterById(rid);
        if (null == chapter) {
            chapter = new Chapter();
            chapter.setRid(rid);
            chapter.setChapters(new HashMap<>(1));
            chapterDao.addChapter(chapter);
        }
        ChapterBean chapterBean = chapter.getChapters().get(chapterId);
        long now = System.currentTimeMillis();
        if (null == chapterBean) {
            chapterBean = new ChapterBean();
            chapterBean.setChapterId(chapterId);
            chapterBean.setBeginTime(now);
            chapterBean.setState(StateCode.IN_PROGRESS);
            chapter.getChapters().put(chapterId, chapterBean);
            this.chapterDao.updateChapter(chapter);
            return ErrorCode.SERVER_SUCCESS;
        }

        if (chapterBean.getState() == StateCode.COMPLETED) {
            return ErrorCode.CHAPTER_NOT_REPEAT_CHALLENG;
        }
        // 重置开始时间
        chapterBean.setBeginTime(now);

        this.chapterDao.updateChapter(chapter);

        return ErrorCode.SERVER_SUCCESS;
    }

    @Override
    public List<Integer> progressList(Long rid) {
        Chapter chapter = chapterDao.getChapterById(rid);
        if (null == chapter) {
            return null;
        }

        Map<Integer, ChapterBean> chapters = chapter.getChapters();
        if (null == chapters || chapters.size() == 0) {
            return null;
        }

        List<Integer> chapterIds = new ArrayList<>();
        for (Map.Entry<Integer, ChapterBean> entry : chapters.entrySet()) {
            if (entry.getValue().getState() == StateCode.COMPLETED) {
                chapterIds.add(entry.getKey());
            }
        }
        return chapterIds;
    }

    @Override
    public JSONObject challengeOver(Long rid, Integer schoolId, Integer chapterId, Integer state) {

        JSONObject jsonObject = new JSONObject();
        ErrorCode code = null;
        String drop = null;

        Role role = roleService.selectByRoleId(rid);
        if (Objects.isNull(role)) {
            code = ErrorCode.ACCOUNT_NOT_EXIST;
        }

        if (!ConfigManager.chapterConfMap.containsKey(chapterId)) {
            code = ErrorCode.CHAPTER_ID_NOT_EXIST;
        }
        ChapterConf chapterConf = ConfigManager.chapterConfMap.get(chapterId);

        Chapter chapter = this.chapterDao.getChapterById(rid);
        if (null == chapter) {
            code = ErrorCode.CLIENT_PARAMS_ERROR;
        }

        ChapterBean chapterBean = chapter.getChapters().get(chapterId);
        if (null == chapterBean) {
            code = ErrorCode.CLIENT_PARAMS_ERROR;
        }

        if (chapterBean.getState() != StateCode.IN_PROGRESS) {
            code = ErrorCode.CLIENT_PARAMS_ERROR;
        }

        long now = System.currentTimeMillis();

        GlobalVariableConf globalVariable = ConfigManager.globalVariableConfMap.get(1024);
        int minTime = Integer.parseInt(globalVariable.getValue());
        if (now - chapterBean.getBeginTime() < minTime) {
            code = ErrorCode.CHAPTER_BATTLE_TIME_SHORT;
        }

        chapterBean.setEndTime(now);

        //通关后解锁职业
        Map<Integer, OccupationConf> occupationConfMap = ConfigManager.occupationConfMap;
        Integer vocationId = chapterConf.getVocationId();
        if (Objects.nonNull(vocationId) && occupationConfMap.containsKey(vocationId)) {
            schoolService.addSchoolByRoleId(rid, vocationId);
        }

        boolean upgrade = false;
        // 发放奖励
        if (state == 0) {
            drop = chapterConf.getFailDrop();
            List<HoldItem> holdItems = ItemMgr.dropItem(drop);
            roleService.distributeAwardToActor(role, holdItems);
            for (HoldItem hold : holdItems) {
                if (hold.getCode() == AttrCode.EXP.value) {
                    upgrade = occupationService.upgrade(rid, schoolId, hold.getNum());
                }
            }
        } else {
            chapterBean.setState(StateCode.COMPLETED);
            drop = chapterConf.getDrop();
            List<HoldItem> holdItems = ItemMgr.dropItem(drop);
            roleService.distributeAwardToActor(role, holdItems);
            for (HoldItem hold : holdItems) {
                if (hold.getCode() == AttrCode.EXP.value) {
                    upgrade = occupationService.upgrade(rid, schoolId, hold.getNum());
                }
            }
        }
        //推送职业列表
//        if (upgrade) {
//            School actorSchool = schoolService.getSchoolByRoleId(rid);
//            sendMessage.send(rid, Resoult.ok(RegisterProtocol.SCHOOL_LIST_RESP).responseBody(actorSchool));
//        }
        chapter.getChapters().put(chapterId, chapterBean);

        chapterDao.updateChapter(chapter);

        code = ErrorCode.SERVER_SUCCESS;
        jsonObject.put("code", code);
        jsonObject.put("drop", drop);
        return jsonObject;
    }

    /**
     * 是否通关到该主线章节
     *
     * @param rid
     * @param chapterId
     * @param condition
     * @return
     */
    @Override
    public boolean hasPass(Long rid, Integer chapterId, Integer condition) {
        AtomicBoolean pass = new AtomicBoolean(false);
        Chapter actorChapter = chapterDao.getChapterById(rid);
        if (Objects.isNull(actorChapter) || Objects.isNull(actorChapter.getChapters())) {
            pass.set(false);
        }
        Map<Integer, ChapterBean> chapters = actorChapter.getChapters();
        int initChapterId = Integer.parseInt(ConfigManager.globalVariableConfMap.get(1034).getValue());
        if (chapterId == initChapterId) {
            if (chapters.get(chapterId).getState() == StateCode.IN_PROGRESS) {
                pass.set(false);
            }
            if (chapters.get(chapterId).getState() == StateCode.COMPLETED) {
                pass.set(true);
            }
        } else {
            if (chapters.containsKey(chapterId) && chapters.get(chapterId).getState() == StateCode.COMPLETED) {
                pass.set(true);
            } else {
                pass.set(false);
            }
        }
        return pass.get();
    }

    @Override
    public void initChapter(long rid) {
        Chapter chapter = chapterDao.getChapterById(rid);
        if (Objects.nonNull(chapter)) {
            return;
        }
        chapter = new Chapter();
        chapter.setRid(rid);
        Map<Integer, ChapterBean> chapters = new HashMap<>();
        String chapterId = ConfigManager.globalVariableConfMap.get(1034).getValue();
        ChapterBean chapterBean = new ChapterBean();
        chapterBean.setChapterId(Integer.parseInt(chapterId));
        long now = System.currentTimeMillis();
        chapterBean.setBeginTime(now);
        chapterBean.setState(StateCode.IN_PROGRESS);
        chapters.putIfAbsent(Integer.parseInt(chapterId), chapterBean);
        chapter.setChapters(chapters);
        chapterDao.addChapter(chapter);
    }

    @Override
    public int getActorChapterProgressMax(Long rid) {
        Chapter chapter = chapterDao.getChapterById(rid);
        Map<Integer, ChapterBean> chapters = chapter.getChapters();
        Optional<ChapterBean> chapterBean = chapters.values().stream()
                .filter(progress -> progress.getState().value() > StateCode.NOT_STARTED.value())
                .sorted(Comparator.reverseOrder()).findFirst();
        ChapterBean bean = chapterBean.isPresent() ? chapterBean.get() : null;
        int initChapterId = Integer.parseInt(ConfigManager.globalVariableConfMap.get(1034).getValue());
        if (bean.getChapterId() == initChapterId && bean.getState() != StateCode.COMPLETED) {
            return 0;
        }
        return bean.getChapterId() > 1000 ? bean.getChapterId() - 1000 : bean.getChapterId();
//        return bean.getChapterId();
    }

    // 获取LinkedHashMap中的头部元素（最早添加的元素）时间复杂度O(1)
    // {@linked https://blog.csdn.net/evilcry2012/article/details/84937322 } Java LinkedHashMap获取第一个元素和最后一个元素
    // linkedMap.entrySet().iterator().next().getValue();
    public static Map<Integer, ChapterConf> sortHashMap(Map<Integer, ChapterConf> hashMap) {
        Map<Integer, ChapterConf> sortedMap = new LinkedHashMap<>();
        List<Integer> list = new ArrayList<>();
        Iterator<Integer> item = hashMap.keySet().iterator();
        while (item.hasNext()) {
            list.add(item.next());
        }
        Collections.sort(list);
        Iterator<Integer> item2 = list.iterator();
        while (item2.hasNext()) {
            Integer next = item2.next();
            sortedMap.putIfAbsent(next, hashMap.get(next));
        }
        return sortedMap;
    }


}

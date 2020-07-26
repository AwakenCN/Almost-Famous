package com.noseparte.game.mission.service.impl;

import com.noseparte.common.bean.MissionBean;
import com.noseparte.common.bean.StateCode;
import com.noseparte.common.cache.RedissonUtils;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.global.KeyPrefix;
import com.noseparte.common.global.Misc;
import com.noseparte.common.global.Resoult;
import com.noseparte.common.resources.MissionConf;
import com.noseparte.common.resources.OccupationConf;
import com.noseparte.common.utils.DateUtils;
import com.noseparte.game.base.RegisterProtocol;
import com.noseparte.game.base.SendMessage;
import com.noseparte.game.card.service.CardService;
import com.noseparte.game.chapter.service.ChapterService;
import com.noseparte.game.item.HoldItem;
import com.noseparte.game.item.ItemMgr;
import com.noseparte.game.mission.entity.Mission;
import com.noseparte.game.mission.mongo.MissionDao;
import com.noseparte.game.mission.service.MissionService;
import com.noseparte.game.pack.service.BagCardStrategyService;
import com.noseparte.game.role.entity.Role;
import com.noseparte.game.role.service.RoleService;
import com.noseparte.game.school.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IMissionServiceImpl implements MissionService {

    @Resource
    MissionDao missionDao;
    @Resource
    RoleService roleService;
    @Autowired
    ChapterService iChapterService;
    @Autowired
    SchoolService iSchoolService;
    @Autowired
    CardService iCardPackageService;
    @Autowired
    SendMessage sendMessage;
    @Autowired
    BagCardStrategyService bagCardService;
    @Resource
    RedissonClient redissonClient;


    @Override
    public void initMission(Long rid) {
        Mission mission = missionDao.getActorMissionsByRole(rid);
        Map<Integer, MissionBean> groups = new HashMap<>();
        if (Objects.nonNull(mission)) {
            return;
        }
        // 从配置表中获取任务列表
        Map<Integer, MissionConf> missions = ConfigManager.missionConfMap;
        if (Objects.nonNull(missions) && !missions.isEmpty()) {
            MissionBean bean = null;
            for (MissionConf miss : missions.values()) {
                bean = new MissionBean();
                bean.setStatus(StateCode.NOT_STARTED.value());
                bean.setMissionId(miss.getId());
                bean.setIsShow(1);
                groups.putIfAbsent(miss.getId(), bean);
            }
            mission = new Mission();
            mission.setRid(rid);
            mission.setMissions(groups);
            missionDao.insertActorMission(mission);
        }
    }

    @Override
    public Mission getMission(Long rid) {
        Mission mission = RedissonUtils.get(redissonClient, KeyPrefix.GameCoreRedisPrefix.CACHE_MISSION + rid, Mission.class);
        if (null != mission) {
            return mission;
        }
        mission = missionDao.getActorMissionsByRole(rid);
        if (null != mission) {
            try {
                if (RedissonUtils.lock(redissonClient, KeyPrefix.GameCoreRedisPrefix.CACHE_MISSION + rid)) {
                    RedissonUtils.set(mission, redissonClient, KeyPrefix.GameCoreRedisPrefix.CACHE_MISSION + rid, KeyPrefix.GameCoreRedisPrefix.CACHE_MISSION_EXPIRE_TIME);
                }
            } finally {
                RedissonUtils.unlock(redissonClient, KeyPrefix.GameCoreRedisPrefix.CACHE_MISSION + rid);
            }
        }
        return mission;

    }

    @Override
    public Mission getCurrentMission(Long rid) {
        Mission mission = this.getMission(rid);
        // 判定第几天的任务
        // 开服时间
        Long OnLineTime = Objects.requireNonNull(DateUtils.stringToDate(Misc.getStringVariable(1055), DateUtils.DATE_PATTERN)).getTime();
        long current = System.currentTimeMillis();
        long remaining = current - OnLineTime;
        int today = remaining < 0L ? -1 : (int) (remaining / (3600*1000*24));

        List<MissionBean> missionBeanList = new ArrayList<>();
        for(MissionBean missionBean : mission.getMissions().values()){
            // 第几日的任务
            int whichDay = missionBean.getMissionId() / 100;
            if(missionBean.getIsShow() == 1){
                if(today + 1 > 0){
                    if(today + 1 >= whichDay){
                        missionBeanList.add(missionBean);
                    }
                }
            }
        }
        Collections.sort(missionBeanList);

        Map<Integer, MissionBean> resultMap = new LinkedHashMap<>(3);
        if (missionBeanList.size() > MissionBean.MAX) {
            List<MissionBean> missions = missionBeanList.subList(MissionBean.MIN, MissionBean.MAX);
            missions.forEach(item -> resultMap.putIfAbsent(item.getMissionId(), item));
            mission.setMissions(resultMap);
        }else {
            missionBeanList.forEach(item -> resultMap.putIfAbsent(item.getMissionId(), item));
            mission.setMissions(resultMap);
        }
        return mission;
    }

    @Override
    public void completionMission(Long rid, MissionBean bean) {
        Mission mission = this.getMission(rid);
        bean.setStatus(StateCode.IN_PROGRESS.value());
        mission.getMissions().put(bean.getMissionId(), bean);
        this.updateMission(mission);
    }

    @Override
    public boolean addMission(Mission mission) {
        boolean success = missionDao.insertActorMission(mission);
        if (success) {
            cacheMission(mission);
        }
        return success;
    }

    @Override
    public boolean updateMission(Mission mission) {
        boolean success = missionDao.updateActorMission(mission);
        if (success) {
            cacheMission(mission);
        }
        return success;
    }

    private void cacheMission(Mission mission) {
        Long rid = mission.getRid();
        String chapter_lock_key = KeyPrefix.GameCoreRedisPrefix.CACHE_MISSION + rid;
        try {
            if (RedissonUtils.lock(redissonClient, chapter_lock_key)) {
                RedissonUtils.set(mission, redissonClient, chapter_lock_key, KeyPrefix.GameCoreRedisPrefix.CACHE_MISSION_EXPIRE_TIME);
            }
        } finally {
            RedissonUtils.unlock(redissonClient, chapter_lock_key);
        }
    }

    @Override
    public Mission actorMissionMgr(Role role, Mission mission, Integer missionType, Integer model) {
        Long rid = role.getRid();
        // 从配置表中获取任务列表
        Map<Integer, MissionConf> documents = ConfigManager.missionConfMap;
        List<MissionConf> missionList = documents.values().stream()
                .filter(missionConf -> missionConf.getType().equals(missionType))
                .collect(Collectors.toList());

        List<MissionBean> roleMissionQueue = mission.getMissions().values().stream()
                .filter(roleMission -> roleMission.getType() == missionType && roleMission.getIsShow() == 1)
                .collect(Collectors.toList());

        boolean pass = false;
        for (MissionBean bean : roleMissionQueue) {
            MissionConf conf = missionList.stream().filter(miss -> miss.getId() == bean.getMissionId()).findFirst().get();
            Integer subType = conf.getSubType();
            switch (missionType) {
                case MissionBean.MAIN_LEVEL_PROGRESS:
                    // 关卡主线进度
                    pass = iChapterService.hasPass(rid, subType, conf.getCondition());
                    bean.setCondition(iChapterService.getActorChapterProgressMax(rid));
                    break;
                case MissionBean.GRADE_OF_OCCUPATIONS:
                    // 某职业等级
                    pass = iSchoolService.upgradeSucceed(rid, subType, conf.getCondition());
                    bean.setCondition(iSchoolService.getSchoolLevelByRoleId(rid, subType));
                    break;
                case MissionBean.TOTAL_GRADE_OCCUPATIONS:
                    // 总等级
                    pass = iSchoolService.gltTotalLevel(rid, conf.getCondition());
                    bean.setCondition(iSchoolService.getAlmostSchoolLevel(rid));
                    break;
                case MissionBean.NUMBER_OF_OCCUPATION:
                    // 某职业对战次数
                    Integer number = Misc.increase(bean.getCondition(), 1);
                    Map<Integer, OccupationConf> occupationConfMap = ConfigManager.occupationConfMap;
                    if (role.getLastBattleSchool() != null) {
                        Integer occupation = occupationConfMap.get(role.getLastBattleSchool()).getOccupation();
                        pass = number >= conf.getCondition() && conf.getSubType().equals(occupation);
                        isCompleted(mission, occupation, pass, bean, conf, number);
                    }
                    break;
                case MissionBean.NUMBER_OF_PARTICULAR_MODE:
                    // 某模式对战次数
                    Integer progress = Misc.increase(bean.getCondition(), 1);
                    pass = progress >= conf.getCondition();
                    if (conf.getSubType() == 0) {
                        bean.setCondition(progress);
                        if (!pass) {
                            mission.getMissions().putIfAbsent(conf.getId(), bean);
                            this.updateMission(mission);
                        }
                    } else {
                        isCompleted(mission, model, pass, bean, conf, progress);
                    }
                    break;
                case MissionBean.DRAW_CARD:
                    // 抽卡
                    pass = bagCardService.compareSelectCnt(rid, conf.getCondition());
                    bean.setCondition(bagCardService.getSelectCardTimes(rid));
                    break;
                case MissionBean.PURCHASE_CARD_PACKAGE:
                    // 购买卡包
                    pass = bagCardService.totalBuyCount(rid, conf.getCondition());
                    bean.setCondition(bagCardService.getTotalBuyCount(rid));
                    break;
                case MissionBean.DAN_GRADING:
                    // 是否达到某个段位
                    pass = roleService.reachGrading(rid, conf.getSubType(), conf.getCondition());
                    bean.setCondition(roleService.reachGoal(rid, conf.getSubType(), conf.getCondition()));
                    break;
                case MissionBean.NUMBER_OF_OCCUPATION_WINNER:
                    // 使用某职业进行对战并获胜
                    Integer winCount = Misc.increase(bean.getCondition(), 1);
                    pass = winCount >= conf.getCondition();
                    Map<Integer, OccupationConf> occupationMap = ConfigManager.occupationConfMap;
                    if (role.getLastBattleSchool() != null) {
                        Integer actor = occupationMap.get(role.getLastBattleSchool()).getOccupation();
                        pass = winCount >= conf.getCondition() && conf.getSubType().equals(actor);
                        isCompleted(mission, actor, pass, bean, conf, winCount);
                    }
                    break;
                default:
                    log.error(ErrorCode.SERVER_ERROR.name(), "未知任务类型");
                    break;
            }
            if (pass) {
                completionMission(rid, bean);
            }
        }
        return mission;
    }

    private void isCompleted(Mission mission, Integer model, boolean pass, MissionBean bean, MissionConf conf, Integer progress) {
        if (conf.getSubType().equals(model)) {
            bean.setCondition(progress);
            if (!pass) {
                mission.getMissions().putIfAbsent(conf.getId(), bean);
                this.updateMission(mission);
            }
        }
    }

    @Override
    public ErrorCode receiveAwardMission(Long rid, Integer missionId) {
        Mission mission = this.getMission(rid);
        if (Objects.isNull(mission)) {
            return ErrorCode.MISSION_NOT_EXIST;
        }
        Role role = roleService.selectByRoleId(rid);
        Map<Integer, MissionConf> missionConfMap = ConfigManager.missionConfMap;
        // 任务队列中移除该任务
        MissionConf missionConf = missionConfMap.get(missionId);
        MissionBean bean = mission.getMissions().get(missionId);
        if (bean.getIsShow() == 0) {
            return ErrorCode.MISSION_REWARD_RECEIVED;
        }
        bean.setStatus(StateCode.COMPLETED.value());
        bean.setIsShow(0);
        bean.setCompleteTick(System.currentTimeMillis());
        updateMission(mission);
        List<HoldItem> holdItems = ItemMgr.dropItem(missionConf.getDrop());
        // 获取drop的信息
        roleService.distributeAwardToActor(role, holdItems);
        return ErrorCode.SERVER_SUCCESS;
    }


}
package com.noseparte.game.mission.service.impl;

import com.noseparte.common.bean.MissionBean;
import com.noseparte.common.bean.StateCode;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.global.Resoult;
import com.noseparte.common.resources.MissionConf;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    public Mission getActorMissionById(Long rid) {
        Mission actor = missionDao.getActorMissionsByRole(rid);
        return Optional.ofNullable(actor).orElse(null);
    }

    @Override
    public void updateActorMission(Long rid, Integer missionId, boolean pass) {
        Mission mission = missionDao.getActorMissionsByRole(rid);
        if (pass) {
            MissionBean missionBean = mission.getMissions().values()
                    .stream().filter(miss -> miss.getMissionId() == missionId)
                    .findFirst().get();
            missionBean.setStatus(StateCode.IN_PROGRESS.value());
            mission.getMissions().putIfAbsent(missionId, missionBean);
            missionDao.updateActorMission(mission);
        }
    }

    @Override
    public void updateRoleMission(Mission mission) {
        missionDao.updateActorMission(mission);
    }

    @Override
    public Mission actorMissionMgr(Mission mission, Role role) {
        Long rid = role.getRid();
        Map<Integer, MissionBean> actorMissions = new ConcurrentHashMap<>();
        // 从配置表中获取任务列表
        Map<Integer, MissionConf> documents = ConfigManager.missionConfMap;
        Map<Integer, MissionBean> roleQueue = mission.getMissions();

        boolean pass = false;
        for (MissionConf conf : documents.values()) {
            for (MissionBean bean : roleQueue.values()) {
                if (bean.getMissionId() == conf.getId() && bean.getIsShow() == 1) {
                    Integer type = conf.getType();
                    Integer subType = conf.getSubType();
                    switch (type) {
                        case MissionBean.MAIN_LEVEL_PROGRESS:
                            pass = iChapterService.hasPass(rid, subType, conf.getCondition());
                            bean.setCondition(iChapterService.getActorChapterProgressMax(rid));
                            break;
                        case MissionBean.GRADE_OF_OCCUPATIONS:
                            // 某职业等级
                            pass = iSchoolService.upgradeSuccessed(rid, subType, conf.getCondition());
                            bean.setCondition(iSchoolService.getSchoolLevelByRoleId(rid, subType));
                            break;
                        case MissionBean.TOTAL_GRADE_OCCUPATIONS:
                            // 总等级
                            pass = iSchoolService.gltTotalLevel(rid, conf.getCondition());
                            bean.setCondition(iSchoolService.getAlmostSchoolLevel(rid));
                            break;
                        case MissionBean.NUMBER_OF_OCCUPATION:
                            break;
                        case MissionBean.NUMBER_OF_PARTICULAR_MODE:
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
                            break;
                        default:
                            log.error(ErrorCode.SERVER_ERROR.name(), "未知任务类型");
                    }
                    if (pass) {
                        updateActorMission(rid, conf.getId(), pass);
                    }
                    actorMissions.putIfAbsent(conf.getId(), bean);
                }
            }
        }
        List<MissionBean> list = new ArrayList<MissionBean>(actorMissions.values());
        list.sort(MissionBean::compareTo);
        if (list.size() > MissionBean.MAX) {
            Map<Integer, MissionBean> resultMap = new LinkedHashMap<>(3);
            List<MissionBean> missionConfs = list.subList(MissionBean.MIN, MissionBean.MAX);
            missionConfs.forEach(item -> {
                resultMap.putIfAbsent(item.getMissionId(), item);
            });
            mission.setMissions(resultMap);
        } else {
            mission.setMissions(actorMissions);
        }
        return mission;
    }

    @Override
    public void noticeMission(Long rid) {
        Role role = roleService.selectByRoleId(rid);
        Mission mission = getActorMissionById(rid);
        Mission currentMission = actorMissionMgr(mission, role);
        Map<Integer, MissionBean> missions = currentMission.getMissions();
        if (Objects.isNull(missions)) {
            return;
        }
        //任务推送
        for (MissionBean bean : missions.values()) {
            if (bean.getStatus() == StateCode.IN_PROGRESS.value()) {
                sendMessage.send(rid, Resoult.ok(RegisterProtocol.MISSION_ACTOR_LIST_RESP).responseBody(currentMission));
                break;
            }
        }
    }

    @Override
    public ErrorCode receivedMission(Long rid, Integer missionId) {
        Mission mission = missionDao.getActorMissionsByRole(rid);
        if (Objects.isNull(mission)) {
            return ErrorCode.MISSION_NOT_EXIST;
        }
        if (Objects.nonNull(mission)) {
            Role role = roleService.selectByRoleId(rid);
            Map<Integer, MissionConf> missionConfMap = ConfigManager.missionConfMap;
            // 任务队列中移除该任务
            MissionConf missionConf = missionConfMap.values()
                    .stream().filter(conf -> conf.getId() == missionId).findFirst().get();
            MissionBean bean = mission.getMissions().get(missionId);
            if (bean.getIsShow() == 0) {
                return ErrorCode.MISSION_REWARD_RECEIVED;
            }
            bean.setIsShow(0);
            bean.setCompleteTick(new Date().getTime());
            updateRoleMission(mission);
            List<HoldItem> holdItems = ItemMgr.dropItem(missionConf.getDrop());
            // 获取drop的信息
            roleService.distributeAwardToActor(role, holdItems);
        }
        return ErrorCode.SERVER_SUCCESS;
    }

}
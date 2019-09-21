package com.noseparte.game.role.service.impl;

import com.noseparte.common.bean.AttrCode;
import com.noseparte.common.bean.BattleRankBean;
import com.noseparte.common.bean.CardBean;
import com.noseparte.common.bean.EventCode;
import com.noseparte.common.bean.protocol.RoleBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.resources.BattleRankConf;
import com.noseparte.game.base.SendMessage;
import com.noseparte.game.card.entity.Card;
import com.noseparte.game.card.service.CardService;
import com.noseparte.game.item.HoldItem;
import com.noseparte.game.mission.service.MissionService;
import com.noseparte.game.occuption.service.OccupationService;
import com.noseparte.game.pack.entity.ActorBag;
import com.noseparte.game.pack.service.BagCardStrategyService;
import com.noseparte.game.role.entity.Role;
import com.noseparte.game.role.mongo.RoleDao;
import com.noseparte.game.role.service.RoleService;
import com.noseparte.game.school.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author Noseparte
 * @date 2019/8/20 18:20
 * @Description
 */
@Slf4j
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleDao roleDao;
    @Resource
    CardService cardService;
    @Resource
    SchoolService schoolService;
    @Resource
    OccupationService occupationService;
    @Resource
    BagCardStrategyService iBagCardStrategyService;
    @Resource
    MissionService missionService;
    @Autowired
    private SendMessage sendMessage;

    @Override
    public Role selectById(Long uid) {
        return roleDao.selectById(uid);
    }

    @Override
    public Role selectByRoleId(Long rid) {
        return roleDao.selectByRoleId(rid);
    }

    @Override
    public void addRole(Role role) {
        roleDao.addRole(role);
    }

    @Override
    public void updateByRoleId(Role role) {
        roleDao.updateByRoleId(role);
    }

    @Override
    public int cost(Role role, AttrCode attrCode, long value, EventCode eventCode) {
        if (value >= 0)
            return 0;
        return attrFace(role, attrCode, value);
    }

    @Override
    public int add(Role role, AttrCode attrCode, long value, EventCode eventCode) {
        if (value <= 0)
            return 0;
        return attrFace(role, attrCode, value);
    }

    @Override
    public int newcomerGuide(Role role, int guideId) {
        role.setNewcomerGuide(guideId);
        return 0;
    }

    private int gold(Role role, long value) {
        long currValue = role.getGold();
        currValue += value;
        if (currValue >= 0) {
            role.setGold(currValue);
            roleDao.updateByRoleId(role);
            return 1;
        }
        return -1;
    }

    private int silver(Role role, long value) {
        long currValue = role.getSilver();
        currValue += value;
        if (currValue >= 0) {
            role.setSilver(currValue);
            roleDao.updateByRoleId(role);
            return 1;
        }
        return -1;

    }

    private int diamond(Role role, long value) {
        long currValue = role.getDiamond();
        currValue += value;
        if (currValue >= 0) {
            role.setDiamond(currValue);
            roleDao.updateByRoleId(role);
            return 1;
        }
        return -1;
    }

    private int attrFace(Role role, AttrCode attrCode, long value) {
        switch (attrCode) {
            case GOLD:
                return gold(role, value);
            case SILVER:
                return silver(role, value);
            case DIAMOND:
                return diamond(role, value);
        }
        return 0;
    }

    /**
     * 发放奖励
     *
     * @param holdItems
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean distributeAwardToActor(Role role, List<HoldItem> holdItems) {

        ExecutorService executorService = Executors.newCachedThreadPool();
        FutureTask task = new FutureTask(new ActorReward(role, holdItems));
        Future<?> future = executorService.submit(task);
        try {
            Boolean success = (Boolean) future.get();
            return success;
        } catch (InterruptedException e) {
            log.error("InterruptedException, {}", e.getMessage());
            e.printStackTrace();
        } catch (ExecutionException e) {
            log.error("ExecutionException, {}", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键
        }
        return false;
    }

    // 多线程清算
    protected class ActorReward implements Callable {

        public Role role;
        public List<HoldItem> holdItems;

        public ActorReward(Role role, List<HoldItem> holdItems) {
            this.role = role;
            this.holdItems = holdItems;
        }

        @Override
        public Object call() {
            if (Objects.isNull(role) || holdItems.isEmpty()) {
                return false;
            }
            Long rid = role.getRid();
            try {
                for (HoldItem item : holdItems) {
                    // 货币的发放
                    if (item.getCode() == AttrCode.GOLD.value) {
                        add(role, AttrCode.GOLD, item.getNum(), EventCode.SIGN_ADD_MONEY);
                    }
                    if (item.getCode() == AttrCode.SILVER.value) {
                        add(role, AttrCode.SILVER, item.getNum(), EventCode.SIGN_ADD_MONEY);
                    }
                    if (item.getCode() == AttrCode.DIAMOND.value) {
                        add(role, AttrCode.DIAMOND, item.getNum(), EventCode.SIGN_ADD_MONEY);
                    }
                    if (item.getCode() == AttrCode.EXP.value) {
                        // 等级
//                        boolean upgrade = occupationService.upgrade(rid, item.getItemId(), item.getNum());

                        //任务推送
//                        missionService.noticeMission(rid);
                    }
                    if (item.getCode() == AttrCode.CARD.value) {
                        // 卡牌
                        Card cardPackage = cardService.getCardById(role.getRid());
                        CardBean card = new CardBean();
                        card.setCardId(item.getItemId());
                        card.setNum(item.getNum());
                        cardPackage.getCards().putIfAbsent(item.getItemId(), card);
                        cardService.updateCard(cardPackage);
                    }
                    if (item.getCode() == AttrCode.ITEM.value) {
                        // 道具
                        ActorBag actorBag = iBagCardStrategyService.getSpecificBackpack(rid);
                        iBagCardStrategyService.putItem(actorBag, item);
                    }
                    if (item.getCode() == AttrCode.CARD_PACKAGE.value) {
                        // 卡包
                        ActorBag actorBag = iBagCardStrategyService.getSpecificBackpack(rid);
                        iBagCardStrategyService.putItem(actorBag, item);
                    }
                    if (item.getCode() == AttrCode.PACKAGES.value) {
                        // 礼包
                        ActorBag actorBag = iBagCardStrategyService.getSpecificBackpack(rid);
                        iBagCardStrategyService.putItem(actorBag, item);
                    }
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override
    public ErrorCode battleStart(long roomId, List<Long> winners, List<Long> losers) {
        return ErrorCode.SERVER_SUCCESS;
    }

    @Override
    public ErrorCode battleEnd(long roomId, List<Long> winners, List<Long> losers) {
        for (Long winnerId : winners) {
            Role winner = roleDao.selectByRoleId(winnerId);
            if (Objects.isNull(winner)) {
                return ErrorCode.ACCOUNT_NOT_EXIST;
            }
            changeDanGrading(winner, Boolean.TRUE);
        }
        for (Long loserId : losers) {
            Role loser = roleDao.selectByRoleId(loserId);
            if (Objects.isNull(loser)) {
                return ErrorCode.ACCOUNT_NOT_EXIST;
            }
            changeDanGrading(loser, Boolean.FALSE);
        }

        return ErrorCode.SERVER_SUCCESS;
    }

    @Override
    public ErrorCode batchBattleHistory(long roomId, long winnerId, long loserId) {
        Role winner = roleDao.selectByRoleId(winnerId);
        Role loser = roleDao.selectByRoleId(loserId);
        if (Objects.isNull(winner) || Objects.isNull(loser)) {
            return ErrorCode.ACCOUNT_NOT_EXIST;
        }
        changeDanGrading(winner, Boolean.TRUE);
        changeDanGrading(loser, Boolean.FALSE);
        return ErrorCode.SERVER_SUCCESS;
    }

    @Override
    public void currencyChangeGM(Role role) {
        add(role, AttrCode.GOLD, RoleBean.CURRENCY, EventCode.GM_ADD_MONEY);
        add(role, AttrCode.SILVER, RoleBean.CURRENCY, EventCode.GM_ADD_MONEY);
        add(role, AttrCode.DIAMOND, RoleBean.CURRENCY, EventCode.GM_ADD_MONEY);
    }

    /**
     * 玩家段位变化
     *
     * @param role
     * @param upOrDown
     */
    protected void changeDanGrading(Role role, boolean upOrDown) {
        BattleRankBean battleRank = role.getBattleRank();
        Map<Integer, BattleRankConf> battleRankConfMap = ConfigManager.battleRankConfMap;
        BattleRankConf battleRankConf = battleRankConfMap.get(battleRank.getRankId());
        Integer star = battleRankConf.getId();// 段位ID
        Integer isProtect = battleRankConf.getIsProtect();// 是否有掉星保护
        Integer starLimit = battleRankConf.getStarLimit();// 星级上限
        if (upOrDown) { //胜利方
            if (battleRank.getStarCount() >= starLimit) {// 升段
                battleRank.setRankId(star + 1);
                battleRank.setStarCount(1);// 初始为1星
            } else {
                battleRank.setStarCount(battleRank.getStarCount() + 1);
            }
        } else { //失败方
            if (Objects.isNull(isProtect) || isProtect == 0) {// 默认为无保护
                if (isProtect == 2) {// 掉星或者掉段
                    if (battleRank.getStarCount() > 1) {
                        battleRank.setStarCount(battleRank.getStarCount() - 1);
                    } else {
                        battleRank.setRankId(star - 1);
                        BattleRankConf preBattleRank = battleRankConfMap.get(star - 1);
                        battleRank.setStarCount(preBattleRank.getStarLimit());
                    }
                }
            }
        }
        role.setBattleRank(battleRank);
        updateByRoleId(role);  //同步
    }

}

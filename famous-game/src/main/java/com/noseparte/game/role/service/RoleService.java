package com.noseparte.game.role.service;

import com.noseparte.common.bean.AttrCode;
import com.noseparte.common.bean.EventCode;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.game.item.HoldItem;
import com.noseparte.game.role.entity.Role;

import java.util.List;

public interface RoleService {

    Role selectById(Long uid);

    Role selectByRoleId(Long rid);

    void addRole(Role role);

    void updateByRoleId(Role role);

    /**
     * 扣除货币或经验
     *
     * @return 0无变化，1成功，-1失败
     */
    int cost(Role role, AttrCode attrCode, long value, EventCode eventCode);

    /**
     * 增加货币或经验
     */
    int add(Role role, AttrCode attrCode, long value, EventCode eventCode);

    int newcomerGuide(Role role, int progressId);

    boolean distributeAwardToActor(Role role, List<HoldItem> holdItems);

    ErrorCode battleStart(long roomId, List<Long> winners, List<Long> losers);

    ErrorCode battleEnd(long roomId, List<Long> winners, List<Long> losers);

    ErrorCode batchBattleHistory(long roomId, long winnerId, long loserId);

    void currencyChangeGM(Role role);

}

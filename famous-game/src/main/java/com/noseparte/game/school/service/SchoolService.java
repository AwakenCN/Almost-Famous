package com.noseparte.game.school.service;

import com.noseparte.common.bean.CardGroup;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.game.school.entity.School;

import java.util.Map;

/**
 * <p>
 * 职业 服务类
 * </p>
 *
 * @author liang
 * @since 2019-04-18
 */
public interface SchoolService {

    /**
     * 给角色添加职业
     *
     * @param rid      角色id
     * @param schoolId 职业id
     * @return
     */
    boolean addSchoolByRoleId(long rid, int schoolId);

    /**
     * 获取某个角色的所有职业列表
     *
     * @param rid 角色id
     */
    School getSchoolByRoleId(long rid);

    /**
     * 创建或更新卡组
     *
     * @return 返回状态码
     */
    ErrorCode updateCardGroup(long rid, int schoolId, CardGroup cardGroup);

    ErrorCode deleteCardGroup(long rid, int schoolId, long groupId);

    public CardGroup getCardGroupById(Map<Long, CardGroup> cardGroupMap, long groupId);

    boolean upgradeSuccessed(Long rid, Integer schoolId, Integer condition);

    boolean gltTotalLevel(Long rid, Integer condition);

    int getSchoolLevelByRoleId(Long rid, Integer schoolId);

    int getAlmostSchoolLevel(Long rid);

    ErrorCode verifyCardGroup(Long rid);

    /**
     * 查询某个玩家某个职业下的 卡组
     *
     * @param rid
     * @param schoolId
     * @param groupId
     * @return
     */
    CardGroup getOneCardGroup(Long rid, Integer schoolId, Long groupId);
}

package com.noseparte.game.school.service.impl;

import com.noseparte.common.bean.CardGroup;
import com.noseparte.common.bean.SchoolBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.global.Misc;
import com.noseparte.common.resources.CardConf;
import com.noseparte.common.resources.OccupationConf;
import com.noseparte.common.resources.SchoolInitConf;
import com.noseparte.common.rpc.RpcClient;
import com.noseparte.game.base.SendMessage;
import com.noseparte.game.card.entity.Card;
import com.noseparte.game.card.service.CardService;
import com.noseparte.game.role.entity.Role;
import com.noseparte.game.role.service.RoleService;
import com.noseparte.game.school.entity.School;
import com.noseparte.game.school.mongo.SchoolDao;
import com.noseparte.game.school.service.SchoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 职业 服务实现类
 * </p>
 *
 * @author liang
 * @since 2019-04-18
 */
@Service
public class SchoolServiceImpl implements SchoolService {

    private static Logger LOG = LoggerFactory.getLogger("GameCore");

    @Resource
    RpcClient rpcClient;
    @Autowired
    RoleService roleService;
    @Resource
    SchoolDao schoolDao;
    @Autowired
    CardService cardService;
    @Autowired
    SendMessage sendMessage;

    @Override
    public boolean addSchoolByRoleId(long rid, int schoolId) {
        School school = this.getSchoolByRoleId(rid);
        // 新建角色添加职业
        if (null == school) {
            Map<Integer, SchoolBean> map = new HashMap<>(1);
            newSchool(map, rid, schoolId);
            school = new School();
            school.setRid(rid);
            school.setSchools(map);
            return schoolDao.addSchool(school);
        }
        // 已有角色添加职业
        Map<Integer, SchoolBean> map = school.getSchools();
        if (!newSchool(map, rid, schoolId)) {
            return false;
        }
        return schoolDao.upSchool(school);
    }

    @Override
    public School getSchoolByRoleId(long rid) {
        return schoolDao.getSchool(rid);
    }

    @Override
    public ErrorCode updateCardGroup(long rid, int schoolId, CardGroup cardGroup) {
        Role role = roleService.selectByRoleId(rid);
        if (Objects.isNull(role)) {
            return ErrorCode.ACCOUNT_NOT_EXIST;
        }

        School school = getSchoolByRoleId(rid);
        Map<Integer, SchoolBean> schoolBeanMap = school.getSchools();
        // 验证职业
        if (!schoolBeanMap.containsKey(schoolId)) {
            LOG.error("角色{}，职业{}不存在", rid, schoolId);
            return ErrorCode.SCHOOL_NOT_EXIST;
        }

        // 职业所有卡组
        SchoolBean schoolBean = schoolBeanMap.get(schoolId);
        Map<Long, CardGroup> cardGroupMap = schoolBean.getCardGroup();

        // 没有id是新增卡组
        long groupId = cardGroup.getId();

        // 检查卡组名是否重复
        // 校验敏感字
        String cardGroupName = cardGroup.getName();
        //卡组名称不能为空
        if (Objects.isNull(cardGroupName)) {
            return ErrorCode.CARD_GROUP_NAME_NOT_EXIST;
        }
//        boolean sensitive = SensitiveWordFilter.isContaintBadWord(cardGroupName, minMatchTYpe);
//        if (sensitive) {
//            return ErrorCode.CARD_GROUP_NAME_ERROR;
//        }
//        CardGroup c = cardGroupMap.get(cardGroupName);
//        if (c != null) {
//            if (c.getId() != groupId) {
//                LOG.error("角色{}，卡组名称{}重复", rid, cardGroupName);
//                return ErrorCode.CARD_GROUP_NAME_ERROR;
//            }
//        }

        if (groupId < 0) {
            LOG.error("角色{}，卡组id不合法.", rid, cardGroupName);
            return ErrorCode.CARD_GROUP_ID_ERROR;
        }

        if (groupId == 0) {
            cardGroup.setId(rpcClient.getUniqueId());
            if (cardGroupMap.size() >= 5) {
                return ErrorCode.CARD_GROUP_SIZE_OVERFLOW;
            }
        }

        /**已组装*/
        List<String> useCards = cardGroup.getUseCards();
        // 卡牌模组
        String module = ConfigManager.globalVariableConfMap.get(1022).getValue();
        // 底板
        String[] baseboard = Misc.split(module, "\\,");
        boolean verified = verifyAssembleRegular(schoolId, cardGroup, useCards, baseboard);
        if (verified) {
            LOG.info("已组装的卡牌验证通过");
            /**待组装*/
            List<Integer> checkCards = cardGroup.getCheckCards();
            int min_deck = Integer.parseInt(ConfigManager.globalVariableConfMap.get(1037).getValue());
            cardGroup.setPerfection(useCards.size() >= min_deck ? 0 : 1);
            cardGroupMap.put(cardGroup.getId(), cardGroup);
        } else {
            return ErrorCode.CARD_GROUP_ASSEMBLE_ERROR;
        }

        schoolBean.setCardGroup(cardGroupMap);
        schoolBeanMap.put(schoolId, schoolBean);
        school.setSchools(schoolBeanMap);
        return schoolDao.upSchool(school) ? ErrorCode.SERVER_SUCCESS : ErrorCode.SERVER_ERROR;
    }

    /**
     * condition1:  有颜色的方块必须放在黑色格子上
     * condition2:  模组不能重叠
     * condition3:  模组不能超出底板边界
     */
    protected boolean verifyAssembleRegular(int schoolId, CardGroup cardGroup, List<String> useCards, String[] baseboard) {
        LOG.info("useCards: {}, baseboard: {}", useCards, baseboard);
        Map<Integer, Integer> cardLocation = new LinkedHashMap<>();
        int agi = cardGroup.getAgi();
        int iq = cardGroup.getIq();
        int str = cardGroup.getStr();

        Map<Integer, OccupationConf> occupationConfMap = ConfigManager.occupationConfMap;
        OccupationConf occupationConf = occupationConfMap.get(schoolId);
        Integer agility = occupationConf.getAgility();
        Integer intelligence = occupationConf.getIntelligence();
        Integer strength = occupationConf.getStrength();

        for (String card : useCards) {
            String[] pos = Misc.split(card, "\\,");
            cardLocation.putIfAbsent(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]));
        }
        // 模组不能超出底板边界
        if (cardLocation.size() > baseboard.length) {
            return false;
        }
        // 模组不能重叠
        Set<Integer> items = cardLocation.keySet();
        Map<Integer, String> cloneItems = null;
        for (Integer cardId : items) {
            cloneItems = new ConcurrentHashMap<>();
            Map<Integer, CardConf> cardMap = ConfigManager.cardConfMap;
            CardConf card = cardMap.get(cardId);
            String module = card.getModule();
            String quotes = Misc.cutQuotes(module);
            cloneItems.putIfAbsent(cardId, quotes);
            //   1.	所有卡牌模组都不超出5*5方格范围
            //   2.	格子数量最少为1，最大为25
            for (String moudule : cloneItems.values()) {
                String[] modules = Misc.split(Misc.cutQuotes(moudule), "\\;");
                for (int i = 0; i < modules.length; i++) {
                    String[] cols = Misc.split(modules[i], "\\,");
                    int size = modules.length * cols.length;
                    // 模组不能超出底板边界
                    if (size > 25) {
                        return false;
                    }
                    for (int j = 0; j < cols.length; j++) {
                        int index = cardLocation.get(cardId) + i * 10 + j;
                        int color = Integer.parseInt(cols[j]);
                        // 有颜色的方块必须放在黑色格子上
                        if (Objects.nonNull(card.getVocation())) {
                            if (card.getVocation() > 0 && card.getVocation() == color) {
                                int board = Integer.parseInt(baseboard[index]);
                                if (board == 0) {
                                    continue;
                                } else {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
//            card.get
        }
        return true;
    }

    @Override
    public ErrorCode deleteCardGroup(long rid, int schoolId, long groupId) {
        School school = getSchoolByRoleId(rid);
        Map<Integer, SchoolBean> schoolBeanMap = school.getSchools();
        // 验证职业
        if (!schoolBeanMap.containsKey(schoolId)) {
            LOG.error("角色{}，职业{}不存在", rid, schoolId);
            return ErrorCode.SCHOOL_NOT_EXIST;
        }

        // 职业所有卡组
        SchoolBean schoolBean = schoolBeanMap.get(schoolId);
        boolean flag = false;
        Map<Long, CardGroup> cardGroupMap = schoolBean.getCardGroup();
        // 查找删除卡组
        CardGroup c = getCardGroupById(cardGroupMap, groupId);
        if (c != null && cardGroupMap.remove(c.getId()) != null) {
            return schoolDao.upSchool(school) ? ErrorCode.SERVER_SUCCESS : ErrorCode.SERVER_ERROR;
        }

        return ErrorCode.CARD_GROUP_ID_NOT_EXIST;
    }

    protected SchoolBean newSchoolBean(int schoolId) {
        SchoolBean schoolBean = new SchoolBean();
        schoolBean.setSchoolId(schoolId);

        SchoolInitConf schoolInitConf = ConfigManager.schoolInitConfMap.get(schoolId);
        // 没有职业初始配置或职业初始配置没有开放
        if (null == schoolInitConf || schoolInitConf.getIsLock() == 0) {
            return schoolBean;
        }
//        schoolBean.setWeapon(schoolInitConf.getDefaultWeapon());
        // 卡组初始化数据
        CardGroup cardGroup = new CardGroup();
        cardGroup.setId(rpcClient.getUniqueId());// 卡组id
        cardGroup.setName(schoolInitConf.getDeckName());// 卡组名称

        String cardId;
        /**已上阵*/
        String[] useCardIds = Misc.split(schoolInitConf.getCardDeck(), "\\;");
        for (String useCardId : useCardIds) {
            cardGroup.getUseCards().add(useCardId);
        }
        /**未上阵*/
        String[] checkCardIds = Misc.split(schoolInitConf.getCheckCard(), "\\,");
        for (String checkCardId : checkCardIds) {
            int tmp = Integer.parseInt(checkCardId);
            cardGroup.getCheckCards().add(tmp);
        }

        cardGroup.setAgi(schoolInitConf.getAgility());
        cardGroup.setIq(schoolInitConf.getIntelligence());
        cardGroup.setStr(schoolInitConf.getStrength());

        /**职业下增加卡组 */
        schoolBean.getCardGroup().put(cardGroup.getId(), cardGroup);

        return schoolBean;
    }

    private boolean newSchool(Map<Integer, SchoolBean> map, long rid, int schoolId) {
        if (map.containsKey(schoolId)) {
            LOG.error("角色 {}，已存在新职业 {}", rid, schoolId);
            return false;
        }
        map.put(schoolId, newSchoolBean(schoolId));
        return true;
    }

    public CardGroup getCardGroupById(Map<Long, CardGroup> cardGroupMap, long groupId) {
        for (Map.Entry<Long, CardGroup> cardGroupEntry : cardGroupMap.entrySet()) {
            if (cardGroupEntry.getValue().getId() == groupId) {
                return cardGroupEntry.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean upgradeSuccessed(Long rid, Integer schoolId, Integer condition) {
        School school = schoolDao.getSchool(rid);
        Optional<SchoolBean> optional = school.getSchools().values()
                .stream().filter(sc -> sc.getSchoolId() / 1000 == schoolId / 1000 && sc.getLevel() >= condition)
                .findFirst();
        SchoolBean schoolBean = optional.isPresent() ? optional.get() : null;
        if (Objects.nonNull(schoolBean)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean gltTotalLevel(Long rid, Integer condition) {
        int total = 0;
        School school = schoolDao.getSchool(rid);
        for (SchoolBean one : school.getSchools().values()) {
            total += one.getLevel();
        }
        if (total >= condition) {
            return true;
        }
        return false;
    }

    @Override
    public int getSchoolLevelByRoleId(Long rid, Integer schoolId) {
        School school = schoolDao.getSchool(rid);
        Optional<SchoolBean> schoolOptional = school.getSchools().values()
                .stream().filter(guest -> guest.getSchoolId() / 1000 == schoolId / 1000)
                .findFirst();
        SchoolBean schoolBean = schoolOptional.isPresent() ? schoolOptional.get() : schoolOptional.orElse(null);
        return schoolBean.getLevel();
    }

    @Override
    public int getAlmostSchoolLevel(Long rid) {
        int total = 0;
        School school = schoolDao.getSchool(rid);
        for (SchoolBean one : school.getSchools().values()) {
            total += one.getLevel();
        }
        return total;
    }

    @Override
    public ErrorCode verifyCardGroup(Long rid) {
        School school = getSchoolByRoleId(rid);
        if (Objects.isNull(school)) {
            return ErrorCode.SCHOOL_NOT_EXIST;
        }
        Map<Integer, SchoolBean> schools = school.getSchools();
        if (Objects.isNull(schools)) {
            return ErrorCode.CARD_GROUP_NOT_EXIST;
        }
        Card Card = cardService.getCardById(rid);
        Set<Integer> cardIds = Card.getCards().keySet();
        List<Integer> cards = new ArrayList<>(cardIds);
        for (SchoolBean bean : schools.values()) {
            //每个职业下的卡组
            Map<Long, CardGroup> cardGroups = bean.getCardGroup();
            for (CardGroup group : cardGroups.values()) {
                List<String> useCards = group.getUseCards();
                // i.	已组装的卡牌数少于n张（配置）
                // ii.	已组装的卡牌有缺失（被出售或未购买）
                int min_deck = Integer.parseInt(ConfigManager.globalVariableConfMap.get(1037).getValue());
                if (useCards.size() >= min_deck && cards.containsAll(useCards)) {
                    group.setPerfection(0); //完美
                } else {
                    group.setPerfection(1);
                }
            }
        }
        return ErrorCode.SERVER_SUCCESS;
    }

    @Override
    public CardGroup getOneCardGroup(Long rid, Integer schoolId, Long groupId) {
        School school = getSchoolByRoleId(rid);
        Map<Integer, SchoolBean> schools = school.getSchools();
        SchoolBean schoolBean = schools.get(schoolId);
        Map<Long, CardGroup> cardGroups = schoolBean.getCardGroup();
        CardGroup cardGroup = cardGroups.get(groupId);
        return cardGroup;
    }

}

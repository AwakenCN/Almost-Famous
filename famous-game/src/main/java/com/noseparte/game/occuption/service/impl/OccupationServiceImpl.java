package com.noseparte.game.occuption.service.impl;

import com.noseparte.common.bean.SchoolBean;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.resources.OccupationConf;
import com.noseparte.game.occuption.entity.Occupation;
import com.noseparte.game.occuption.mongo.OccupationDao;
import com.noseparte.game.occuption.service.OccupationService;
import com.noseparte.game.school.entity.School;
import com.noseparte.game.school.mongo.SchoolDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OccupationServiceImpl implements OccupationService {

    @Resource
    SchoolDao schoolDao;
    @Resource
    OccupationDao occupationDao;

    @Override
    public void initOccupationConfig() {
        Map<Integer, OccupationConf> map = ConfigManager.occupationConfMap;
        map.values().forEach(occupation -> {
            Occupation target = new Occupation();
            BeanUtils.copyProperties(occupation, target);
            occupationDao.insert(target);
        });
    }

    @Override
    public List<Occupation> getOccupationalGroup(int schoolId) {
        return occupationDao.getOccupationalGroup(schoolId);
    }


    public boolean update(Long rid, int schoolId, int exp) {
        School actorSchool = schoolDao.getSchool(rid);
        if (Objects.isNull(actorSchool)) {
            return false;
        }
        Map<Integer, OccupationConf> occupationConfMap = ConfigManager.occupationConfMap;
        for (SchoolBean school : actorSchool.getSchools().values()) {
            if (school.getSchoolId() == schoolId) {
                school.setExp(school.getExp() + exp);
                for (OccupationConf occupation : occupationConfMap.values()) {
                    if (occupation.getLevel() == school.getLevel() && occupation.getId() == schoolId) {
                        // 升级
                        if (school.getExp() > occupation.getLvUpExp() && school.getLevel() < 10) {
                            school.setLevel(school.getLevel() + 1);
                            if (Objects.nonNull(occupation.getNextLvID()) && occupation.getNextLvID() > 0) {
                                school.setExp(school.getExp() - occupation.getLvUpExp());
                                school.setSchoolId(occupation.getNextLvID());
                                actorSchool.getSchools().remove(schoolId);
                                actorSchool.getSchools().putIfAbsent(occupation.getNextLvID(), school);
                                schoolDao.upSchool(actorSchool);
                                return true;
                            } else {
                                schoolDao.upSchool(actorSchool);
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private int getTotalLevel(School school) {
        int sum = 0;
        for (SchoolBean sc : school.getSchools().values()) {
            sum += sc.getLevel();
        }
        return sum;
    }

    @Override
    public boolean upgrade(Long rid, int schoolId, int exp) {
        School school = schoolDao.getSchool(rid);
        if (Objects.isNull(school)) {
            return false;
        }
        Map<Integer, SchoolBean> schools = school.getSchools();
        // 职业列表
        Map<Integer, OccupationConf> occupationConfMap = ConfigManager.occupationConfMap;
        if (!occupationConfMap.keySet().contains(schoolId)) {
            return false;
        }
        // 当前职业
        SchoolBean schoolBean = schools.get(schoolId);
        // 当前职业的所有升级机制
        List<OccupationConf> occupationList = occupationConfMap.values().stream()
                .filter(occupation -> occupation.getId() / 1000 == schoolId / 1000)
                .sorted(Comparator.comparing(OccupationConf::getId))
                .collect(Collectors.toList());
        // 目前 经验值
        int nowExp = schoolBean.getExp();
        OccupationConf occupationConf = occupationList.stream()
                .filter(occupation1 -> occupation1.getId() == schoolBean.getSchoolId()).findFirst().get();
        if (Objects.isNull(occupationConf)) {
            return false;
        }
        // 预升级 经验值
        int upgradeExp = nowExp + exp;
        // Level提升 更新职业ID
        if (upgradeExp > occupationConf.getLvUpExp() && schoolBean.getLevel() < 10) {
            OccupationConf upgradeOccupation = afterUpgrade(schoolBean, upgradeExp, occupationConf);
            if (Objects.nonNull(upgradeOccupation)) {
                schoolBean.setSchoolId(upgradeOccupation.getId());
                schoolBean.setLevel(upgradeOccupation.getLevel());
                schoolBean.setExp(upgradeExp - occupationConf.getLvUpExp());
                school.getSchools().remove(schoolId);
                school.getSchools().putIfAbsent(upgradeOccupation.getId(), schoolBean);
                schoolDao.upSchool(school);
            }
        } else {
            schoolBean.setExp(upgradeExp);
            schoolDao.upSchool(school);
            return false;
        }

        return true;
    }

    // 递归
    private OccupationConf afterUpgrade(SchoolBean schoolBean, int upgradeExp, OccupationConf occupationConf) {
        // 升级
        if (upgradeExp > occupationConf.getLvUpExp() && schoolBean.getLevel() < 10) {
            int lowExp = upgradeExp - occupationConf.getLvUpExp();
            Integer nextLvID = occupationConf.getNextLvID();
            // 职业列表
            Map<Integer, OccupationConf> occupationConfMap = ConfigManager.occupationConfMap;
            OccupationConf nextOccuption = occupationConfMap.get(nextLvID);
            Integer nextlvUpExp = nextOccuption.getLvUpExp();
            if (lowExp < nextlvUpExp) {
                return nextOccuption;
            } else {
                return afterUpgrade(schoolBean, upgradeExp - occupationConf.getLvUpExp(), nextOccuption);
            }
        }
        return null;
    }


}

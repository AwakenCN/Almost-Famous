package com.noseparte.game.role.mongo;

import com.noseparte.common.db.dao.GeneralDaoImpl;
import com.noseparte.game.role.entity.Role;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Noseparte
 * @date 2019/9/10 10:28
 * @Description
 */
@Repository
public class RoleDaoImpl extends GeneralDaoImpl<Role> implements RoleDao {


    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }

    @Resource
    @Qualifier(value = "gameMongoTemplate")
    private MongoTemplate gameMongoTemplate;


    @Override
    public Role selectById(Long uid) {
        Query query = new Query().addCriteria(Criteria.where("uid").is(uid));
        return gameMongoTemplate.findOne(query, getEntityClass());
    }

    @Override
    public Role selectByRoleId(Long rid) {
        Query query = new Query().addCriteria(Criteria.where("rid").is(rid));
        return gameMongoTemplate.findOne(query, getEntityClass());
    }

    @Override
    public void addRole(Role role) {
        gameMongoTemplate.insert(role);
    }

    @Override
    public void updateByRoleId(Role role) {
        Query query = new Query().addCriteria(Criteria.where("rid").is(role.getRid()));
        Update update = new Update()
                .set("roleName", role.getRoleName())
                .set("school", role.getSchool())
                .set("gold", role.getGold())
                .set("silver", role.getSilver())
                .set("diamond", role.getDiamond())
                .set("lastBattleSchool", role.getLastBattleSchool())
                .set("lastBattleCardGroup", role.getLastBattleCardGroup())
                .set("newcomerGuide", role.getNewcomerGuide())
                .set("battleRank", role.getBattleRank());
        gameMongoTemplate.findAndModify(query, update, getEntityClass());
    }
}

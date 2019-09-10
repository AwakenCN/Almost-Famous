package com.liema.game.role.mongo;

import com.liema.common.db.dao.GeneralDao;
import com.liema.game.role.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Noseparte
 * @date 2019/8/20 18:14
 * @Description
 */
public interface RoleDao extends GeneralDao<Role> {

    Role selectById(Long uid);


    Role selectByRoleId(Long rid);

    void addRole(Role role);

    void updateByRoleId(Role role);
}

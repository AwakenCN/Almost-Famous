package com.noseparte.game.role.mongo;

import com.noseparte.common.db.dao.GeneralDao;
import com.noseparte.game.role.entity.Role;

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

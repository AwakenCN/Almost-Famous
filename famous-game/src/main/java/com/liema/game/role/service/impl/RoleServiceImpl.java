package com.liema.game.role.service.impl;

import com.liema.game.role.entity.Role;
import com.liema.game.role.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * @author Noseparte
 * @date 2019/8/20 18:20
 * @Description
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Override
    public Role selectById(Long uid) {
        return null;
    }

    @Override
    public Role selectByRoleId(long rid) {
        return null;
    }

    @Override
    public void newcomerGuide(Role role, Integer guideId) {

    }

    @Override
    public void updateByRoleId(Role role) {

    }

    @Override
    public void currencyChangeGM(Role role) {

    }
}

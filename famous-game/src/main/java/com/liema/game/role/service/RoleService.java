package com.liema.game.role.service;

import com.liema.game.role.entity.Role;

public interface RoleService {

    Role selectById(Long uid);

    Role selectByRoleId(long rid);

    void newcomerGuide(Role role, Integer guideId);

    void updateByRoleId(Role role);

    void currencyChangeGM(Role role);


}

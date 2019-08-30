package com.liema.game.role.service;

import com.liema.game.item.HoldItem;
import com.liema.game.role.entity.Role;

import java.util.List;

public interface RoleService {

    Role selectById(Long uid);

    Role selectByRoleId(long rid);

    void newcomerGuide(Role role, Integer guideId);

    void updateByRoleId(Role role);

    void currencyChangeGM(Role role);

    boolean distributeAwardToActor(Role role, List<HoldItem> holdItems);

}

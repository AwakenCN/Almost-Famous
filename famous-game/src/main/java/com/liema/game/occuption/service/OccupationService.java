package com.liema.game.occuption.service;

import com.liema.game.occuption.entity.Occupation;

import java.util.List;

public interface OccupationService {

    void initOccupationConfig();

    List<Occupation> getOccupationalGroup(int schoolId);

    boolean upgrade(Long rid, int schoolId, int exp);
}

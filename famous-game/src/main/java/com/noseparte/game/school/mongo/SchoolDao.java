package com.noseparte.game.school.mongo;

import com.noseparte.common.db.dao.GeneralDao;
import com.noseparte.game.school.entity.School;

/**
 * <p>
 * 职业 Mapper 接口
 * </p>
 *
 * @author liang
 * @since 2019-04-18
 */
public interface SchoolDao extends GeneralDao<School> {

    boolean addSchool(School school);

    boolean upSchool(School school);

    School getSchool(long rid);
}

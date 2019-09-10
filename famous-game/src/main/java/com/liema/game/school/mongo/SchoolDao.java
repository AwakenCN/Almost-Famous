package com.liema.game.school.mongo;

import com.liema.common.db.dao.GeneralDao;
import com.liema.common.db.pojo.GeneralBean;
import com.liema.game.school.entity.School;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

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

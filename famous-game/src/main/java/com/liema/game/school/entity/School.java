package com.liema.game.school.entity;

import com.liema.common.bean.SchoolBean;
import com.liema.common.db.pojo.GeneralBean;
import com.liema.common.global.Misc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 一个角色下拥有多个职业
 * 职业
 * </p>
 *
 * @author liang
 * @since 2019-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class School extends GeneralBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public School() {
    }

    public School(Long rid, Map<Integer, SchoolBean> schools) {
        this.rid = rid;
        this.schools = schools;
    }

    private Long rid;

    /**
     * 多个职业
     */
    private Map<Integer, SchoolBean> schools;


}

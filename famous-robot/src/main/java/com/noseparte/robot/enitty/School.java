package com.noseparte.robot.enitty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.noseparte.common.bean.SchoolBean;
import com.noseparte.common.global.Misc;
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
public class School implements Serializable {

    private static final long serialVersionUID = 1L;

    public School() {
    }

    public School(Long rid, String json) {
        this.rid = rid;
        this.schools = Misc.parseToMap(json, Integer.class, SchoolBean.class);
    }

    @TableId(type = IdType.INPUT)
    private Long rid;

    /**
     * 多个职业
     */
    private Map<Integer, SchoolBean> schools = new HashMap<>();



}

package com.noseparte.common.bean.protocol;

import lombok.Data;

/**
 * @author Noseparte
 * @date 2019/8/13 9:58
 * @Description
 */
@Data
public class RoleBean {

    public final static long CURRENCY = 1000;

    private long rid;
    private int attrId;
    private long attrVal;

}

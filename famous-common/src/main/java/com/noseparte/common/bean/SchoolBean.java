package com.noseparte.common.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class SchoolBean implements Serializable {

    int schoolId;
    /**
     * 职业武器
     */
    int weapon;

    /**
     * 职业等级 default=1
     */
    int level = 1;

    /**
     * 当前经验值
     */
    int exp;

    /**
     * 多个卡组
     */
    Map<Long, CardGroup> cardGroup = new HashMap<>();

}

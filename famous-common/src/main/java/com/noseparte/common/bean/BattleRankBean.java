package com.noseparte.common.bean;

import lombok.Data;

import java.io.Serializable;

@Data
/**
 * 角色段位
 */
public class BattleRankBean implements Serializable {
    int rankId;//当前段位配表id
    int starCount;//当前星
}

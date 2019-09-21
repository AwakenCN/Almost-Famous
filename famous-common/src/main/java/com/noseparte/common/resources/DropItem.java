package com.noseparte.common.resources;

import com.noseparte.common.bean.DropCode;
import lombok.Data;

@Data
public class DropItem {
	/**
	 * 掉落物品ID
	 */
	public int id;
	/**
	 * 掉落物品品质
	 */
	public int quality;
	/**
	 * 掉落物品权重
	 */
	public int weight;
	/**
	 * 掉落物品数量
	 */
	public int count;
	/**
	 * 随机上限
	 */
	public int max;
	/**
	 * 随机下限
	 */
	public int min;

	DropCode type;
}

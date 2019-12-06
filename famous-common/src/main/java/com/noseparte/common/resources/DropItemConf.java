package com.noseparte.common.resources;

import com.noseparte.common.bean.DropCode;
import com.noseparte.common.global.Misc;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Slf4j
public class DropItemConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

/********** attribute ***********/
    private Integer id;

    private String drop;

    public List<DropItem> dropItemList = new ArrayList<>();

    public int maxWeight = 0;

    /********** constructors ***********/
    public DropItemConf() {

    }

    public DropItemConf(Integer id, String drop) {
        this.id = id;
        this.drop = drop;
    }

/********** get/set ***********/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDrop() {
        return drop;
    }

    public void setDrop(String drop) {
        this.drop = drop;
        if (drop != null && !"".equals(drop)) {
            String[] items = Misc.split(drop, "\\;");
            if (null != items && items.length > 0) {
                for (String item : items) {
                    String[] fields = Misc.split(item, "\\,");
                    DropItem dropItem = new DropItem();
                    dropItem.setType(DropCode.parase(Integer.parseInt(fields[0])));
                    dropItem.setId(Integer.parseInt(fields[1]));
                    dropItem.setWeight(Integer.parseInt(fields[2]));
                    maxWeight += dropItem.weight;
                    dropItemList.add(dropItem);
                }
                if (maxWeight > Misc.MAX_WEIGHT) {
                    log.error("warning  *******  weight maybe overflow  , droplist id = " + this.id);
                }
                if (maxWeight < Misc.MAX_WEIGHT) maxWeight = Misc.MAX_WEIGHT;
                maxWeight = maxWeight;
                int minWight = 0;
                // 权重边界
                for (DropItem di : dropItemList) {
                    di.min = minWight;
                    minWight += di.weight;
                    di.max = minWight;
                }
            }
        }

    }

}

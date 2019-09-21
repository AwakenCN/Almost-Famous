package com.noseparte.common.bean;

import lombok.Data;

@Data
public class PackBean implements Comparable<PackBean> {
    /**
     * 包裹类型 背包
     */
    public static final int PACK_TYPE_BAG = 0;
    /**
     * 包裹类型 卡包
     */
    public static final int PACK_TYPE_CARD = 1;
    public final static int BAG_TYPE_ORDER = 2;

    private int orderId;
    private AttrCode code;
    private boolean isStacked;//可堆叠的
    private int num;
    private int index;

    @Override
    public int compareTo(PackBean o) {
        if (this.code.value > o.getCode().value) {
            return 1;
        } else if (this.code.value < o.getCode().value) {
            return -1;
        } else {
            if (this.orderId < o.getOrderId()) {
                return -1;
            } else if (this.orderId > o.getOrderId()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}

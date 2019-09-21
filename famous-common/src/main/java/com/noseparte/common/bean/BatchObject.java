package com.noseparte.common.bean;

import lombok.Data;

@Data
public class BatchObject {

    private BatchObjectEnum batchObjectEnum;
    private byte[] key;
    private byte[] value;
}

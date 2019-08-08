package com.liema.bean;

import lombok.Data;

@Data
public class BatchObject {

    private BatchObjectEnum batchObjectEnum;
    private byte[] key;
    private byte[] value;
}

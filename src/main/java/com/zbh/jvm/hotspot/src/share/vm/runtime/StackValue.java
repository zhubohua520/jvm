package com.zbh.jvm.hotspot.src.share.vm.runtime;

import lombok.Data;

@Data
public class StackValue {

    private int type;

//    /**
//     * 数据
//     */
//    private int val;

    private byte[] bytes;

    private Object object;

//    public StackValue(int type, int val) {
//        this.type = type;
//        this.val = val;
//    }
//
public StackValue(int type, Object val) {
    this.type = type;
    this.object = val;
}


    public StackValue(int type, byte[] bytes) {
        this.type = type;
        this.bytes = bytes;
    }
}

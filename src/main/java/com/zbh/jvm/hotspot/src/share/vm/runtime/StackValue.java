package com.zbh.jvm.hotspot.src.share.vm.runtime;

public class StackValue {

    private int type;

    /**
     * 数据
     */
    private int val;

    private Object object;

    public StackValue(int type, int val) {
        this.type = type;
        this.val = val;
    }

    public StackValue(int type, Object val) {
        this.type = type;
        this.object = val;
    }
}

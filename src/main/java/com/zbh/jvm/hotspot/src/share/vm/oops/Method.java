package com.zbh.jvm.hotspot.src.share.vm.oops;

import lombok.Data;

@Data
public class Method {

    private int accessFlags; //u2

    private int nameIndex; //u2

    private int descriptorIndex; //u2

    private int attributesCount; //u2;

    private AttributeInfo[] attributes;

    //自定义属性，他爹是谁
    private InstanceKlass belongKlass;
}

package com.zbh.jvm.hotspot.src.share.vm.oops;

import lombok.Data;

import java.util.List;

@Data
public class InstanceKlass extends Klass {

    private byte[] magic; //u4
    private byte[] minorVersion; //u2
    private byte[] majorVersion; //u2

    private int constantPoolCount; //u2

    private ConstantPool constantPool; //size:constantPoolCount-1 TODO 为什么要减1？

    private int accessFlags; //u2

    private int superClass; //u2

    private int interfacesCount; //u2

    InterfaceInfo[] interfaces; //u2 结构是Constant_class_info

    private int fieldsCount; //u2

    FieldInfo[] fields;

    private int methodCount; //u2

    private Method[] methods;

    private int attributesCount; //u2

    AttributeInfo[] attributes;




}

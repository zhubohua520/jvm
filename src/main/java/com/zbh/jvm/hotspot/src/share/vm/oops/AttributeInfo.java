package com.zbh.jvm.hotspot.src.share.vm.oops;


import lombok.Data;

@Data
public abstract class AttributeInfo {

    //region attribute
    public static String CONSTANT_VALUE = "ConstantValue";

    public static String CODE = "Code";

    public static String LINE_NUMBER_TABLE = "LineNumberTable";

    public static String LOCAL_VARIABLE_TABLE = "LocalVariableTable";

    public static String EXCEPTIONS = "Exceptions";

    public static String SOURCE_FILE = "SourceFile";
    //endregion

    private int attributeNameIndex; //u2

    private int attributeLength;  //u4


}

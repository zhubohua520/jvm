package com.zbh.jvm.hotspot.src.share.vm.oops;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class CodeAttribute extends AttributeInfo {

    private int maxStack; //u2

    private int maxLocals; //u2

    private int codeLength; //u4

    private byte[] code; //大小为codeLength

    private int exceptionTableLength; //u2

    private Exception[] exceptionTable;

    private int attributesCount; //u2

    private AttributeInfo[] attributes;

    @Data
    public class Exception {

        private int startPc; //u2

        private int endPc;  //u2

        private int handlePc;  //u2

        private int catchType; //u2
    }
}



package com.zbh.jvm.hotspot.src.share.vm.oops;


import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class LineNumberTableAttribute extends AttributeInfo {


    private int lineNumberTableLength;

    private LineNumber[] lineNumberTable;



    @Data
    public class LineNumber {

        private int startPc;

        private int lineNumber;
    }
}

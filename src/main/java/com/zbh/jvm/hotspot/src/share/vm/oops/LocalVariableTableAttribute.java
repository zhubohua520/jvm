package com.zbh.jvm.hotspot.src.share.vm.oops;


import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class LocalVariableTableAttribute extends AttributeInfo {


    private int localVariableTableLength;

    private LocalVariable[] localVariableTable;


    @Data
    public class LocalVariable {

        private int startPc;
        private int length;
        private int nameIndex;
        private int descriptorIndex;
        private int index;

    }

}

package com.zbh.jvm.hotspot.src.share.vm.oops;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "attributes")
public class FieldInfo {

    private int accessFlags;
    private int nameIndex;
    private int descriptorIndex;
    private int attributesCount;

    AttributeInfo[] attributes;

}

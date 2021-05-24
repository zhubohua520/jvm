package com.zbh.jvm.hotspot.src.share.vm.oops;

import lombok.Data;
import lombok.ToString;


@Data
@ToString(callSuper = true)
public class ConstantValueAttribute extends AttributeInfo {

    //attributeLength定长为2;

    private int constantValueIndex; //u2


}

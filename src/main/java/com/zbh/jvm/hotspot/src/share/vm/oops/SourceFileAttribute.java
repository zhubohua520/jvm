package com.zbh.jvm.hotspot.src.share.vm.oops;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class SourceFileAttribute extends AttributeInfo {

    private int sourceFileIndex;
}

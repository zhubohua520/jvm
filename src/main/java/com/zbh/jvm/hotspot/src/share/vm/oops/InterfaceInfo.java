package com.zbh.jvm.hotspot.src.share.vm.oops;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InterfaceInfo {

    private int constantPoolIndex;

    private String interfaceName;

}

package com.zbh.jvm.hotspot.src.share.vm.runtime;

import lombok.Data;

import java.util.Stack;

@Data
public class JavaThread extends Thread {

    //栈里面放栈帧，一个方法一个栈帧
    private Stack<VFrame> stack = new Stack<>();

}

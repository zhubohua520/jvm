package com.zbh.jvm.hotspot.src.share.vm.runtime;


import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

//TODO 操作数栈跟局部变量表是用的同一个结构？
@Data
public class StackValueCollection {

    private Logger logger = LoggerFactory.getLogger(StackValueCollection.class);


    //操作数栈
    private Stack<StackValue> container = new Stack<>();

    public StackValueCollection() {
    }

    public void push(StackValue value) {
        getContainer().push(value);
    }

    public StackValue pop() {
        return getContainer().pop();
    }

    public StackValue peek() {
        return getContainer().peek();
    }


    //局部变量表
    private int maxLocals;
    private int index;
    private StackValue[] locals;

    public StackValueCollection(int size) {
        maxLocals = size;

        locals = new StackValue[size];
    }


}

package com.zbh.jvm.hotspot.src.share.vm.runtime;


import com.zbh.jvm.hotspot.src.share.tools.BytesConverter;
import com.zbh.jvm.hotspot.src.share.vm.utilities.BasicType;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
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
        container.push(value);
    }

    public void pushDouble(double d) {


        byte[] bytes = BytesConverter.toBytes(d);

        ByteBuffer bb = ByteBuffer.wrap(bytes);

        int anInt1 = bb.getInt(0);

        // 为了后面取数据
//        bb.order(ByteOrder.LITTLE_ENDIAN);
        int anInt2 = bb.getInt(4);


        push(new StackValue(BasicType.T_DOUBLE, anInt1));

        push(new StackValue(BasicType.T_DOUBLE, anInt2));


    }

    public double popDouble() {
        int i1 = pop().getVal();
        int i2 = pop().getVal();

        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putInt(i2);
        bb.putInt(i1);
        bb.position(0);
//        bb.order(ByteOrder.BIG_ENDIAN);
        double aDouble = bb.getDouble();


        return aDouble;
    }

    public StackValue pop() {
        return container.pop();
    }

    public StackValue peek() {
        return container.peek();
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

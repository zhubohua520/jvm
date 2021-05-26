package com.zbh.jvm.hotspot.src.share.vm.runtime;

import com.zbh.jvm.hotspot.src.share.vm.oops.Method;
import lombok.Data;

@Data
public class JavaVFrame extends VFrame {


    //局部变量表
    private StackValueCollection locals;

    //操作数栈
    private StackValueCollection stack = new StackValueCollection();

    //动态链接

    //方法返回地址

    private Method method; //TODO 对应方法返回地址？


    /**
     * @param method    栈帧的方法
     * @param maxLocals 局部变量表的最大值
     */
    public JavaVFrame(Method method, int maxLocals) {
        this.method = method;
        locals = new StackValueCollection(maxLocals);
    }

}

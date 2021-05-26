package com.zbh.jvm.jdk;

import com.zbh.jvm.hotspot.src.share.vm.classfile.BootClassLoader;
import com.zbh.jvm.hotspot.src.share.vm.oops.InstanceKlass;
import com.zbh.jvm.hotspot.src.share.vm.oops.Method;
import com.zbh.jvm.hotspot.src.share.vm.runtime.JavaNativeInterface;
import com.zbh.jvm.hotspot.src.share.vm.runtime.JavaThread;
import com.zbh.jvm.hotspot.src.share.vm.runtime.Threads;


public class JDKStartMain {

    public static void main(String[] args) throws Exception {

        String path = "com.zbh.jvm.example.HelloWorld";
        System.out.println(path);

        InstanceKlass instanceKlass = BootClassLoader.loadClass(path);

        //找到main方法
        Method main = findMain(instanceKlass);

        JavaThread thread = new JavaThread();

        Threads.setCurrentThread(thread);

        JavaNativeInterface.callStaticMethod(main);


    }


    private static Method findMain(InstanceKlass instanceKlass) throws Exception {
        for (Method method : instanceKlass.getMethods()) {
            String methodName = (String) instanceKlass.getConstantPool().get(method.getNameIndex());
            String descriptor = (String) instanceKlass.getConstantPool().get(method.getDescriptorIndex());

            if (descriptor.equals("([Ljava/lang/String;)V") && methodName.equals("main")) {
                return method;
            }
        }

        throw new Exception("没有找到main方法");
    }
}

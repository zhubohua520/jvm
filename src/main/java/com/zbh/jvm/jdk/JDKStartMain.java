package com.zbh.jvm.jdk;

import com.zbh.jvm.hotspot.src.share.vm.classfile.BootClassLoader;


public class JDKStartMain {

    public static void main(String[] args) throws Exception {

        String path = "com.zbh.jvm.example.HelloWorld";
        System.out.println(path);

        BootClassLoader.loadClass(path);


    }
}

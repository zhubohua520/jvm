package com.zbh.jvm.jdk;

import com.zbh.jvm.hotspot.src.share.vm.classfile.BootClassLoader;

import java.io.IOException;


public class JDKStartMain {

    public static void main(String[] args) throws Exception {

        String path = "com.zbh.jvm.example.InterfaceTest";
        System.out.println(path);

        BootClassLoader.loadClass(path);


    }
}

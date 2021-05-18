package com.zbh.jvm.hotspot.src.share.vm.classfile;

import com.zbh.jvm.hotspot.src.share.vm.oops.InstanceKlass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BootClassLoader {

    private static Logger logger = LoggerFactory.getLogger(BootClassLoader.class);

    private static String SUFFIX = ".class";

    private static String ROOT_PATH = "/Users/zbh/test/java/zbh-jvm/target/classes/";

    public static void loadClass(String path) throws Exception {

        String classPath = path.replace('.', '/');
        classPath = ROOT_PATH + classPath + SUFFIX;

        logger.debug("class路径：{}", classPath);

        FileInputStream fis = new FileInputStream(classPath);

        ClassFileParser.parseClassFile(fis);

        fis.close();


    }
}

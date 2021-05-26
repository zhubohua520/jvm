package com.zbh.jvm.hotspot.src.share.vm.classfile;

import com.zbh.jvm.hotspot.src.share.vm.oops.InstanceKlass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class BootClassLoader {

    private static Logger logger = LoggerFactory.getLogger(BootClassLoader.class);

    private static String SUFFIX = ".class";

    private static String ROOT_PATH = "/Users/zbh/test/java/zbh-jvm/target/classes/";

    private static Map<String, InstanceKlass> classLoaderData = new HashMap<>();


    public static InstanceKlass loadClass(String path) throws Exception {

        InstanceKlass instanceKlass = classLoaderData.get(path);
        if (instanceKlass != null) {
            return instanceKlass;
        }

        String classPath = path.replace('.', '/');
        classPath = ROOT_PATH + classPath + SUFFIX;

        logger.debug("class路径：{}", classPath);

        FileInputStream fis = new FileInputStream(classPath);

        instanceKlass = ClassFileParser.parseClassFile(fis);

        fis.close();

        classLoaderData.put(path, instanceKlass);

        return instanceKlass;

    }


}

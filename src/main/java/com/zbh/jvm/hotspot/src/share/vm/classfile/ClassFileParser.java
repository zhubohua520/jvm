package com.zbh.jvm.hotspot.src.share.vm.classfile;


import com.zbh.jvm.hotspot.src.share.tools.BytesConverter;
import com.zbh.jvm.hotspot.src.share.vm.oops.ConstantPool;
import com.zbh.jvm.hotspot.src.share.vm.oops.InstanceKlass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

public class ClassFileParser {

    private static Logger logger = LoggerFactory.getLogger(BootClassLoader.class);


    public static InstanceKlass parseClassFile(FileInputStream fis) throws Exception {


        InstanceKlass instanceKlass = new InstanceKlass();

        //魔术
        instanceKlass.setMagic(readBytes(fis, 4));
        logger.debug("魔术：{}", BytesConverter.toHexBinaryStr(instanceKlass.getMagic()));
        //副版本号
        instanceKlass.setMinorVersion(readBytes(fis, 2));
        logger.debug("副版本号：{}", BytesConverter.toInt(instanceKlass.getMinorVersion()));
        //主版本号
        instanceKlass.setMajorVersion(readBytes(fis, 2));
        logger.debug("主版本号：{}", BytesConverter.toInt(instanceKlass.getMajorVersion()));
        //常量池长度
        instanceKlass.setConstantPoolCount(BytesConverter.toInt(readBytes(fis, 2)));
        logger.debug("常量池长度：{}", instanceKlass.getConstantPoolCount());
        //常量池
        parseConstantPool(fis, instanceKlass);

        return instanceKlass;
    }


    private static void parseConstantPool(FileInputStream fis, InstanceKlass instanceKlass) throws Exception {
        logger.debug("开始解析常量池");

        int count = instanceKlass.getConstantPoolCount();
        if (count <= 0) {
            throw new Exception("常量池计数器为0");
        }

        while (count > 0) {
            byte aByte = readByte(fis);
            int tag = BytesConverter.toInt(aByte);


            switch (tag) {
                case ConstantPool.JVM_CONSTANT_Methodref:
                    logger.debug("开始解析:{}", "JVM_CONSTANT_Methodref");




                    break;
                case ConstantPool.JVM_CONSTANT_Fieldref:
                    logger.debug("开始解析:{}", "JVM_CONSTANT_Fieldref");
                    break;

                default:
                    throw new Exception("未知的常量池类型");
            }


            count--;
        }
    }

    private static byte[] readBytes(FileInputStream fis, int capacity) throws IOException {


        byte[] bytes = new byte[capacity];

        fis.read(bytes);


        return bytes;
    }

    private static byte readByte(FileInputStream fis) throws IOException {

        byte[] bytes = readBytes(fis, 1);

        return bytes[0];

    }

}

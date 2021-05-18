package com.zbh.jvm.hotspot.src.share.vm.classfile;


import com.zbh.jvm.hotspot.src.share.tools.BytesConverter;
import com.zbh.jvm.hotspot.src.share.vm.oops.ConstantPool;
import com.zbh.jvm.hotspot.src.share.vm.oops.InstanceKlass;
import com.zbh.jvm.hotspot.src.share.vm.oops.InterfaceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
        //类的访问控制权限
        instanceKlass.setAccessFlags(BytesConverter.toInt(readBytes(fis, 2)));
        logger.debug("类的访问控制权限：{}", instanceKlass.getAccessFlags());
        //类索引
        instanceKlass.setThisClass(BytesConverter.toInt(readBytes(fis, 2)));
        logger.debug("类索引：{}", instanceKlass.getThisClass());
        //父类名
        instanceKlass.setSuperClass(BytesConverter.toInt(readBytes(fis, 2)));
        logger.debug("父类索引：{}", instanceKlass.getSuperClass());
        //接口长度
        instanceKlass.setInterfacesCount(BytesConverter.toInt(readBytes(fis, 2)));
        logger.debug("接口长度：{}", instanceKlass.getInterfacesCount());
        //接口
        parseInterface(fis, instanceKlass);

        return instanceKlass;
    }


    private static void parseConstantPool(FileInputStream fis, InstanceKlass instanceKlass) throws Exception {
        logger.debug("开始解析常量池");

        int count = instanceKlass.getConstantPoolCount();
        if (count <= 0) {
            throw new Exception("常量池计数器为0");
        }

        ConstantPool constantPool = new ConstantPool(count);
        //常量池从1开始
        int i = 1;
        while (count - 1 > 0) {
            byte aByte = readByte(fis);
            int tag = BytesConverter.toInt(aByte);


            switch (tag) {
                case ConstantPool.JVM_CONSTANT_Methodref: {

                    byte[] classIndexBytes = readBytes(fis, 2);
                    byte[] nameAndTypeIndexBytes = readBytes(fis, 2);

                    int classIndex = BytesConverter.toInt(classIndexBytes);
                    int nameAndTypeIndex = BytesConverter.toInt(nameAndTypeIndexBytes);

                    constantPool.put(i, classIndex << 16 | nameAndTypeIndex);

                    logger.debug("解析第{}个:{},值为{}", i, "JVM_CONSTANT_Methodref",
                            "0x" + Integer.toHexString((int) constantPool.get(i)));
                    break;
                }
                case ConstantPool.JVM_CONSTANT_Fieldref: {
                    byte[] classIndexBytes = readBytes(fis, 2);
                    byte[] nameAndTypeIndexBytes = readBytes(fis, 2);

                    int classIndex = BytesConverter.toInt(classIndexBytes);
                    int nameAndTypeIndex = BytesConverter.toInt(nameAndTypeIndexBytes);

                    constantPool.put(i, classIndex << 16 | nameAndTypeIndex);

                    logger.debug("解析第{}个:{},值为{}", i, "JVM_CONSTANT_Fieldref",
                            "0x" + Integer.toHexString((int) constantPool.get(i)));
                    break;
                }
                case ConstantPool.JVM_CONSTANT_String: {
                    byte[] stringIndexBytes = readBytes(fis, 2);

                    int stringIndex = BytesConverter.toInt(stringIndexBytes);

                    constantPool.put(i, stringIndex);

                    logger.debug("解析第{}个:{},值为{}", i, "JVM_CONSTANT_String",
                            "0x" + Integer.toHexString((int) constantPool.get(i)));
                    break;
                }
                case ConstantPool.JVM_CONSTANT_Class: {
                    byte[] nameIndexBytes = readBytes(fis, 2);

                    int nameIndex = BytesConverter.toInt(nameIndexBytes);

                    constantPool.put(i, nameIndex);

                    logger.debug("解析第{}个:{},值为{}", i, "JVM_CONSTANT_Class",
                            "0x" + Integer.toHexString((int) constantPool.get(i)));
                    break;
                }
                case ConstantPool.JVM_CONSTANT_Utf8: {

                    byte[] lengthBytes = readBytes(fis, 2);

                    int length = BytesConverter.toInt(lengthBytes);

                    if (length <= 0) {
                        throw new Exception("字符串没有长度？");
                    }

                    byte[] bytes = readBytes(fis, length);

                    String string = new java.lang.String(bytes, StandardCharsets.UTF_8);

                    constantPool.put(i, string);

                    logger.debug("解析第{}个:{},值为{}", i, "JVM_CONSTANT_Utf8", constantPool.get(i));
                    break;


                }
                case ConstantPool.JVM_CONSTANT_NameAndType: {
                    byte[] nameIndexBytes = readBytes(fis, 2);
                    int nameIndex = BytesConverter.toInt(nameIndexBytes);

                    byte[] descriptorIndexBytes = readBytes(fis, 2);
                    int descriptorIndex = BytesConverter.toInt(descriptorIndexBytes);
                    constantPool.put(i, nameIndex << 16 | descriptorIndex);

                    logger.debug("解析第{}个:{},值为{}", i, "JVM_CONSTANT_NameAndType",
                            "0x" + Integer.toHexString((int) constantPool.get(i)));

                    break;
                }
                default:
                    throw new Exception("未知的常量池类型");
            }


            count--;
            i++;
        }
        instanceKlass.setConstantPool(constantPool);
        logger.debug("常量池解析完毕");
    }

    private static void parseInterface(FileInputStream fis, InstanceKlass instanceKlass) throws Exception {
        int count = instanceKlass.getInterfacesCount();

        InterfaceInfo[] interfaceInfos = new InterfaceInfo[count];

        for (int i = 0; i < count; i++) {

            byte[] interfaceBytes = readBytes(fis, 2);
            int constantPoolIndex = BytesConverter.toInt(interfaceBytes);
            InterfaceInfo interfaceInfo = new InterfaceInfo(constantPoolIndex, instanceKlass.getConstantPool().getClassInfo(constantPoolIndex));
            interfaceInfos[i] = interfaceInfo;
        }

        instanceKlass.setInterfaces(interfaceInfos);


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

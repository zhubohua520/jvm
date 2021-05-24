package com.zbh.jvm.hotspot.src.share.vm.classfile;


import com.zbh.jvm.hotspot.src.share.tools.BytesConverter;
import com.zbh.jvm.hotspot.src.share.vm.oops.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ClassFileParser {


    private static final Logger logger = LoggerFactory.getLogger(BootClassLoader.class);


    public static InstanceKlass parseClassFile(FileInputStream fis) throws Exception {


        InstanceKlass instanceKlass = new InstanceKlass();

        //魔术
        instanceKlass.setMagic(readBytes(fis, 4));
        logger.debug("魔术：{}", BytesConverter.toHexBinaryStr(instanceKlass.getMagic()));
        //副版本号
        instanceKlass.setMinorVersion(readBytes(fis, 2));
        int minorVersion = BytesConverter.toInt(instanceKlass.getMinorVersion());
        logger.debug("副版本号：{}", minorVersion);
//        if (minorVersion != 0) {
//            throw new Exception("未知的class文件");
//        }
        //主版本号
        instanceKlass.setMajorVersion(readBytes(fis, 2));
        int majorVersion = BytesConverter.toInt(instanceKlass.getMajorVersion());
        logger.debug("主版本号：{}", majorVersion);
        if (majorVersion != 52) {
            throw new Exception("未知的class文件");
        }

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
        //字段长度
        instanceKlass.setFieldsCount(BytesConverter.toInt(readBytes(fis, 2)));
        logger.debug("字段长度：{}", instanceKlass.getFieldsCount());
        //字段
        parseFieldInfo(fis, instanceKlass);
        //方法长度
        instanceKlass.setMethodCount(BytesConverter.toInt(readBytes(fis, 2)));
        logger.debug("方法长度：{}", instanceKlass.getMethodCount());
        //方法
        parseMethodInfo(fis, instanceKlass);
        //属性长度
        instanceKlass.setAttributesCount(BytesConverter.toInt(readBytes(fis, 2)));
        //属性
        parseAttributeInfo(fis, instanceKlass);


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

                    logger.debug("解析第{}个:{},值:{}", i, "JVM_CONSTANT_Methodref",
                            "0x" + Integer.toHexString((int) constantPool.get(i)));
                    break;
                }
                case ConstantPool.JVM_CONSTANT_Fieldref: {
                    byte[] classIndexBytes = readBytes(fis, 2);
                    byte[] nameAndTypeIndexBytes = readBytes(fis, 2);

                    int classIndex = BytesConverter.toInt(classIndexBytes);
                    int nameAndTypeIndex = BytesConverter.toInt(nameAndTypeIndexBytes);

                    constantPool.put(i, classIndex << 16 | nameAndTypeIndex);

                    logger.debug("解析第{}个:{},值:{}", i, "JVM_CONSTANT_Fieldref",
                            "0x" + Integer.toHexString((int) constantPool.get(i)));
                    break;
                }
                case ConstantPool.JVM_CONSTANT_String: {
                    byte[] stringIndexBytes = readBytes(fis, 2);

                    int stringIndex = BytesConverter.toInt(stringIndexBytes);

                    constantPool.put(i, stringIndex);

                    logger.debug("解析第{}个:{},值:{}", i, "JVM_CONSTANT_String",
                            "0x" + Integer.toHexString((int) constantPool.get(i)));
                    break;
                }
                case ConstantPool.JVM_CONSTANT_Class: {
                    byte[] nameIndexBytes = readBytes(fis, 2);

                    int nameIndex = BytesConverter.toInt(nameIndexBytes);

                    constantPool.put(i, nameIndex);

                    logger.debug("解析第{}个:{},值:{}", i, "JVM_CONSTANT_Class",
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

                    logger.debug("解析第{}个:{},值:{}", i, "JVM_CONSTANT_Utf8", constantPool.get(i));
                    break;


                }
                case ConstantPool.JVM_CONSTANT_NameAndType: {
                    byte[] nameIndexBytes = readBytes(fis, 2);
                    int nameIndex = BytesConverter.toInt(nameIndexBytes);

                    byte[] descriptorIndexBytes = readBytes(fis, 2);
                    int descriptorIndex = BytesConverter.toInt(descriptorIndexBytes);
                    constantPool.put(i, nameIndex << 16 | descriptorIndex);

                    logger.debug("解析第{}个:{},值:{}", i, "JVM_CONSTANT_NameAndType",
                            "0x" + Integer.toHexString((int) constantPool.get(i)));

                    break;
                }
                case ConstantPool.JVM_CONSTANT_Integer: {
                    byte[] bytes = readBytes(fis, 4);

                    constantPool.put(i, BytesConverter.toInt(bytes));

                    logger.debug("解析第{}个:{},值:{}", i, "JVM_CONSTANT_Integer",
                            constantPool.get(i));
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
            InterfaceInfo interfaceInfo = new InterfaceInfo(constantPoolIndex,
                    instanceKlass.getConstantPool().getClassInfo(constantPoolIndex));
            interfaceInfos[i] = interfaceInfo;

            logger.debug("第{}个接口信息：{}", i + 1, interfaceInfo);
        }

        if (count > 0) {
            instanceKlass.setInterfaces(interfaceInfos);
        }

    }

    private static void parseFieldInfo(FileInputStream fis, InstanceKlass instanceKlass) throws Exception {
        int fieldsCount = instanceKlass.getFieldsCount();

        for (int i = 0; i < fieldsCount; i++) {

            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setAccessFlags(BytesConverter.toInt(readBytes(fis, 2)));

            fieldInfo.setNameIndex(BytesConverter.toInt(readBytes(fis, 2)));

            fieldInfo.setDescriptorIndex(BytesConverter.toInt(readBytes(fis, 2)));

            fieldInfo.setAttributesCount(BytesConverter.toInt(readBytes(fis, 2)));

            int attributesCount = fieldInfo.getAttributesCount();

            if (attributesCount > 0) {
                AttributeInfo[] attributes = new AttributeInfo[attributesCount];
                for (int j = 0; j < attributesCount; j++) {
                    attributes[j] = parseAttributeInfo(fis, instanceKlass.getConstantPool());
                }

                fieldInfo.setAttributes(attributes);
            }


            logger.debug("解析第{}个字段，值：{}", i + 1, fieldInfo);

        }
    }

    private static void parseMethodInfo(FileInputStream fis, InstanceKlass instanceKlass) throws Exception {

        int methodCount = instanceKlass.getMethodCount();

        for (int i = 0; i < methodCount; i++) {

            Method method = new Method();
            method.setAccessFlags(BytesConverter.toInt(readBytes(fis, 2)));

            method.setNameIndex(BytesConverter.toInt(readBytes(fis, 2)));

            method.setDescriptorIndex(BytesConverter.toInt(readBytes(fis, 2)));

            method.setAttributesCount(BytesConverter.toInt(readBytes(fis, 2)));

            if (method.getAttributesCount() > 0) {
                AttributeInfo[] attributeInfos = new AttributeInfo[method.getAttributesCount()];
                for (int j = 0; j < method.getAttributesCount(); j++) {
                    attributeInfos[j] = parseAttributeInfo(fis, instanceKlass.getConstantPool());
                }
                method.setAttributes(attributeInfos);
            }

            logger.debug("解析第{}个方法，值：{}", i + 1, method);


        }


    }

    private static AttributeInfo parseAttributeInfo(FileInputStream fis, ConstantPool constantPool) throws Exception {

        byte[] attributeNameIndexBytes = readBytes(fis, 2);
        int attributeNameIndex = BytesConverter.toInt(attributeNameIndexBytes);
        String attributeStr = (String) constantPool.get(attributeNameIndex);

        AttributeInfo baseAttribute;
        if (AttributeInfo.CONSTANT_VALUE.equals(attributeStr)) {
            baseAttribute = new ConstantValueAttribute();
            ConstantValueAttribute attribute = (ConstantValueAttribute) baseAttribute;
            attribute.setAttributeNameIndex(attributeNameIndex);
            //长度应该固定是2
            attribute.setAttributeLength(BytesConverter.toInt(readBytes(fis, 4)));

            attribute.setConstantValueIndex(BytesConverter.toInt(readBytes(fis, 2)));

        } else if (AttributeInfo.CODE.equals(attributeStr)) {

            baseAttribute = new CodeAttribute();

            CodeAttribute attribute = (CodeAttribute) baseAttribute;

            attribute.setAttributeNameIndex(attributeNameIndex);
            attribute.setAttributeLength(BytesConverter.toInt(readBytes(fis, 4)));

            attribute.setMaxStack(BytesConverter.toInt(readBytes(fis, 2)));
            attribute.setMaxLocals(BytesConverter.toInt(readBytes(fis, 2)));

            attribute.setCodeLength(BytesConverter.toInt(readBytes(fis, 4)));
            attribute.setCode(readBytes(fis, attribute.getCodeLength()));

            attribute.setExceptionTableLength(BytesConverter.toInt(readBytes(fis, 2)));

            if (attribute.getExceptionTableLength() > 0) {
                CodeAttribute.Exception[] exceptionTable = new CodeAttribute.Exception[attribute.getExceptionTableLength()];

                for (int i = 0; i < attribute.getExceptionTableLength(); i++) {
                    CodeAttribute.Exception exception = attribute.new Exception();
                    exception.setStartPc(BytesConverter.toInt(readBytes(fis, 2)));
                    exception.setEndPc(BytesConverter.toInt(readBytes(fis, 2)));
                    exception.setHandlePc(BytesConverter.toInt(readBytes(fis, 2)));
                    exception.setCatchType(BytesConverter.toInt(readBytes(fis, 2)));
                    exceptionTable[i] = exception;
                }

                attribute.setExceptionTable(exceptionTable);
            }

            attribute.setAttributesCount(BytesConverter.toInt(readBytes(fis, 2)));

            if (attribute.getAttributesCount() > 0) {

                AttributeInfo[] attributeInfos = new AttributeInfo[attribute.getAttributesCount()];
                for (int i = 0; i < attribute.getAttributesCount(); i++) {
                    AttributeInfo attributeInfo = parseAttributeInfo(fis, constantPool);
                    attributeInfos[i] = attributeInfo;
                }

                attribute.setAttributes(attributeInfos);
            }


        } else if (AttributeInfo.LINE_NUMBER_TABLE.equals(attributeStr)) {

            baseAttribute = new LineNumberTableAttribute();

            LineNumberTableAttribute attribute = (LineNumberTableAttribute) baseAttribute;

            attribute.setAttributeNameIndex(attributeNameIndex);
            attribute.setAttributeLength(BytesConverter.toInt(readBytes(fis, 4)));

            attribute.setLineNumberTableLength(BytesConverter.toInt(readBytes(fis, 2)));

            if (attribute.getLineNumberTableLength() > 0) {

                LineNumberTableAttribute.LineNumber[] lineNumberTable =
                        new LineNumberTableAttribute.LineNumber[attribute.getLineNumberTableLength()];

                for (int i = 0; i < attribute.getLineNumberTableLength(); i++) {
                    LineNumberTableAttribute.LineNumber lineNumber = attribute.new LineNumber();
                    lineNumber.setStartPc(BytesConverter.toInt(readBytes(fis, 2)));
                    lineNumber.setLineNumber(BytesConverter.toInt(readBytes(fis, 2)));
                    lineNumberTable[i] = lineNumber;
                }


                attribute.setLineNumberTable(lineNumberTable);
            }


        } else if (AttributeInfo.LOCAL_VARIABLE_TABLE.equals(attributeStr)) {


            baseAttribute = new LocalVariableTableAttribute();

            LocalVariableTableAttribute attribute = (LocalVariableTableAttribute) baseAttribute;

            attribute.setAttributeNameIndex(attributeNameIndex);
            attribute.setAttributeLength(BytesConverter.toInt(readBytes(fis, 4)));

            attribute.setLocalVariableTableLength(BytesConverter.toInt(readBytes(fis, 2)));

            if (attribute.getLocalVariableTableLength() > 0) {

                LocalVariableTableAttribute.LocalVariable[] localVariableTable =
                        new LocalVariableTableAttribute.LocalVariable[attribute.getLocalVariableTableLength()];

                for (int i = 0; i < attribute.getLocalVariableTableLength(); i++) {
                    LocalVariableTableAttribute.LocalVariable localVariable = attribute.new LocalVariable();
                    localVariable.setStartPc(BytesConverter.toInt(readBytes(fis, 2)));
                    localVariable.setLength(BytesConverter.toInt(readBytes(fis, 2)));
                    localVariable.setNameIndex(BytesConverter.toInt(readBytes(fis, 2)));
                    localVariable.setDescriptorIndex(BytesConverter.toInt(readBytes(fis, 2)));
                    localVariable.setIndex(BytesConverter.toInt(readBytes(fis, 2)));


                    localVariableTable[i] = localVariable;
                }


                attribute.setLocalVariableTable(localVariableTable);
            }
        } else if (AttributeInfo.EXCEPTIONS.equals(attributeStr)) {

            baseAttribute = new ExceptionsAttribute();

            ExceptionsAttribute attribute = (ExceptionsAttribute) baseAttribute;

            attribute.setAttributeNameIndex(attributeNameIndex);
            attribute.setAttributeLength(BytesConverter.toInt(readBytes(fis, 4)));

            attribute.setNumberOfExceptions(BytesConverter.toInt(readBytes(fis, 2)));

            if (attribute.getNumberOfExceptions() > 0) {

                attribute.setExceptionIndexTables(new Integer[attribute.getNumberOfExceptions()]);
                for (int i = 0; i < attribute.getNumberOfExceptions(); i++) {
                    attribute.getExceptionIndexTables()[i] = BytesConverter.toInt(readBytes(fis, 2));

                }
            }


        } else if (AttributeInfo.SOURCE_FILE.equals(attributeStr)) {

            baseAttribute = new SourceFileAttribute();

            SourceFileAttribute attribute = (SourceFileAttribute) baseAttribute;

            attribute.setAttributeNameIndex(attributeNameIndex);
            attribute.setAttributeLength(BytesConverter.toInt(readBytes(fis, 4)));
            attribute.setSourceFileIndex(BytesConverter.toInt(readBytes(fis, 2)));


        } else {
            throw new Exception("未能解析的属性！");
        }


        logger.debug("解析属性：{}，值：{}", attributeStr, baseAttribute);

        return baseAttribute;
    }

    private static void parseAttributeInfo(FileInputStream fis, InstanceKlass instanceKlass) throws Exception {

        if (instanceKlass.getAttributesCount() > 0) {
            instanceKlass.setAttributes(new AttributeInfo[instanceKlass.getAttributesCount()]);

            for (int i = 0; i < instanceKlass.getAttributesCount(); i++) {
                instanceKlass.getAttributes()[i] = parseAttributeInfo(fis, instanceKlass.getConstantPool());
            }
        }
    }


    private static byte[] readBytes(FileInputStream fis, int capacity) throws IOException {


        byte[] bytes = new byte[capacity];

        int read = fis.read(bytes);


        return bytes;
    }

    private static byte readByte(FileInputStream fis) throws IOException {

        byte[] bytes = readBytes(fis, 1);

        return bytes[0];

    }

}

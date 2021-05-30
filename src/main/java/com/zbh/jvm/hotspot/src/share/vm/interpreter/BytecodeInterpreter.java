package com.zbh.jvm.hotspot.src.share.vm.interpreter;

import com.zbh.jvm.hotspot.src.share.tools.BytesConverter;
import com.zbh.jvm.hotspot.src.share.vm.classfile.DescriptorStream;
import com.zbh.jvm.hotspot.src.share.vm.oops.CodeAttribute;
import com.zbh.jvm.hotspot.src.share.vm.oops.ConstantPool;
import com.zbh.jvm.hotspot.src.share.vm.oops.Method;
import com.zbh.jvm.hotspot.src.share.vm.runtime.JavaThread;
import com.zbh.jvm.hotspot.src.share.vm.runtime.JavaVFrame;
import com.zbh.jvm.hotspot.src.share.vm.runtime.StackValue;
import com.zbh.jvm.hotspot.src.share.vm.utilities.BasicType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class BytecodeInterpreter {

    private static Logger logger = LoggerFactory.getLogger(BytecodeInterpreter.class);

    public static void run(JavaThread thread, Method method) throws Exception {

        //栈帧
        JavaVFrame frame = (JavaVFrame) thread.getStack().peek();

        CodeAttribute attribute = (CodeAttribute) method.getAttributes()[0];
        //字节码
        byte[] codeBytes = attribute.getCode();

        //tip:每个字节码只占一个字节，所以这里读取应该是一个字节一个字节的读取
        ByteCode byteCode = new ByteCode(codeBytes);
        while (!byteCode.isTail()) {

            byte aByte = byteCode.read();
            int code = BytesConverter.toUnsignedInt(aByte);

            ConstantPool constantPool = method.getBelongKlass().getConstantPool();

            switch (code) {

                case ByteCodes._getstatic: {
                    logger.debug("执行指令：{}", "getstatic");

                    int fieldRefInfoKey = BytesConverter.toInt(byteCode.read(2));
                    String classInfo = constantPool.getClassInfoByRefInfo(fieldRefInfoKey);

                    String fieldName = constantPool.getNameByRefInfo(fieldRefInfoKey);


                    //这里不懂，为什么会有压栈操作
                    Class<?> aClass = Class.forName(classInfo.replace('/', '.'));
                    Field field = aClass.getField(fieldName);

                    frame.getStack().push(new StackValue(BasicType.T_OBJECT, field.get(null)));

                    break;
                }
                case ByteCodes._ldc: {

                    logger.debug("执行指令：{}", "ldc");

                    int key = BytesConverter.toInt(byteCode.read());

                    int constantPoolTag = constantPool.getTag(key);
                    switch (constantPoolTag) {
                        case ConstantPool.JVM_CONSTANT_String: {

                            String str = constantPool.getStr(key);

                            //TODO String也是OBJECT类型？
                            frame.getStack().push(new StackValue(BasicType.T_OBJECT, str));

                            break;
                        }
                        case ConstantPool.JVM_CONSTANT_Float: {
                            float aFloat = (float) constantPool.get(key);
                            byte[] bytes = BytesConverter.toBytes(aFloat);
                            frame.getStack().push(new StackValue(BasicType.T_FLOAT, bytes));
                            break;
                        }
                        default:
                            throw new Exception("未解析的类型");

                    }


                    break;
                }
                case ByteCodes._invokevirtual: {

                    logger.debug("执行指令：{}", "invokevirtual");
                    int key = BytesConverter.toInt(byteCode.read(2));

                    String classInfo = constantPool.getClassInfoByRefInfo(key);

                    String methodName = constantPool.getNameByRefInfo(key);

                    String descriptorName = constantPool.getTypeByRefInfo(key);

                    //java类走反射逻辑
                    if (classInfo.startsWith("java")) {
                        DescriptorStream descriptorStream = new DescriptorStream(descriptorName);
                        descriptorStream.parseMethod();


                        Class<?>[] parameterTypes = descriptorStream.getParameterTypes();


                        DescriptorStream.ClassInfo returnClass = descriptorStream.getReturnClass();


                        Object[] paramValues = descriptorStream.getParamValues(frame);


                        Object object = frame.getStack().pop().getObject();

                        java.lang.reflect.Method reflectMethod = object.getClass().getMethod(methodName, parameterTypes);


                        Object returnValue = reflectMethod.invoke(object, paramValues);

                        if (returnClass != null) {
                            //TODO 压栈操作需要转化类型？暂时不理解

                            int stackType = returnClass.getStackType();
                            StackValue stackValue;
                            switch (stackType) {
                                case BasicType.T_BOOLEAN: {
                                    boolean b = (boolean) returnValue;
                                    stackValue = new StackValue(stackType, BytesConverter.toBytes(b));
                                    frame.getStack().push(stackValue);
                                    break;
                                }
                                case BasicType.T_CHAR: {
                                    char c = (char) returnValue;
                                    stackValue = new StackValue(stackType, BytesConverter.toBytes(c));
                                    frame.getStack().push(stackValue);
                                    break;
                                }
                                case BasicType.T_FLOAT: {
                                    float f = (float) returnValue;
                                    stackValue = new StackValue(stackType, BytesConverter.toBytes(f));
                                    frame.getStack().push(stackValue);
                                    break;
                                }
                                case BasicType.T_DOUBLE: {
                                    double d = (double) returnValue;
                                    frame.getStack().pushDouble(d);
                                    break;
                                }
                                case BasicType.T_BYTE: {
                                    byte b = (byte) returnValue;
                                    stackValue = new StackValue(stackType, new byte[]{b});
                                    frame.getStack().push(stackValue);
                                    break;
                                }
                                case BasicType.T_SHORT: {
                                    short s = (short) returnValue;
                                    stackValue = new StackValue(stackType, BytesConverter.toBytes(s));
                                    frame.getStack().push(stackValue);
                                    break;
                                }
                                case BasicType.T_INT: {
                                    int i = (int) returnValue;
                                    stackValue = new StackValue(stackType, BytesConverter.toBytes(i));
                                    frame.getStack().push(stackValue);
                                    break;
                                }
                                case BasicType.T_LONG: {
                                    long l = (long) returnValue;
                                    frame.getStack().pushLong(l);
                                    break;
                                }
                                case BasicType.T_OBJECT: {
                                    stackValue = new StackValue(stackType, returnValue);
                                    frame.getStack().push(stackValue);
                                    break;
                                }
                                default:
                                    throw new Exception("未解析的异常");
                            }


                        }

                    } else {
                        throw new Exception("暂时未实现");
                    }


                    break;
                }
                case ByteCodes._return: {
                    logger.debug("执行指令：{}", "return");

                    //弹出栈帧
                    thread.getStack().pop();

                    logger.debug("剩余栈帧数量：{}", thread.getStack().size());


                    break;
                }
                //region iconst
                case ByteCodes._iconst_0: {
                    logger.debug("执行指令：{}", "iconst_0");
                    byte[] bytes = BytesConverter.toBytes(0);
                    frame.getStack().push(new StackValue(BasicType.T_INT, bytes));

                    break;
                }
                case ByteCodes._iconst_1: {
                    logger.debug("执行指令：{}", "iconst_1");
                    byte[] bytes = BytesConverter.toBytes(1);
                    frame.getStack().push(new StackValue(BasicType.T_INT, bytes));

                    break;
                }
                case ByteCodes._iconst_2: {
                    logger.debug("执行指令：{}", "iconst_2");
                    byte[] bytes = BytesConverter.toBytes(2);
                    frame.getStack().push(new StackValue(BasicType.T_INT, bytes));

                    break;
                }
                case ByteCodes._iconst_3: {
                    logger.debug("执行指令：{}", "iconst_3");
                    byte[] bytes = BytesConverter.toBytes(3);
                    frame.getStack().push(new StackValue(BasicType.T_INT, bytes));

                    break;
                }
                case ByteCodes._iconst_4: {
                    logger.debug("执行指令：{}", "iconst_4");
                    byte[] bytes = BytesConverter.toBytes(4);
                    frame.getStack().push(new StackValue(BasicType.T_INT, bytes));

                    break;
                }
                case ByteCodes._iconst_5: {
                    logger.debug("执行指令：{}", "iconst_5");
                    byte[] bytes = BytesConverter.toBytes(5);
                    frame.getStack().push(new StackValue(BasicType.T_INT, bytes));

                    break;
                }
                //endregion
                case ByteCodes._ldc2_w: {
                    logger.debug("执行指令：{}", "ldc2_w");
                    int key = BytesConverter.toInt(byteCode.read(2));
                    //long or double
                    int constantPoolTag = constantPool.getTag(key);

                    switch (constantPoolTag) {
                        case ConstantPool.JVM_CONSTANT_Long: {

                            long l = (long) constantPool.get(key);
                            frame.getStack().pushLong(l);
//                            frame.getStack().push(new StackValue(BasicType.T_LONG, l));

                            break;
                        }
                        case ConstantPool.JVM_CONSTANT_Double: {

                            double d = (double) constantPool.get(key);
                            frame.getStack().pushDouble(d);

                            break;
                        }
                        default:
                            throw new Exception("未知的格式");

                    }


                    break;
                }
                //region istore
                case ByteCodes._istore_1: {
                    logger.debug("执行指令：{}", "istore_1");

                    StackValue value = frame.getStack().pop();
                    frame.getLocals().add(1, value);
                    break;
                }
                case ByteCodes._istore_2: {
                    logger.debug("执行指令：{}", "istore_2");

                    StackValue value = frame.getStack().pop();
                    frame.getLocals().add(2, value);
                    break;
                }
                case ByteCodes._istore_3: {
                    logger.debug("执行指令：{}", "istore_3");

                    StackValue value = frame.getStack().pop();
                    frame.getLocals().add(3, value);
                    break;
                }
                //endregion
                //region iload
                case ByteCodes._iload_1: {
                    logger.debug("执行指令：{}", "iload_1");

                    StackValue stackValue = frame.getLocals().get(1);
                    frame.getStack().push(stackValue);
                    break;
                }
                case ByteCodes._iload_2: {
                    logger.debug("执行指令：{}", "iload_2");

                    StackValue stackValue = frame.getLocals().get(2);
                    frame.getStack().push(stackValue);
                    break;
                }
                case ByteCodes._iload_3: {
                    logger.debug("执行指令：{}", "iload_3");

                    StackValue stackValue = frame.getLocals().get(3);
                    frame.getStack().push(stackValue);
                    break;
                }
                //endregion iload
                //region lstore
                case ByteCodes._lstore_1: {
                    logger.debug("执行指令：{}", "lstore_1");

                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();
                    frame.getLocals().add(1, value2);
                    frame.getLocals().add(2, value1);
                    break;
                }
                case ByteCodes._lstore_2: {
                    logger.debug("执行指令：{}", "lstore_2");

                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();
                    frame.getLocals().add(2, value2);
                    frame.getLocals().add(3, value1);
                    break;
                }
                case ByteCodes._lstore_3: {
                    logger.debug("执行指令：{}", "lstore_3");

                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();
                    frame.getLocals().add(3, value2);
                    frame.getLocals().add(4, value1);
                    break;
                }
                //endregion
                //region lload
                case ByteCodes._lload_1: {
                    logger.debug("执行指令：{}", "lload_1");

                    StackValue value1 = frame.getLocals().get(1);
                    StackValue value2 = frame.getLocals().get(2);
                    frame.getStack().push(value1);
                    frame.getStack().push(value2);

                    break;
                }
                case ByteCodes._lload_2: {
                    logger.debug("执行指令：{}", "lload_2");

                    StackValue value1 = frame.getLocals().get(2);
                    StackValue value2 = frame.getLocals().get(3);
                    frame.getStack().push(value1);
                    frame.getStack().push(value2);

                    break;
                }
                case ByteCodes._lload_3: {
                    logger.debug("执行指令：{}", "lload_3");

                    StackValue value1 = frame.getLocals().get(3);
                    StackValue value2 = frame.getLocals().get(4);
                    frame.getStack().push(value1);
                    frame.getStack().push(value2);

                    break;
                }
                //endregion
                //region dstore
                case ByteCodes._dstore_1: {
                    logger.debug("执行指令：{}", "dstore_1");

                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();
                    frame.getLocals().add(1, value2);
                    frame.getLocals().add(2, value1);
                    break;
                }
                case ByteCodes._dstore_2: {
                    logger.debug("执行指令：{}", "dstore_2");

                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();
                    frame.getLocals().add(2, value2);
                    frame.getLocals().add(3, value1);
                    break;
                }
                case ByteCodes._dstore_3: {
                    logger.debug("执行指令：{}", "dstore_3");

                    StackValue value1 = frame.getStack().pop();
                    StackValue value2 = frame.getStack().pop();
                    frame.getLocals().add(3, value2);
                    frame.getLocals().add(4, value1);
                    break;
                }
                //endregion
                //region dload
                case ByteCodes._dload_1: {
                    logger.debug("执行指令：{}", "dload_1");

                    StackValue value1 = frame.getLocals().get(1);
                    StackValue value2 = frame.getLocals().get(2);
                    frame.getStack().push(value1);
                    frame.getStack().push(value2);

                    break;
                }
                case ByteCodes._dload_2: {
                    logger.debug("执行指令：{}", "dload_2");

                    StackValue value1 = frame.getLocals().get(2);
                    StackValue value2 = frame.getLocals().get(3);
                    frame.getStack().push(value1);
                    frame.getStack().push(value2);

                    break;
                }
                case ByteCodes._dload_3: {
                    logger.debug("执行指令：{}", "dload_3");

                    StackValue value1 = frame.getLocals().get(3);
                    StackValue value2 = frame.getLocals().get(4);
                    frame.getStack().push(value1);
                    frame.getStack().push(value2);

                    break;
                }
                //endregion
                //region fstore
                case ByteCodes._fstore_1: {
                    logger.debug("执行指令：{}", "fstore_1");

                    StackValue value = frame.getStack().pop();
                    frame.getLocals().add(1, value);
                    break;
                }
                case ByteCodes._fstore_2: {
                    logger.debug("执行指令：{}", "fstore_2");

                    StackValue value = frame.getStack().pop();
                    frame.getLocals().add(2, value);
                    break;
                }
                case ByteCodes._fstore_3: {
                    logger.debug("执行指令：{}", "fstore_3");

                    StackValue value = frame.getStack().pop();
                    frame.getLocals().add(3, value);
                    break;
                }
                //endregion
                //region fload
                case ByteCodes._fload_1: {
                    logger.debug("执行指令：{}", "fload_1");

                    StackValue stackValue = frame.getLocals().get(1);
                    frame.getStack().push(stackValue);
                    break;
                }
                case ByteCodes._fload_2: {
                    logger.debug("执行指令：{}", "fload_2");

                    StackValue stackValue = frame.getLocals().get(2);
                    frame.getStack().push(stackValue);
                    break;
                }
                case ByteCodes._fload_3: {
                    logger.debug("执行指令：{}", "fload_3");

                    StackValue stackValue = frame.getLocals().get(3);
                    frame.getStack().push(stackValue);
                    break;
                }
                //endregion
                case ByteCodes._bipush: {
                    logger.debug("执行指令：{}", "bipush");

                    int value = BytesConverter.toInt(byteCode.read());

                    byte[] bytes = BytesConverter.toBytes(value);
                    frame.getStack().push(new StackValue(BasicType.T_INT, bytes));

                    break;
                }
                case ByteCodes._sipush: {
                    logger.debug("执行指令：{}", "sipush");

                    short value = BytesConverter.toShort(byteCode.read(2));

                    byte[] bytes = BytesConverter.toBytes(value);

                    frame.getStack().push(new StackValue(BasicType.T_SHORT, bytes));


                    break;
                }
                //region i2
                case ByteCodes._i2l: {
                    logger.debug("执行指令：{}", "i2l");


                    StackValue stackValue = frame.getStack().pop();

//                    int value = stackValue.getVal();
//                    long aLong = value & 0x00FF;

                    byte[] bytes = stackValue.getBytes();
                    long aLong = BytesConverter.toLong(bytes);

                    frame.getStack().pushLong(aLong);

                    break;
                }
                case ByteCodes._i2f: {
                    logger.debug("执行指令：{}", "i2f");


                    StackValue stackValue = frame.getStack().pop();

                    byte[] bytes = stackValue.getBytes();

                    int anInt = BytesConverter.toInt(bytes);

                    float aFloat = (float) anInt;
                    bytes = BytesConverter.toBytes(aFloat);

                    frame.getStack().push(new StackValue(BasicType.T_FLOAT, bytes));

                    break;
                }
                case ByteCodes._i2d: {
                    logger.debug("执行指令：{}", "i2d");


                    StackValue stackValue = frame.getStack().pop();

                    byte[] bytes = stackValue.getBytes();
                    int anInt = BytesConverter.toInt(bytes);

                    double aDouble = anInt;

                    frame.getStack().pushDouble(aDouble);

                    break;
                }
                //endregion
                default:
                    throw new Exception("未解析的字节码");


            }


        }


    }


}

class ByteCode {

    private final byte[] codeBytes;

    private int index = 0;

    ByteCode(byte[] codeBytes) {
        this.codeBytes = codeBytes;
    }

    public byte read() throws Exception {
        if (index >= codeBytes.length) {
            throw new Exception("字节码已经读完了");
        }
        return codeBytes[index++];
    }

    public byte[] read(int length) throws Exception {

        if (index + length >= codeBytes.length) {
            throw new Exception("字节码没有那么长");
        }
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = codeBytes[index++];
        }

        return result;
    }

    public boolean isTail() {
        return index >= codeBytes.length;
    }

}

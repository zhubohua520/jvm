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


                        DescriptorStream.ReturnClass returnClass = descriptorStream.getReturnClass();


                        Object[] paramValues = descriptorStream.getParamValues(frame);


                        Object object = frame.getStack().pop().getObject();

                        java.lang.reflect.Method reflectMethod = object.getClass().getMethod(methodName, parameterTypes);


                        Object returnValue = reflectMethod.invoke(object, paramValues);

                        if (returnClass != null) {
                            //TODO 压栈操作需要转化类型？暂时不理解
                            frame.getStack().push(new StackValue(returnClass.getStackType(), returnValue));
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
                case ByteCodes._iconst_1: {
                    logger.debug("执行指令：{}", "iconst_1");

                    frame.getStack().push(new StackValue(BasicType.T_INT, 1));

                    break;
                }
                case ByteCodes._ldc2_w: {
                    logger.debug("执行指令：{}", "ldc2_w");
                    int key = BytesConverter.toInt(byteCode.read(2));
                    //long or double
                    int constantPoolTag = constantPool.getTag(key);

                    switch (constantPoolTag) {
                        case ConstantPool.JVM_CONSTANT_Long: {

                            long l = (long) constantPool.get(key);
                            frame.getStack().push(new StackValue(BasicType.T_LONG, l));

                            break;
                        }
                        case ConstantPool.JVM_CONSTANT_Double: {

                            double d = (double) constantPool.get(key);
                            //TODO 这里没写完
                            frame.getStack().pushDouble(d);

                            break;
                        }
                        default:
                            throw new Exception("未知的格式");

                    }


                    break;
                }
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

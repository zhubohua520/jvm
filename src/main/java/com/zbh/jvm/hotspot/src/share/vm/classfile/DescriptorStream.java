package com.zbh.jvm.hotspot.src.share.vm.classfile;

import com.zbh.jvm.hotspot.src.share.vm.runtime.JavaVFrame;
import com.zbh.jvm.hotspot.src.share.vm.runtime.StackValue;
import com.zbh.jvm.hotspot.src.share.vm.utilities.BasicType;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DescriptorStream {

    private static final Logger logger = LoggerFactory.getLogger(BootClassLoader.class);

    private String descriptor;

    private List<Class<?>> parameterTypes = new ArrayList<>(); //方法参数类型

    //有返回类型证明有返回值，返回类型为null,证明没有返回值
    private ReturnClass returnClass; //返回类型


    private final Map<String, Class<?>> classMap = new HashMap<>();

    public Class<?>[] getParameterTypes() {
        return parameterTypes.toArray(new Class<?>[0]);
    }

    public Object[] getParamValues(JavaVFrame frame) throws Exception {

        Object[] objects = new Object[parameterTypes.size()];
        for (int i = 0; i < parameterTypes.size(); i++) {
            StackValue stackValue = frame.getStack().peek();

            switch (stackValue.getType()) {
                case BasicType.T_OBJECT: {
                    stackValue = frame.getStack().pop();
                    objects[i] = stackValue.getObject();
                    break;
                }
                case BasicType.T_INT: {
                    stackValue = frame.getStack().pop();
                    objects[i] = stackValue.getVal();
                    break;
                }
                case BasicType.T_LONG: {
                    stackValue = frame.getStack().pop();
                    objects[i] = stackValue.getObject();
                    break;
                }
                case BasicType.T_DOUBLE: {
                    double d = frame.getStack().popDouble();
                    objects[i] = d;
                    break;
                }
                default:
                    throw new Exception("未解析的类型");
            }
        }

        return objects;
    }


    public DescriptorStream(String descriptor) {
        this.descriptor = descriptor;
    }


    public void parseMethod() throws Exception {

        boolean phase = true;

        byte[] bytes = descriptor.getBytes();


        int index = 0;
        while (index < bytes.length) {
            byte aByte = bytes[index];

            switch (aByte) {
                case BasicType.JVM_SIGNATURE_FUNC: {
                    logger.debug("开始解析函数.....");
                    break;
                }
                case BasicType.JVM_SIGNATURE_ENDFUNC: {
                    logger.debug("函数解析完成.....");
                    phase = false;
                    break;
                }
                case BasicType.JVM_SIGNATURE_CLASS: {
                    logger.debug("开始解析对象...");

                    index++;
                    StringBuilder sb = new StringBuilder();

                    while (true) {
                        aByte = bytes[index];
                        if (aByte == BasicType.JVM_SIGNATURE_ENDCLASS) {
                            break;
                        } else {
                            sb.append((char) aByte);
                        }
                        index++;

                    }
                    logger.debug("对象路径为：{}", sb);


                    Class<?> clazz = getClassByPath(sb.toString());
                    if (phase) {
                        parameterTypes.add(clazz);
                    } else {
                        returnClass = new ReturnClass();
                        returnClass.setAClass(clazz);
                        returnClass.setStackType(BasicType.T_OBJECT);
                    }


                    break;
                }
                case BasicType.JVM_SIGNATURE_VOID: {
                    logger.debug("无返回值");
                    returnClass = null;
                    break;
                }
                case BasicType.JVM_SIGNATURE_INT: {
                    logger.debug("类型为int");

                    if (phase) {
                        parameterTypes.add(int.class);
                    } else {
                        returnClass = new ReturnClass();
                        returnClass.setAClass(int.class);
                        returnClass.setStackType(BasicType.T_INT);
                    }
                    break;
                }
                case BasicType.JVM_SIGNATURE_LONG: {
                    logger.debug("类型为long");

                    if (phase) {
                        parameterTypes.add(long.class);
                    } else {
                        returnClass = new ReturnClass();
                        returnClass.setAClass(long.class);
                        returnClass.setStackType(BasicType.T_LONG);
                    }
                    break;
                }
                case BasicType.JVM_SIGNATURE_DOUBLE: {
                    logger.debug("类型为double");
                    if (phase) {
                        parameterTypes.add(double.class);
                    } else {
                        returnClass = new ReturnClass();
                        returnClass.setAClass(double.class);
                        returnClass.setStackType(BasicType.T_DOUBLE);
                    }
                    break;
                }
                default:
                    throw new Exception("等待解析...");

            }

            index++;
        }


    }


    private Class<?> getClassByPath(String classPath) throws ClassNotFoundException {
        String className = classPath.replace('/', '.');

        Class<?> clazz = classMap.get(className);


        if (clazz == null) {
            clazz = Class.forName(className);
            classMap.put(className, clazz);
        }

        return clazz;
    }


    @Data
    public class ReturnClass {

        private Class<?> aClass;

        private int stackType;

    }
}

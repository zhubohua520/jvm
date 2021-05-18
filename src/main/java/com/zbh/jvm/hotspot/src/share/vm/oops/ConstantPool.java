package com.zbh.jvm.hotspot.src.share.vm.oops;

import java.util.HashMap;
import java.util.Map;

public class ConstantPool {

    public static final int JVM_CONSTANT_Utf8 = 1;
    public static final int JVM_CONSTANT_Unicode = 2;   /* unused */
    public static final int JVM_CONSTANT_Integer = 3;
    public static final int JVM_CONSTANT_Float = 4;
    public static final int JVM_CONSTANT_Long = 5;
    public static final int JVM_CONSTANT_Double = 6;
    public static final int JVM_CONSTANT_Class = 7;
    public static final int JVM_CONSTANT_String = 8;
    public static final int JVM_CONSTANT_Fieldref = 9;
    public static final int JVM_CONSTANT_Methodref = 10;
    public static final int JVM_CONSTANT_InterfaceMethodref = 11;
    public static final int JVM_CONSTANT_NameAndType = 12;
    public static final int JVM_CONSTANT_MethodHandle = 15; /* JSR 292 */
    public static final int JVM_CONSTANT_MethodType = 16;   /* JSR 292 */
    public static final int JVM_CONSTANT_InvokeDynamic = 18;    /* JSR 292 */
    public static final int JVM_CONSTANT_ExternalMax = 18;  /* Last tag found in classfiles */


    Map<Integer, Object> cpMap;

    public ConstantPool(int capacity) {

        cpMap = new HashMap<>(capacity);
    }

    public Object put(Integer key, Object value) {
        return cpMap.put(key, value);
    }

    public Object get(Integer key) {
        return cpMap.get(key);
    }

    public String getClassInfo(Integer key) throws Exception {

        Object o = cpMap.get(key);
        if (o instanceof Integer) {
            return getClassInfo((Integer) o);
        } else if (o instanceof String) {
            return (String) o;
        } else {
            throw new Exception("不应该发生的异常");
        }
    }
}

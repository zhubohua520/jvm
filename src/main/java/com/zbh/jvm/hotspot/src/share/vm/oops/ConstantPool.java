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


    private final Map<Integer, Object> cpMap;

    private int[] tags;

    public int getTag(int key) {
        return tags[key];
    }

    public void setTag(int key, int constantType) {
        tags[key] = constantType;
    }

    public ConstantPool(int capacity) {

        cpMap = new HashMap<>(capacity);
        tags = new int[capacity];
    }

    public Object put(Integer key, Object value) {
        return cpMap.put(key, value);
    }

    public Object get(Integer key) {
        return cpMap.get(key);
    }

    public String getStr(Integer key) throws Exception {

        Object o = cpMap.get(key);
        if (o instanceof Integer) {
            return getStr((Integer) o);
        } else if (o instanceof String) {
            return (String) o;
        } else {
            throw new Exception("不应该发生的异常");
        }
    }

//    public float getFloat(Integer key){
//        return (float)cpMap.get(key);
//    }


    /**
     * 三种常量池类型：Fieldref_info,Methodref_info,InterfaceMethodref_info的结构是一样的
     *
     * @param key 这三种常量池的key
     * @return
     * @throws Exception
     */
    public String getClassInfoByRefInfo(Integer key) throws Exception {
        int refInfoKey = (int) cpMap.get(key);

        int classInfoKey = refInfoKey >> 16;

        return getStr(classInfoKey);
    }

    /**
     * 三种常量池类型：Fieldref_info,Methodref_info,InterfaceMethodref_info的结构是一样的
     *
     * @param key 这三种常量池的key
     * @return 可以是方法名或者属性名
     * @throws Exception
     */
    public String getNameByRefInfo(Integer key) {
        int refInfoKey = (int) cpMap.get(key);

        int nameAndTypeInfoKey = refInfoKey & 0xFF;

        int nameAndTypeInfo = (int) get(nameAndTypeInfoKey);


        int nameKey = nameAndTypeInfo >> 16;

        return (String) get(nameKey);
    }

    /**
     * 三种常量池类型：Fieldref_info,Methodref_info,InterfaceMethodref_info的结构是一样的
     *
     * @param key 这三种常量池的key
     * @return 描述符
     */
    public String getTypeByRefInfo(Integer key) {
        int refInfoKey = (int) cpMap.get(key);

        int nameAndTypeInfoKey = refInfoKey & 0xFF;

        int nameAndTypeInfo = (int) get(nameAndTypeInfoKey);


        int typeKey = nameAndTypeInfo & 0xFF;

        return (String) get(typeKey);
    }


}

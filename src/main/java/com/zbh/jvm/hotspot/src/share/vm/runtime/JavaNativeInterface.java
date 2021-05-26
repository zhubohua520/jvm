package com.zbh.jvm.hotspot.src.share.vm.runtime;

import com.zbh.jvm.hotspot.src.share.vm.interpreter.BytecodeInterpreter;
import com.zbh.jvm.hotspot.src.share.vm.oops.AttributeInfo;
import com.zbh.jvm.hotspot.src.share.vm.oops.CodeAttribute;
import com.zbh.jvm.hotspot.src.share.vm.oops.Method;
import com.zbh.jvm.hotspot.src.share.vm.utilities.BasicType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaNativeInterface {

    private final static Logger logger = LoggerFactory.getLogger(JavaNativeInterface.class);

    public static void callStaticMethod(Method method) throws Exception {

        JavaThread currentThread = Threads.getCurrentThread();

        int accessFlags = method.getAccessFlags();

        //变量都是2的整次方，所以可以这么写
        if ((accessFlags & BasicType.JVM_ACC_STATIC) == 0) {
            throw new Exception("只能执行静态方法！");
        }

        AttributeInfo attribute = method.getAttributes()[0];

        if (attribute instanceof CodeAttribute) {
            CodeAttribute codeAttribute = (CodeAttribute) attribute;

            //创建栈帧
            JavaVFrame frame = new JavaVFrame(method, codeAttribute.getMaxLocals());

            //压入栈顶
            currentThread.getStack().push(frame);

            logger.debug("第{}个栈帧", currentThread.getStack().size());

            //字节码解释器执行任务
            BytecodeInterpreter.run(currentThread, method);


        } else {
            throw new Exception("不应该发生的异常");
        }


    }

}

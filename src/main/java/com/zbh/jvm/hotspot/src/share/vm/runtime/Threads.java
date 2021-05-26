package com.zbh.jvm.hotspot.src.share.vm.runtime;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Threads {

    private static List<Thread> threadList;

    private static Thread currentThread;

    static {
        threadList = new ArrayList<>();
    }

    public static JavaThread getCurrentThread() {
        return (JavaThread) currentThread;
    }

    public static void setCurrentThread(Thread thread) {
        currentThread = thread;
    }
}

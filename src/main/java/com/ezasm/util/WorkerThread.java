package com.ezasm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WorkerThread extends ThreadPoolExecutor {

    private final Map<Runnable, Thread> executingThreads;

    public WorkerThread() {
        super(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        executingThreads = new HashMap<>();
    }

    @Override
    protected synchronized void beforeExecute(Thread thread, Runnable runnable) {
        super.beforeExecute(thread, runnable);
        executingThreads.put(runnable, thread);
    }

    @Override
    protected synchronized void afterExecute(Runnable runnable, Throwable throwable) {
        super.afterExecute(runnable, throwable);
        executingThreads.remove(runnable);
    }

    public synchronized void kill() {
        for (Thread thread : executingThreads.values()) {
            thread.stop(); // since just interrupting would not work
        }
        executingThreads.clear();
    }
}

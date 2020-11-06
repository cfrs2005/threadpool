package com.aj.tl;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * @author zhangqingyue
 * @date 2020/11/6
 */
public class FutureTask<T> implements Runnable, Future {

    private static final int NEW = 0;
    private static final int FINISHED = 1;
    private static final int EXCEPTION = 2;

    private Callable<T> task;
    private AtomicInteger state = new AtomicInteger(NEW);
    private AtomicReference<Thread> caller = new AtomicReference<>();

    private Object result;

    public FutureTask(Callable<T> task) {
        this.task = task;
    }

    @Override
    public Object get() {
        int s = state.get();
        if (s == NEW) {
            boolean flag = false;
            for (; ; ) {
                s = state.get();
                if (s > NEW) {
                    break;
                } else if (!flag) {
                    flag = caller.compareAndSet(null, Thread.currentThread());
                } else {
                    LockSupport.park();
                }
            }

        }
        if (s == FINISHED) {
            return (T) result;

        }
        throw new RuntimeException((Throwable) result);
    }

    @Override
    public void run() {

        if (state.get() != NEW) {
            return;
        }
        try {
            T t = task.call();
            if (state.compareAndSet(NEW, FINISHED)) {
                this.result = t;
                finish();
            }


        } catch (Exception e) {
            if (state.compareAndSet(NEW, EXCEPTION)) {
                this.result = e;
                finish();
            }
        }
    }

    private void finish() {
        for (Thread c; (c = caller.get()) != null; ) {
            if (caller.compareAndSet(c, null)) {
                LockSupport.unpark(c);

            }
        }
    }
}

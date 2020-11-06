package com.aj.tl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * @author zhangqingyue
 * @date 2020/11/6
 */
class AcThreadPoolFutureExecutor extends AcThreadPool implements FutureExecutor, Executor {

    public AcThreadPoolFutureExecutor(String name,
                                      int coreSize,
                                      int maxSize,
                                      BlockingQueue<Runnable> taskQueue,
                                      RejectPolicy rejectPolicy) {
        super(name, coreSize, maxSize, taskQueue, rejectPolicy);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        FutureTask<T> futureTask = new FutureTask<>(task);
        execute(futureTask);
        return futureTask;
    }


}

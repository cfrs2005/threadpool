package com.aj.tl;

import java.util.concurrent.Callable;

/**
 * @author zhangqingyue
 * @date 2020/11/6
 */
public interface FutureExecutor extends Executor{
    /**
     *
     * @param task
     * @param <T>
     * @return
     */
    <T> Future<T> submit(Callable<T> task);

}

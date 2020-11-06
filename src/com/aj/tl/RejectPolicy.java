package com.aj.tl;

/**
 * @author zhangqingyue
 * @date 2020/11/6
 */
public interface RejectPolicy {

    /**
     * @param task
     * @param acThreadPool
     */
    void reject(Runnable task, AcThreadPool acThreadPool);
}

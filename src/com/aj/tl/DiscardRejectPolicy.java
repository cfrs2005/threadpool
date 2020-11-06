package com.aj.tl;

/**
 * @author zhangqingyue
 * @date 2020/11/6
 */
public class DiscardRejectPolicy implements RejectPolicy {
    @Override
    public void reject(Runnable task, AcThreadPool acThreadPool) {
        System.out.println("discard one task");
    }
}

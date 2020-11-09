package com.aj.tl;

import java.util.concurrent.CyclicBarrier;

/**
 * @author zhangqingyue
 * @date 2020/11/9
 */
public class TestCyclicBarrier {
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

    public static void main(String[] args) {
        new TestCyclicBarrier().begin();
    }

    public void begin() {
        for (int i = 0; i < 5; i++) {
            new Thread(new Student()).start();
        }

    }

    private class Student implements Runnable {
        @Override
        public void run() {
            try {
                // 该学生正在赶往饭店的路上
                Thread.sleep(2000);
                // 到了就等着，等其他人都到了，就进饭店
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // TODO:大家都到了，进去吃饭吧！
        }
    }
}

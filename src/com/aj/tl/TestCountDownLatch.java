package com.aj.tl;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhangqingyue
 * @date 2020/11/9
 */
public class TestCountDownLatch {

    // 构造方法指明计数数量
    private CountDownLatch countDownLatch = new CountDownLatch(4);

    public static void main(String[] args) {
        TestCountDownLatch testCountDownLatch = new TestCountDownLatch();
        testCountDownLatch.begin();
    }

    /**
     * 运动员类
     */
    private class Runner implements Runnable {
        private int result;

        public Runner(int result) {
            this.result = result;
        }

        @Override
        public void run() {
            try {
                System.out.println("模拟跑 " + result + " 秒");
                // 模拟跑了多少秒
                Thread.sleep(result * 1000);
                // 跑完了就计数器减1
                countDownLatch.countDown();
                System.out.println("result " + result + " 跑完了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void begin() {
        System.out.println("赛跑开始");
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 4; i++) {
            // 随机设置每个运动员跑多少秒结束
            int result = random.nextInt(3) + 1;
            new Thread(new Runner(result)).start();
        }
        try {
            // 线程等待倒数为 0
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("所有人都跑完了，裁判开始算成绩");
    }

}

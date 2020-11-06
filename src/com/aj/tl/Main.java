package com.aj.tl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.IntStream;

/**
 * @author zhangqingyue
 * @date 2020/11/6
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        FutureExecutor executor = new AcThreadPoolFutureExecutor("test", 2, 4,
                new ArrayBlockingQueue<>(6), new DiscardRejectPolicy());
        List<Future<Integer>> list = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> {
            Future<Integer> future = executor.submit(() -> {
                Thread.sleep(1000);
                System.out.println("running: " + i);
                return i;
            });

            list.add(future);
        });

        list.forEach(item -> System.out.println(item.get()));
    }

}

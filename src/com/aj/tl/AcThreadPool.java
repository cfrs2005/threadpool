package com.aj.tl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author zhangqingyue
 * @date 2020/11/6
 */
public class AcThreadPool implements Executor {

    private String name;
    private int coreSize;
    private int maxSize;
    private BlockingQueue<Runnable> taskQueue;
    private RejectPolicy rejectPolicy;

    private AtomicInteger runningCount = new AtomicInteger(0);

    private AtomicInteger threadCounter = new AtomicInteger(0);


    /**
     * @param args
     */
    public static void main(String[] args) {
        Executor threadPool = new AcThreadPool("aj", 5, 8, new ArrayBlockingQueue<>(5), new DiscardRejectPolicy());
        AtomicInteger counter = new AtomicInteger(0);
        IntStream.range(0, 15).forEach(index -> threadPool.execute(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("running " + System.currentTimeMillis() + " : " + counter.incrementAndGet());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }));
    }

    /**
     * @param name
     * @param coreSize
     * @param maxSize
     * @param taskQueue
     * @param rejectPolicy
     */
    public AcThreadPool(String name, int coreSize, int maxSize, BlockingQueue<Runnable> taskQueue, RejectPolicy rejectPolicy) {
        this.name = name;
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.taskQueue = taskQueue;
        this.rejectPolicy = rejectPolicy;
    }


    /**
     * @param newTask
     * @param isCore
     * @return
     */
    private boolean addWorker(Runnable newTask, boolean isCore) {
        while (true) {
            int count = runningCount.get();
            int maxNum = isCore ? coreSize : maxSize;
            if (count >= maxNum) {
                return false;
            }
            if (runningCount.compareAndSet(count, count + 1)) {
                String threadName = name + threadCounter.incrementAndGet();
                new Thread(() -> {
                    System.out.println("thread name : " + Thread.currentThread().getName());
                    Runnable task = newTask;
                    while (task != null || (task = getTask()) != null) {
                        try {
                            task.run();
                        } finally {
                            task = null;
                        }
                    }
                }, threadName).start();
                break;
            }
        }
        return true;
    }

    /**
     * @return
     */
    private Runnable getTask() {
        try {
            return taskQueue.take();
        } catch (InterruptedException e) {
            runningCount.decrementAndGet();
            return null;
        }
    }


    @Override
    public void execute(Runnable task) {
        int count = runningCount.get();
        if (count < coreSize) {
            if (addWorker(task, true)) {
                return;
            }
        }
        if (!taskQueue.offer(task)) {
            if (!addWorker(task, false)) {
                rejectPolicy.reject(task, this);
            }
        }
    }
}

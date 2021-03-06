package ru.fizteh.fivt.students.ocksumoron.threads;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ocksumoron on 14.12.15.
 */
public class RollCall {

    private static volatile boolean allReady;
    private static boolean finish;
    //private static CountDownLatch countDownLatch;
    private static volatile Random chance = new Random();
    private static CyclicBarrier beginAnswer;
    private static CyclicBarrier endAnswer;
    private static final int NO_PROBABILITY = 10;

    private static class Player extends Thread {

        public static Semaphore semaphore = new Semaphore(0);

        public void setLock() {
            semaphore.release();
        }

        @Override
        public void run() {
            while (!finish) {
                try {
                    beginAnswer.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                if (chance.nextInt(100) > NO_PROBABILITY) {
                    System.out.println("Yes");
                } else {
                    System.out.println("No");
                    allReady = false;
                }
                try {
                    endAnswer.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public static void main(String[] args) {
        finish = false;

        int n = Integer.valueOf(args[0]);
        beginAnswer = new CyclicBarrier(n + 1);
        endAnswer = new CyclicBarrier(n + 1);
        Player[] players = new Player[n];
        for (int i = 0; i < n; i++) {
            players[i] = new Player();
            players[i].start();
        }
        finish = false;
        while (!finish) {
            System.out.println("Are you ready?");
            allReady = true;
            try {
                beginAnswer.await();
            } catch (BrokenBarrierException | InterruptedException e) {
                e.printStackTrace();
            }
            beginAnswer.reset();
            try {
                endAnswer.await();
                if (allReady) {
                    finish = true;
                    for (int i = 0; i < n; i++) {
                        players[i].join();
                    }
                    System.exit(0);
                }
            } catch (BrokenBarrierException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

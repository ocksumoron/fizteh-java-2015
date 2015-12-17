package ru.fizteh.fivt.students.ocksumoron.threads.blockingqueue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ocksumoron on 16.12.15.
 */
public class BlockingQueue<T> {

    private Queue<T> queue;
    private static int maxSize;
    private Lock lock = new ReentrantLock();
    private Condition notEnoughSpace = lock.newCondition();
    private Condition notEnoughElements = lock.newCondition();

    BlockingQueue(int size) {
        queue = new ArrayDeque<T>();
        maxSize = size;
    }

    synchronized void offer(List<T> toAdd) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() + toAdd.size() > maxSize) {
                notEnoughSpace.await();
            }
            queue.addAll(toAdd);
            notEnoughElements.notifyAll();
        } finally {
            lock.unlock();
        }
    }

    synchronized List<T> take(int n) throws InterruptedException {
        lock.lock();
        List<T> ans = new ArrayList<T>();
        try {
            while (queue.size() < n) {
                notEnoughElements.await();
            }
            for (int i = 0; i < n; ++i) {
                ans.add(queue.remove());
            }
            notEnoughElements.notifyAll();
            return ans;
        } finally {
            lock.unlock();
        }
    }

    synchronized void offer(List<T> toAdd, long timeout) throws  InterruptedException {
        lock.lock();
        long waitingTime = timeout;
        final long startTime = System.currentTimeMillis();
        try {
            while (queue.size() + toAdd.size() > maxSize && waitingTime > 0) {
                notEnoughElements.await(waitingTime, TimeUnit.MILLISECONDS);
                waitingTime = timeout - (System.currentTimeMillis() - startTime);
            }
            if (queue.size() + toAdd.size() <= maxSize) {
                queue.addAll(toAdd);
                notEnoughElements.notifyAll();
            }
        } finally {
            lock.unlock();
        }
    }

    synchronized List<T> take(int n, long timeout) throws InterruptedException {
        lock.lock();
        List<T> ans = new ArrayList<T>();
        long waitingTime = timeout;
        final long startTime = System.currentTimeMillis();
        try {
            while (queue.size() < n && waitingTime > 0) {
                notEnoughElements.await(waitingTime, TimeUnit.MILLISECONDS);
                waitingTime = timeout - (System.currentTimeMillis() - startTime);
            }
            if (queue.size() >= n) {
                for (int i = 0; i < n; ++i) {
                    ans.add(queue.remove());
                }
                notEnoughElements.notifyAll();
            }
            return ans;
        } finally {
            lock.unlock();
        }
    }
}

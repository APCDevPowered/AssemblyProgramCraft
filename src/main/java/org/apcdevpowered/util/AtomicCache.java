package org.apcdevpowered.util;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class AtomicCache<E>
{
    private final Lock lock = new ReentrantLock();
    private final Condition hasFree = lock.newCondition();
    private final Supplier<E> supplier;
    private volatile int currentSize;
    private final int maxCapacity;
    private final LinkedList<E> usingList = new LinkedList<E>();
    private final LinkedList<E> freeList = new LinkedList<E>();
    
    public AtomicCache(Supplier<E> supplier)
    {
        this(supplier, 0);
    }
    public AtomicCache(Supplier<E> supplier, int maxCapacity)
    {
        this.supplier = supplier;
        this.maxCapacity = maxCapacity;
    }
    public int getCurrentSize()
    {
        return currentSize;
    }
    public int getMaxCapacity()
    {
        return maxCapacity;
    }
    public void free(E element)
    {
        lock.lock();
        try
        {
            if (usingList.remove(element))
            {
                freeList.add(element);
            }
            hasFree.signalAll();
        }
        finally
        {
            lock.unlock();
        }
    }
    public E get() throws InterruptedException
    {
        lock.lock();
        try
        {
            if (!freeList.isEmpty())
            {
                E element = freeList.pop();
                usingList.push(element);
                return element;
            }
            if (maxCapacity <= 0 || currentSize < maxCapacity)
            {
                return createAndGet();
            }
            while (freeList.isEmpty())
            {
                hasFree.await();
            }
            E element = freeList.pop();
            usingList.push(element);
            return element;
        }
        finally
        {
            lock.unlock();
        }
    }
    public E tryGet()
    {
        lock.lock();
        try
        {
            if (!freeList.isEmpty())
            {
                E element = freeList.pop();
                usingList.push(element);
                return element;
            }
            if (maxCapacity <= 0 || currentSize < maxCapacity)
            {
                return createAndGet();
            }
            return null;
        }
        finally
        {
            lock.unlock();
        }
    }
    public E forceGet()
    {
        lock.lock();
        try
        {
            if (!freeList.isEmpty())
            {
                E element = freeList.pop();
                usingList.push(element);
                return element;
            }
            if (maxCapacity <= 0 || currentSize < maxCapacity)
            {
                return createAndGet();
            }
            while (freeList.isEmpty())
            {
                hasFree.awaitUninterruptibly();
            }
            E element = freeList.pop();
            usingList.push(element);
            return element;
        }
        finally
        {
            lock.unlock();
        }
    }
    private E createAndGet()
    {
        E element = supplier.get();
        if (element == null)
        {
            throw new NullPointerException();
        }
        currentSize++;
        usingList.push(element);
        return element;
    }
}

package org.apcdevpowered.util.array;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DynamicSparseArray<T> implements Iterable<T>, Cloneable
{
    public final int initialSize;
    public final int minExpandSide;
    private int length;
    private Object[] arr;
    
    public DynamicSparseArray()
    {
        this(100, 300);
    }
    public DynamicSparseArray(int initialSize, int minExpandSide)
    {
        this.initialSize = initialSize;
        this.minExpandSide = minExpandSide;
        arr = new Object[initialSize];
    }
    public DynamicSparseArray(DynamicSparseArray<T> dynamicSparseArray)
    {
        initialSize = dynamicSparseArray.initialSize;
        minExpandSide = dynamicSparseArray.minExpandSide;
        arr = new Object[dynamicSparseArray.arr.length];
        System.arraycopy(dynamicSparseArray.arr, 0, arr, 0, arr.length);
        length = dynamicSparseArray.length;
    }
    @SuppressWarnings("unchecked")
    public T get(int index)
    {
        if(index < 0)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        if(index >= arr.length || index >= length)
        {
            return null;
        }
        return (T)arr[index];
    }
    public void set(int index, T value)
    {
        if(index < 0)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        if(index >= arr.length)
        {
            arr = Arrays.copyOf(arr, index + 1 + minExpandSide);
        }
        arr[index] = value;
        if(index + 1 > length)
        {
            length = index + 1;
        }
    }
    public void remove(int index)
    {
        if(index < 0)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        if(index >= arr.length || index >= length)
        {
            return;
        }
        if(index == length - 1)
        {
            length--;
        }
        arr[index] = null;
    }
    public void clear()
    {
        Arrays.fill(arr, null);
        length = 0;
    }
    public void add(T value)
    {
        push(value);
    }
    public void push(T value)
    {
        if(length + 1 > arr.length)
        {
            arr = Arrays.copyOf(arr, length + minExpandSide);
        }
        arr[length] = value;
        length++;
    }
    @SuppressWarnings("unchecked")
    public T pop()
    {
        if(length < 1 || arr.length < 1)
        {
            return null;
        }
        length--;
        T tmp = (T)arr[length];
        arr[length] = null;
        return tmp;
    }
    @SuppressWarnings("unchecked")
    public T shift()
    {
        if(length < 1 || arr.length < 1)
        {
            return null;
        }
        T tmp = (T)arr[0];
        arr = Arrays.copyOfRange(arr, 1, arr.length);
        length--;
        return tmp;
    }
    public void addAll(T[] array)
    {
        int addLength = array.length;
        if(length + addLength > arr.length)
        {
            int remaining = arr.length - length;
            int less = addLength - remaining;
            arr = Arrays.copyOf(arr, length + minExpandSide * ((less % minExpandSide == 0) ? (less / minExpandSide) : (less / minExpandSide + 1)));
        }
        for(T t : array)
        {
            arr[length] = t;
            length++;
        }
    }
    public void addAll(DynamicSparseArray<T> array)
    {
        int addLength = array.length;
        if(length + addLength > arr.length)
        {
            int remaining = arr.length - length;
            int less = addLength - remaining;
            arr = Arrays.copyOf(arr, length + minExpandSide * ((less % minExpandSide == 0) ? (less / minExpandSide) : (less / minExpandSide + 1)));
        }
        for(T t : array)
        {
            arr[length] = t;
            length++;
        }
    }
    public void copyData(T[] array, int srcPos, int destPos, int length)
    {
        if(array == null)
        {
            throw new NullPointerException();
        }
        if(srcPos < 0 || destPos < 0 || length < 0 || srcPos + length > array.length)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        int needLength = destPos + length + 1;
        if(needLength > arr.length)
        {
            if(needLength / minExpandSide != 0)
            {
                needLength += (minExpandSide - (needLength % minExpandSide));
            }
            arr = Arrays.copyOf(arr, needLength);
        }
        for(int i = 0;i < length;i++)
        {
            arr[destPos + i] = array[srcPos + i];
            if(destPos + i + 1 > this.length)
            {
                this.length = destPos + i + 1;
            }
        }
    }
    public void copyData(DynamicSparseArray<T> array, int srcPos, int destPos, int length)
    {
        if(array == null)
        {
            throw new NullPointerException();
        }
        if(srcPos < 0 || destPos < 0 || length < 0 || srcPos + length > array.length)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        int needLength = destPos + length + 1;
        if(needLength > arr.length)
        {
            if(needLength / minExpandSide != 0)
            {
                needLength += (minExpandSide - (needLength % minExpandSide));
            }
            arr = Arrays.copyOf(arr, needLength);
        }
        for(int i = 0;i < length;i++)
        {
            arr[destPos + i] = array.get(srcPos + i);
            if(destPos + i + 1 > this.length)
            {
                this.length = destPos + i + 1;
            }
        }
    }
    public Object[] toArray()
    {
        return Arrays.copyOf(arr, (arr.length >= length ? length : arr.length));
    }
    @SuppressWarnings("unchecked")
    public T[] toArray(T[] aobj)
    {
        if(aobj.length < length)
        {
            return (T[])Arrays.copyOf(arr, arr.length >= length ? length : arr.length, aobj.getClass());
        }
        System.arraycopy(arr, 0, aobj, 0, arr.length >= length ? length : arr.length);
        return aobj;
    }
    public void length(int length)
    {
        this.length = (length < 0 ? 0 : length);
        if(arr.length > length)
        {
            Arrays.fill(arr, length, arr.length, (Object)null);
        }
    }
    public int length()
    {
        return length;
    }
    public void size(int size)
    {
        length(size);
    }
    public int size()
    {
        return length();
    }
    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            private int pointer = 0;
            @Override
            public boolean hasNext()
            {
                return pointer < DynamicSparseArray.this.length();
            }
            @Override
            public T next()
            {
                if(pointer < DynamicSparseArray.this.length())
                {
                    return DynamicSparseArray.this.get(pointer++);
                }
                else
                {
                    throw new NoSuchElementException();
                }
            }
            @Override
            public void remove()
            {
                DynamicSparseArray.this.remove(pointer);
            }
        };
    }
    @Override
    public DynamicSparseArray<T> clone()
    {
        return new DynamicSparseArray<T>(this);
    }
}
package org.apcdevpowered.util;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DynamicIntArray implements Iterable<Integer>, Cloneable {
	public final int initialSize;
	public final int minExpandSize;
	private int length;
	private int[] arr;
    
    public DynamicIntArray()
    {
        this(100, 300);
    }
    public DynamicIntArray(int initialSize, int minExpandSize)
    {
        this.initialSize = initialSize;
        this.minExpandSize = minExpandSize;
        arr = new int[initialSize];
    }
    public DynamicIntArray(DynamicIntArray dynamicSparseArray)
    {
        initialSize = dynamicSparseArray.initialSize;
        minExpandSize = dynamicSparseArray.minExpandSize;
        arr = new int[dynamicSparseArray.arr.length];
        System.arraycopy(dynamicSparseArray.arr, 0, arr, 0, arr.length);
        length = dynamicSparseArray.length;
    }
    public int get(int index)
    {
        if (index < 0)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (index >= arr.length || index >= length)
        {
            return 0;
        }
        return arr[index];
    }
    public void set(int index, int value)
    {
        if (index < 0)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        int newLength = index + 1;
        ensureLength(newLength);
        arr[index] = value;
        if (index + 1 > length)
        {
            length = index + 1;
        }
    }
    public void remove(int index)
    {
        if (index < 0)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (index >= arr.length || index >= length)
        {
            return;
        }
        if (index == length - 1)
        {
            length--;
        }
        arr[index] = 0;
    }
    public void clear()
    {
        Arrays.fill(arr, 0);
        length = 0;
    }
    public void add(int value)
    {
        push(value);
    }
    public void push(int value)
    {
        int newLength = length + 1;
        ensureLength(newLength);
        arr[length] = value;
        length++;
    }
    public int pop()
    {
        if (length < 1 || arr.length < 1)
        {
            throw new EmptyStackException();
        }
        length--;
        int tmp = arr[length];
        arr[length] = 0;
        return tmp;
    }
    public int shift()
    {
        if (length < 1 || arr.length < 1)
        {
            throw new EmptyStackException();
        }
        int tmp = arr[0];
        arr = Arrays.copyOfRange(arr, 1, arr.length);
        length--;
        return tmp;
    }
    public void addAll(int[] array)
    {
        int newLength = length + array.length;
        ensureLength(newLength);
        for (int i = 0;i < array.length;i++)
        {
            arr[length] = array[i];
            length++;
        }
    }
    public void addAll(DynamicIntArray array)
    {
        int newLength = length + array.length;
        ensureLength(newLength);
        for (int i = 0;i < array.length;i++)
        {
            arr[length] = array.arr[i];
            length++;
        }
    }
    public void copyData(int[] array, int srcPos, int destPos, int length)
    {
        if (array == null)
        {
            throw new NullPointerException();
        }
        if (srcPos < 0 || destPos < 0 || length < 0 || srcPos + length > array.length)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        int newLength = destPos + length + 1;
        ensureLength(newLength);
        for (int i = 0; i < length; i++)
        {
            arr[destPos + i] = array[srcPos + i];
            if (destPos + i + 1 > this.length)
            {
                this.length = destPos + i + 1;
            }
        }
    }
    public void copyData(DynamicIntArray array, int srcPos, int destPos, int length)
    {
        if (array == null)
        {
            throw new NullPointerException();
        }
        if (srcPos < 0 || destPos < 0 || length < 0 || srcPos + length > array.length)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        int newLength = destPos + length + 1;
        ensureLength(newLength);
        for (int i = 0; i < length; i++)
        {
            arr[destPos + i] = array.get(srcPos + i);
            if (destPos + i + 1 > this.length)
            {
                this.length = destPos + i + 1;
            }
        }
    }
    public int[] toArray()
    {
        return Arrays.copyOf(arr, length);
    }
    public int[] toArray(int[] aobj)
    {
        if (aobj.length < length)
        {
            return Arrays.copyOf(arr, length);
        }
        if (arr.length >= length)
        {
        	System.arraycopy(arr, 0, aobj, 0, length);
        }
        else
        {
        	System.arraycopy(arr, 0, aobj, 0, arr.length);
        	Arrays.fill(aobj, arr.length, length, 0);
        }
        if (aobj.length > length)
        {
        	aobj[length] = 0;
        }
        return aobj;
    }
    public void length(int length)
    {
    	if (length < 0)
    	{
    		throw new IndexOutOfBoundsException();
    	}
        if (length < this.length && length < arr.length)
        {
            Arrays.fill(arr, length, arr.length < this.length ? arr.length : this.length, 0);
        }
        this.length = length;
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
    public Iterator<Integer> iterator()
    {
        return new Iterator<Integer>()
        {
            private int pointer = 0;
            
            @Override
            public boolean hasNext()
            {
                return pointer < DynamicIntArray.this.length();
            }
            @Override
            public Integer next()
            {
                if (pointer < DynamicIntArray.this.length())
                {
                    return DynamicIntArray.this.get(pointer++);
                }
                else
                {
                    throw new NoSuchElementException();
                }
            }
            @Override
            public void remove()
            {
                DynamicIntArray.this.remove(pointer);
            }
        };
    }
    @Override
    public DynamicIntArray clone()
    {
        return new DynamicIntArray(this);
    }
    protected void ensureLength(int minLength)
    {
    	int currentLength = arr.length;
    	int expandSize = minLength - currentLength;
    	if (expandSize <= 0)
    	{
    		return;
    	}
    	if (expandSize < minExpandSize)
    	{
    		expandSize = minExpandSize;
    	}
        arr = Arrays.copyOf(arr, currentLength + expandSize);
    }
}
package org.apcdevpowered.util;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DynamicArray<T> implements Iterable<T>, Cloneable {
	public final int initialSize;
	public final int minExpandSize;
	private int length;
	private Object[] arr;
    
    public DynamicArray()
    {
        this(100, 300);
    }
    public DynamicArray(int initialSize, int minExpandSize)
    {
        this.initialSize = initialSize;
        this.minExpandSize = minExpandSize;
        arr = new Object[initialSize];
    }
    public DynamicArray(DynamicArray<T> dynamicSparseArray)
    {
        initialSize = dynamicSparseArray.initialSize;
        minExpandSize = dynamicSparseArray.minExpandSize;
        arr = new Object[dynamicSparseArray.arr.length];
        System.arraycopy(dynamicSparseArray.arr, 0, arr, 0, arr.length);
        length = dynamicSparseArray.length;
    }
    @SuppressWarnings("unchecked")
    public T get(int index)
    {
        if (index < 0)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (index >= arr.length || index >= length)
        {
            return null;
        }
        return (T) arr[index];
    }
    public void set(int index, T value)
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
        int newLength = length + 1;
        ensureLength(newLength);
        arr[length] = value;
        length++;
    }
    @SuppressWarnings("unchecked")
    public T pop()
    {
        if (length < 1 || arr.length < 1)
        {
            throw new EmptyStackException();
        }
        length--;
        T tmp = (T) arr[length];
        arr[length] = null;
        return tmp;
    }
    @SuppressWarnings("unchecked")
    public T shift()
    {
        if (length < 1 || arr.length < 1)
        {
            throw new EmptyStackException();
        }
        T tmp = (T) arr[0];
        arr = Arrays.copyOfRange(arr, 1, arr.length);
        length--;
        return tmp;
    }
    public void addAll(T[] array)
    {
        int newLength = length + array.length;
        ensureLength(newLength);
        for (int i = 0;i < array.length;i++)
        {
            arr[length] = array[i];
            length++;
        }
    }
    public void addAll(DynamicArray<T> array)
    {
        int newLength = length + array.length;
        ensureLength(newLength);
        for (int i = 0;i < array.length;i++)
        {
            arr[length] = array.arr[i];
            length++;
        }
    }
    public void copyData(T[] array, int srcPos, int destPos, int length)
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
    public void copyData(DynamicArray<T> array, int srcPos, int destPos, int length)
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
    public Object[] toArray()
    {
        return Arrays.copyOf(arr, length);
    }
    @SuppressWarnings("unchecked")
    public T[] toArray(T[] aobj)
    {
        if (aobj.length < length)
        {
            return (T[]) Arrays.copyOf(arr, length, aobj.getClass());
        }
        if (arr.length >= length)
        {
        	System.arraycopy(arr, 0, aobj, 0, length);
        }
        else
        {
        	System.arraycopy(arr, 0, aobj, 0, arr.length);
        	Arrays.fill(aobj, arr.length, length, null);
        }
        if (aobj.length > length)
        {
        	aobj[length] = null;
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
            Arrays.fill(arr, length, arr.length < this.length ? arr.length : this.length, null);
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
    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            private int pointer = 0;
            
            @Override
            public boolean hasNext()
            {
                return pointer < DynamicArray.this.length();
            }
            @Override
            public T next()
            {
                if (pointer < DynamicArray.this.length())
                {
                    return DynamicArray.this.get(pointer++);
                }
                else
                {
                    throw new NoSuchElementException();
                }
            }
            @Override
            public void remove()
            {
                DynamicArray.this.remove(pointer);
            }
        };
    }
    @Override
    public DynamicArray<T> clone()
    {
        return new DynamicArray<T>(this);
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
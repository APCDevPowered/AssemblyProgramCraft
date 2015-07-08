package org.apcdevpowered.vcpu32.vm.storage.container;

import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apcdevpowered.util.reflection.GenericsUtil;
import org.apcdevpowered.vcpu32.vm.storage.ElementKey;
import org.apcdevpowered.vcpu32.vm.storage.NodeContainer;
import org.apcdevpowered.vcpu32.vm.storage.NodeElement;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementNotFoundException;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementTypeMismatchException;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarByte;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarByteArray;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarDouble;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarFloat;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarInteger;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarIntegerArray;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarLong;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarShort;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarString;

public final class NodeContainerArray extends NodeContainer<NodeContainerArray>
{
    private transient int modCount;
    private TreeMap<Integer, NodeElement> elementArray = new TreeMap<Integer, NodeElement>(new Comparator<Integer>()
    {
        @Override
        public int compare(Integer o1, Integer o2)
        {
            return o1.compareTo(o2);
        }
    });
    
    public NodeContainerArray()
    {
        super(NodeContainerArrayElementKey.class);
    }
    @Override
    public final void addElement(ElementKey<NodeContainerArray> key, NodeElement element)
    {
        if (element == null)
        {
            throw new NullPointerException();
        }
        NodeContainerArrayElementKey arrayKey = key.castKey(NodeContainerArrayElementKey.class);
        synchronized (elementArray)
        {
            NodeElement previousElement = elementArray.put(arrayKey.getIndex(), element);
            if (previousElement != null)
            {
                previousElement.removeFromParent();
            }
            setElementParent(element, arrayKey);
            modCount++;
        }
    }
    @Override
    public final boolean hasElement(ElementKey<NodeContainerArray> key)
    {
        NodeContainerArrayElementKey arrayKey = key.castKey(NodeContainerArrayElementKey.class);
        synchronized (elementArray)
        {
            return elementArray.containsKey(arrayKey.getIndex());
        }
    }
    @Override
    public final NodeElement getElement(ElementKey<NodeContainerArray> key) throws ElementNotFoundException
    {
        NodeContainerArrayElementKey arrayKey = key.castKey(NodeContainerArrayElementKey.class);
        synchronized (elementArray)
        {
            NodeElement element = elementArray.get(arrayKey.getIndex());
            if (element == null)
            {
                throw new ElementNotFoundException(arrayKey);
            }
            return element;
        }
    }
    @Override
    public final boolean removeElement(ElementKey<NodeContainerArray> key)
    {
        NodeContainerArrayElementKey arrayKey = key.castKey(NodeContainerArrayElementKey.class);
        synchronized (elementArray)
        {
            if (!elementArray.containsKey(arrayKey.getIndex()))
            {
                return false;
            }
            NodeElement element = elementArray.remove(arrayKey.getIndex());
            resetElementParent(element);
            modCount++;
            return true;
        }
    }
    @Override
    public final int countElement()
    {
        synchronized (elementArray)
        {
            return elementArray.size();
        }
    }
    @Override
    public void clearElement()
    {
        synchronized (elementArray)
        {
            modCount++;
            for (Entry<Integer, NodeElement> entry : elementArray.entrySet())
            {
                resetElementParent(entry.getValue());
            }
            elementArray.clear();
        }
    }
    @Override
    public NodeContainerArrayIterator iterator()
    {
        return new NodeContainerArrayIterator();
    }
    @Override
    public Set<NodeContainerArrayEntry> entrySet()
    {
        synchronized (elementArray)
        {
            Set<NodeContainerArrayEntry> entrySet = new HashSet<NodeContainerArrayEntry>();
            for (Entry<Integer, NodeElement> entry : elementArray.entrySet())
            {
                entrySet.add(new NodeContainerArrayEntry(entry.getKey()));
            }
            return Collections.unmodifiableSet(entrySet);
        }
    }
    public final void add(NodeElement element)
    {
        synchronized (elementArray)
        {
            addElement(makeKey(length()), element);
        }
    }
    public final void add(boolean data)
    {
        add(new NodeScalarByte(data ? (byte) 1 : (byte) 0));
    }
    public final void add(byte data)
    {
        add(new NodeScalarByte(data));
    }
    public final void add(char data)
    {
        add(new NodeScalarShort((short) data));
    }
    public final void add(short data)
    {
        add(new NodeScalarShort(data));
    }
    public final void add(int data)
    {
        add(new NodeScalarInteger(data));
    }
    public final void add(long data)
    {
        add(new NodeScalarLong(data));
    }
    public final void add(float data)
    {
        add(new NodeScalarFloat(data));
    }
    public final void add(double data)
    {
        add(new NodeScalarDouble(data));
    }
    public final void add(byte[] data)
    {
        add(new NodeScalarByteArray(data));
    }
    public final void add(int[] data)
    {
        add(new NodeScalarIntegerArray(data));
    }
    public final void add(String data)
    {
        add(new NodeScalarString(data));
    }
    public final int length()
    {
        synchronized (elementArray)
        {
            if (elementArray.isEmpty())
            {
                return 0;
            }
            return elementArray.lastKey() + 1;
        }
    }
    @Override
    public int hashCode()
    {
        int result = 1;
        result = result * 31 + elementArray.hashCode();
        return result;
    }
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            throw new NullPointerException();
        }
        if (obj == this)
        {
            return true;
        }
        if (obj instanceof NodeContainerArray)
        {
            NodeContainerArray container = ((NodeContainerArray) obj);
            if (container.elementArray.equals(elementArray))
            {
                return false;
            }
            return true;
        }
        return false;
    }
    @Override
    public String toString()
    {
        return "{elementArray:" + elementArray + "}";
    }
    @Override
    public NodeContainerArray clone()
    {
        NodeContainerArray nodeContainerArray = new NodeContainerArray();
        synchronized (elementArray)
        {
            nodeContainerArray.elementArray = GenericsUtil.genericUnsafeCast(elementArray.clone());
        }
        return nodeContainerArray;
    }
    public static NodeContainerArrayElementKey makeKey(int index)
    {
        return new NodeContainerArrayElementKey(index);
    }
    
    public final static class NodeContainerArrayElementKey extends ElementKey<NodeContainerArray>
    {
        private final int index;
        
        private NodeContainerArrayElementKey(int index)
        {
            super(NodeContainerArray.class);
            if (index < 0)
            {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            this.index = index;
        }
        public int getIndex()
        {
            return index;
        }
        @Override
        public int hashCode()
        {
            int result = 1;
            result = result * 31 + index;
            return result;
        }
        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                throw new NullPointerException();
            }
            if (obj == this)
            {
                return true;
            }
            if (obj instanceof NodeContainerArrayElementKey)
            {
                NodeContainerArrayElementKey elementKey = ((NodeContainerArrayElementKey) obj);
                if (elementKey.index != index)
                {
                    return false;
                }
                return true;
            }
            return false;
        }
        @Override
        public String toString()
        {
            return "{index:" + index + "}";
        }
    }
    public final class NodeContainerArrayIterator implements Iterator<Entry<ElementKey<NodeContainerArray>, NodeElement>>
    {
        private int expectedModCount;
        private NodeContainerArrayEntry currentEntry;
        private NodeContainerArrayEntry nextEntry;
        
        private NodeContainerArrayIterator()
        {
            synchronized (elementArray)
            {
                expectedModCount = modCount;
                if (elementArray.size() > 0)
                {
                    nextEntry = new NodeContainerArrayEntry(elementArray.firstKey());
                }
            }
        }
        @Override
        public boolean hasNext()
        {
            return nextEntry != null;
        }
        @Override
        public NodeContainerArrayEntry next()
        {
            return nextEntry();
        }
        @Override
        public void remove()
        {
            if (currentEntry == null)
            {
                throw new IllegalStateException();
            }
            synchronized (elementArray)
            {
                if (expectedModCount != modCount)
                {
                    throw new ConcurrentModificationException();
                }
                removeElement(currentEntry.getKey());
                expectedModCount = modCount;
            }
        }
        private final NodeContainerArrayEntry nextEntry()
        {
            synchronized (elementArray)
            {
                if (expectedModCount != modCount)
                {
                    throw new ConcurrentModificationException();
                }
                NodeContainerArrayEntry entry = nextEntry;
                if (nextEntry == null)
                {
                    throw new NoSuchElementException();
                }
                Integer nextKey = elementArray.higherKey(entry.getKey().castKey(NodeContainerArrayElementKey.class).getIndex());
                if (nextKey != null)
                {
                    nextEntry = new NodeContainerArrayEntry(nextKey);
                }
                else
                {
                    nextEntry = null;
                }
                currentEntry = entry;
                return currentEntry;
            }
        }
    }
    public final class NodeContainerArrayEntry implements Entry<ElementKey<NodeContainerArray>, NodeElement>
    {
        private final NodeContainerArrayElementKey key;
        
        private NodeContainerArrayEntry(int index)
        {
            key = makeKey(index);
        }
        @Override
        public NodeContainerArrayElementKey getKey()
        {
            return key;
        }
        @Override
        public NodeElement getValue()
        {
            try
            {
                return getElement(key);
            }
            catch (ElementNotFoundException e)
            {
                return null;
            }
        }
        public <E extends NodeElement> E getValue(Class<E> clazz)
        {
            try
            {
                return getElement(key, clazz);
            }
            catch (ElementNotFoundException e)
            {
                return null;
            }
            catch (ElementTypeMismatchException e)
            {
                return null;
            }
        }
        @Override
        public NodeElement setValue(NodeElement value)
        {
            synchronized (elementArray)
            {
                NodeElement old;
                try
                {
                    old = getElement(key);
                }
                catch (ElementNotFoundException e)
                {
                    old = null;
                }
                addElement(key, value);
                return old;
            }
        }
    }
}
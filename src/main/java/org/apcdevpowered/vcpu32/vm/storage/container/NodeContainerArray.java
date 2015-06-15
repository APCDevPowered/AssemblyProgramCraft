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

import org.apcdevpowered.vcpu32.vm.storage.ElementKey;
import org.apcdevpowered.vcpu32.vm.storage.NodeContainer;
import org.apcdevpowered.vcpu32.vm.storage.NodeElement;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementNotFoundException;

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
    
    @Override
    public final void addElement(ElementKey<NodeContainerArray> key, NodeElement component)
    {
        if (component == null)
        {
            throw new NullPointerException();
        }
        NodeContainerArrayElementKey arrayKey = key.castKey(NodeContainerArrayElementKey.class);
        synchronized (elementArray)
        {
            elementArray.put(arrayKey.getIndex(), component);
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
            elementArray.remove(arrayKey.getIndex());
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
            elementArray.clear();
        }
    }
    @Override
    public Iterator<Entry<ElementKey<NodeContainerArray>, NodeElement>> iterator()
    {
        return new NodeContainerArrayIterator();
    }
    @Override
    public Set<Entry<ElementKey<NodeContainerArray>, NodeElement>> entrySet()
    {
        synchronized (elementArray)
        {
           Set<Entry<ElementKey<NodeContainerArray>, NodeElement>> entrySet = new HashSet<Entry<ElementKey<NodeContainerArray>, NodeElement>>();
           for(Entry<Integer, NodeElement> entry : elementArray.entrySet())
           {
               entrySet.add(new NodeContainerArrayEntry(entry.getKey()));
           }
           return Collections.unmodifiableSet(entrySet);
        }
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
    }
    private final class NodeContainerArrayIterator implements Iterator<Entry<ElementKey<NodeContainerArray>, NodeElement>>
    {
        private int expectedModCount;
        private Entry<ElementKey<NodeContainerArray>, NodeElement> currentEntry;
        private Entry<ElementKey<NodeContainerArray>, NodeElement> nextEntry;
        
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
        public Entry<ElementKey<NodeContainerArray>, NodeElement> next()
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
        private Entry<ElementKey<NodeContainerArray>, NodeElement> nextEntry()
        {
            synchronized (elementArray)
            {
                if (expectedModCount != modCount)
                {
                    throw new ConcurrentModificationException();
                }
                Entry<ElementKey<NodeContainerArray>, NodeElement> entry = nextEntry;
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
    private final class NodeContainerArrayEntry implements Entry<ElementKey<NodeContainerArray>, NodeElement>
    {
        private final ElementKey<NodeContainerArray> key;
        
        private NodeContainerArrayEntry(int index)
        {
            key = makeKey(index);
        }
        @Override
        public ElementKey<NodeContainerArray> getKey()
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
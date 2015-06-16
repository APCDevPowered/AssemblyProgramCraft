package org.apcdevpowered.vcpu32.vm.storage.container;

import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

import org.apcdevpowered.util.reflection.GenericsUtil;
import org.apcdevpowered.vcpu32.vm.storage.ElementKey;
import org.apcdevpowered.vcpu32.vm.storage.NodeContainer;
import org.apcdevpowered.vcpu32.vm.storage.NodeElement;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementNotFoundException;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementTypeMismatchException;

public final class NodeContainerMap extends NodeContainer<NodeContainerMap>
{
    private transient int modCount;
    private HashMap<String, NodeElement> elementMap = new HashMap<String, NodeElement>();
    
    public NodeContainerMap()
    {
        super(NodeContainerMapElementKey.class);
    }
    @Override
    public final void addElement(ElementKey<NodeContainerMap> key, NodeElement element)
    {
        if (element == null)
        {
            throw new NullPointerException();
        }
        NodeContainerMapElementKey arrayKey = key.castKey(NodeContainerMapElementKey.class);
        synchronized (elementMap)
        {
            NodeElement previousElement = elementMap.put(arrayKey.getKey(), element);
            resetElementParent(previousElement);
            setElementParent(element, arrayKey);
            modCount++;
        }
    }
    @Override
    public final boolean hasElement(ElementKey<NodeContainerMap> key)
    {
        NodeContainerMapElementKey arrayKey = key.castKey(NodeContainerMapElementKey.class);
        synchronized (elementMap)
        {
            return elementMap.containsKey(arrayKey.getKey());
        }
    }
    @Override
    public final NodeElement getElement(ElementKey<NodeContainerMap> key) throws ElementNotFoundException
    {
        NodeContainerMapElementKey arrayKey = key.castKey(NodeContainerMapElementKey.class);
        synchronized (elementMap)
        {
            NodeElement element = elementMap.get(arrayKey.getKey());
            if (element == null)
            {
                throw new ElementNotFoundException(arrayKey);
            }
            return element;
        }
    }
    @Override
    public final boolean removeElement(ElementKey<NodeContainerMap> key)
    {
        NodeContainerMapElementKey arrayKey = key.castKey(NodeContainerMapElementKey.class);
        synchronized (elementMap)
        {
            if (!elementMap.containsKey(arrayKey.getKey()))
            {
                return false;
            }
            NodeElement element = elementMap.remove(arrayKey.getKey());
            resetElementParent(element);
            modCount++;
            return true;
        }
    }
    @Override
    public final int countElement()
    {
        synchronized (elementMap)
        {
            return elementMap.size();
        }
    }
    @Override
    public void clearElement()
    {
        synchronized (elementMap)
        {
            modCount++;
            for (Entry<String, NodeElement> entry : elementMap.entrySet())
            {
                resetElementParent(entry.getValue());
            }
            elementMap.clear();
        }
    }
    @Override
    public NodeContainerMapIterator iterator()
    {
        return new NodeContainerMapIterator();
    }
    @Override
    public Set<NodeContainerMapEntry> entrySet()
    {
        synchronized (elementMap)
        {
            Set<NodeContainerMapEntry> entrySet = new HashSet<NodeContainerMapEntry>();
            for (Entry<String, NodeElement> entry : elementMap.entrySet())
            {
                entrySet.add(new NodeContainerMapEntry(entry.getKey()));
            }
            return Collections.unmodifiableSet(entrySet);
        }
    }
    @Override
    public int hashCode()
    {
        int result = 1;
        result = result * 31 + elementMap.hashCode();
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
        if (obj instanceof NodeContainerMap)
        {
            NodeContainerMap container = ((NodeContainerMap) obj);
            if (container.elementMap.equals(elementMap))
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
        return "{elementMap:" + elementMap + "}";
    }
    @Override
    public NodeContainerMap clone()
    {
        NodeContainerMap nodeContainerMap = new NodeContainerMap();
        synchronized (elementMap)
        {
            nodeContainerMap.elementMap = GenericsUtil.genericUnsafeCast(elementMap.clone());
        }
        return nodeContainerMap;
    }
    public static NodeContainerMapElementKey makeKey(String key)
    {
        return new NodeContainerMapElementKey(key);
    }
    
    public final static class NodeContainerMapElementKey extends ElementKey<NodeContainerMap>
    {
        private final String key;
        
        private NodeContainerMapElementKey(String key)
        {
            super(NodeContainerMap.class);
            if (key == null)
            {
                throw new NullPointerException();
            }
            this.key = key;
        }
        public String getKey()
        {
            return key;
        }
        @Override
        public int hashCode()
        {
            int result = 1;
            result = result * 31 + key.hashCode();
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
            if (obj instanceof NodeContainerMapElementKey)
            {
                NodeContainerMapElementKey elementKey = ((NodeContainerMapElementKey) obj);
                if (elementKey.key != key)
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
            return "{key:" + key + "}";
        }
    }
    public final class NodeContainerMapIterator implements Iterator<Entry<ElementKey<NodeContainerMap>, NodeElement>>
    {
        private int expectedModCount;
        private Iterator<Entry<String, NodeElement>> innerIterator;
        private NodeContainerMapEntry currentEntry;
        private NodeContainerMapEntry nextEntry;
        
        private NodeContainerMapIterator()
        {
            synchronized (elementMap)
            {
                expectedModCount = modCount;
                innerIterator = elementMap.entrySet().iterator();
                if (innerIterator.hasNext())
                {
                    nextEntry = new NodeContainerMapEntry(innerIterator.next().getKey());
                }
            }
        }
        @Override
        public boolean hasNext()
        {
            return nextEntry != null;
        }
        @Override
        public NodeContainerMapEntry next()
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
            synchronized (elementMap)
            {
                if (expectedModCount != modCount)
                {
                    throw new ConcurrentModificationException();
                }
                innerIterator.remove();
                resetElementParent(currentEntry.getValue());
                modCount++;
                expectedModCount = modCount;
            }
        }
        private final NodeContainerMapEntry nextEntry()
        {
            synchronized (elementMap)
            {
                if (expectedModCount != modCount)
                {
                    throw new ConcurrentModificationException();
                }
                NodeContainerMapEntry entry = nextEntry;
                if (nextEntry == null)
                {
                    throw new NoSuchElementException();
                }
                if (innerIterator.hasNext())
                {
                    nextEntry = new NodeContainerMapEntry(innerIterator.next().getKey());
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
    public final class NodeContainerMapEntry implements Entry<ElementKey<NodeContainerMap>, NodeElement>
    {
        private final NodeContainerMapElementKey key;
        
        private NodeContainerMapEntry(String key)
        {
            this.key = makeKey(key);
        }
        @Override
        public NodeContainerMapElementKey getKey()
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
            synchronized (elementMap)
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
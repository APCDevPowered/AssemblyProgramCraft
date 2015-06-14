package org.apcdevpowered.vcpu32.vm.storage.container;

import org.apcdevpowered.util.array.DynamicSparseArray;
import org.apcdevpowered.vcpu32.vm.storage.ElementKey;
import org.apcdevpowered.vcpu32.vm.storage.NodeContainer;
import org.apcdevpowered.vcpu32.vm.storage.NodeElement;

public final class NodeArray extends NodeContainer<NodeArray>
{
    private DynamicSparseArray<NodeElement> elementArray = new DynamicSparseArray<NodeElement>();
    
    @Override
    public final void addElement(ElementKey<NodeArray> key, NodeElement component)
    {
        NodeArrayElementKey arrayKey = key.castKey(NodeArrayElementKey.class);
        synchronized (elementArray)
        {
            elementArray.set(arrayKey.getIndex(), component);
        }
    }
    @Override
    public final boolean hasElement(ElementKey<NodeArray> key)
    {
        NodeArrayElementKey arrayKey = key.castKey(NodeArrayElementKey.class);
        synchronized (elementArray)
        {
            return elementArray.get(arrayKey.getIndex()) != null;
        }
    }
    @Override
    public final NodeElement getElement(ElementKey<NodeArray> key)
    {
        NodeArrayElementKey arrayKey = key.castKey(NodeArrayElementKey.class);
        synchronized (elementArray)
        {
            return elementArray.get(arrayKey.getIndex());
        }
    }
    @Override
    public final boolean removeElement(ElementKey<NodeArray> key)
    {
        NodeArrayElementKey arrayKey = key.castKey(NodeArrayElementKey.class);
        synchronized (elementArray)
        {
            if (elementArray.get(arrayKey.getIndex()) == null)
            {
                return false;
            }
            elementArray.remove(arrayKey.getIndex());
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
    public static NodeArrayElementKey makeKey(int index)
    {
        return new NodeArrayElementKey(index);
    }
    
    public final static class NodeArrayElementKey extends ElementKey<NodeArray>
    {
        private final int index;
        
        private NodeArrayElementKey(int index)
        {
            super(NodeArray.class);
            this.index = index;
        }
        public int getIndex()
        {
            return index;
        }
    }
}
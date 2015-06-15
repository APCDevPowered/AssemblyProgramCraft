package org.apcdevpowered.vcpu32.vm.storage;

import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerArray;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerMap;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementParentNotFoundException;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementTypeMismatchException;

public abstract class NodeElement
{
    private transient Object parentLock = new Object();
    private ElementParentCach<?> elementParentCach;
    
    private static final class ElementParentCach<C extends NodeContainer<C>>
    {
        private final ElementKey<C> key;
        private final NodeContainer<C> parent;
        
        protected ElementParentCach(ElementKey<C> key, NodeContainer<C> parent)
        {
            this.key = key;
            this.parent = parent;
        }
        public ElementKey<C> getKey()
        {
            return key;
        }
        public NodeContainer<C> getParent()
        {
            return parent;
        }
        public void removeFromParent()
        {
            parent.removeElement(key);
        }
    }
    
    protected NodeElement()
    {
    }
    public final NodeContainer<?> getParent()
    {
        synchronized (parentLock)
        {
            return elementParentCach == null ? null : elementParentCach.getParent();
        }
    }
    public final <C extends NodeContainer<C>> C getParent(Class<C> clazz) throws ElementParentNotFoundException, ElementTypeMismatchException
    {
        NodeContainer<?> parent = getParent();
        if (parent == null)
        {
            throw new ElementParentNotFoundException();
        }
        if (!clazz.isInstance(parent))
        {
            throw new ElementTypeMismatchException(parent.getClass(), clazz);
        }
        return clazz.cast(parent);
    }
    public final NodeContainerMap getParentMap() throws ElementParentNotFoundException, ElementTypeMismatchException
    {
        return getParent(NodeContainerMap.class);
    }
    public final NodeContainerArray getParentArray() throws ElementParentNotFoundException, ElementTypeMismatchException
    {
        return getParent(NodeContainerArray.class);
    }
    public final ElementKey<?> getKey()
    {
        synchronized (parentLock)
        {
            return elementParentCach == null ? null : elementParentCach.getKey();
        }
    }
    public <E extends NodeElement> E castElemenet(Class<E> clazz) throws ElementTypeMismatchException
    {
        if(!clazz.isAssignableFrom(this.getClass()))
        {
            throw new ElementTypeMismatchException(this.getClass(), clazz);
        }
        return clazz.cast(this);
    }
    public void removeFromParent()
    {
        synchronized (parentLock)
        {
            elementParentCach.removeFromParent();
        }
    }
    protected <C extends NodeContainer<C>> void setParent(ElementKey<C> key, NodeContainer<C> parent)
    {
        if (key == null || parent == null)
        {
            throw new IllegalStateException(new NullPointerException());
        }
        synchronized (parentLock)
        {
            this.elementParentCach = new ElementParentCach<C>(key, parent);
        }
    }
    protected void resetParent()
    {
        synchronized (parentLock)
        {
            elementParentCach = null;
        }
    }
}
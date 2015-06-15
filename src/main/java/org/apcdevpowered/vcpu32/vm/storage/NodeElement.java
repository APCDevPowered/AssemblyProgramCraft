package org.apcdevpowered.vcpu32.vm.storage;

import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerArray;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerMap;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementParentNotFoundException;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementTypeMismatchException;

public abstract class NodeElement
{
    private ElementKey<?> key;
    private NodeContainer<?> parent;
    
    protected NodeElement()
    {
    }
    public final NodeContainer<?> getParent()
    {
        return parent;
    }
    public final <C extends NodeContainer<C>> C getParent(Class<C> clazz) throws ElementParentNotFoundException, ElementTypeMismatchException
    {
        NodeContainer<?> parent = getParent();
        if(parent == null)
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
        return key;
    }
    protected void setParent(ElementKey<?> key, NodeContainer<?> parent)
    {
        this.key = key;
        this.parent = parent;
    }
    protected void resetParent()
    {
        key = null;
        parent = null;
    }
}
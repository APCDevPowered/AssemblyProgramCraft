package org.apcdevpowered.vcpu32.vm.storage;

import org.apcdevpowered.vcpu32.vm.storage.container.NodeArray;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeMap;
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
    public final <C extends NodeContainer<C>> C getParent(Class<C> clazz) throws ElementTypeMismatchException
    {
        NodeContainer<?> parent = getParent();
        if (!clazz.isInstance(parent))
        {
            throw new ElementTypeMismatchException(parent.getClass(), clazz);
        }
        return clazz.cast(parent);
    }
    public final NodeMap getParentMap() throws ElementTypeMismatchException
    {
        return getParent(NodeMap.class);
    }
    public final NodeArray getParentArray() throws ElementTypeMismatchException
    {
        return getParent(NodeArray.class);
    }
    public final ElementKey<?> getKey()
    {
        return key;
    }
}
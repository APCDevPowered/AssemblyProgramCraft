package org.apcdevpowered.vcpu32.vm.storage;

import org.apcdevpowered.vcpu32.vm.storage.container.NodeArray;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeMap;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementTypeMismatchException;

public abstract class NodeContainer<C extends NodeContainer<C>> extends NodeElement
{
    protected NodeContainer()
    {
    }
    public abstract void addElement(ElementKey<C> key, NodeElement component);
    public abstract boolean hasElement(ElementKey<C> key);
    public abstract NodeElement getElement(ElementKey<C> key);
    public final <E extends NodeElement> E getElement(ElementKey<C> key, Class<E> clazz) throws ElementTypeMismatchException
    {
        NodeElement element = getElement(key);
        if (!clazz.isInstance(element))
        {
            throw new ElementTypeMismatchException(element.getClass(), clazz);
        }
        return clazz.cast(element);
    }
    public final NodeScalar<?> getScalar(ElementKey<C> key) throws ElementTypeMismatchException
    {
        return getElement(key, NodeScalar.class);
    }
    public final <S extends NodeScalar<S>> S getScalar(ElementKey<C> key, Class<S> clazz) throws ElementTypeMismatchException
    {
        return getElement(key, clazz);
    }
    public final NodeContainer<?> getContainer(ElementKey<C> key) throws ElementTypeMismatchException
    {
        return getElement(key, NodeContainer.class);
    }
    public final NodeMap getMap(ElementKey<C> key) throws ElementTypeMismatchException
    {
        return getElement(key, NodeMap.class);
    }
    public final NodeArray getArray(ElementKey<C> key) throws ElementTypeMismatchException
    {
        return getElement(key, NodeArray.class);
    }
    public abstract boolean removeElement(ElementKey<C> key);
    public abstract int countElement();
}
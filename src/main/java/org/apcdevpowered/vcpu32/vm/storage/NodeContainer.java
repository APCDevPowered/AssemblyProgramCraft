package org.apcdevpowered.vcpu32.vm.storage;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerArray;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerMap;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementNotFoundException;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementTypeMismatchException;

public abstract class NodeContainer<C extends NodeContainer<C>> extends NodeElement
{
    protected NodeContainer()
    {
    }
    public abstract void addElement(ElementKey<C> key, NodeElement component);
    public abstract boolean hasElement(ElementKey<C> key);
    public abstract NodeElement getElement(ElementKey<C> key) throws ElementNotFoundException;
    public final <E extends NodeElement> E getElement(ElementKey<C> key, Class<E> clazz) throws ElementNotFoundException, ElementTypeMismatchException
    {
        NodeElement element = getElement(key);
        if (!clazz.isInstance(element))
        {
            throw new ElementTypeMismatchException(element.getClass(), clazz);
        }
        return clazz.cast(element);
    }
    public final NodeScalar<?> getScalar(ElementKey<C> key) throws ElementNotFoundException, ElementTypeMismatchException
    {
        return getElement(key, NodeScalar.class);
    }
    public final <S extends NodeScalar<S>> S getScalar(ElementKey<C> key, Class<S> clazz) throws ElementNotFoundException, ElementTypeMismatchException
    {
        return getElement(key, clazz);
    }
    public final NodeContainer<?> getContainer(ElementKey<C> key) throws ElementNotFoundException, ElementTypeMismatchException
    {
        return getElement(key, NodeContainer.class);
    }
    public final NodeContainerMap getMap(ElementKey<C> key) throws ElementNotFoundException, ElementTypeMismatchException
    {
        return getElement(key, NodeContainerMap.class);
    }
    public final NodeContainerArray getArray(ElementKey<C> key) throws ElementNotFoundException, ElementTypeMismatchException
    {
        return getElement(key, NodeContainerArray.class);
    }
    public abstract boolean removeElement(ElementKey<C> key);
    public abstract int countElement();
    public abstract void clearElement();
    public abstract Iterator<Entry<ElementKey<C>, NodeElement>> iterator();
    public abstract Set<Entry<ElementKey<C>, NodeElement>> entrySet();
}
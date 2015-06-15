package org.apcdevpowered.vcpu32.vm.storage;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerArray;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerMap;
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

public abstract class NodeContainer<C extends NodeContainer<C>> extends NodeElement
{
    private final Class<? extends ElementKey<C>> keyType;
    
    protected NodeContainer(Class<? extends ElementKey<C>> keyType)
    {
        this.keyType = keyType;
    }
    public final Class<? extends ElementKey<C>> getKeyType()
    {
        return keyType;
    }
    public abstract void addElement(ElementKey<C> key, NodeElement element);
    public void addElement(ElementKey<C> key, boolean data)
    {
        addElement(key, new NodeScalarByte(data ? (byte)1 : (byte)0));
    }
    public void addElement(ElementKey<C> key, byte data)
    {
        addElement(key, new NodeScalarByte(data));
    }
    public void addElement(ElementKey<C> key, short data)
    {
        addElement(key, new NodeScalarShort(data));
    }
    public void addElement(ElementKey<C> key, int data)
    {
        addElement(key, new NodeScalarInteger(data));
    }
    public void addElement(ElementKey<C> key, long data)
    {
        addElement(key, new NodeScalarLong(data));
    }
    public void addElement(ElementKey<C> key, float data)
    {
        addElement(key, new NodeScalarFloat(data));
    }
    public void addElement(ElementKey<C> key, double data)
    {
        addElement(key, new NodeScalarDouble(data));
    }
    public void addElement(ElementKey<C> key, byte[] data)
    {
        addElement(key, new NodeScalarByteArray(data));
    }
    public void addElement(ElementKey<C> key, int[] data)
    {
        addElement(key, new NodeScalarIntegerArray(data));
    }
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
    public final boolean getBoolean(ElementKey<C> key) throws ElementNotFoundException, ElementTypeMismatchException
    {
        return (getElement(key, NodeScalarByte.class).getData() != 0) ? true : false;
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
    protected void setElementParent(NodeElement element, ElementKey<C> key)
    {
        if (key == null)
        {
            throw new IllegalStateException(new NullPointerException());
        }
        element.setParent(key, this);
    }
    protected void resetElementParent(NodeElement element)
    {
        element.resetParent();
    }
}
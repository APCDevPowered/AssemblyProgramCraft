package org.apcdevpowered.vcpu32.vm.storage.persistence;

import java.util.HashMap;
import java.util.Map;

import org.apcdevpowered.vcpu32.vm.storage.NodeElement;

public class ElementTypeDataValueMapping
{
    private Object mappingLock = new Object();
    private Map<Class<? extends NodeElement>, Integer> typeMapping = new HashMap<Class<? extends NodeElement>, Integer>();
    private Map<Integer, Class<? extends NodeElement>> dataValueMapping = new HashMap<Integer, Class<? extends NodeElement>>();
    
    public final void addMapping(int dataValue, Class<? extends NodeElement> element)
    {
        if (element == null)
        {
            throw new NullPointerException();
        }
        if (dataValue <= 0)
        {
            throw new IndexOutOfBoundsException();
        }
        synchronized (mappingLock)
        {
            if(typeMapping.containsKey(element))
            {
                throw new IllegalStateException();
            }
            if(dataValueMapping.containsKey(dataValue))
            {
                throw new IllegalStateException();
            }
            typeMapping.put(element, dataValue);
            dataValueMapping.put(dataValue, element);
        }
    }
    public final Integer getMapping(Class<? extends NodeElement> element)
    {
        if (element == null)
        {
            throw new NullPointerException();
        }
        synchronized (mappingLock)
        {
            return typeMapping.get(element);
        }
    }
    public final Class<? extends NodeElement> getMapping(int dataValue)
    {
        synchronized (mappingLock)
        {
            return dataValueMapping.get(dataValue);
        }
    }
}
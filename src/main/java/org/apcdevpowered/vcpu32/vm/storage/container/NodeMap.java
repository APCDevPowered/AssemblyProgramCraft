package org.apcdevpowered.vcpu32.vm.storage.container;

import java.util.HashMap;
import java.util.Map;

import org.apcdevpowered.vcpu32.vm.storage.ElementKey;
import org.apcdevpowered.vcpu32.vm.storage.NodeContainer;
import org.apcdevpowered.vcpu32.vm.storage.NodeElement;

public final class NodeMap extends NodeContainer<NodeMap>
{
    private Map<String, NodeElement> elementMap = new HashMap<String, NodeElement>();
    
    @Override
    public void addElement(ElementKey<NodeMap> key, NodeElement component)
    {
        NodeMapElementKey mapKey = key.castKey(NodeMapElementKey.class);
        synchronized (elementMap)
        {
            elementMap.put(mapKey.getKey(), component);
        }
    }
    @Override
    public boolean hasElement(ElementKey<NodeMap> key)
    {
        NodeMapElementKey mapKey = key.castKey(NodeMapElementKey.class);
        synchronized (elementMap)
        {
            return elementMap.containsKey(mapKey.getKey());
        }
    }
    @Override
    public NodeElement getElement(ElementKey<NodeMap> key)
    {
        NodeMapElementKey mapKey = key.castKey(NodeMapElementKey.class);
        synchronized (elementMap)
        {
            return elementMap.get(mapKey.getKey());
        }
    }
    @Override
    public boolean removeElement(ElementKey<NodeMap> key)
    {
        NodeMapElementKey mapKey = key.castKey(NodeMapElementKey.class);
        synchronized (elementMap)
        {
            if (!elementMap.containsKey(mapKey.getKey()))
            {
                return false;
            }
            elementMap.remove(mapKey.getKey());
            return true;
        }
    }
    @Override
    public int countElement()
    {
        synchronized (elementMap)
        {
            return elementMap.size();
        }
    }
    public static NodeMapElementKey makeKey(String key)
    {
        return new NodeMapElementKey(key);
    }
    
    public final static class NodeMapElementKey extends ElementKey<NodeMap>
    {
        private final String key;
        
        private NodeMapElementKey(String key)
        {
            super(NodeMap.class);
            this.key = key;
        }
        public String getKey()
        {
            return key;
        }
    }
}
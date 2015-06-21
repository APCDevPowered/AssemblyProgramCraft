package org.apcdevpowered.vcpu32.vm.storage;

public abstract class ElementKey<C extends NodeContainer<C>>
{
    private final Class<C> containerType;
    
    protected ElementKey(Class<C> containerType)
    {
        this.containerType = containerType;
    }
    public final Class<C> getContainerType()
    {
        return containerType;
    }
    public final <K extends ElementKey<C>> K castKey(Class<K> clazz)
    {
        return clazz.cast(this);
    }
}
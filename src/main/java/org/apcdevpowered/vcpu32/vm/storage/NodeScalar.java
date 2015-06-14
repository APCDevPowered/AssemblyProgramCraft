package org.apcdevpowered.vcpu32.vm.storage;

public class NodeScalar<S extends NodeScalar<S>> extends NodeElement
{
    private final ScalarType<S> type;
    
    private NodeScalar(ScalarType<S> type)
    {
        this.type = type;
    }
    public ScalarType<S> getType()
    {
        return type;
    }
    public <T extends ScalarType<S>> T getType(Class<T> clazz)
    {
        return clazz.cast(type);
    }
}
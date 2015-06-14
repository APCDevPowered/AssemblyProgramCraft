package org.apcdevpowered.vcpu32.vm.storage;

public abstract class NodeScalar<S extends NodeScalar<S>> extends NodeElement
{
    private final ScalarType<S> type;
    
    protected NodeScalar(ScalarType<S> type)
    {
        this.type = type;
    }
    public final ScalarType<S> getType()
    {
        return type;
    }
    public final <T extends ScalarType<S>> T getType(Class<T> clazz)
    {
        return clazz.cast(type);
    }
}
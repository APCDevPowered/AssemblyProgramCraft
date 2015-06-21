package org.apcdevpowered.util.reflection;

public class GenericsUtil
{
    @SuppressWarnings("unchecked")
    public static <O, N extends O> N genericUnsafeCast(O obj)
    {
        return ((Class<N>) obj.getClass()).cast(obj);
    }
}
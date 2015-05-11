package yuxuanchiadm.apc.apc.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodHandler
{
    private final Object obj;
    private final Method method;
    
    public MethodHandler(Object obj, Method method)
    {
        this.obj = obj;
        this.method = method;
    }
    public Object getObj()
    {
        return obj;
    }
    public Method getMethod()
    {
        return method;
    }
    public Object invoke(Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        return method.invoke(obj, args);
    }
}
package org.apcdevpowered.util;

import java.lang.reflect.Array;

public class ArrayUtils
{
    public static void safeArraycopy(Object src, int srcPos, Object dest, int destPos, int length)
    {
        if (!src.getClass().isArray() || !dest.getClass().isArray())
        {
            throw new ArrayStoreException();
        }
        if (length <= 0)
        {
            return;
        }
        int srcLength = getArrayLength(src);
        int destLength = getArrayLength(dest);
        Object temp = Array.newInstance(src.getClass().getComponentType(), length);
        {
            int safeSrcStart = IntUtils.betweenMinMax(srcPos, 0, srcLength);
            int safeTmpStart = IntUtils.betweenMinMax(safeSrcStart - srcPos, 0, length);
            int safeSrcLength = IntUtils.betweenMinMax(IntUtils.betweenMinMax(length, 0, srcLength - safeSrcStart), 0, Math.max(0, length - safeTmpStart));
            System.arraycopy(src, safeSrcStart, temp, safeTmpStart, safeSrcLength);
        }
        {
            int safeTmpStart = IntUtils.betweenMinMax(0 - destPos, 0, length);
            int safeDestStart = IntUtils.betweenMinMax(destPos, 0, destLength);
            int safeTmpLength = IntUtils.betweenMinMax(length - safeTmpStart, 0, Math.max(0, destLength - safeDestStart));
            System.arraycopy(temp, safeTmpStart, dest, safeDestStart, safeTmpLength);
        }
    }
    public static void safeSet(Object array, int index, Object value)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            Array.set(array, index, value);
        }
    }
    public static void safeSetBoolean(Object array, int index, boolean value)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            Array.setBoolean(array, index, value);
        }
    }
    public static void safeSetByte(Object array, int index, byte value)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            Array.setByte(array, index, value);
        }
    }
    public static void safeSetChar(Object array, int index, char value)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            Array.setChar(array, index, value);
        }
    }
    public static void safeSetShort(Object array, int index, short value)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            Array.setShort(array, index, value);
        }
    }
    public static void safeSetInt(Object array, int index, int value)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            Array.setInt(array, index, value);
        }
    }
    public static void safeSetLong(Object array, int index, long value)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            Array.setLong(array, index, value);
        }
    }
    public static void safeSetFloat(Object array, int index, float value)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            Array.setFloat(array, index, value);
        }
    }
    public static void safeSetDouble(Object array, int index, double value)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            Array.setDouble(array, index, value);
        }
    }
    public static Object safeGet(Object array, int index)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            return Array.get(array, index);
        }
        return null;
    }
    public static boolean safeGetBoolean(Object array, int index)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            return Array.getBoolean(array, index);
        }
        return false;
    }
    public static byte safeGetByte(Object array, int index)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            return Array.getByte(array, index);
        }
        return 0;
    }
    public static char safeGetChar(Object array, int index)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            return Array.getChar(array, index);
        }
        return 0;
    }
    public static short safeGetShort(Object array, int index)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            return Array.getShort(array, index);
        }
        return 0;
    }
    public static int safeGetInt(Object array, int index)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            return Array.getInt(array, index);
        }
        return 0;
    }
    public static long safeGetLong(Object array, int index)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            return Array.getLong(array, index);
        }
        return 0;
    }
    public static float safeGetFloat(Object array, int index)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            return Array.getFloat(array, index);
        }
        return 0;
    }
    public static double safeGetDouble(Object array, int index)
    {
        int arrayLength = getArrayLength(array);
        if (index >= 0 && index < arrayLength)
        {
            return Array.getDouble(array, index);
        }
        return 0;
    }
    public static int getArrayLength(Object array)
    {
        return Array.getLength(array);
    }
    public static String dumpArray(Object array)
    {
        if (!array.getClass().isArray())
        {
            throw new IllegalArgumentException();
        }
        int length = getArrayLength(array);
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        boolean isFirst = true;
        for (int index = 0; index < length; index++)
        {
            if (isFirst)
            {
                isFirst = false;
            }
            else
            {
                builder.append(',');
            }
            builder.append(Array.get(array, index));
        }
        builder.append(']');
        return builder.toString();
    }
}
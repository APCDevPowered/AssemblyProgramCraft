package org.apcdevpowered.util;

import java.util.Collection;

public class IntUtils
{
    public static int[] castToPrimitiveArray(Integer[] params)
    {
        int len = params.length;
        int[] temp = new int[len];
        for (int i = 0; i < len; i++)
        {
            temp[i] = (params[i] == null ? 0 : params[i]);
        }
        return temp;
    }
    public static int[] castToPrimitiveArray(Collection<Integer> params)
    {
        int[] temp = new int[params.size()];
        int index = 0;
        for (Integer value : params)
        {
            temp[index++] = (value == null ? 0 : value);
        }
        return temp;
    }
    public static Integer[] castToWrapperArray(int[] params)
    {
        int len = params.length;
        Integer[] temp = new Integer[len];
        for (int i = 0; i < len; i++)
        {
            temp[i] = params[i];
        }
        return temp;
    }
    public static Integer[] castToWrapperArray(Collection<Integer> params)
    {
        Integer[] temp = new Integer[params.size()];
        int index = 0;
        for (Integer value : params)
        {
            temp[index++] = (value == null ? 0 : value);
        }
        return temp;
    }
    public static <C extends Collection<Integer>> C addToCollection(C collection, int[] params)
    {
        for (int value : params)
        {
            collection.add(value);
        }
        return collection;
    }
    public static <C extends Collection<Integer>> C addToCollection(C collection, Integer[] params)
    {
        for (Integer value : params)
        {
            collection.add(value);
        }
        return collection;
    }
    public static int betweenMinMax(int i, int min, int max)
    {
        if (min > max)
        {
            throw new IllegalArgumentException();
        }
        return Math.min(max, Math.max(min, i));
    }
    public static int cycleInt(int i, int scope)
    {
        return i < 0 ? (i % scope) + scope : i % scope;
    }
    public static boolean copyArray(int source[], int tarage[], int sourceStartIdx, int sourceLength, int targetIdx)
    {
        if (source == null || tarage == null)
        {
            return false;
        }
        if (sourceStartIdx + sourceLength > source.length)
        {
            return false;
        }
        if (targetIdx + sourceLength > tarage.length)
        {
            return false;
        }
        for (int i = 0; i < sourceLength; i++)
        {
            tarage[targetIdx + i] = source[sourceStartIdx + i];
        }
        return true;
    }
    public static boolean copyArray(int source[], Integer tarage[], int sourceStartIdx, int sourceLength, int targetIdx)
    {
        if (source == null || tarage == null)
        {
            return false;
        }
        if (sourceStartIdx + sourceLength > source.length)
        {
            return false;
        }
        if (targetIdx + sourceLength > tarage.length)
        {
            return false;
        }
        for (int i = 0; i < sourceLength; i++)
        {
            tarage[targetIdx + i] = source[sourceStartIdx + i];
        }
        return true;
    }
    public static boolean copyArray(Integer source[], Integer tarage[], int sourceStartIdx, int sourceLength, int targetIdx)
    {
        if (source == null || tarage == null)
        {
            return false;
        }
        if (sourceStartIdx + sourceLength > source.length)
        {
            return false;
        }
        if (targetIdx + sourceLength > tarage.length)
        {
            return false;
        }
        for (int i = 0; i < sourceLength; i++)
        {
            tarage[targetIdx + i] = source[sourceStartIdx + i];
        }
        return true;
    }
    public static boolean copyArray(Integer source[], int tarage[], int sourceStartIdx, int sourceLength, int targetIdx)
    {
        if (source == null || tarage == null)
        {
            return false;
        }
        if (sourceStartIdx + sourceLength > source.length)
        {
            return false;
        }
        if (targetIdx + sourceLength > tarage.length)
        {
            return false;
        }
        for (int i = 0; i < sourceLength; i++)
        {
            tarage[targetIdx + i] = source[sourceStartIdx + i];
        }
        return true;
    }
    public static String toBinaryUintString(int i)
    {
        return toBinaryUintString(i, false);
    }
    public static String toOctalUintString(int i)
    {
        return toOctalUintString(i, false);
    }
    public static String toDecimalUintString(int i)
    {
        return toDecimalUintString(i, false);
    }
    public static String toHexUintString(int i)
    {
        return toHexUintString(i, false);
    }
    public static String toBinaryUintString(int i, boolean fixBit)
    {
        String str = Integer.toUnsignedString(i, 2);
        if (fixBit == true)
        {
            for (int j = str.length(); j < 32; j++)
            {
                str = "0" + str;
            }
        }
        return str;
    }
    public static String toOctalUintString(int i, boolean fixBit)
    {
        String str = Integer.toUnsignedString(i, 8);
        if (fixBit == true)
        {
            for (int j = str.length(); j < 32; j++)
            {
                str = "0" + str;
            }
        }
        return str;
    }
    public static String toDecimalUintString(int i, boolean fixBit)
    {
        String str = Integer.toUnsignedString(i, 10);
        if (fixBit == true)
        {
            for (int j = str.length(); j < 10; j++)
            {
                str = "0" + str;
            }
        }
        return str;
    }
    public static String toHexUintString(int i, boolean fixBit)
    {
        String str = Integer.toUnsignedString(i, 16);
        if (fixBit == true)
        {
            for (int j = str.length(); j < 8; j++)
            {
                str = "0" + str;
            }
        }
        return str;
    }
}
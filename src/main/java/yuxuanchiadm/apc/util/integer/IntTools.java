package yuxuanchiadm.apc.util.integer;

import java.util.Collection;

public class IntTools
{
    public static int[] toIntArray(Integer[] params)
    {
        int len = params.length;
        int[] temp = new int[len];
        for (int i = 0; i < len; i++)
        {
            temp[i] = (params[i] == null ? 0 : params[i]);
        }
        return temp;
    }
    public static int[] toIntArray(Collection<Integer> params)
    {
        int[] temp = new int[params.size()];
        int index = 0;
        for(Integer value : params)
        {
            temp[index++] = (value == null ? 0 : value);
        }
        return temp;
    }
    public static Integer[] toIntegerArray(int[] params)
    {
        int len = params.length;
        Integer[] temp = new Integer[len];
        for (int i = 0; i < len; i++)
        {
            temp[i] = params[i];
        }
        return temp;
    }
    public static <C extends Collection<Integer>> C addToIntegerCollection(C collection, int[] params)
    {
        for(int value : params)
        {
            collection.add(value);
        }
        return collection;
    }
    public static Integer parseHex(String str)
    {
        try
        {
            long l = Long.parseLong(str, 16);
            if (l > 0xFFFFFFFFL || l < 0L)
            {
                return null;
            }
            return (int) l;
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }
    public static Integer parseDec(String str)
    {
        try
        {
            return Integer.parseInt(str);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }
    public static Integer parseOct(String str)
    {
        try
        {
            long l = Long.parseLong(str, 8);
            if (l > 0xFFFFFFFFL || l < 0L)
            {
                return null;
            }
            return (int) l;
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }
    public static Integer parseBin(String str)
    {
        try
        {
            long l = Long.parseLong(str, 2);
            if (l > 0xFFFFFFFFL || l < 0L)
            {
                return null;
            }
            return (int) l;
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }
    public static int betweenMinMax(int i, int min, int max)
    {
        if(min > max)
        {
            throw new IllegalArgumentException();
        }
        return Math.min(max, Math.max(min, i));
    }
}
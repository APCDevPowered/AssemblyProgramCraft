package org.apcdevpowered.util.unsigned;

public class UnsignedTools
{
    public static long copyIntBitToLong(int i)
    {
        return (i & 0xFFFFFFFFL);
    }
    public static int copyLongBitToInt(long l)
    {
        return (int)l;
    }
    public static boolean copyArray(int source[],int tarage[],int sourceStartIdx,int sourceLength,int targetIdx)
    {
        if(source == null || tarage == null)
        {
            return false;
        }
        if(sourceStartIdx + sourceLength > source.length)
        {
            return false;
        }
        if(targetIdx + sourceLength > tarage.length)
        {
            return false;
        }
        for(int i = 0;i < sourceLength;i++)
        {
            tarage[targetIdx + i] = source[sourceStartIdx + i];
        }
        return true;
    }
    public static boolean copyArray(int source[],Integer tarage[],int sourceStartIdx,int sourceLength,int targetIdx)
    {
        if(source == null || tarage == null)
        {
            return false;
        }
        if(sourceStartIdx + sourceLength > source.length)
        {
            return false;
        }
        if(targetIdx + sourceLength > tarage.length)
        {
            return false;
        }
        for(int i = 0;i < sourceLength;i++)
        {
            tarage[targetIdx + i] = source[sourceStartIdx + i];
        }
        return true;
    }
    public static boolean copyArray(Integer source[],Integer tarage[],int sourceStartIdx,int sourceLength,int targetIdx)
    {
        if(source == null || tarage == null)
        {
            return false;
        }
        if(sourceStartIdx + sourceLength > source.length)
        {
            return false;
        }
        if(targetIdx + sourceLength > tarage.length)
        {
            return false;
        }
        for(int i = 0;i < sourceLength;i++)
        {
            tarage[targetIdx + i] = source[sourceStartIdx + i];
        }
        return true;
    }
    public static boolean copyArray(Integer source[],int tarage[],int sourceStartIdx,int sourceLength,int targetIdx)
    {
        if(source == null || tarage == null)
        {
            return false;
        }
        if(sourceStartIdx + sourceLength > source.length)
        {
            return false;
        }
        if(targetIdx + sourceLength > tarage.length)
        {
            return false;
        }
        for(int i = 0;i < sourceLength;i++)
        {
            tarage[targetIdx + i] = source[sourceStartIdx + i];
        }
        return true;
    }
    public static String read2RadixUintValue(int i)
    {
        return read2RadixUintValue(i,false);
    }
    public static String read10RadixUintValue(int i)
    {
        return read10RadixUintValue(i,false);
    }
    public static String read16RadixUintValue(int i)
    {
        return read16RadixUintValue(i,false);
    }
    public static String read2RadixUintValue(int i,boolean fixBit)
    {
        long l = (i & 0xFFFFFFFFL);
        String str = Long.toString(l,2);
        if(fixBit == true)
        {
            for(int j = str.length();j < 32;j++)
            {
                str = "0" + str;
            }
        }
        return str;
    }
    public static String read10RadixUintValue(int i,boolean fixBit)
    {
        long l = (i & 0xFFFFFFFFL);
        String str = Long.toString(l,10);
        if(fixBit == true)
        {
            for(int j = str.length();j < 10;j++)
            {
                str = "0" + str;
            }
        }
        return str;
    }
    public static String read16RadixUintValue(int i,boolean fixBit)
    {
        long l = (i & 0xFFFFFFFFL);
        String str = Long.toString(l,16);
        if(fixBit == true)
        {
            for(int j = str.length();j < 8;j++)
            {
                str = "0" + str;
            }
        }
        return str;
    }
}
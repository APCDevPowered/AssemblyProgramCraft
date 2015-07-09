package org.apcdevpowered.util;

public class UnsignedUtils
{
    public static long copyIntBitToLong(int i)
    {
        return (i & 0xFFFFFFFFL);
    }
    public static int copyLongBitToInt(long l)
    {
        return (int) l;
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
        long l = (i & 0xFFFFFFFFL);
        String str = Long.toString(l, 2);
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
        long l = (i & 0xFFFFFFFFL);
        String str = Long.toString(l, 8);
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
        long l = (i & 0xFFFFFFFFL);
        String str = Long.toString(l, 10);
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
        long l = (i & 0xFFFFFFFFL);
        String str = Long.toString(l, 16);
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
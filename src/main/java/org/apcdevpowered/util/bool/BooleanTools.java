package org.apcdevpowered.util.bool;

public class BooleanTools
{
    public static byte[] ToByteArray(boolean[] params)
    {
        int len = params.length;
        byte[] temp = new byte[len];
        for (int i = 0; i < len; i++)
        {
            temp[i] = (byte)(params[i] ? 1 : 0);
        }
        return temp;
    }
}

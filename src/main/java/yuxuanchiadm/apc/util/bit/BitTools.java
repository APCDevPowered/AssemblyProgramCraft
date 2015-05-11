package yuxuanchiadm.apc.util.bit;

public class BitTools
{
	public static int copyBit(int src, int srcPos, int dest, int destPos, int length)
	{
	    return ((((src >>> srcPos) & (int)Math.pow(2, length) - 1) << destPos) | (dest & (~(((int)Math.pow(2, length) - 1) << destPos))));
	}
	public static int setBit(int i,int bit,boolean flag)
	{
		if(bit >= 32 || bit < 0)
		{
			return i;
		}
		int opt = 1;
		for(int j = 0 ; j < bit ; j++)
		{
			opt = opt << 1;
		}
		if(flag == true)
		{
			i = i | opt;
		}
		else
		{
			i = i & ~opt;
		}
		return i;
	}
	public static int turnBit(int i,int bit)
	{
		if(bit >= 32 || bit < 0)
		{
			return i;
		}
		int opt = 1;
		for(int j = 0 ; j < bit ; j++)
		{
			opt = opt << 1;
		}
		i = i ^ opt;
		return i;
	}
	public static boolean getBit(int i,int bit)
	{
		if(bit >= 32 || bit < 0)
		{
			return false;
		}
		for(int j = 0 ; j < bit ; j++)
		{
			i = i >>> 1;
		}
		i = i & 1;
		return (i == 0 ? false : true);
	}
}
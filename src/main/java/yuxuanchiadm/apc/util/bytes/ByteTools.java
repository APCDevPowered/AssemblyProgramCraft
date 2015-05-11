package yuxuanchiadm.apc.util.bytes;

public class ByteTools
{
	public static boolean[] ToBooleanArray(byte[] params)
	{
		int len = params.length;
		boolean[] temp = new boolean[len];
		for (int i = 0; i < len; i++)
		{
			temp[i] = ((params[i] != 0) ? true : false);
		}
		return temp;
	}
}
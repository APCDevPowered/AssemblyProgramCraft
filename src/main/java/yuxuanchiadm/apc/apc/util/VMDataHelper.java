package yuxuanchiadm.apc.apc.util;

import net.minecraft.nbt.NBTTagCompound;
import yuxuanchiadm.apc.vcpu32.vm.VirtualMachine;

public class VMDataHelper
{
	public static void writeToNBT(NBTTagCompound par1NBTTagCompound, VirtualMachine VM)
	{	
		VM.writeDataToNBT(par1NBTTagCompound);
	}
	public static Object[] readFormNBT(NBTTagCompound par1NBTTagCompound, VirtualMachine VM)
	{
		Object vmObject[] = VM.readDataFormNBT(par1NBTTagCompound);
		
		Object object[] = new Object[3];
		object[0] = VM;
		object[1] = vmObject[0];
		object[2] = vmObject[1];
		
		return object;
	}
}
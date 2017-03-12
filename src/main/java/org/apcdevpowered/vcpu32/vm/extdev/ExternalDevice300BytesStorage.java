package org.apcdevpowered.vcpu32.vm.extdev;

import org.apcdevpowered.util.ArrayUtils;
import org.apcdevpowered.vcpu32.vm.AbstractExternalDevice;

import net.minecraft.nbt.NBTTagCompound;

public class ExternalDevice300BytesStorage extends AbstractExternalDevice
{    
    private int[] data = new int[300];
    @Override
	public String getDevicesName()
    {
        return "300BytesStorage";
    }
    @Override
	public DeviceTypes getDeviceType()
    {
        return DeviceTypes.Storage;
    }
    @Override
	public void setMemoryValue(int idx, int value)
    {
        if(idx >= data.length || idx < 0)
        {
            return;
        }
        if(data == null)
        {
            return;
        }
        data[idx] = value;
    }
    @Override
	public int getMemoryValue(int idx)
    {
        if(idx >= data.length || idx < 0)
        {
            return 0;
        }
        if(data == null)
        {
            return 0;
        }
        return data[idx];
    }
    @Override
	public void setMemoryValues(int idx, int[] values)
    {
        if(data == null)
        {
            return;
        }
        ArrayUtils.safeArraycopy(values, 0, data, idx, values.length);
    }
    @Override
	public int[] getMemoryValues(int idx, int length)
    {
        if(length <= 0)
        {
            return new int[0];
        }
        if(data == null)
        {
            return new int[length];
        }
        int[] tempArray = new int[length];
        ArrayUtils.safeArraycopy(data, idx, tempArray, 0, length);
        return tempArray;
    }
    @Override
	public boolean shutDown()
    {
        return true;
    }
    @Override
	public boolean reset()
    {
        return true;
    }
    @Override
	public boolean start()
    {
        return true;
    }
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        data = par1NBTTagCompound.getIntArray("data");
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setIntArray("data", data);
    }
}
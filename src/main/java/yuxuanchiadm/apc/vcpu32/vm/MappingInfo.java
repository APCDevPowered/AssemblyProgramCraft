package yuxuanchiadm.apc.vcpu32.vm;

import net.minecraft.nbt.NBTTagCompound;

public class MappingInfo
{
    public int mappingRAMStart;
    public int mappingLength;
    public int mappingDevice;
    public int mappingDeviceRAM;
    public VirtualMachine vm;
    public MappingInfo(VirtualMachine vm)
    {
        this.vm = vm;
    }
    public boolean verifyNotDuplicateConflict(MappingInfo mi)
    {
        if(mi.mappingRAMStart == this.mappingRAMStart)
        {
            return false;
        }
        else if(mi.mappingRAMStart < this.mappingRAMStart)
        {
            if((mi.mappingRAMStart + mi.mappingLength - 1) < this.mappingRAMStart)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if(mi.mappingRAMStart > this.mappingRAMStart)
        {
            if((this.mappingRAMStart + this.mappingLength - 1) < mi.mappingRAMStart)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;
    }
    public boolean verifyInMapping(int idx)
    {
        if((idx >= mappingRAMStart) || (idx < (mappingRAMStart + mappingLength)))
        {
            return true;
        }
        return false;
    }
    public int doGetMappingValue(int idx)
    {
        if(verifyInMapping(idx) == false)
        {
            return 0;
        }
        return vm.inputValueFormDevices(mappingDevice, getDeviceMappingIdx(idx));
    }
    public void doSetMappingValue(int idx, int value)
    {
        if(verifyInMapping(idx))
        {
            vm.outputValueToDevices(mappingDevice, getDeviceMappingIdx(idx), value);
        }
    }
    public int getDeviceMappingIdx(int idx)
    {
        return (mappingDeviceRAM + (idx - mappingRAMStart));
    }
    public void writeToNBT(NBTTagCompound mappingInfonbttagcompound)
    {
        mappingInfonbttagcompound.setInteger("MappingRAMStart", mappingRAMStart);
        mappingInfonbttagcompound.setInteger("MappingLength", mappingLength);
        mappingInfonbttagcompound.setInteger("MappingDevice", mappingDevice);
        mappingInfonbttagcompound.setInteger("MappingDeviceRAM", mappingDeviceRAM);
    }
    public void readFormNBT(NBTTagCompound mappingInfonbttagcompound)
    {
        mappingRAMStart = mappingInfonbttagcompound.getInteger("MappingRAMStart");
        mappingLength = mappingInfonbttagcompound.getInteger("MappingLength");
        mappingDevice = mappingInfonbttagcompound.getInteger("MappingDevice");
        mappingDeviceRAM = mappingInfonbttagcompound.getInteger("MappingDeviceRAM");
    }
}
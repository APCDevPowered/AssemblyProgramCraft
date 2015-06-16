package org.apcdevpowered.vcpu32.vm;

import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerMap;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementNotFoundException;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementTypeMismatchException;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarInteger;

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
        if (mi.mappingRAMStart == this.mappingRAMStart)
        {
            return false;
        }
        else if (mi.mappingRAMStart < this.mappingRAMStart)
        {
            if ((mi.mappingRAMStart + mi.mappingLength - 1) < this.mappingRAMStart)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if (mi.mappingRAMStart > this.mappingRAMStart)
        {
            if ((this.mappingRAMStart + this.mappingLength - 1) < mi.mappingRAMStart)
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
        if ((idx >= mappingRAMStart) || (idx < (mappingRAMStart + mappingLength)))
        {
            return true;
        }
        return false;
    }
    public int doGetMappingValue(int idx)
    {
        if (verifyInMapping(idx) == false)
        {
            return 0;
        }
        return vm.inputValueFormDevices(mappingDevice, getDeviceMappingIdx(idx));
    }
    public void doSetMappingValue(int idx, int value)
    {
        if (verifyInMapping(idx))
        {
            vm.outputValueToDevices(mappingDevice, getDeviceMappingIdx(idx), value);
        }
    }
    public int getDeviceMappingIdx(int idx)
    {
        return (mappingDeviceRAM + (idx - mappingRAMStart));
    }
    public void writeToNode(NodeContainerMap mappingInfoNodeContainerMap)
    {
        mappingInfoNodeContainerMap.addElement(NodeContainerMap.makeKey("mappingRAMStart"), mappingRAMStart);
        mappingInfoNodeContainerMap.addElement(NodeContainerMap.makeKey("mappingLength"), mappingLength);
        mappingInfoNodeContainerMap.addElement(NodeContainerMap.makeKey("mappingDevice"), mappingDevice);
        mappingInfoNodeContainerMap.addElement(NodeContainerMap.makeKey("mappingDeviceRAM"), mappingDeviceRAM);
    }
    public void readFormNode(NodeContainerMap mappingInfoNodeContainerMap) throws ElementNotFoundException, ElementTypeMismatchException
    {
        mappingRAMStart = mappingInfoNodeContainerMap.getElement(NodeContainerMap.makeKey("mappingRAMStart"), NodeScalarInteger.class).getData();
        mappingLength = mappingInfoNodeContainerMap.getElement(NodeContainerMap.makeKey("mappingLength"), NodeScalarInteger.class).getData();
        mappingDevice = mappingInfoNodeContainerMap.getElement(NodeContainerMap.makeKey("mappingDevice"), NodeScalarInteger.class).getData();
        mappingDeviceRAM = mappingInfoNodeContainerMap.getElement(NodeContainerMap.makeKey("mappingDeviceRAM"), NodeScalarInteger.class).getData();
    }
    public void writeToNBT(NBTTagCompound mappingInfonbttagcompound)
    {
        mappingInfonbttagcompound.setInteger("mappingRAMStart", mappingRAMStart);
        mappingInfonbttagcompound.setInteger("mappingLength", mappingLength);
        mappingInfonbttagcompound.setInteger("mappingDevice", mappingDevice);
        mappingInfonbttagcompound.setInteger("mappingDeviceRAM", mappingDeviceRAM);
    }
    public void readFormNBT(NBTTagCompound mappingInfonbttagcompound)
    {
        mappingRAMStart = mappingInfonbttagcompound.getInteger("mappingRAMStart");
        mappingLength = mappingInfonbttagcompound.getInteger("mappingLength");
        mappingDevice = mappingInfonbttagcompound.getInteger("mappingDevice");
        mappingDeviceRAM = mappingInfonbttagcompound.getInteger("mappingDeviceRAM");
    }
}
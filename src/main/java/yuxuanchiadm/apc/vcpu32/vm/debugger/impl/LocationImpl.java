package yuxuanchiadm.apc.vcpu32.vm.debugger.impl;

import yuxuanchiadm.apc.vcpu32.vm.debugger.Location;

public class LocationImpl implements Location
{
    private VirtualMachineReferenceImpl virtualMachineReference;
    
    private int memoryAddress = -1;
    
    public LocationImpl(VirtualMachineReferenceImpl virtualMachineReference)
    {
        this.virtualMachineReference = virtualMachineReference;
    }
    public LocationImpl(VirtualMachineReferenceImpl virtualMachineReference, int memoryAddress)
    {
        this.virtualMachineReference = virtualMachineReference;
        this.memoryAddress = memoryAddress;
    }
    
    @Override
    public VirtualMachineReferenceImpl virtualMachine()
    {
        return virtualMachineReference;
    }
    @Override
    public int compareTo(Location location)
    {
        return Integer.compare(this.memoryAddress(), location.memoryAddress());
    }
    @Override
    public boolean equals(Object object)
    {
        if(object == this)
        {
            return true;
        }
        else
        {
            if(object instanceof LocationImpl)
            {
                if(((LocationImpl)object).virtualMachine() == virtualMachine() && ((LocationImpl)object).memoryAddress() == memoryAddress())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
    }
    @Override
    public int memoryAddress()
    {
        return memoryAddress;
    }
}

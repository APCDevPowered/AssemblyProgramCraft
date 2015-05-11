package yuxuanchiadm.apc.vcpu32.vm.debugger.impl;

import yuxuanchiadm.apc.vcpu32.vm.AdvancedRAMArray;
import yuxuanchiadm.apc.vcpu32.vm.debugger.MemoryReference;
import yuxuanchiadm.apc.vcpu32.vm.debugger.VirtualMachineReference;

public class MemoryReferenceImpl implements MemoryReference
{
    private VirtualMachineReferenceImpl virtualMachineReference;
    
    private AdvancedRAMArray ram;
    
    public MemoryReferenceImpl(VirtualMachineReferenceImpl virtualMachineReference, AdvancedRAMArray ram)
    {
        this.virtualMachineReference = virtualMachineReference;
        this.ram = ram;
    }
    
    @Override
    public VirtualMachineReference virtualMachine()
    {
        return virtualMachineReference;
    }
    @Override
    public int getInt(int address)
    {
        return ram.getValue(address);
    }
    @Override
    public boolean setInt(int address, int value)
    {
        return ram.setValue(address, value);
    }
    @Override
    public float getFloat(int address)
    {
        return Float.intBitsToFloat(ram.getValue(address));
    }
    @Override
    public boolean setFloat(int address, float value)
    {
        return ram.setValue(address, Float.floatToRawIntBits(value));
    }
    @Override
    public String getString(int address)
    {
        return ram.readStringFromAddress(address);
    }
    @Override
    public boolean setString(int address, String value)
    {
        return ram.writeStringToAddress(address, value);
    }
    @Override
    public char getChar(int address)
    {
        return (char)ram.getValue(address);
    }
    @Override
    public boolean setChar(int address, char value)
    {
        return ram.setValue(address, value);
    }
    @Override
    public int[] getValues(int address, int length)
    {
        return ram.getValues(address, length);
    }
    @Override
    public boolean setValues(int address, int[] value)
    {
        return ram.setValues(address, 0, value.length, value);
    }
    @Override
    public int getSize()
    {
        return ram.getSize();
    }
    @Override
    public void clear()
    {
        ram.clear();
    }
}

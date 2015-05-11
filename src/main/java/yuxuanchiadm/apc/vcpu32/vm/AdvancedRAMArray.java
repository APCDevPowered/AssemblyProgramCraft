package yuxuanchiadm.apc.vcpu32.vm;

import java.util.Arrays;
import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.MemoryReferenceImpl;

public class AdvancedRAMArray
{
    private VirtualMachine vm;
    private int[] ram;
    private HashMap<Integer,MappingInfo> mappingList;
    
    public AdvancedRAMArray(VirtualMachine vm)
    {
       this(vm, 0);
    }
    public AdvancedRAMArray(VirtualMachine vm, int size)
    {
        this.vm = vm;
        this.ram = new int[size];
        this.mappingList = new HashMap<Integer,MappingInfo>();
    }
    public synchronized int[] getValues(int idx, int length)
    {
        if(idx + length - 1 >= ram.length || idx < 0 || length < 0)
        {
            System.out.println("[VCPU-32]内存寻址超出范围");
            return null;
        }
        int temp[] = new int[length];
        for(int i = 0; i < length; i++)
        {
            MappingInfo mappingInfo = getMapping(idx + i);
            if(mappingInfo == null)
            {
                temp[i] = ram[idx + i];
            }
            else
            {
                int inputLength = Math.min(mappingInfo.mappingLength, temp.length - i);
                int[] inputsValue = vm.inputsValueFormDevices(mappingInfo.mappingDevice, mappingInfo.getDeviceMappingIdx(idx + i), inputLength);
                if(inputsValue != null)
                {
                    System.arraycopy(inputsValue, 0, temp, i, inputLength);
                }
                i = i + inputLength - 1;
            }
        }
        return temp;
    }
    public synchronized boolean setValues(int idx, int startIdx, int length, int values[])
    {
        if(idx + length - 1 >= ram.length || idx < 0 || startIdx < 0 || length < 0 || startIdx + length - 1 >= values.length)
        {
            System.out.println("[VCPU-32]内存寻址超出范围");
            return false;
        }
        for(int i = 0; i < length; i++)
        {
            MappingInfo mappingInfo = getMapping(idx + i);
            if(mappingInfo == null)
            {
                ram[idx + i] = values[startIdx + i];
            }
            else
            {
                int inputLength = Math.min(mappingInfo.mappingLength, length - i);
                int[] temp = new int[inputLength];
                System.arraycopy(values, startIdx + i, temp, 0, inputLength);
                vm.outputsValueToDevices(mappingInfo.mappingDevice, mappingInfo.getDeviceMappingIdx(idx + i), temp);
                i = i + inputLength - 1;
            }
        }
        return true;
    }
    public synchronized int getValue(int idx)
    {
        if(idx >= ram.length || idx < 0)
        {
            System.out.println("[VCPU-32]内存寻址超出范围");
            return 0;
        }
        MappingInfo mappingInfo = getMapping(idx);
        if(mappingInfo == null)
        {
            return ram[idx];
        }
        else
        {
            return vm.inputValueFormDevices(mappingInfo.mappingDevice, mappingInfo.getDeviceMappingIdx(idx));
        }
    }
    public synchronized boolean setValue(int idx, int value)
    {
        if(idx >= ram.length || idx < 0)
        {
            System.out.println("[VCPU-32]内存寻址超出范围");
            return false;
        }
        MappingInfo mappingInfo = getMapping(idx);
        if(mappingInfo == null)
        {
            ram[idx] = value;
        }
        else
        {
            vm.outputValueToDevices(mappingInfo.mappingDevice, mappingInfo.getDeviceMappingIdx(idx), value);
        }
        return true;
    }
    public String readStringFromAddress(int address)
    {
        int ramSize = this.getSize();
        
        if(address >= ramSize)
        {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        int length = this.getValue(address);
        boolean halfEnding = length % 2 == 1;
        int end = (halfEnding ? ((length + 1) / 2) : (length / 2)) + address;
        if(end >= ramSize)
        {
            return null;
        }
        for (int current = address + 1; current <= end; current++)
        {
            if (current == end && halfEnding)
            {
                char char1 = (char) (this.getValue(current) >>> 16);
                builder.append(char1);
            }
            else
            {
                char char1 = (char) (this.getValue(current) >>> 16);
                char char2 = (char) (this.getValue(current) >>> 0);
                builder.append(char1);
                builder.append(char2);
            }
        }
        return builder.toString();
    }
    public boolean writeStringToAddress(int address, String string)
    {
        int ramSize = this.getSize();
        
        if(address >= ramSize)
        {
            return false;
        }
        char[] charArray = string.toCharArray();
        int length = charArray.length;
        boolean halfEnding = length % 2 == 1;
        int end = (halfEnding ? ((length + 1) / 2) : (length / 2)) + address;
        if(end >= ramSize)
        {
            return false;
        }
        this.setValue(address, length);
        int charAt = 0;
        while(true)
        {
            int current = charAt / 2 + address + 1;
            int value = 0;
            if(charAt < length)
            {
                value |= charArray[charAt] << 16;
                charAt++;
            }
            else
            {
                this.setValue(current, value);
                break;
            }
            if(charAt < length)
            {
                value |= charArray[charAt] << 0;
                charAt++;
            }
            else
            {
                this.setValue(current, value);
                break;
            }
            this.setValue(current, value);
            if(charAt >= length)
            {
                break;
            }
        }
        return true;
    }
    public synchronized void clear()
    {
        Arrays.fill(ram, 0);
        mappingList.clear();
    }
    public synchronized boolean addMapping(int mappingDevice, int mappingDeviceRAM, int mappingLength, int mappingRAMStart)
    {
        if(mappingDevice < 0)
        {
            System.out.println("[VCPU-32]设备ID不能小于0");
            return false;
        }
        if(mappingDeviceRAM < 0)
        {
            System.out.println("[VCPU-32]设备ID映射地址不能小于0");
            return false;
        }
        if(mappingLength < 0)
        {
            System.out.println("[VCPU-32]设备ID映射长度不能小于0");
            return false;
        }
        if(mappingRAMStart < 0)
        {
            System.out.println("[VCPU-32]内存ID映射地址不能小于0");
            return false;
        }
        if(mappingRAMStart + mappingLength - 1 >= ram.length || mappingRAMStart < 0)
        {
            System.out.println("[VCPU-32]内存寻址超出范围");
            return false;
        }
        MappingInfo mi = new MappingInfo(vm);
        mi.mappingDevice = mappingDevice;
        mi.mappingDeviceRAM = mappingDeviceRAM;
        mi.mappingLength = mappingLength;
        mi.mappingRAMStart = mappingRAMStart;
        for(MappingInfo tempMI : mappingList.values())
        {
            if(!mi.verifyNotDuplicateConflict(tempMI))
            {
                System.out.println("[VCPU-32]尝试映射 [" + mappingRAMStart + "] - [" + (mappingRAMStart + mappingLength - 1) + "] 到外置设备 " + mappingDevice + " 的内存 [" + mappingDeviceRAM + "] - [" + (mappingDeviceRAM + mappingLength - 1) + "] 时冲突");
                return false;
            }
        }
        for(int i = 0;i < mappingLength;i++)
        {
            ram[mappingRAMStart + i] = 0;
        }
        mappingList.put(mappingRAMStart, mi);
        System.out.println("[VCPU-32]映射 [" + mappingRAMStart + "] - [" + (mappingRAMStart + mappingLength - 1) + "] 到外置设备 " + mappingDevice + " 的内存 [" + mappingDeviceRAM + "] - [" + (mappingDeviceRAM + mappingLength - 1) + "] 成功");
        return true;
    }
    public synchronized boolean removeMapping(int idx)
    {
        MappingInfo mi = mappingList.get(idx);
        if(mi == null)
        {
            return false;
        }
        else
        {
            System.out.println("[VCPU-32] [" + mi.mappingRAMStart + "] - [" + (mi.mappingRAMStart + mi.mappingLength - 1) + "] 已被取消映射到外置设备 " + mi.mappingDevice + " 的内存 [" + mi.mappingDeviceRAM + "] - [" + (mi.mappingDeviceRAM + mi.mappingLength - 1) + "]");
            mappingList.remove(idx);
            return true;
        }
    }
    public synchronized int getSize()
    {
        return ram.length;
    }
    private MappingInfo getMapping(int idx)
    {
        for(MappingInfo mappingInfo : mappingList.values())
        {
            if(mappingInfo.verifyInMapping(idx) == true)
            {
                return mappingInfo;
            }
        }
        return null;
    }
    public synchronized void writeToNBT(NBTTagCompound ramNbtTagCompound)
    {
        int[] temp = new int[ram.length];
        System.arraycopy(ram, 0, temp, 0, ram.length);
        ramNbtTagCompound.setIntArray("ram", temp);
        
        NBTTagList mappingListNbtTagList = new NBTTagList();
        for(MappingInfo tempMI : mappingList.values())
        {
            NBTTagCompound mappingInfoNbtTagCompound = new NBTTagCompound();
            tempMI.writeToNBT(mappingInfoNbtTagCompound);
            mappingListNbtTagList.appendTag(mappingInfoNbtTagCompound);
        }
        ramNbtTagCompound.setTag("mappingList", mappingListNbtTagList);
    }
    public synchronized void readFromNBT(NBTTagCompound ramNbtTagCompound)
    {
        ram = ramNbtTagCompound.getIntArray("ram");
        
        NBTTagList mappingListNbtTagList = ramNbtTagCompound.getTagList("mappingList", 10);
        for(int i = 0;i < mappingListNbtTagList.tagCount();i++)
        {
            NBTTagCompound mappingInfoNbtTagCompound = mappingListNbtTagList.getCompoundTagAt(i);
            if(mappingInfoNbtTagCompound == null)
            {
                break;
            }
            MappingInfo mi = new MappingInfo(vm);
            mi.readFormNBT(mappingInfoNbtTagCompound);
            mappingList.put(mappingInfoNbtTagCompound.getInteger("mappingRAMStart"), mi);
        }
    }
    
    public int[] getInnerArray()
    {
        return ram;
    }
    
    private Object referenceInitLock = new Object();
    private MemoryReferenceImpl reference;
    
    public MemoryReferenceImpl getReference()
    {
        synchronized(referenceInitLock)
        {
            if(reference == null)
            {
                reference = new MemoryReferenceImpl(vm.getReference(), this);
            }
            return reference;
        }
    }
}
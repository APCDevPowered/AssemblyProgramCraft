package yuxuanchiadm.apc.vcpu32.vm;

import net.minecraft.nbt.NBTTagCompound;

public abstract class AbstractExternalDevice
{
    //操作锁
    public Object uselock = new Object();
    //设备类型枚举常量
    public static enum DeviceTypes
    {
        Unknown, Display, Sound, Storage, Input, Network, Auxiliary, Other
    }
    //得到设备的名字
    public abstract String getDevicesName();
    //得到设备的类型
    public abstract DeviceTypes getDeviceType();
    //向设备写入数据
    public abstract void setMemoryValue(int idx,int value);
    //从设备获得数据
    public abstract int getMemoryValue(int idx);
    //向设备写入数据组
    public abstract void setMemoryValues(int idx,int[] values);
    //从设备获得数据组
    public abstract int[] getMemoryValues(int idx,int length);
    //关闭设备时调用
    public abstract boolean shutDown();
    //重置设备时调用
    public abstract boolean reset();
    //启动设备时调用
    public abstract boolean start();
    //获取特征码
    public String getDevicesSignature()
    {
        return String.valueOf(getClass().getSimpleName().hashCode() + getDeviceType().ordinal());
    }
    
    //以下方法用于保存或读取数据，只会在MC要求保存或读取数据时调用，设备关闭、重置或启动时不会调用。
    
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {}
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {}
}
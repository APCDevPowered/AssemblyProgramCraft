package org.apcdevpowered.vcpu32.asm;

import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerMap;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementNotFoundException;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementTypeMismatchException;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarInteger;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarIntegerArray;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarString;

import net.minecraft.nbt.NBTTagCompound;

public class ProgramPackage
{
    // 程序数据
    public int[] data;
    // 程序名称
    public String programName;
    // 程序代码结束位置
    public int programEnd;
    // 静态储存区结束位置
    public int staticRAMEnd;
    // 是否为BIOS程序
    public boolean isBIOS;
    // 程序装入位置
    public int startRAM;
    // 程序静态储存区装入位置
    public int startStaticRAM;
    // 程序调试信息
    public DebugInfo debugInfo;
    
    public ProgramPackage()
    {
    }
    public ProgramPackage(int[] data, int programEnd, int staticRAMEnd, boolean isBIOS, int startRAM, int startStaticRAMOffset, String programName, DebugInfo debugInfo)
    {
        this.data = data;
        this.programEnd = programEnd;
        this.staticRAMEnd = staticRAMEnd;
        this.isBIOS = isBIOS;
        this.startRAM = startRAM;
        this.startStaticRAM = startStaticRAMOffset;
        this.programName = programName;
        this.debugInfo = debugInfo;
    }
    public void writeToNode(NodeContainerMap programPackageNodeContainerMap)
    {
        programPackageNodeContainerMap.addElement(NodeContainerMap.makeKey("data"), data);
        programPackageNodeContainerMap.addElement(NodeContainerMap.makeKey("programName"), programName);
        programPackageNodeContainerMap.addElement(NodeContainerMap.makeKey("programEnd"), programEnd);
        programPackageNodeContainerMap.addElement(NodeContainerMap.makeKey("staticRAMEnd"), staticRAMEnd);
        programPackageNodeContainerMap.addElement(NodeContainerMap.makeKey("isBIOS"), isBIOS);
        programPackageNodeContainerMap.addElement(NodeContainerMap.makeKey("startRAM"), startRAM);
        programPackageNodeContainerMap.addElement(NodeContainerMap.makeKey("startStaticRAMOffset"), startStaticRAM);
        if (debugInfo != null)
        {
            NodeContainerMap debugInfoNodeContainerMap = new NodeContainerMap();
            debugInfo.writeToNode(debugInfoNodeContainerMap);
            programPackageNodeContainerMap.addElement(NodeContainerMap.makeKey("debugInfo"), debugInfoNodeContainerMap);
        }
    }
    public void readFromNode(NodeContainerMap programPackageNodeContainerMap) throws ElementNotFoundException, ElementTypeMismatchException
    {
        data = programPackageNodeContainerMap.getElement(NodeContainerMap.makeKey("data"), NodeScalarIntegerArray.class).getData();
        programName = programPackageNodeContainerMap.getElement(NodeContainerMap.makeKey("programName"), NodeScalarString.class).getData();
        programEnd = programPackageNodeContainerMap.getElement(NodeContainerMap.makeKey("programEnd"), NodeScalarInteger.class).getData();
        staticRAMEnd = programPackageNodeContainerMap.getElement(NodeContainerMap.makeKey("staticRAMEnd"), NodeScalarInteger.class).getData();
        isBIOS = programPackageNodeContainerMap.getBoolean(NodeContainerMap.makeKey("isBIOS"));
        startRAM = programPackageNodeContainerMap.getElement(NodeContainerMap.makeKey("startRAM"), NodeScalarInteger.class).getData();
        startStaticRAM = programPackageNodeContainerMap.getElement(NodeContainerMap.makeKey("startStaticRAMOffset"), NodeScalarInteger.class).getData();
        if (programPackageNodeContainerMap.hasElement(NodeContainerMap.makeKey("debugInfo")))
        {
            debugInfo = new DebugInfo();
            debugInfo.readFromNode(programPackageNodeContainerMap.getMap(NodeContainerMap.makeKey("debugInfo")));
        }
    }
    @Deprecated
    public void writeToNBT(NBTTagCompound programPackageNBTTagCompound)
    {
        programPackageNBTTagCompound.setIntArray("data", data);
        programPackageNBTTagCompound.setString("programName", programName);
        programPackageNBTTagCompound.setInteger("programEnd", programEnd);
        programPackageNBTTagCompound.setInteger("staticRAMEnd", staticRAMEnd);
        programPackageNBTTagCompound.setBoolean("isBIOS", isBIOS);
        programPackageNBTTagCompound.setInteger("startRAM", startRAM);
        programPackageNBTTagCompound.setInteger("startStaticRAMOffset", startStaticRAM);
        if (debugInfo != null)
        {
            NBTTagCompound debugInfoNBTTagCompound = new NBTTagCompound();
            debugInfo.writeToNBT(debugInfoNBTTagCompound);
            programPackageNBTTagCompound.setTag("debugInfo", debugInfoNBTTagCompound);
        }
    }
    @Deprecated
    public void readFromNBT(NBTTagCompound programPackageNBTTagCompound)
    {
        data = programPackageNBTTagCompound.getIntArray("data");
        programName = programPackageNBTTagCompound.getString("programName");
        programEnd = programPackageNBTTagCompound.getInteger("programEnd");
        staticRAMEnd = programPackageNBTTagCompound.getInteger("staticRAMEnd");
        isBIOS = programPackageNBTTagCompound.getBoolean("isBIOS");
        startRAM = programPackageNBTTagCompound.getInteger("startRAM");
        startStaticRAM = programPackageNBTTagCompound.getInteger("startStaticRAMOffset");
        if (programPackageNBTTagCompound.hasKey("debugInfo"))
        {
            debugInfo = new DebugInfo();
            debugInfo.readFromNBT(programPackageNBTTagCompound.getCompoundTag("debugInfo"));
        }
    }
}
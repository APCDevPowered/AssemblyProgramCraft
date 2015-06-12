package org.apcdevpowered.apc.common.init;

import org.apcdevpowered.apc.common.init.blockRegistry.AssemblyProgramCraftItemRegistry;
import org.apcdevpowered.apc.common.item.ItemBIOSWriter;
import org.apcdevpowered.apc.common.item.ItemPortSettingTool;
import org.apcdevpowered.apc.common.item.ItemVCPU32ComputerCMOSChip;
import org.apcdevpowered.apc.common.item.ItemVCPU32Debugger;

import net.minecraft.init.Bootstrap;
import net.minecraft.item.Item;

public class AssemblyProgramCraftItems
{    
    public static final ItemVCPU32ComputerCMOSChip item_vcpu_32_computer_cmos_chip;
    public static final ItemVCPU32Debugger item_vcpu_32_debugger;
    public static final ItemBIOSWriter item_bios_writer;
    public static final ItemPortSettingTool item_port_setting_tool;
    
    private static Item getRegisteredItem(String name)
    {
        return AssemblyProgramCraftItemRegistry.getRegisteredItem(name);
    }
    static
    {
        if (!Bootstrap.isRegistered())
        {
            throw new RuntimeException("Accessed Items before Bootstrap!");
        }
        else
        {
            item_vcpu_32_computer_cmos_chip = (ItemVCPU32ComputerCMOSChip) getRegisteredItem("item_vcpu_32_computer_cmos_chip");
            item_vcpu_32_debugger = (ItemVCPU32Debugger) getRegisteredItem("item_vcpu_32_debugger");
            item_bios_writer = (ItemBIOSWriter) getRegisteredItem("item_bios_writer");
            item_port_setting_tool = (ItemPortSettingTool) getRegisteredItem("item_port_setting_tool");
        }
    }
}
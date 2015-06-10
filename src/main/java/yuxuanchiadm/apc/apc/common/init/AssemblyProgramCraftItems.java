package yuxuanchiadm.apc.apc.common.init;

import yuxuanchiadm.apc.apc.common.init.blockRegistry.AssemblyProgramCraftItemRegistry;
import yuxuanchiadm.apc.apc.common.item.ItemBIOSWriter;
import yuxuanchiadm.apc.apc.common.item.ItemPortSettingTool;
import yuxuanchiadm.apc.apc.common.item.ItemVCPU32ComputerCMOSChip;
import yuxuanchiadm.apc.apc.common.item.ItemVCPU32Debugger;
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
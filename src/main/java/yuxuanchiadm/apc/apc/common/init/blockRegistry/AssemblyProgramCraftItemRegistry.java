package yuxuanchiadm.apc.apc.common.init.blockRegistry;

import java.util.HashMap;
import java.util.Map;

import yuxuanchiadm.apc.apc.common.item.ItemBIOSWriter;
import yuxuanchiadm.apc.apc.common.item.ItemPortSettingTool;
import yuxuanchiadm.apc.apc.common.item.ItemVCPU32ComputerCMOSChip;
import yuxuanchiadm.apc.apc.common.item.ItemVCPU32Debugger;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class AssemblyProgramCraftItemRegistry
{
    private static final Map<String, Item> registeredItem = new HashMap<String, Item>();
    
    public static void registerItems()
    {
        registerItem(new ItemVCPU32ComputerCMOSChip(), "item_vcpu_32_computer_cmos_chip");
        registerItem(new ItemVCPU32Debugger(), "item_vcpu_32_debugger");
        registerItem(new ItemBIOSWriter(), "item_bios_writer");
        registerItem(new ItemPortSettingTool(), "item_port_setting_tool");
    }
    public static void registerItem(Item item, String name)
    {
        GameRegistry.registerItem(item, name);
        
        item.setUnlocalizedName(name);
        
        registeredItem.put(name, item);
    }
    public static Item getRegisteredItem(String name)
    {
        return registeredItem.get(name);
    }
}
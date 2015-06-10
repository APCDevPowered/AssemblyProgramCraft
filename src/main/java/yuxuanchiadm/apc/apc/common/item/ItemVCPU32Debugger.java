package yuxuanchiadm.apc.apc.common.item;

import yuxuanchiadm.apc.apc.common.creativetab.AssemblyProgramCraftCreativeTabs;
import net.minecraft.item.Item;

public class ItemVCPU32Debugger extends Item
{
    public ItemVCPU32Debugger()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(AssemblyProgramCraftCreativeTabs.tabApc);
    }
}
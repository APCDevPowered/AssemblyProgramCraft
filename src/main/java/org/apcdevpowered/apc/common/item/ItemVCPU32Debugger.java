package org.apcdevpowered.apc.common.item;

import org.apcdevpowered.apc.common.creativetab.AssemblyProgramCraftCreativeTabs;

import net.minecraft.item.Item;

public class ItemVCPU32Debugger extends Item
{
    public ItemVCPU32Debugger()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(AssemblyProgramCraftCreativeTabs.tabApc);
    }
}
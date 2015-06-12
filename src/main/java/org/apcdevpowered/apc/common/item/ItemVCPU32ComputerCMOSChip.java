package org.apcdevpowered.apc.common.item;

import org.apcdevpowered.apc.common.creativetab.AssemblyProgramCraftCreativeTabs;

import net.minecraft.item.Item;

public class ItemVCPU32ComputerCMOSChip extends Item
{
    public ItemVCPU32ComputerCMOSChip()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(AssemblyProgramCraftCreativeTabs.tabApc);
    }
}
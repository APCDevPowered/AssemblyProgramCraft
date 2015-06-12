package org.apcdevpowered.apc.common.creativetab;

import org.apcdevpowered.apc.common.init.AssemblyProgramCraftBlocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public abstract class AssemblyProgramCraftCreativeTabs extends CreativeTabs
{
    public static final AssemblyProgramCraftCreativeTabs tabApc = new AssemblyProgramCraftCreativeTabs("apc")
    {
        @Override
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(AssemblyProgramCraftBlocks.block_vcpu_32_computer);
        }
    };

    public AssemblyProgramCraftCreativeTabs(String label)
    {
        super(label);
    }
}
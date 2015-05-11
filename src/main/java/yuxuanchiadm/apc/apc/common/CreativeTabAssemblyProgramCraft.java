package yuxuanchiadm.apc.apc.common;

import yuxuanchiadm.apc.AssemblyProgramCraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabAssemblyProgramCraft extends CreativeTabs
{
	public CreativeTabAssemblyProgramCraft()
	{
		super("AssemblyProgramCraft");
	}
	public String getTranslatedTabLabel()
    {
        return "AssemblyProgramCraft";
    }
    @Override
    public Item getTabIconItem()
    {
        return Item.getItemFromBlock(AssemblyProgramCraft.instance.block_computer);
    }
}
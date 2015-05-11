package yuxuanchiadm.apc.apc.item;

import yuxuanchiadm.apc.AssemblyProgramCraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemVCPU32ComputerCMOSChip extends Item
{
    public ItemVCPU32ComputerCMOSChip()
    {
        this.setUnlocalizedName("item_vcpu_32_computer_cmos_chip");
        this.setMaxStackSize(1);
        this.setCreativeTab(AssemblyProgramCraft.instance.creativeTabAPC);
    }
    public void registerIcons(IIconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("AssemblyProgramCraft:Chip");
    }
}
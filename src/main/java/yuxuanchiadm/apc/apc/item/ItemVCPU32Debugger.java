package yuxuanchiadm.apc.apc.item;

import yuxuanchiadm.apc.AssemblyProgramCraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemVCPU32Debugger extends Item
{
    public ItemVCPU32Debugger()
    {
        super();
        this.setUnlocalizedName("item_vcpu_32_debugger");
        this.setMaxStackSize(1);
        this.setCreativeTab(AssemblyProgramCraft.instance.creativeTabAPC);
    }
    public void registerIcons(IIconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("AssemblyProgramCraft:Debugger");
    }
}
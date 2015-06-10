package yuxuanchiadm.apc.apc.common.item;

import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import yuxuanchiadm.apc.apc.common.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.common.creativetab.AssemblyProgramCraftCreativeTabs;
import yuxuanchiadm.apc.apc.common.network.AssemblyProgramCraftGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBIOSWriter extends Item
{
    public ItemBIOSWriter()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(AssemblyProgramCraftCreativeTabs.tabApc);
    }
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if(par2World.isRemote)
        {
            par3EntityPlayer.swingItem();
            return par1ItemStack;
        }
        FMLNetworkHandler.openGui(par3EntityPlayer, AssemblyProgramCraft.instance, AssemblyProgramCraftGuiHandler.BIOS_WRITER_GUI_ID, null, 0, 0, 0);
        return par1ItemStack;
    }
}
package yuxuanchiadm.apc.apc.item;

import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.network.AssemblyProgramCraftGuiHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBIOSWriter extends Item
{
    public ItemBIOSWriter()
    {
        super();
        this.setUnlocalizedName("item_bios_writer");
        this.setMaxStackSize(1);
        this.setCreativeTab(AssemblyProgramCraft.instance.creativeTabAPC);
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
    public void registerIcons(IIconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("AssemblyProgramCraft:Writer");
    }
}
package yuxuanchiadm.apc.apc.item;

import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.network.AssemblyProgramCraftGuiHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPortSettingTool extends Item
{
    public ItemPortSettingTool()
    {
        super();
        this.setUnlocalizedName("item_port_setting_tool");
        this.setMaxStackSize(1);
        this.setCreativeTab(AssemblyProgramCraft.instance.creativeTabAPC);
    }
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if(world.isRemote)
        {
            FMLNetworkHandler.openGui(player, AssemblyProgramCraft.instance, AssemblyProgramCraftGuiHandler.ID_SETTIING_GUI_ID, world, x, y, z);
        }
        return true;
    }
    public void registerIcons(IIconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("AssemblyProgramCraft:PortSettingTool");
    }
}

package yuxuanchiadm.apc.apc.common.item;

import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import yuxuanchiadm.apc.apc.common.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.common.creativetab.AssemblyProgramCraftCreativeTabs;
import yuxuanchiadm.apc.apc.common.network.AssemblyProgramCraftGuiHandler;
import yuxuanchiadm.apc.apc.common.tileEntity.TileEntityExternalDevice;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemPortSettingTool extends Item
{
    public ItemPortSettingTool()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(AssemblyProgramCraftCreativeTabs.tabApc);
    }
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if(world.isRemote)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof TileEntityExternalDevice)
            {
                FMLNetworkHandler.openGui(player, AssemblyProgramCraft.instance, AssemblyProgramCraftGuiHandler.ID_SETTIING_GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
        }
        return false;
    }
}

package yuxuanchiadm.apc.apc.block;

import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceRedstoneController;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockExternalDeviceRedstoneController extends BlockContainer
{
    public BlockExternalDeviceRedstoneController()
    {
        super(Material.rock);
        this.setBlockName("block_external_device_redstone_controller");
        this.setCreativeTab(AssemblyProgramCraft.instance.creativeTabAPC);
    }
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TileEntityExternalDeviceRedstoneController();
    }
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("AssemblyProgramCraft:RedstoneController");
    }
    public boolean canProvidePower()
    {
        return true;
    }
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        TileEntity tileEntity = par1IBlockAccess.getTileEntity(par2, par3, par4);
        if(tileEntity instanceof TileEntityExternalDeviceRedstoneController)
        {
            TileEntityExternalDeviceRedstoneController tileEntityExternalDeviceRedstoneController = (TileEntityExternalDeviceRedstoneController)tileEntity;
            return tileEntityExternalDeviceRedstoneController.redstoneOutputPower[Facing.oppositeSide[par5]];
        }
        return 0;
    }
    public boolean onBlockActivated(World theworld, int x, int y, int z, EntityPlayer entityplayer, int sideHit, float xCoord, float yCoord, float zCoord)
    {
        if(theworld.isRemote)
        {
            entityplayer.addChatMessage(new ChatComponentText("[红石控制器]sideHit : " + sideHit));
        }
        return true;
    }
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
    	return true;
    }
}

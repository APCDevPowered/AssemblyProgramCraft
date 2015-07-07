package org.apcdevpowered.apc.common.block;

import org.apcdevpowered.apc.common.creativetab.AssemblyProgramCraftCreativeTabs;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceRedstoneController;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * The block of external device redstone controller.
 * 
 * @author yuxuanchiadm
 * @see org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceRedstoneController
 * @see org.apcdevpowered.vcpu32.vm.extdev.ExternalDeviceRedstoneController
 * @see net.minecraft.block.BlockRedstoneWire
 * @since a1.0.0
 */
public class BlockExternalDeviceRedstoneController extends BlockExternlaDevice
{
    /**
     * Constructs a block.
     */
    public BlockExternalDeviceRedstoneController()
    {
        super(Material.rock);
        this.setCreativeTab(AssemblyProgramCraftCreativeTabs.tabApc);
    }
    /**
     * Returns a new instance of a block's tile entity class. Called on placing
     * the block.
     * 
     * @param worldIn
     *            The world of block in.
     * 
     * @param meta
     *            The metadata of the block.
     */
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityExternalDeviceRedstoneController();
    }
    /**
     * Can this block provide power. Only wire currently seems to have this
     * change based on its state.
     * 
     * @return Whether can provide power.
     */
    @Override
    public boolean canProvidePower()
    {
        return true;
    }
    /**
     * Returns the weak power level of the {@code side}.
     * 
     * @param worldIn
     *            The world of block in.
     * 
     * @param pos
     *            The position of block at.
     * 
     * @param state
     *            The block state of the block.
     * 
     * @param side
     *            The side to get weak power level.
     * 
     * @return The power level of the {@code side}.
     */
    @Override
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityExternalDeviceRedstoneController)
        {
            TileEntityExternalDeviceRedstoneController tileEntityExternalDeviceRedstoneController = (TileEntityExternalDeviceRedstoneController) tileEntity;
            return tileEntityExternalDeviceRedstoneController.redstoneOutputPower[side.getOpposite().getIndex()];
        }
        return 0;
    }
    /**
     * Called when block activated by player.
     * 
     * @param worldIn
     *            The world of block activated in.
     * 
     * @param pos
     *            The location of block activated at.
     * 
     * @param state
     *            The state of block activated with.
     * 
     * @param playerIn
     *            Who activated this block.
     * 
     * @param side
     *            The side which is activated.
     * 
     * @param hitX
     *            The x axis position of activated position.
     * 
     * @param hitY
     *            The y axis position of activated position.
     * 
     * @param hitZ
     *            The z axis position of activated position.
     * 
     * @return Whether block response.
     */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            playerIn.addChatMessage(new ChatComponentText("[RedstoneController]This side id is : " + side.getIndex()));
        }
        return true;
    }
    /**
     * Checks if the block is a solid face on the given side, used by placement
     * logic.
     *
     * @param world
     *            The current world.
     * 
     * @param pos
     *            Block position in world.
     * 
     * @param side
     *            The side to check.
     * 
     * @return True if the block is solid on the specified side.
     */
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return true;
    }
    /**
     * Returns the type of render function that is called for this block.
     * 
     * @return The type of render function that is called for this block.
     */
    @Override
    public int getRenderType()
    {
        return 3;
    }
}

package org.apcdevpowered.apc.common.block;

import org.apcdevpowered.apc.common.creativetab.AssemblyProgramCraftCreativeTabs;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceNoteBox;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * The block of external device note box.
 * 
 * @author yuxuanchiadm
 * @see org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceNoteBox
 * @see org.apcdevpowered.vcpu32.extdev.ExternalDeviceNoteBox
 * @see net.minecraft.block.BlockNote
 * @since a1.1.3
 */
public class BlockExternalDeviceNoteBox extends BlockExternlaDevice
{
    /**
     * Constructs a block.
     */
    public BlockExternalDeviceNoteBox()
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
    public TileEntity createNewTileEntity(World worldIn, int metadata)
    {
        return new TileEntityExternalDeviceNoteBox();
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
    @Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return true;
    }
    /**
     * Called on both Client and Server when
     * {@link net.minecraft.world.World#addBlockEvent} is called.
     * 
     * @param worldIn
     *            The world of block in.
     * 
     * @param pos
     *            The location of block placed at.
     * 
     * @param state
     *            The state of block placed with.
     * 
     * @param eventID
     *            The event id of the event received.
     * 
     * @param eventParam
     *            The event param of the event received.
     * 
     * @return Whether block handled event.
     */
    @Override
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
    {
        boolean isBlockHandledEvent = false;
        if (super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam))
        {
            isBlockHandledEvent = true;
        }
        switch (eventID)
        {
            case 0:
                worldIn.spawnParticle(EnumParticleTypes.NOTE, (double) pos.getX() + 0.5D, (double) pos.getY() + 1.2D, (double) pos.getZ() + 0.5D, (double) eventParam / 24.0D, 0.0D, 0.0D, new int[0]);
                isBlockHandledEvent = true;
                break;
            default:
                break;
        }
        return isBlockHandledEvent;
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

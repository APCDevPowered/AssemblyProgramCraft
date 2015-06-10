package yuxuanchiadm.apc.apc.common.block;

import yuxuanchiadm.apc.apc.common.creativetab.AssemblyProgramCraftCreativeTabs;
import yuxuanchiadm.apc.apc.common.tileEntity.TileEntityExternalDeviceNumberMonitor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * The block of external device number monitor.
 * 
 * @author yuxuanchiadm
 * @see yuxuanchiadm.apc.apc.common.tileEntity.TileEntityExternalDeviceNumberMonitor
 * @see yuxuanchiadm.apc.vcpu32.extdev.ExternalDeviceNumberMonitor
 * @see yuxuanchiadm.apc.apc.client.renderer.tileentity.RenderTileEntityExternalDeviceNumberMonitor
 * @since a1.0.0
 */
public class BlockExternalDeviceNumberMonitor extends BlockExternlaDevice
{
    /** The the block state property of block`s face. */
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    
    /**
     * Constructs a block.
     */
    public BlockExternalDeviceNumberMonitor()
    {
        super(Material.rock);
        this.setDefaultState(this.getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH));
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
        return new TileEntityExternalDeviceNumberMonitor();
    }
    /**
     * Get the block collision box.
     * 
     * @param worldIn
     *            The world of block in.
     * 
     * @param pos
     *            The position of block at.
     * 
     * @param state
     *            The block state of the block.
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        // Set block bounds first.
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }
    /**
     * Set the block bounds.
     * 
     * @param worldIn
     *            The world of block in.
     * 
     * @param pos
     *            The position of block at.
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == this)
        {
            if (iblockstate.getValue(FACING) == EnumFacing.NORTH)
            {
                this.setBlockBounds(0.0F, 1.0F / 16.0F, 0.0F, 1.0F, 14.0F / 16.0F, 4.0F / 16.0F);
            }
            else if (iblockstate.getValue(FACING) == EnumFacing.EAST)
            {
                this.setBlockBounds(12.0F / 16.0F, 1.0F / 16.0F, 0.0F, 1.0F, 14.0F / 16.0F, 1.0F);
            }
            else if (iblockstate.getValue(FACING) == EnumFacing.SOUTH)
            {
                this.setBlockBounds(0.0F, 1.0F / 16.0F, 12.0F / 16.0F, 1.0F, 14.0F / 16.0F, 1.0F);
            }
            else if (iblockstate.getValue(FACING) == EnumFacing.WEST)
            {
                this.setBlockBounds(0.0F, 1.0F / 16.0F, 0.0F, 4.0F / 16.0F, 14.0F / 16.0F, 1.0F);
            }
        }
    }
    /**
     * Called when living placed a block.
     * 
     * @param worldIn
     *            The world of block placed in.
     * 
     * @param pos
     *            The location of block placed at.
     * 
     * @param state
     *            The state of block placed with.
     * 
     * @param placer
     *            What living placed this block.
     * 
     * @param stack
     *            The item stack of the placer hold in hand.
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()));
    }
    /**
     * Returns whether is a opaque block.
     * 
     * @return Whether is a opaque block.
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    /**
     * Returns whether is a full cube.
     * 
     * @return Whether is a full cuble.
     */
    @Override
    public boolean isFullCube()
    {
        return false;
    }
    /**
     * Returns the type of render function that is called for this block.
     * 
     * @return The type of render function that is called for this block.
     */
    @Override
    public int getRenderType()
    {
        return -1;
    }
    /**
     * Convert the given metadata into a block state for this Block.
     * 
     * @param meta
     *            Metadata convert from.
     * 
     * @return Block state convert from metadata.
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }
    /**
     * Convert the block state into the correct metadata value.
     * 
     * @param state
     *            Block state convert from.
     * 
     * @return Metadata convert from block state.
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
    }
    /**
     * Create block state of this block.
     * 
     * @return Block state of this block.
     */
    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, FACING);
    }
}

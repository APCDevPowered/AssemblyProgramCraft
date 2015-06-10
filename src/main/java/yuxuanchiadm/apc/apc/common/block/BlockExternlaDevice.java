package yuxuanchiadm.apc.apc.common.block;

import yuxuanchiadm.apc.apc.common.tileEntity.TileEntityExternalDevice;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * The block of external device note box.
 * 
 * @author yuxuanchiadm
 * @see yuxuanchiadm.apc.apc.common.tileEntity.TileEntityExternalDevice
 * @see yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice
 * @since a1.2.0
 */
public abstract class BlockExternlaDevice extends BlockContainer
{
    /**
     * Constructs a block.
     */
    public BlockExternlaDevice(Material materialIn)
    {
        super(materialIn);
    }
    /**
     * Called when block is break.
     * 
     * @param worldIn
     *            The world of block placed in.
     * 
     * @param pos
     *            The location of block placed at.
     * 
     * @param state
     *            The state of block placed with.
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntityExternalDevice tileentity = (TileEntityExternalDevice) worldIn.getTileEntity(pos);
        super.breakBlock(worldIn, pos, state);
        tileentity.updataConnector(state);
    }
}

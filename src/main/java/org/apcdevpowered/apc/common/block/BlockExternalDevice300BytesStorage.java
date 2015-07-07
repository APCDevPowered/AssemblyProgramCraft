package org.apcdevpowered.apc.common.block;

import org.apcdevpowered.apc.common.creativetab.AssemblyProgramCraftCreativeTabs;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDevice300BytesStorage;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * The block of external device 300 bytes storage.
 * 
 * @author yuxuanchiadm
 * @see org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDevice300BytesStorage
 * @see org.apcdevpowered.vcpu32.vm.extdev.ExternalDevice300BytesStorage
 * @since a1.0.0
 */
public class BlockExternalDevice300BytesStorage extends BlockExternlaDevice
{
    /**
     * Constructs a block.
     */
    public BlockExternalDevice300BytesStorage()
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
        return new TileEntityExternalDevice300BytesStorage();
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

package yuxuanchiadm.apc.apc.block;

import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDevice300BytesStorage;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockExternalDevice300BytesStorage extends BlockContainer
{
    public BlockExternalDevice300BytesStorage()
    {
        super(Material.rock);
        this.setBlockName("block_external_device_300_byte_storage");
        this.setCreativeTab(AssemblyProgramCraft.instance.creativeTabAPC);
    }
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TileEntityExternalDevice300BytesStorage();
    }
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("AssemblyProgramCraft:BaseTexture");
    }
}

package yuxuanchiadm.apc.apc.block;

import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceNoteBox;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockExternalDeviceNoteBox extends BlockContainer
{
    public BlockExternalDeviceNoteBox()
    {
        super(Material.rock);
        this.setBlockName("block_external_device_note_box");
        this.setCreativeTab(AssemblyProgramCraft.instance.creativeTabAPC);
    }
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TileEntityExternalDeviceNoteBox();
    }
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("AssemblyProgramCraft:NoteBox");
    }
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
    	return true;
    }
    public boolean onBlockEventReceived(World world, int x, int y, int z, int type, int data)
    {
        switch (type)
        {
            case 0:
                world.spawnParticle("note", x + 0.5D, y + 1.2D, z + 0.5D, data / 24.0D, 0.0D, 0.0D);
                break;
            default:
                break;
        }
        return true;
    }
}

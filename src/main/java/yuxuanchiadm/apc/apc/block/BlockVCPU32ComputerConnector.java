package yuxuanchiadm.apc.apc.block;

import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.client.AssemblyProgramCraftProxyClient;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32ComputerConnector;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVCPU32ComputerConnector extends BlockContainer
{
	public BlockVCPU32ComputerConnector()
	{
		super(Material.rock);
		this.setBlockName("block_vcpu_32_computer_connector");
		this.setCreativeTab(AssemblyProgramCraft.instance.creativeTabAPC);
	}
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return new TileEntityVCPU32ComputerConnector();
	}
	public int getRenderType()
    {
        return AssemblyProgramCraftProxyClient.RENDER_TYPE_BLOCK_VCPU_32_COMPUTER_CONNECTOR;
    }
	public boolean isOpaqueCube()
    {
        return false;
    }
	public boolean renderAsNormalBlock()
    {
        return false;
    }
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(par1World, x, y, z);
        return super.getCollisionBoundingBoxFromPool(par1World, x, y, z);
    }
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
        if(blockAccess.getBlock(x, y, z).equals(this))
        {
            int direction = blockAccess.getBlockMetadata(x, y, z) & 3;
            if(direction == 0)
            {
                this.setBlockBounds(3.0F/16.0F, 0.0F, 0.0F, 13.0F/16.0F, 6.0F/16.0F, 0.75F);
            }
            else if(direction == 1)
            {
                this.setBlockBounds(0.25F, 0.0F, 3.0F/16.0F, 1.0F, 6.0F/16.0F, 13.0F/16.0F);
            }
            else if(direction == 2)
            {
                this.setBlockBounds(3.0F/16.0F, 0.0F, 0.25F, 13.0F/16.0F, 6.0F/16.0F, 1.0F);
            }
            else if(direction == 3)
            {
                this.setBlockBounds(0.0F, 0.0F, 3.0F/16.0F, 0.75F, 6.0F/16.0F, 13.0F/16.0F);
            }
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
		int direction = MathHelper.floor_double((double)(entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int metadata = world.getBlockMetadata(x, y, z) & 12;
        if (direction == 0)
        {
            world.setBlockMetadataWithNotify(x, y, z, 0 | metadata, 2);
        }
        if (direction == 1)
        {
            world.setBlockMetadataWithNotify(x, y, z, 1 | metadata, 2);
        }
        if (direction == 2)
        {
            world.setBlockMetadataWithNotify(x, y, z, 2 | metadata, 2);
        }
        if (direction == 3)
        {
            world.setBlockMetadataWithNotify(x, y, z, 3 | metadata, 2);
        }
    }
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("AssemblyProgramCraft:Connector");
    }
}
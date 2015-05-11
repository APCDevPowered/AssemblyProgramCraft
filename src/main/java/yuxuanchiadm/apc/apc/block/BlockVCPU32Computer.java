package yuxuanchiadm.apc.apc.block;

import java.util.Random;

import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.client.AssemblyProgramCraftProxyClient;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32Computer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVCPU32Computer extends BlockContainer
{
    private final Random random = new Random();
    
	public BlockVCPU32Computer()
	{
		super(Material.rock);
		this.setBlockName("block_vcpu_32_computer");
		this.setCreativeTab(AssemblyProgramCraft.instance.creativeTabAPC);
	}
	/**
	 * @param world 方块所在的世界
	 * @param x 方块的 x 坐标
	 * @param y 方块的 y 坐标
	 * @param z 方块的 z 坐标
	 * @param entityplayer 激活（右键点击）方块的玩家
	 * @param sideHit 玩家击中的表面
	 * @param xCoord 玩家击中的表面的位置的 x 坐标
	 * @param yCoord 玩家击中的表面的位置的 y 坐标
	 * @param zCoord 玩家击中的表面的位置的 z 坐标
	 * @return 是否触发动作
	 */
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int sideHit, float xCoord, float yCoord, float zCoord)
	{
        int direction = world.getBlockMetadata(x, y, z) & 3;
        if(direction == 0)
		{
			if(sideHit == 2)
			{
				if((xCoord >= (6.0/16.0)) && (xCoord <= (7.0/16.0)) && (yCoord >= (5.0/16.0)) && (yCoord <= (6.0/16.0)))
				{
					if (world.isRemote)
			        {
			            return true;
			        }
					TileEntityVCPU32Computer tileentity = (TileEntityVCPU32Computer)world.getTileEntity(x, y, z);
					if(tileentity == null)
					{
						return false;
					}
					tileentity.turnPower();
					return true;
				}
			}
			else if(sideHit == 5 || sideHit == 4)
			{
			    if (world.isRemote)
                {
                    return true;
                }
                TileEntityVCPU32Computer tileentity = (TileEntityVCPU32Computer)world.getTileEntity(x, y, z);
                if(tileentity == null)
                {
                    return false;
                }
                tileentity.modifyComputer(entityplayer);
                return true;
			}
		}
		else if(direction == 1)
		{
			if(sideHit == 5)
			{
				if((zCoord >= (6.0/16.0)) && (zCoord <= (7.0/16.0)) && (yCoord >= (5.0/16.0)) && (yCoord <= (6.0/16.0)))
				{
					if (world.isRemote)
			        {
			            return true;
			        }
					TileEntityVCPU32Computer tileentity = (TileEntityVCPU32Computer)world.getTileEntity(x, y, z);
					if(tileentity == null)
					{
						return false;
					}
					tileentity.turnPower();
					return true;
				}
			}
			else if(sideHit == 3 || sideHit == 2)
            {
			    if (world.isRemote)
                {
                    return true;
                }
                TileEntityVCPU32Computer tileentity = (TileEntityVCPU32Computer)world.getTileEntity(x, y, z);
                if(tileentity == null)
                {
                    return false;
                }
                tileentity.modifyComputer(entityplayer);
                return true;
            }
		}
		else if(direction == 2)
		{
			if(sideHit == 3)
			{
				if((xCoord >= (9.0/16.0)) && (xCoord <= (10.0/16.0)) && (yCoord >= (5.0/16.0)) && (yCoord <= (6.0/16.0)))
				{
					if (world.isRemote)
			        {
			            return true;
			        }
					TileEntityVCPU32Computer tileentity = (TileEntityVCPU32Computer)world.getTileEntity(x, y, z);
					if(tileentity == null)
					{
						return false;
					}
					tileentity.turnPower();
					return true;
				}
			}
			else if(sideHit == 5 || sideHit == 4)
            {
			    if (world.isRemote)
                {
                    return true;
                }
                TileEntityVCPU32Computer tileentity = (TileEntityVCPU32Computer)world.getTileEntity(x, y, z);
                if(tileentity == null)
                {
                    return false;
                }
                tileentity.modifyComputer(entityplayer);
                return true;
            }
		}
		else if(direction == 3)
		{
			if(sideHit == 4)
			{
				if((zCoord >= (9.0/16.0)) && (zCoord <= (10.0/16.0)) && (yCoord >= (5.0/16.0)) && (yCoord <= (6.0/16.0)))
				{
					if (world.isRemote)
			        {
			            return true;
			        }
					TileEntityVCPU32Computer tileentity = (TileEntityVCPU32Computer)world.getTileEntity(x, y, z);
					if(tileentity == null)
					{
						return false;
					}
					tileentity.turnPower();
					return true;
				}
			}
			else if(sideHit == 3 || sideHit == 2)
            {
			    if (world.isRemote)
                {
                    return true;
                }
                TileEntityVCPU32Computer tileentity = (TileEntityVCPU32Computer)world.getTileEntity(x, y, z);
                if(tileentity == null)
                {
                    return false;
                }
                tileentity.modifyComputer(entityplayer);
                return true;
            }
		}
		return false;
    }
	public boolean isOpaqueCube()
    {
        return false;
    }
	public boolean renderAsNormalBlock()
    {
        return false;
    }
	public int getRenderType()
    {
        return AssemblyProgramCraftProxyClient.RENDER_TYPE_BLOCK_VCPU_32_COMPUTER;
    }
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return new TileEntityVCPU32Computer();
	}
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
	    if(blockAccess.getBlock(x, y, z).equals(this))
	    {
	        int direction = blockAccess.getBlockMetadata(x, y, z) & 3;
	        if(direction == 0)
	        {
	            this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.75F, 1.0F);
	        }
	        else if(direction == 1)
	        {
	            this.setBlockBounds(0.0F, 0.0F, 0.25F, 0.75F, 0.75F, 0.75F);
	        }
	        else if(direction == 2)
	        {
	            this.setBlockBounds(0.25F, 0.0F, 0.0F, 0.75F, 0.75F, 0.75F);
	        }
	        else if(direction == 3)
	        {
	            this.setBlockBounds(0.25F, 0.0F, 0.25F, 1.0F, 0.75F, 0.75F);
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
        blockIcon = iconRegister.registerIcon("AssemblyProgramCraft:Computer");
	}
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        TileEntityVCPU32Computer tileEntityVCPU32Computer = (TileEntityVCPU32Computer)world.getTileEntity(x, y, z);
        
        if(tileEntityVCPU32Computer != null)
        {
            ItemStack itemstack = tileEntityVCPU32Computer.getStackInSlot(0);

            if (itemstack != null)
            {
                float randomX = this.random.nextFloat() * 0.8F + 0.1F;
                float randomY = this.random.nextFloat() * 0.8F + 0.1F;
                float randomZ = this.random.nextFloat() * 0.8F + 0.1F;
                EntityItem entityitem = new EntityItem(world, (double)((float)x + randomX), (double)((float)y + randomY), (double)((float)z + randomZ), new ItemStack(itemstack.getItem(), itemstack.stackSize, itemstack.getItemDamage()));
                entityitem.delayBeforeCanPickup = 10;
                float motionRandom = 0.05F;
                entityitem.motionX = (double)((float)this.random.nextGaussian() * motionRandom);
                entityitem.motionY = (double)((float)this.random.nextGaussian() * motionRandom + 0.2F);
                entityitem.motionZ = (double)((float)this.random.nextGaussian() * motionRandom);

                if (itemstack.hasTagCompound())
                {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                }
                
                world.spawnEntityInWorld(entityitem);
            }
        }
        super.breakBlock(world, x, y, z, block, metadata);
    }
}
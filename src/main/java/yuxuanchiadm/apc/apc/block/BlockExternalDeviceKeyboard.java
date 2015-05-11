package yuxuanchiadm.apc.apc.block;

import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.client.AssemblyProgramCraftProxyClient;
import yuxuanchiadm.apc.apc.network.AssemblyProgramCraftGuiHandler;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceKeyboard;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockExternalDeviceKeyboard extends BlockContainer
{
    public BlockExternalDeviceKeyboard()
    {
        super(Material.rock);
        this.setBlockName("block_external_device_keyboard");
        this.setCreativeTab(AssemblyProgramCraft.instance.creativeTabAPC);
    }
    public boolean onBlockActivated(World theworld, int x, int y, int z, EntityPlayer entityplayer, int sideHit, float xCoord, float yCoord, float zCoord)
    {
        if(theworld.isRemote)
        {
            FMLNetworkHandler.openGui(entityplayer, AssemblyProgramCraft.instance, AssemblyProgramCraftGuiHandler.KEYBOARD_GUI_ID, theworld, x, y, z);
        }
        return true;
    }
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TileEntityExternalDeviceKeyboard();
    }
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
        if(blockAccess.getBlock(x, y, z).equals(this))
        {
            int direction = blockAccess.getBlockMetadata(x, y, z) & 3;
            if(direction == 0)
            {
                this.setBlockBounds(0.0F, 0.0F, 17F / 32F, 1.0F, 0.125F, 1.0F);
            }
            else if(direction == 1)
            {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 15F / 32F, 0.125F, 1.0F);
            }
            else if(direction == 2)
            {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 15F / 32F);
            }
            else if(direction == 3)
            {
                this.setBlockBounds(17F / 32F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
            }
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }
    public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLivingBase par5EntityLiving, ItemStack itemStack)
    {
        int var6 = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int var7 = par1World.getBlockMetadata(x, y, z) & 12;
        if (var6 == 0)
        {
            par1World.setBlockMetadataWithNotify(x, y, z, 0 | var7, 2);
        }
        if (var6 == 1)
        {
            par1World.setBlockMetadataWithNotify(x, y, z, 1 | var7, 2);
        }
        if (var6 == 2)
        {
            par1World.setBlockMetadataWithNotify(x, y, z, 2 | var7, 2);
        }
        if (var6 == 3)
        {
            par1World.setBlockMetadataWithNotify(x, y, z, 3 | var7, 2);
        }
    }
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("AssemblyProgramCraft:BaseTexture");
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
        return AssemblyProgramCraftProxyClient.RENDER_TYPE_BLOCK_EXTERNAL_DEVICE_KEYBOARD;
    }
}

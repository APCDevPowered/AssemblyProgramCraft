package yuxuanchiadm.apc.apc.block;

import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.client.AssemblyProgramCraftProxyClient;
import yuxuanchiadm.apc.apc.common.util.ConnectorFinder.WirePartInfo;
import yuxuanchiadm.apc.apc.item.ItemBlockVCPU32ComputerWire;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32ComputerWire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockVCPU32ComputerWire extends BlockContainer
{
    public static final float WIRE_HEIGTH = 0.25F;
	public BlockVCPU32ComputerWire()
	{
		super(Material.cloth);
		this.setHardness(0.1F);
		this.setBlockName("block_vcpu_32_computer_wire");
		this.setCreativeTab(AssemblyProgramCraft.instance.creativeTabAPC);
		this.setStepSound(soundTypeCloth);
	}
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("AssemblyProgramCraft:Wire");
    }
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata)
    {
        updateIndirectNeighbors(world, x, y, z, this);
    }
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        TileEntityVCPU32ComputerWire tileEntity = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
        if(tileEntity == null)
        {
            return false;
        }
        int lastSide = tileEntity.onlyHaveOneSide();
        if(lastSide != 0)
        {
            boolean flag = super.removedByPlayer(world, player, x, y, z);
            if(!world.isRemote)
            {
                for(WirePartInfo wirePartInfo : new WirePartInfo(lastSide, tileEntity).getLinkedWirePart())
                {
                    ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                }
                if(lastSide == TileEntityVCPU32ComputerWire.SIDE_BOTTOM)
                {
                    TileEntity tmpTileEntity = null;
                    tmpTileEntity = world.getTileEntity(x + 1, y, z);
                    if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                    }
                    tmpTileEntity = world.getTileEntity(x - 1, y, z);
                    if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                    }
                    tmpTileEntity = world.getTileEntity(x, y, z + 1);
                    if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                    }
                    tmpTileEntity = world.getTileEntity(x, y, z - 1);
                    if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                    }
                }
            }
            return flag;
        }
        else
        {
            MovingObjectPosition pos = retraceBlock(world, player, x, y, z);
            if (pos == null)
            {
                return false;
            }
            if (pos.typeOfHit != MovingObjectType.BLOCK)
            {
                return false;
            }
            tileEntity.removeSide(pos.subHit);
            if(!player.capabilities.isCreativeMode)
            {
                this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            }
            updateIndirectNeighbors(world, x, y, z, this);
            if(!world.isRemote)
            {
                for(WirePartInfo wirePartInfo : new WirePartInfo(pos.subHit, tileEntity).getLinkedWirePart())
                {
                    ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                }
                if(pos.subHit == TileEntityVCPU32ComputerWire.SIDE_BOTTOM)
                {
                    TileEntity tmpTileEntity = null;
                    tmpTileEntity = world.getTileEntity(x + 1, y, z);
                    if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                    }
                    tmpTileEntity = world.getTileEntity(x - 1, y, z);
                    if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                    }
                    tmpTileEntity = world.getTileEntity(x, y, z + 1);
                    if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                    }
                    tmpTileEntity = world.getTileEntity(x, y, z - 1);
                    if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                    }
                }
            }
            return false;
        }
    }
    public static void updateIndirectNeighbors(World world,int par1, int par2, int par3, Block block)
    {
        for(int x = -1;x <= 1;x++)
        {
            for(int y = -1;y <= 1;y++)
            {
                for(int z = -1;z <= 1;z++)
                {
                    world.notifyBlockOfNeighborChange(par1 + x, par2 + y, par3 + z, block);
                }
            }
        }
    }
    public static MovingObjectPosition retraceBlock(World world, EntityLivingBase entityLiving, int x, int y, int z)
    {
      Vec3 org = Vec3.createVectorHelper(entityLiving.posX, entityLiving.posY + 1.62D - entityLiving.yOffset, entityLiving.posZ);

      Vec3 vec = entityLiving.getLook(1.0F);
      Vec3 end = org.addVector(vec.xCoord * 5.0D, vec.yCoord * 5.0D, vec.zCoord * 5.0D);

      Block block = world.getBlock(x, y, z);
      if (block == null)
      {
          return null;
      }
      return block.collisionRayTrace(world, x, y, z, org, end);
    }
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int par5)
    {
        if(par5 == 0)
        {
            return world.isSideSolid(x, y + 1, z, ForgeDirection.getOrientation(par5),  true);
        }
        else if(par5 == 1)
        {
            return world.isSideSolid(x, y - 1, z, ForgeDirection.getOrientation(par5),  true);
        }
        else if(par5 == 2)
        {
            return world.isSideSolid(x, y, z + 1, ForgeDirection.getOrientation(par5),  true);
        }
        else if(par5 == 3)
        {
            return world.isSideSolid(x, y, z - 1, ForgeDirection.getOrientation(par5),  true);
        }
        else if(par5 == 4)
        {
            return world.isSideSolid(x + 1, y, z, ForgeDirection.getOrientation(par5),  true);
        }
        else if(par5 == 5)
        {
            return world.isSideSolid(x - 1, y, z, ForgeDirection.getOrientation(par5),  true);
        }
        return false;
    }
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TileEntityVCPU32ComputerWire();
    }
    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
    {
        TileEntityVCPU32ComputerWire tileentity = (TileEntityVCPU32ComputerWire)par1World.getTileEntity(par2, par3, par4);
        if (tileentity == null)
        {
            return null;
        }
        
        MovingObjectPosition p1 = null;
        int subHit = -1;
        double maxDistance = 0.0D;
        if(tileentity.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
        {
            this.setBlockBounds(0.0F, 1.0F - WIRE_HEIGTH, 0.0F, 1.0F, 1.0F, 1.0F);
            MovingObjectPosition p2 = super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
            if (p2 != null)
            {
                double thisDistance = p2.hitVec.squareDistanceTo(par5Vec3);
                if ((p1 == null) || (thisDistance < maxDistance))
                {
                    maxDistance = thisDistance; p1 = p2; subHit = TileEntityVCPU32ComputerWire.SIDE_TOP;
                }
            }
        }
        if(tileentity.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + WIRE_HEIGTH, 1.0F);
            MovingObjectPosition p2 = super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
            if (p2 != null)
            {
                double thisDistance = p2.hitVec.squareDistanceTo(par5Vec3);
                if ((p1 == null) || (thisDistance < maxDistance))
                {
                    maxDistance = thisDistance; p1 = p2; subHit = TileEntityVCPU32ComputerWire.SIDE_BOTTOM;
                }
            }
        }
        if(tileentity.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F + WIRE_HEIGTH, 1.0F, 1.0F);
            MovingObjectPosition p2 = super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
            if (p2 != null)
            {
                double thisDistance = p2.hitVec.squareDistanceTo(par5Vec3);
                if ((p1 == null) || (thisDistance < maxDistance))
                {
                    maxDistance = thisDistance; p1 = p2; subHit = TileEntityVCPU32ComputerWire.SIDE_FRONT;
                }
            }
        }
        if(tileentity.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
        {
            this.setBlockBounds(1.0F - WIRE_HEIGTH, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            MovingObjectPosition p2 = super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
            if (p2 != null)
            {
                double thisDistance = p2.hitVec.squareDistanceTo(par5Vec3);
                if ((p1 == null) || (thisDistance < maxDistance))
                {
                    maxDistance = thisDistance; p1 = p2; subHit = TileEntityVCPU32ComputerWire.SIDE_BACK;
                }
            }
        }
        if(tileentity.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
        {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - WIRE_HEIGTH, 1.0F, 1.0F, 1.0F);
            MovingObjectPosition p2 = super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
            if (p2 != null)
            {
                double thisDistance = p2.hitVec.squareDistanceTo(par5Vec3);
                if ((p1 == null) || (thisDistance < maxDistance))
                {
                    maxDistance = thisDistance; p1 = p2; subHit = TileEntityVCPU32ComputerWire.SIDE_LEFT;
                }
            }
        }
        if(tileentity.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F + WIRE_HEIGTH);
            MovingObjectPosition p2 = super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
            if (p2 != null)
            {
                double thisDistance = p2.hitVec.squareDistanceTo(par5Vec3);
                if ((p1 == null) || (thisDistance < maxDistance))
                {
                    maxDistance = thisDistance; p1 = p2; subHit = TileEntityVCPU32ComputerWire.SIDE_RIGHT;
                }
            }
        }
        
        if (p1 == null)
        {
            return null;
        }
        if(subHit == TileEntityVCPU32ComputerWire.SIDE_TOP)
        {
            this.setBlockBounds(0.0F, 1.0F - WIRE_HEIGTH, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else if(subHit == TileEntityVCPU32ComputerWire.SIDE_BOTTOM)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + WIRE_HEIGTH, 1.0F);
        }
        else if(subHit == TileEntityVCPU32ComputerWire.SIDE_FRONT)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F + WIRE_HEIGTH, 1.0F, 1.0F);
        }
        else if(subHit == TileEntityVCPU32ComputerWire.SIDE_BACK)
        {
            this.setBlockBounds(1.0F - WIRE_HEIGTH, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else if(subHit == TileEntityVCPU32ComputerWire.SIDE_LEFT)
        {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - WIRE_HEIGTH, 1.0F, 1.0F, 1.0F);
        }
        else if(subHit == TileEntityVCPU32ComputerWire.SIDE_RIGHT)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F + WIRE_HEIGTH);
        }
        p1.subHit = subHit;
        return p1;
    }
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if(tileEntity instanceof TileEntityVCPU32ComputerWire)
        {
            TileEntityVCPU32ComputerWire tileEntityWire = (TileEntityVCPU32ComputerWire)tileEntity;
            if(tileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
            {
                if(tileEntityWire.onlyHaveOneSide() != 0)
                {
                    if(!canPlaceBlockOnSide(world, x, y, z, 0))
                    {
                        world.setBlockToAir(x, y, z);
                        this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                        updateIndirectNeighbors(world, x, y, z, this);
                        for(WirePartInfo wirePartInfo : new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, tileEntityWire).getLinkedWirePart())
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                        }
                    }
                }
                else if(!canPlaceBlockOnSide(world, x, y, z, 0))
                {
                    tileEntityWire.removeSide(TileEntityVCPU32ComputerWire.SIDE_TOP);
                    this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                    updateIndirectNeighbors(world, x, y, z, this);
                    for(WirePartInfo wirePartInfo : new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, tileEntityWire).getLinkedWirePart())
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                    }
                }
            }
            if(tileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
            {
                if(tileEntityWire.onlyHaveOneSide() != 0)
                {
                    if(!canPlaceBlockOnSide(world, x, y, z, 1))
                    {
                        world.setBlockToAir(x, y, z);
                        this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                        updateIndirectNeighbors(world, x, y, z, this);
                        for(WirePartInfo wirePartInfo : new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, tileEntityWire).getLinkedWirePart())
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                        }
                        TileEntity tmpTileEntity = null;
                        tmpTileEntity = world.getTileEntity(x + 1, y, z);
                        if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                        }
                        tmpTileEntity = world.getTileEntity(x - 1, y, z);
                        if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                        }
                        tmpTileEntity = world.getTileEntity(x, y, z + 1);
                        if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                        }
                        tmpTileEntity = world.getTileEntity(x, y, z - 1);
                        if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                        }
                    }
                }
                else if(!canPlaceBlockOnSide(world, x, y, z, 1))
                {
                    tileEntityWire.removeSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM);
                    this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                    updateIndirectNeighbors(world, x, y, z, this);
                    for(WirePartInfo wirePartInfo : new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, tileEntityWire).getLinkedWirePart())
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                    }
                    TileEntity tmpTileEntity = null;
                    tmpTileEntity = world.getTileEntity(x + 1, y, z);
                    if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                    }
                    tmpTileEntity = world.getTileEntity(x - 1, y, z);
                    if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                    }
                    tmpTileEntity = world.getTileEntity(x, y, z + 1);
                    if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                    }
                    tmpTileEntity = world.getTileEntity(x, y, z - 1);
                    if(tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector)tmpTileEntity);
                    }
                }
            }
            if(tileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
            {
                if(tileEntityWire.onlyHaveOneSide() != 0)
                {
                    if(!canPlaceBlockOnSide(world, x, y, z, 2))
                    {
                        world.setBlockToAir(x, y, z);
                        this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                        updateIndirectNeighbors(world, x, y, z, this);
                        for(WirePartInfo wirePartInfo : new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, tileEntityWire).getLinkedWirePart())
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                        }
                    }
                }
                else if(!canPlaceBlockOnSide(world, x, y, z, 2))
                {
                    tileEntityWire.removeSide(TileEntityVCPU32ComputerWire.SIDE_LEFT);
                    this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                    updateIndirectNeighbors(world, x, y, z, this);
                    for(WirePartInfo wirePartInfo : new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, tileEntityWire).getLinkedWirePart())
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                    }
                }
            }
            if(tileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
            {
                if(tileEntityWire.onlyHaveOneSide() != 0)
                {
                    if(!canPlaceBlockOnSide(world, x, y, z, 3))
                    {
                        world.setBlockToAir(x, y, z);
                        this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                        updateIndirectNeighbors(world, x, y, z, this);
                        for(WirePartInfo wirePartInfo : new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, tileEntityWire).getLinkedWirePart())
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                        }
                    }
                }
                else if(!canPlaceBlockOnSide(world, x, y, z, 3))
                {
                    tileEntityWire.removeSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT);
                    this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                    updateIndirectNeighbors(world, x, y, z, this);
                    for(WirePartInfo wirePartInfo : new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, tileEntityWire).getLinkedWirePart())
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                    }
                }
            }
            if(tileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
            {
                if(tileEntityWire.onlyHaveOneSide() != 0)
                {
                    if(!canPlaceBlockOnSide(world, x, y, z, 4))
                    {
                        world.setBlockToAir(x, y, z);
                        this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                        updateIndirectNeighbors(world, x, y, z, this);
                        for(WirePartInfo wirePartInfo : new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, tileEntityWire).getLinkedWirePart())
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                        }
                    }
                }
                else if(!canPlaceBlockOnSide(world, x, y, z, 4))
                {
                    tileEntityWire.removeSide(TileEntityVCPU32ComputerWire.SIDE_BACK);
                    this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                    updateIndirectNeighbors(world, x, y, z, this);
                    for(WirePartInfo wirePartInfo : new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, tileEntityWire).getLinkedWirePart())
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                    }
                }
            }
            if(tileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
            {
                if(tileEntityWire.onlyHaveOneSide() != 0)
                {
                    if(!canPlaceBlockOnSide(world, x, y, z, 5))
                    {
                        world.setBlockToAir(x, y, z);
                        this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                        updateIndirectNeighbors(world, x, y, z, this);
                        for(WirePartInfo wirePartInfo : new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, tileEntityWire).getLinkedWirePart())
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                        }
                    }
                }
                else if(!canPlaceBlockOnSide(world, x, y, z, 5))
                {
                    tileEntityWire.removeSide(TileEntityVCPU32ComputerWire.SIDE_FRONT);
                    this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                    updateIndirectNeighbors(world, x, y, z, this);
                    for(WirePartInfo wirePartInfo : new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, tileEntityWire).getLinkedWirePart())
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                    }
                }
            }
        }
    }
    public int getRenderType()
    {
        return AssemblyProgramCraftProxyClient.RENDER_TYPE_BLOCK_VCPU_32_COMPUTER_WIRE;
    }
    public boolean isOpaqueCube()
    {
        return false;
    }
    public boolean renderAsNormalBlock()
    {
        return false;
    }
}
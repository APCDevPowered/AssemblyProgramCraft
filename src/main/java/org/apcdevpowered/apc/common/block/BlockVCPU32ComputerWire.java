package org.apcdevpowered.apc.common.block;

import org.apcdevpowered.apc.common.creativetab.AssemblyProgramCraftCreativeTabs;
import org.apcdevpowered.apc.common.item.ItemBlockVCPU32ComputerWire;
import org.apcdevpowered.apc.common.tileEntity.TileEntityVCPU32ComputerConnector;
import org.apcdevpowered.apc.common.tileEntity.TileEntityVCPU32ComputerWire;
import org.apcdevpowered.apc.common.util.BlockHelper;
import org.apcdevpowered.apc.common.util.ConnectorFinder.WirePartInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * The block of vcpu32 computer wire.
 * 
 * @author yuxuanchiadm
 * @see org.apcdevpowered.apc.common.tileEntity.TileEntityVCPU32ComputerWire
 * @see org.apcdevpowered.apc.client.renderer.tileentity.RenderTileEntityVCPU32ComputerWire
 * @since a1.0.0
 */
public class BlockVCPU32ComputerWire extends BlockContainer
{
    /** The block height of wire */
    public static final float WIRE_HEIGTH = 0.25F;
    
    /**
     * Constructs a block.
     */
    public BlockVCPU32ComputerWire()
    {
        super(Material.cloth);
        this.setHardness(0.1F);
        this.setCreativeTab(AssemblyProgramCraftCreativeTabs.tabApc);
        this.setStepSound(soundTypeCloth);
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
        return new TileEntityVCPU32ComputerWire();
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
        return null;
    }
    /**
     * Called when a player destroys this Block.
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
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        BlockHelper.updateIndirectNeighbors(worldIn, pos, this);
    }
    /**
     * Called when a player removes a block. This is responsible for actually
     * destroying the block, and the block is intact at time of call. This is
     * called regardless of whether the player can harvest the block or not.
     *
     * Return true if the block is actually destroyed.
     *
     * Note: When used in multiplayer, this is called on both client and server
     * sides!
     *
     * @param world
     *            The current world.
     * 
     * @param player
     *            The player damaging the block, may be null.
     * 
     * @param pos
     *            Block position in world.
     * 
     * @param willHarvest
     *            True if Block.harvestBlock will be called after this, if the
     *            return in true. Can be useful to delay the destruction of tile
     *            entities till after harvestBlock.
     * 
     * @return True if the block is actually destroyed.
     */
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        TileEntityVCPU32ComputerWire tileEntity = (TileEntityVCPU32ComputerWire) world.getTileEntity(pos);
        if (tileEntity == null)
        {
            return false;
        }
        EnumFacing lastSide = tileEntity.onlyHaveOneSide();
        if (lastSide != null)
        {
            boolean flag = super.removedByPlayer(world, pos, player, willHarvest);
            if (!world.isRemote)
            {
                for (WirePartInfo wirePartInfo : new WirePartInfo(lastSide, tileEntity).getLinkedWirePart())
                {
                    ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                }
                if (lastSide == EnumFacing.DOWN)
                {
                    TileEntity tmpTileEntity = null;
                    tmpTileEntity = world.getTileEntity(pos.add(1, 0, 0));
                    if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                    }
                    tmpTileEntity = world.getTileEntity(pos.add(-1, 0, 0));
                    if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                    }
                    tmpTileEntity = world.getTileEntity(pos.add(0, 0, 1));
                    if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                    }
                    tmpTileEntity = world.getTileEntity(pos.add(0, 0, -1));
                    if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                    }
                }
            }
            return flag;
        }
        else
        {
            MovingObjectPosition movingObjectPosition = BlockHelper.rayTraceBlock(world, player, pos);
            if (movingObjectPosition == null)
            {
                return false;
            }
            if (movingObjectPosition.typeOfHit != MovingObjectType.BLOCK)
            {
                return false;
            }
            EnumFacing subHit = EnumFacing.getFront(movingObjectPosition.subHit);
            WirePartInfo[] linkedWirePart = new WirePartInfo(subHit, tileEntity).getLinkedWirePart();
            tileEntity.removeSide(subHit);
            if (!player.capabilities.isCreativeMode)
            {
                this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
            }
            BlockHelper.updateIndirectNeighbors(world, pos, this);
            if (!world.isRemote)
            {
                for (WirePartInfo wirePartInfo : linkedWirePart)
                {
                    ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                }
                if (subHit == EnumFacing.DOWN)
                {
                    TileEntity tmpTileEntity = null;
                    tmpTileEntity = world.getTileEntity(pos.add(1, 0, 0));
                    if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                    }
                    tmpTileEntity = world.getTileEntity(pos.add(-1, 0, 0));
                    if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                    }
                    tmpTileEntity = world.getTileEntity(pos.add(0, 0, 1));
                    if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                    }
                    tmpTileEntity = world.getTileEntity(pos.add(0, 0, -1));
                    if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                    }
                }
            }
            return false;
        }
    }
    /**
     * Check whether this Block can be placed on the given {@code side}.
     * 
     * @param worldIn
     *            The world of block in.
     * 
     * @param pos
     *            The position of block at.
     * 
     * @param side
     *            The side try place block on.
     * 
     * @return True if block can place on {@code side}.
     */
    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return worldIn.isSideSolid(pos.offset(side.getOpposite()), side, true);
    }
    /**
     * Ray traces through the blocks collision from start vector to end vector
     * returning a ray trace hit.
     * 
     * @param worldIn
     *            The world of block in.
     * 
     * @param pos
     *            The position of block at.
     * 
     * @param start
     *            The start vector.
     * 
     * @param end
     *            The end vector.
     * 
     * @return {@link net.minecraft.util.MovingObjectPosition} of collision
     *         result.
     */
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        TileEntityVCPU32ComputerWire tileentity = (TileEntityVCPU32ComputerWire) worldIn.getTileEntity(pos);
        if (tileentity == null)
        {
            return null;
        }
        MovingObjectPosition movingObjectPosition = null;
        EnumFacing subHit = null;
        double maxDistance = 0.0D;
        if (tileentity.hasSide(EnumFacing.UP))
        {
            this.setBlockBounds(0.0F, 1.0F - WIRE_HEIGTH, 0.0F, 1.0F, 1.0F, 1.0F);
            MovingObjectPosition p2 = super.collisionRayTrace(worldIn, pos, start, end);
            if (p2 != null)
            {
                double thisDistance = p2.hitVec.squareDistanceTo(start);
                if ((movingObjectPosition == null) || (thisDistance < maxDistance))
                {
                    maxDistance = thisDistance;
                    movingObjectPosition = p2;
                    subHit = EnumFacing.UP;
                }
            }
        }
        if (tileentity.hasSide(EnumFacing.DOWN))
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + WIRE_HEIGTH, 1.0F);
            MovingObjectPosition p2 = super.collisionRayTrace(worldIn, pos, start, end);
            if (p2 != null)
            {
                double thisDistance = p2.hitVec.squareDistanceTo(start);
                if ((movingObjectPosition == null) || (thisDistance < maxDistance))
                {
                    maxDistance = thisDistance;
                    movingObjectPosition = p2;
                    subHit = EnumFacing.DOWN;
                }
            }
        }
        if (tileentity.hasSide(EnumFacing.WEST))
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F + WIRE_HEIGTH, 1.0F, 1.0F);
            MovingObjectPosition p2 = super.collisionRayTrace(worldIn, pos, start, end);
            if (p2 != null)
            {
                double thisDistance = p2.hitVec.squareDistanceTo(start);
                if ((movingObjectPosition == null) || (thisDistance < maxDistance))
                {
                    maxDistance = thisDistance;
                    movingObjectPosition = p2;
                    subHit = EnumFacing.WEST;
                }
            }
        }
        if (tileentity.hasSide(EnumFacing.EAST))
        {
            this.setBlockBounds(1.0F - WIRE_HEIGTH, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            MovingObjectPosition p2 = super.collisionRayTrace(worldIn, pos, start, end);
            if (p2 != null)
            {
                double thisDistance = p2.hitVec.squareDistanceTo(start);
                if ((movingObjectPosition == null) || (thisDistance < maxDistance))
                {
                    maxDistance = thisDistance;
                    movingObjectPosition = p2;
                    subHit = EnumFacing.EAST;
                }
            }
        }
        if (tileentity.hasSide(EnumFacing.SOUTH))
        {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - WIRE_HEIGTH, 1.0F, 1.0F, 1.0F);
            MovingObjectPosition p2 = super.collisionRayTrace(worldIn, pos, start, end);
            if (p2 != null)
            {
                double thisDistance = p2.hitVec.squareDistanceTo(start);
                if ((movingObjectPosition == null) || (thisDistance < maxDistance))
                {
                    maxDistance = thisDistance;
                    movingObjectPosition = p2;
                    subHit = EnumFacing.SOUTH;
                }
            }
        }
        if (tileentity.hasSide(EnumFacing.NORTH))
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F + WIRE_HEIGTH);
            MovingObjectPosition p2 = super.collisionRayTrace(worldIn, pos, start, end);
            if (p2 != null)
            {
                double thisDistance = p2.hitVec.squareDistanceTo(start);
                if ((movingObjectPosition == null) || (thisDistance < maxDistance))
                {
                    maxDistance = thisDistance;
                    movingObjectPosition = p2;
                    subHit = EnumFacing.NORTH;
                }
            }
        }
        if (movingObjectPosition == null)
        {
            return null;
        }
        if (subHit == EnumFacing.UP)
        {
            this.setBlockBounds(0.0F, 1.0F - WIRE_HEIGTH, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else if (subHit == EnumFacing.DOWN)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + WIRE_HEIGTH, 1.0F);
        }
        else if (subHit == EnumFacing.WEST)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F + WIRE_HEIGTH, 1.0F, 1.0F);
        }
        else if (subHit == EnumFacing.EAST)
        {
            this.setBlockBounds(1.0F - WIRE_HEIGTH, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else if (subHit == EnumFacing.SOUTH)
        {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - WIRE_HEIGTH, 1.0F, 1.0F, 1.0F);
        }
        else if (subHit == EnumFacing.NORTH)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F + WIRE_HEIGTH);
        }
        movingObjectPosition.subHit = subHit != null ? subHit.getIndex() : -1;
        return movingObjectPosition;
    }
    /**
     * Called when a neighboring block changes.
     * 
     * @param worldIn
     *            The world of block in.
     * 
     * @param pos
     *            The position of block at.
     * 
     * @param state
     *            The block state of the block.
     * 
     * @param neighborBlock
     *            The neighbor block.
     */
    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityVCPU32ComputerWire)
        {
            TileEntityVCPU32ComputerWire tileEntityWire = (TileEntityVCPU32ComputerWire) tileEntity;
            if (tileEntityWire.hasSide(EnumFacing.DOWN))
            {
                if (tileEntityWire.onlyHaveOneSide() != null)
                {
                    if (!canPlaceBlockOnSide(worldIn, pos, EnumFacing.UP))
                    {
                        worldIn.setBlockToAir(pos);
                        this.dropBlockAsItem(worldIn, pos, state, 0);
                        BlockHelper.updateIndirectNeighbors(worldIn, pos, this);
                        for (WirePartInfo wirePartInfo : new WirePartInfo(EnumFacing.DOWN, tileEntityWire).getLinkedWirePart())
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                        }
                        TileEntity tmpTileEntity = null;
                        tmpTileEntity = worldIn.getTileEntity(pos.add(1, 0, 0));
                        if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                        }
                        tmpTileEntity = worldIn.getTileEntity(pos.add(-1, 0, 0));
                        if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                        }
                        tmpTileEntity = worldIn.getTileEntity(pos.add(0, 0, 1));
                        if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                        }
                        tmpTileEntity = worldIn.getTileEntity(pos.add(0, 0, -1));
                        if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                        }
                    }
                }
                else if (!canPlaceBlockOnSide(worldIn, pos, EnumFacing.UP))
                {
                    tileEntityWire.removeSide(EnumFacing.DOWN);
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                    BlockHelper.updateIndirectNeighbors(worldIn, pos, this);
                    for (WirePartInfo wirePartInfo : new WirePartInfo(EnumFacing.DOWN, tileEntityWire).getLinkedWirePart())
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                    }
                    TileEntity tmpTileEntity = null;
                    tmpTileEntity = worldIn.getTileEntity(pos.add(1, 0, 0));
                    if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                    }
                    tmpTileEntity = worldIn.getTileEntity(pos.add(-1, 0, 0));
                    if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                    }
                    tmpTileEntity = worldIn.getTileEntity(pos.add(0, 0, 1));
                    if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                    }
                    tmpTileEntity = worldIn.getTileEntity(pos.add(0, 0, -1));
                    if (tmpTileEntity != null && tmpTileEntity instanceof TileEntityVCPU32ComputerConnector)
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector((TileEntityVCPU32ComputerConnector) tmpTileEntity);
                    }
                }
            }
            if (tileEntityWire.hasSide(EnumFacing.UP))
            {
                if (tileEntityWire.onlyHaveOneSide() != null)
                {
                    if (!canPlaceBlockOnSide(worldIn, pos, EnumFacing.DOWN))
                    {
                        worldIn.setBlockToAir(pos);
                        this.dropBlockAsItem(worldIn, pos, state, 0);
                        BlockHelper.updateIndirectNeighbors(worldIn, pos, this);
                        for (WirePartInfo wirePartInfo : new WirePartInfo(EnumFacing.UP, tileEntityWire).getLinkedWirePart())
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                        }
                    }
                }
                else if (!canPlaceBlockOnSide(worldIn, pos, EnumFacing.DOWN))
                {
                    tileEntityWire.removeSide(EnumFacing.UP);
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                    BlockHelper.updateIndirectNeighbors(worldIn, pos, this);
                    for (WirePartInfo wirePartInfo : new WirePartInfo(EnumFacing.UP, tileEntityWire).getLinkedWirePart())
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                    }
                }
            }
            if (tileEntityWire.hasSide(EnumFacing.WEST))
            {
                if (tileEntityWire.onlyHaveOneSide() != null)
                {
                    if (!canPlaceBlockOnSide(worldIn, pos, EnumFacing.EAST))
                    {
                        worldIn.setBlockToAir(pos);
                        this.dropBlockAsItem(worldIn, pos, state, 0);
                        BlockHelper.updateIndirectNeighbors(worldIn, pos, this);
                        for (WirePartInfo wirePartInfo : new WirePartInfo(EnumFacing.WEST, tileEntityWire).getLinkedWirePart())
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                        }
                    }
                }
                else if (!canPlaceBlockOnSide(worldIn, pos, EnumFacing.EAST))
                {
                    tileEntityWire.removeSide(EnumFacing.WEST);
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                    BlockHelper.updateIndirectNeighbors(worldIn, pos, this);
                    for (WirePartInfo wirePartInfo : new WirePartInfo(EnumFacing.WEST, tileEntityWire).getLinkedWirePart())
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                    }
                }
            }
            if (tileEntityWire.hasSide(EnumFacing.EAST))
            {
                if (tileEntityWire.onlyHaveOneSide() != null)
                {
                    if (!canPlaceBlockOnSide(worldIn, pos, EnumFacing.WEST))
                    {
                        worldIn.setBlockToAir(pos);
                        this.dropBlockAsItem(worldIn, pos, state, 0);
                        BlockHelper.updateIndirectNeighbors(worldIn, pos, this);
                        for (WirePartInfo wirePartInfo : new WirePartInfo(EnumFacing.EAST, tileEntityWire).getLinkedWirePart())
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                        }
                    }
                }
                else if (!canPlaceBlockOnSide(worldIn, pos, EnumFacing.WEST))
                {
                    tileEntityWire.removeSide(EnumFacing.EAST);
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                    BlockHelper.updateIndirectNeighbors(worldIn, pos, this);
                    for (WirePartInfo wirePartInfo : new WirePartInfo(EnumFacing.EAST, tileEntityWire).getLinkedWirePart())
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                    }
                }
            }
            if (tileEntityWire.hasSide(EnumFacing.SOUTH))
            {
                if (tileEntityWire.onlyHaveOneSide() != null)
                {
                    if (!canPlaceBlockOnSide(worldIn, pos, EnumFacing.NORTH))
                    {
                        worldIn.setBlockToAir(pos);
                        this.dropBlockAsItem(worldIn, pos, state, 0);
                        BlockHelper.updateIndirectNeighbors(worldIn, pos, this);
                        for (WirePartInfo wirePartInfo : new WirePartInfo(EnumFacing.SOUTH, tileEntityWire).getLinkedWirePart())
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                        }
                    }
                }
                else if (!canPlaceBlockOnSide(worldIn, pos, EnumFacing.NORTH))
                {
                    tileEntityWire.removeSide(EnumFacing.SOUTH);
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                    BlockHelper.updateIndirectNeighbors(worldIn, pos, this);
                    for (WirePartInfo wirePartInfo : new WirePartInfo(EnumFacing.SOUTH, tileEntityWire).getLinkedWirePart())
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                    }
                }
            }
            if (tileEntityWire.hasSide(EnumFacing.NORTH))
            {
                if (tileEntityWire.onlyHaveOneSide() != null)
                {
                    if (!canPlaceBlockOnSide(worldIn, pos, EnumFacing. SOUTH))
                    {
                        worldIn.setBlockToAir(pos);
                        this.dropBlockAsItem(worldIn, pos, state, 0);
                        BlockHelper.updateIndirectNeighbors(worldIn, pos, this);
                        for (WirePartInfo wirePartInfo : new WirePartInfo(EnumFacing.NORTH, tileEntityWire).getLinkedWirePart())
                        {
                            ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                        }
                    }
                }
                else if (!canPlaceBlockOnSide(worldIn, pos, EnumFacing. SOUTH))
                {
                    tileEntityWire.removeSide(EnumFacing.NORTH);
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                    BlockHelper.updateIndirectNeighbors(worldIn, pos, this);
                    for (WirePartInfo wirePartInfo : new WirePartInfo(EnumFacing.NORTH, tileEntityWire).getLinkedWirePart())
                    {
                        ItemBlockVCPU32ComputerWire.updataConnector(wirePartInfo.side, wirePartInfo.tileEntity);
                    }
                }
            }
        }
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
}
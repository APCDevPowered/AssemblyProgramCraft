package yuxuanchiadm.apc.apc.common.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;

public class RedirectBlockAccess implements IBlockAccess
{
    private IBlockAccess blockAccess;
    private Vec3i offset;
    private EnumFacing rotation;
    
    public RedirectBlockAccess(IBlockAccess blockAccess, Vec3i offset, EnumFacing rotation)
    {
        this.blockAccess = blockAccess;
        this.offset = offset;
        this.rotation = rotation;
    }
    public BlockPos transformPos(BlockPos pos)
    {
        if(pos == null)
        {
            return null;
        }
        BlockPos result = null;
        if (rotation == EnumFacing.DOWN)
        {
            result = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        }
        if (rotation == EnumFacing.UP)
        {
            result = new BlockPos(-pos.getX(), -pos.getY(), -pos.getZ());
        }
        if (rotation == EnumFacing.NORTH)
        {
            result = new BlockPos(pos.getX(), -pos.getZ(), pos.getY());
        }
        if (rotation == EnumFacing.SOUTH)
        {
            result = new BlockPos(pos.getX(), pos.getZ(), -pos.getY());
        }
        if (rotation == EnumFacing.WEST)
        {
            result = new BlockPos(pos.getY(), -pos.getX(), pos.getZ());
        }
        if (rotation == EnumFacing.EAST)
        {
            result = new BlockPos(-pos.getY(), pos.getX(), pos.getZ());
        }
        result = result.add(offset);
        return result;
    }
    public BlockPos realPos(BlockPos pos)
    {
        if(pos == null)
        {
            return null;
        }
        BlockPos result = pos.subtract(offset);
        if (rotation == EnumFacing.DOWN)
        {
            result = new BlockPos(result.getX(), result.getY(), result.getZ());
        }
        if (rotation == EnumFacing.UP)
        {
            result = new BlockPos(-result.getX(), -result.getY(), -result.getZ());
        }
        if (rotation == EnumFacing.NORTH)
        {
            result = new BlockPos(result.getX(), result.getZ(), -result.getY());
        }
        if (rotation == EnumFacing.SOUTH)
        {
            result = new BlockPos(result.getX(), -result.getZ(), result.getY());
        }
        if (rotation == EnumFacing.WEST)
        {
            result = new BlockPos(-result.getY(), result.getX(), result.getZ());
        }
        if (rotation == EnumFacing.EAST)
        {
            result = new BlockPos(result.getY(), -result.getX(), result.getZ());
        }
        return result;
    }
    
    public EnumFacing transformFace(EnumFacing face)
    {
        if (rotation == EnumFacing.DOWN)
        {
            return face;
        }
        if (rotation == EnumFacing.UP)
        {
            return face.getOpposite();
        }
        if (rotation == EnumFacing.NORTH)
        {
            return face.rotateAround(EnumFacing.Axis.X);
        }
        if (rotation == EnumFacing.SOUTH)
        {
            return face.rotateAround(EnumFacing.Axis.X).rotateAround(EnumFacing.Axis.X).rotateAround(EnumFacing.Axis.X);
        }
        if (rotation == EnumFacing.WEST)
        {
            return face.rotateAround(EnumFacing.Axis.Z).rotateAround(EnumFacing.Axis.Z).rotateAround(EnumFacing.Axis.Z);
        }
        if (rotation == EnumFacing.EAST)
        {
            return face.rotateAround(EnumFacing.Axis.Z);
        }
        return null;
    }
    public EnumFacing realFace(EnumFacing face)
    {
        if (rotation == EnumFacing.DOWN)
        {
            return face;
        }
        if (rotation == EnumFacing.UP)
        {
            return face.getOpposite();
        }
        if (rotation == EnumFacing.NORTH)
        {
            return face.rotateAround(EnumFacing.Axis.X).rotateAround(EnumFacing.Axis.X).rotateAround(EnumFacing.Axis.X);
        }
        if (rotation == EnumFacing.SOUTH)
        {
            return face.rotateAround(EnumFacing.Axis.X);
        }
        if (rotation == EnumFacing.WEST)
        {
            return face.rotateAround(EnumFacing.Axis.Z);
        }
        if (rotation == EnumFacing.EAST)
        {
            return face.rotateAround(EnumFacing.Axis.Z).rotateAround(EnumFacing.Axis.Z).rotateAround(EnumFacing.Axis.Z);
        }
        return null;
    }
    @Override
    public TileEntity getTileEntity(BlockPos pos)
    {
        return blockAccess.getTileEntity(transformPos(pos));
    }
    @Override
    public int getCombinedLight(BlockPos pos, int p_175626_2_)
    {
        return blockAccess.getCombinedLight(transformPos(pos), p_175626_2_);
    }
    @Override
    public IBlockState getBlockState(BlockPos pos)
    {
        return blockAccess.getBlockState(transformPos(pos));
    }
    @Override
    public boolean isAirBlock(BlockPos pos)
    {
        return blockAccess.isAirBlock(transformPos(pos));
    }
    @Override
    public BiomeGenBase getBiomeGenForCoords(BlockPos pos)
    {
        return blockAccess.getBiomeGenForCoords(transformPos(pos));
    }
    @Override
    public boolean extendedLevelsInChunkCache()
    {
        return blockAccess.extendedLevelsInChunkCache();
    }
    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction)
    {
        return blockAccess.getStrongPower(transformPos(pos), transformFace(direction));
    }
    @Override
    public WorldType getWorldType()
    {
        return blockAccess.getWorldType();
    }
    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default)
    {
        return blockAccess.isSideSolid(transformPos(pos), transformFace(side), _default);
    }
}

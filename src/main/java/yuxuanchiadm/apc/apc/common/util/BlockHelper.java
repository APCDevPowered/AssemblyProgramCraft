package yuxuanchiadm.apc.apc.common.util;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockHelper
{
    public static void updateIndirectNeighbors(World world, BlockPos pos, Block block)
    {
        for (int x = -1; x <= 1; x++)
        {
            for (int y = -1; y <= 1; y++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    world.notifyBlockOfStateChange(pos.add(x, y, z), block);
                }
            }
        }
    }
    public static MovingObjectPosition rayTraceBlock(World worldIn, EntityLivingBase entityLiving, BlockPos pos)
    {
        Vec3 start = new Vec3(entityLiving.posX, entityLiving.posY + entityLiving.getEyeHeight(), entityLiving.posZ);
        Vec3 look = entityLiving.getLook(1.0F);
        Vec3 end = start.addVector(look.xCoord * 5.0D, look.yCoord * 5.0D, look.zCoord * 5.0D);
        Block block = worldIn.getBlockState(pos).getBlock();
        if (block == null)
        {
            return null;
        }
        return block.collisionRayTrace(worldIn, pos, start, end);
    }
}

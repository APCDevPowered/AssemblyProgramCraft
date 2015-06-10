package yuxuanchiadm.apc.apc.common.util;

import yuxuanchiadm.apc.apc.common.AssemblyProgramCraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class WorldHelper
{
    public static World getWorldFromDimension(int dimension)
    {
        return getWorldFromDimension(dimension, FMLCommonHandler.instance().getEffectiveSide());
    }
    public static World getWorldFromDimension(int dimension, Side side)
    {
        return AssemblyProgramCraft.proxy.getWorldFromDimension(dimension, side);
    }
}

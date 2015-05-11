package yuxuanchiadm.apc.apc.common.util;

import yuxuanchiadm.apc.AssemblyProgramCraft;
import net.minecraft.world.World;

public class WorldHelper
{
    public static World getWorldFromDimension(int dimension)
    {
        return getWorldFromDimension(dimension, false);
    }
    public static World getWorldFromDimension(int dimension, boolean expectClientWorld)
    {
        return AssemblyProgramCraft.proxy.getWorldFromDimension(dimension, expectClientWorld);
    }
}

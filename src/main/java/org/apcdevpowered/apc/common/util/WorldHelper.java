package org.apcdevpowered.apc.common.util;

import java.io.File;

import org.apcdevpowered.apc.common.AssemblyProgramCraft;

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
    public static File getSaveFolder(World world)
    {
        File worldDirectory = world.getSaveHandler().getWorldDirectory();
        String saveFolder = world.provider.getSaveFolder();
        if(saveFolder != null)
        {
            return new File(worldDirectory, saveFolder);
        }
        return worldDirectory;
    }
}

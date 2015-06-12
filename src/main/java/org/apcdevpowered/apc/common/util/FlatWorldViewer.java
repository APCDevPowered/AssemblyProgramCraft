package org.apcdevpowered.apc.common.util;

import org.apcdevpowered.apc.common.util.FlatMaximumRectangleFinder.FlatEnvironmentViewer;

import net.minecraft.world.World;

public abstract class FlatWorldViewer implements FlatEnvironmentViewer
{
    public static enum Surface
    {
        Surface_X, Surface_Y, Surface_Z
    }
    
    public World world;
    public Surface surface;
    public int thirdAxisPosition;
    
    public FlatWorldViewer(World world, Surface surface, int thirdAxisPosition)
    {
        this.world = world;
        this.surface = surface;
        this.thirdAxisPosition = thirdAxisPosition;
    }
    @Override
    public boolean isHaveObject(int x, int y)
    {
        if (surface == Surface.Surface_X)
        {
            return isThisBlock(world, thirdAxisPosition, y, x);
        }
        else if (surface == Surface.Surface_Y)
        {
            return isThisBlock(world, x, thirdAxisPosition, y);
        }
        else if (surface == Surface.Surface_Z)
        {
            return isThisBlock(world, x, y, thirdAxisPosition);
        }
        return false;
    }
    public abstract boolean isThisBlock(World world, int x, int y, int z);
}

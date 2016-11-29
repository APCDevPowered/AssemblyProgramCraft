package org.apcdevpowered.apc.common.tileEntity;

import java.util.Arrays;

import org.apcdevpowered.apc.common.AssemblyProgramCraft;
import org.apcdevpowered.apc.common.block.BlockExpansionConsoleScreen;
import org.apcdevpowered.apc.common.block.BlockExternalDeviceConsoleScreen;
import org.apcdevpowered.apc.common.network.AssemblyProgramCraftPacket;
import org.apcdevpowered.apc.common.util.FlatMaximumRectangleFinder;
import org.apcdevpowered.apc.common.util.FlatWorldViewer;
import org.apcdevpowered.apc.common.util.FlatWorldViewer.Surface;
import org.apcdevpowered.vcpu32.vm.AbstractExternalDevice;
import org.apcdevpowered.vcpu32.vm.extdev.ExternalDeviceConsoleScreen;
import org.lwjgl.util.Rectangle;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TileEntityExternalDeviceConsoleScreen extends TileEntityExternalDevice
{
    public int[] charBuffer = new int[15000];
    public int defaultChar;
    public int defaultColor;
    public int cursorBlinkRate;
    public int cursorLocation;
    public boolean cursorEnabled;
    public int hardwareScale;
    public int verticalScale;
    public int horizontalScale;
    private int tickCounter;
    private FlatWorldViewer flatWorldConsoleScreenViewer;
    public ExternalDeviceConsoleScreen externalDeviceConsoleScreen = new ExternalDeviceConsoleScreen(this);
    
    public TileEntityExternalDeviceConsoleScreen()
    {
    }
    public AbstractExternalDevice getExternalDevice()
    {
        return externalDeviceConsoleScreen;
    }
    public EnumFacing[] getConnectorConnectableFaces(IBlockState state)
    {
        return new EnumFacing[]
        {
                ((EnumFacing) state.getValue(BlockExternalDeviceConsoleScreen.FACING)).getOpposite()
        };
    }
    public void update()
    {
        super.update();
        tickCounter++;
    }
    public void writeDescriptionNbt(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setIntArray("charBuffer", charBuffer);
        nbtTagCompound.setInteger("defaultChar", defaultChar);
        nbtTagCompound.setInteger("defaultColor", defaultColor);
        nbtTagCompound.setInteger("cursorBlinkRate", cursorBlinkRate);
        nbtTagCompound.setInteger("cursorLocation", cursorLocation);
        nbtTagCompound.setBoolean("cursorEnabled", cursorEnabled);
        nbtTagCompound.setInteger("hardwareScale", hardwareScale);
        nbtTagCompound.setInteger("verticalScale", verticalScale);
        nbtTagCompound.setInteger("horizontalScale", horizontalScale);
    }
    public void readDescriptionNbt(NBTTagCompound nbtTagCompound)
    {
        charBuffer = nbtTagCompound.getIntArray("charBuffer");
        defaultChar = nbtTagCompound.getInteger("defaultChar");
        defaultColor = nbtTagCompound.getInteger("defaultColor");
        cursorBlinkRate = nbtTagCompound.getInteger("cursorBlinkRate");
        cursorLocation = nbtTagCompound.getInteger("cursorLocation");
        cursorEnabled = nbtTagCompound.getBoolean("cursorEnabled");
        hardwareScale = nbtTagCompound.getInteger("hardwareScale");
        verticalScale = nbtTagCompound.getInteger("verticalScale");
        horizontalScale = nbtTagCompound.getInteger("horizontalScale");
    }
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        charBuffer = nbtTagCompound.getIntArray("charBuffer");
        defaultChar = nbtTagCompound.getInteger("defaultChar");
        defaultColor = nbtTagCompound.getInteger("defaultColor");
        cursorBlinkRate = nbtTagCompound.getInteger("cursorBlinkRate");
        cursorLocation = nbtTagCompound.getInteger("cursorLocation");
        cursorEnabled = nbtTagCompound.getBoolean("cursorEnabled");
        hardwareScale = nbtTagCompound.getInteger("hardwareScale");
        verticalScale = nbtTagCompound.getInteger("verticalScale");
        horizontalScale = nbtTagCompound.getInteger("horizontalScale");
    }
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setIntArray("charBuffer", charBuffer);
        nbtTagCompound.setInteger("defaultChar", defaultChar);
        nbtTagCompound.setInteger("defaultColor", defaultColor);
        nbtTagCompound.setInteger("cursorBlinkRate", cursorBlinkRate);
        nbtTagCompound.setInteger("cursorLocation", cursorLocation);
        nbtTagCompound.setBoolean("cursorEnabled", cursorEnabled);
        nbtTagCompound.setInteger("hardwareScale", hardwareScale);
        nbtTagCompound.setInteger("verticalScale", verticalScale);
        nbtTagCompound.setInteger("horizontalScale", horizontalScale);
    }
    public boolean setMemoryValue(int idx, int value)
    {
        if (idx > 15008 || idx < 0)
        {
            System.out.println("超出范围:idx = " + idx);
            return false;
        }
        if (idx >= 0 && idx <= 14999)
        {
            charBuffer[idx] = value;
        }
        else if (idx == 15000)
        {
            if (value == 0)
            {
                Arrays.fill(charBuffer, 0);
            }
            else if (value == 1)
            {
                int index = 0;
                for (int length = charBuffer.length; index < length; index++)
                {
                    if (index % 2 == 0)
                    {
                        charBuffer[index] = defaultChar;
                    }
                    else
                    {
                        charBuffer[index] = defaultColor;
                    }
                }
            }
            else if (value == 2)
            {
                int index = 0;
                for (int length = charBuffer.length; index < length; index++)
                {
                    if (index % 2 == 0)
                    {
                        charBuffer[index] = defaultChar;
                    }
                    else
                    {
                        charBuffer[index] = 0;
                    }
                }
            }
            else if (value == 3)
            {
                int index = 0;
                for (int length = charBuffer.length; index < length; index++)
                {
                    if (index % 2 == 0)
                    {
                        charBuffer[index] = 0;
                    }
                    else
                    {
                        charBuffer[index] = defaultColor;
                    }
                }
            }
        }
        else if (idx == 15001)
        {
            defaultChar = value;
        }
        else if (idx == 15002)
        {
            defaultColor = value;
        }
        else if (idx == 15003)
        {
            cursorBlinkRate = value;
        }
        else if (idx == 15004)
        {
            cursorLocation = value;
        }
        else if (idx == 15005)
        {
            cursorEnabled = value == 0 ? false : true;
        }
        else if (idx == 15006)
        {
            hardwareScale = value;
        }
        else if (idx == 15007)
        {
            horizontalScale = value;
        }
        else if (idx == 15008)
        {
            verticalScale = value;
        }
        return true;
    }
    public void syncConsoleData(int idx, int value)
    {
        AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
        pak.packetType = AssemblyProgramCraftPacket.ServerPacket.SyncConsoleData.getValue();
        pak.dataInt = new int[6];
        pak.dataInt[0] = getPos().getX();
        pak.dataInt[1] = getPos().getY();
        pak.dataInt[2] = getPos().getZ();
        pak.dataInt[3] = getWorld().provider.getDimensionId();
        pak.dataInt[4] = idx;
        pak.dataInt[5] = value;
        AssemblyProgramCraft.sendToAll(pak);
    }
    public int getTickCounter()
    {
        return tickCounter;
    }
    public FlatWorldViewer getFlatWorldViewer()
    {
        if (flatWorldConsoleScreenViewer == null)
        {
            EnumFacing face = (EnumFacing) getWorld().getBlockState(getPos()).getValue(BlockExternalDeviceConsoleScreen.FACING);
            Surface surface = null;
            int thirdAxisPosition = 0;
            if (face.equals(EnumFacing.SOUTH) || face.equals(EnumFacing.NORTH))
            {
                surface = Surface.Surface_Z;
                thirdAxisPosition = getPos().getZ();
            }
            if (face.equals(EnumFacing.WEST) || face.equals(EnumFacing.EAST))
            {
                surface = Surface.Surface_X;
                thirdAxisPosition = getPos().getX();
            }
            flatWorldConsoleScreenViewer = new FlatWorldViewer(getWorld(), surface, thirdAxisPosition)
            {
                @Override
                public boolean isThisBlock(World world, int x, int y, int z)
                {
                    if (x == getPos().getX() && y == getPos().getY() && z == getPos().getZ())
                    {
                        return true;
                    }
                    TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
                    if (tileEntity instanceof TileEntityExpansionConsoleScreen)
                    {
                        if ((((EnumFacing) tileEntity.getWorld().getBlockState(tileEntity.getPos()).getValue(BlockExpansionConsoleScreen.FACING)).equals(getDirection())))
                        {
                            return true;
                        }
                    }
                    return false;
                }
            };
        }
        return flatWorldConsoleScreenViewer;
    }
    public Rectangle getMaximumRectangle()
    {
        EnumFacing face = (EnumFacing) getWorld().getBlockState(getPos()).getValue(BlockExternalDeviceConsoleScreen.FACING);
        int x = 0;
        int y = 0;
        if (face.equals(EnumFacing.SOUTH) || face.equals(EnumFacing.NORTH))
        {
            x = getPos().getX();
            y = getPos().getY();
        }
        if (face.equals(EnumFacing.WEST) || face.equals(EnumFacing.EAST))
        {
            x = getPos().getZ();
            y = getPos().getY();
        }
        return FlatMaximumRectangleFinder.getMaximumRectangle(getFlatWorldViewer(), x, y);
    }
    public EnumFacing getDirection()
    {
        return (EnumFacing) getWorld().getBlockState(getPos()).getValue(BlockExternalDeviceConsoleScreen.FACING);
    }
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }
}

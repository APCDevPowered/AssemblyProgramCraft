package yuxuanchiadm.apc.apc.tileEntity;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.util.Rectangle;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.common.util.FlatMaximumRectangleFinder;
import yuxuanchiadm.apc.apc.common.util.FlatWorldViewer;
import yuxuanchiadm.apc.apc.common.util.FlatWorldViewer.Surface;
import yuxuanchiadm.apc.apc.network.AssemblyProgramCraftPacket;
import yuxuanchiadm.apc.vcpu32.extdev.ExternalDeviceConsoleScreen;
import yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice;

public class TileEntityExternalDeviceConsoleScreen extends ITileEntityExternalDevice
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
    public ArrayList<TileEntityVCPU32ComputerConnector> getAllConnectedConnector()
    {
        ArrayList<TileEntityVCPU32ComputerConnector> connectorList = new ArrayList<TileEntityVCPU32ComputerConnector>();
        if(worldObj.getBlock(xCoord, yCoord, zCoord + 1) == AssemblyProgramCraft.instance.block_connector)
        {
            int blockDirection = worldObj.getBlockMetadata(xCoord, yCoord, zCoord + 1)  & 3;
            if(blockDirection == 2)
            {
                connectorList.add((TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord, yCoord, zCoord + 1));
            }
        }
        if(worldObj.getBlock(xCoord - 1, yCoord, zCoord) == AssemblyProgramCraft.instance.block_connector)
        {
            int blockDirection = worldObj.getBlockMetadata(xCoord - 1, yCoord, zCoord)  & 3;
            if(blockDirection == 3)
            {
                connectorList.add((TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord - 1, yCoord, zCoord));
            }
        }
        if(worldObj.getBlock(xCoord, yCoord, zCoord - 1) == AssemblyProgramCraft.instance.block_connector)
        {
            int blockDirection = worldObj.getBlockMetadata(xCoord, yCoord, zCoord - 1)  & 3;
            if(blockDirection == 0)
            {
                connectorList.add((TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord, yCoord, zCoord - 1));
            }
        }
        if(worldObj.getBlock(xCoord + 1, yCoord, zCoord) == AssemblyProgramCraft.instance.block_connector)
        {
            int blockDirection = worldObj.getBlockMetadata(xCoord + 1, yCoord, zCoord)  & 3;
            if(blockDirection == 1)
            {
                connectorList.add((TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord + 1, yCoord, zCoord));
            }
        }
        return connectorList;
    }
    public void updateEntity()
    {
        super.updateEntity();
        tickCounter++;
    }
    public void writeDescriptionNbt(NBTTagCompound nbt)
    {
        nbt.setIntArray("charBuffer", charBuffer);
        nbt.setInteger("defaultChar", defaultChar);
        nbt.setInteger("defaultColor", defaultColor);
        nbt.setInteger("cursorBlinkRate", cursorBlinkRate);
        nbt.setInteger("cursorLocation", cursorLocation);
        nbt.setBoolean("cursorEnabled", cursorEnabled);
        nbt.setInteger("hardwareScale", hardwareScale);
        nbt.setInteger("verticalScale", verticalScale);
        nbt.setInteger("horizontalScale", horizontalScale);
    }
    public void readDescriptionNbt(NBTTagCompound nbt)
    {
        charBuffer = nbt.getIntArray("charBuffer");
        defaultChar = nbt.getInteger("defaultChar");
        defaultColor = nbt.getInteger("defaultColor");
        cursorBlinkRate = nbt.getInteger("cursorBlinkRate");
        cursorLocation = nbt.getInteger("cursorLocation");
        cursorEnabled = nbt.getBoolean("cursorEnabled");
        hardwareScale = nbt.getInteger("hardwareScale");
        verticalScale = nbt.getInteger("verticalScale");
        horizontalScale = nbt.getInteger("horizontalScale");
    }
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        charBuffer = par1NBTTagCompound.getIntArray("charBuffer");
        defaultChar = par1NBTTagCompound.getInteger("defaultChar");
        defaultColor = par1NBTTagCompound.getInteger("defaultColor");
        cursorBlinkRate = par1NBTTagCompound.getInteger("cursorBlinkRate");
        cursorLocation = par1NBTTagCompound.getInteger("cursorLocation");
        cursorEnabled = par1NBTTagCompound.getBoolean("cursorEnabled");
        hardwareScale = par1NBTTagCompound.getInteger("hardwareScale");
        verticalScale = par1NBTTagCompound.getInteger("verticalScale");
        horizontalScale = par1NBTTagCompound.getInteger("horizontalScale");
    }
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setIntArray("charBuffer", charBuffer);
        par1NBTTagCompound.setInteger("defaultChar", defaultChar);
        par1NBTTagCompound.setInteger("defaultColor", defaultColor);
        par1NBTTagCompound.setInteger("cursorBlinkRate", cursorBlinkRate);
        par1NBTTagCompound.setInteger("cursorLocation", cursorLocation);
        par1NBTTagCompound.setBoolean("cursorEnabled", cursorEnabled);
        par1NBTTagCompound.setInteger("hardwareScale", hardwareScale);
        par1NBTTagCompound.setInteger("verticalScale", verticalScale);
        par1NBTTagCompound.setInteger("horizontalScale", horizontalScale);
    }
    public boolean setMemoryValue(int idx, int value)
    {
        if(idx > 15008 || idx < 0)
        {
            System.out.println("超出范围:idx = " + idx);
            return false;
        }
        if(idx >= 0 && idx <= 14999)
        {
            charBuffer[idx] = value;
        }
        else if(idx == 15000)
        {
            if(value == 0)
            {
                Arrays.fill(charBuffer, 0);
            }
            else if(value == 1)
            {
                int index = 0;
                for(int length = charBuffer.length; index < length; index++)
                {
                    if(index % 2 == 0)
                    {
                        charBuffer[index] = defaultChar;
                    }
                    else
                    {
                        charBuffer[index] = defaultColor;
                    }
                }
            }
            else if(value == 2)
            {
                int index = 0;
                for(int length = charBuffer.length; index < length; index++)
                {
                    if(index % 2 == 0)
                    {
                        charBuffer[index] = defaultChar;
                    }
                    else
                    {
                        charBuffer[index] = 0;
                    }
                }
            }
            else if(value == 3)
            {
                int index = 0;
                for(int length = charBuffer.length; index < length; index++)
                {
                    if(index % 2 == 0)
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
        else if(idx == 15001)
        {
            defaultChar = value;
        }
        else if(idx == 15002)
        {
            defaultColor = value;
        }
        else if(idx == 15003)
        {
            cursorBlinkRate = value;
        }
        else if(idx == 15004)
        {
            cursorLocation = value;
        }
        else if(idx == 15005)
        {
            cursorEnabled = value == 0 ? false : true;
        }
        else if(idx == 15006)
        {
            hardwareScale = value;
        }
        else if(idx == 15007)
        {
            horizontalScale = value;
        }
        else if(idx == 15008)
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
        pak.dataInt[0] = xCoord;
        pak.dataInt[1] = yCoord;
        pak.dataInt[2] = zCoord;
        pak.dataInt[3] = worldObj.getWorldInfo().getVanillaDimension();
        pak.dataInt[4] = idx;
        pak.dataInt[5] = value;
        AssemblyProgramCraft.sendToAllPlayers(pak);
    }
    public int getTickCounter()
    {
        return tickCounter;
    }
    public FlatWorldViewer getFlatWorldViewer()
    {
        if(flatWorldConsoleScreenViewer == null)
        {
            int direction = getBlockMetadata() & 3;
            Surface surface = null;
            int thirdAxisPosition = 0;
            if(direction == 0 || direction == 2)
            {
                surface = Surface.Surface_Z;
                thirdAxisPosition = zCoord;
            }
            if(direction == 1 || direction == 3)
            {
                surface = Surface.Surface_X;
                thirdAxisPosition = xCoord;
            }
            flatWorldConsoleScreenViewer = new FlatWorldViewer(worldObj, surface, thirdAxisPosition)
            {
                @Override
                public boolean isThisBlock(World world, int x, int y, int z)
                {
                    if(x == xCoord && y == yCoord && z == zCoord)
                    {
                        return true;
                    }
                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    if(tileEntity instanceof TileEntityExpansionConsoleScreen)
                    {
                        if((tileEntity.getBlockMetadata() & 3) == getDirection())
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
        int direction = getBlockMetadata() & 3;
        int x = 0;
        int y = 0;
        if(direction == 0 || direction == 2)
        {
            x = xCoord;
            y = yCoord;
        }
        if(direction == 1 || direction == 3)
        {
            x = zCoord;
            y = yCoord;
        }
        return FlatMaximumRectangleFinder.getMaximumRectangle(getFlatWorldViewer(), x, y);
    }
    public int getDirection()
    {
        return getBlockMetadata() & 3;
    }
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }
}

package yuxuanchiadm.apc.apc.tileEntity;

import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.item.ItemBlockVCPU32ComputerWire;
import net.minecraft.tileentity.TileEntity;

public class TileEntityVCPU32ComputerConnector extends TileEntity
{
    private boolean isInIt = false;
    
    public static int FACE_LEFT = 1;
    public static int FACE_RIGHT = 2;
    public static int FACE_FRONT = 4;
    public static int FACE_BACK = 8;
	
	public void updateEntity()
    {
	    super.updateEntity();
        if(isInIt == false)
        {
            init();
            isInIt = true;
        }
        this.markDirty();
    }
    public void invalidate()
    {
        super.invalidate();
        updataConnector();
    }
    public void init()
    {
        updataConnector();
    }
	public boolean hasPower()
	{
	    return false;
	}
    public TileEntityVCPU32ComputerConnector hasConnector()
	{
	    int direction = getBlockMetadata() & 3;
        if(direction == 0)
        {
            if(worldObj.getBlock(xCoord, yCoord, zCoord - 1) == AssemblyProgramCraft.instance.block_connector)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord, yCoord, zCoord - 1)  & 3;
                if(blockDirection == 2)
                {
                    return (TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else if(direction == 1)
        {
            if(worldObj.getBlock(xCoord + 1, yCoord, zCoord) == AssemblyProgramCraft.instance.block_connector)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord + 1, yCoord, zCoord)  & 3;
                if(blockDirection == 3)
                {
                    return (TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else if(direction == 2)
        {
            if(worldObj.getBlock(xCoord, yCoord, zCoord + 1) == AssemblyProgramCraft.instance.block_connector)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord, yCoord, zCoord + 1)  & 3;
                if(blockDirection == 0)
                {
                    return (TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else if(direction == 3)
        {
            if(worldObj.getBlock(xCoord - 1, yCoord, zCoord) == AssemblyProgramCraft.instance.block_connector)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord - 1, yCoord, zCoord)  & 3;
                if(blockDirection == 1)
                {
                    return (TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
	}
	public TileEntityVCPU32ComputerWire hasWire()
	{
	    int direction = getBlockMetadata() & 3;
        if(direction == 0)
        {
            if(worldObj.getBlock(xCoord, yCoord, zCoord - 1) == AssemblyProgramCraft.instance.block_wire)
            {
                TileEntity tileEntity = worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
                if(tileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    if(((TileEntityVCPU32ComputerWire)tileEntity).haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                    {
                        return (TileEntityVCPU32ComputerWire)tileEntity;
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else if(direction == 1)
        {
            if(worldObj.getBlock(xCoord + 1, yCoord, zCoord) == AssemblyProgramCraft.instance.block_wire)
            {
                TileEntity tileEntity = worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
                if(tileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    if(((TileEntityVCPU32ComputerWire)tileEntity).haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                    {
                        return (TileEntityVCPU32ComputerWire)tileEntity;
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else if(direction == 2)
        {
            if(worldObj.getBlock(xCoord, yCoord, zCoord + 1) == AssemblyProgramCraft.instance.block_wire)
            {
                TileEntity tileEntity = worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
                if(tileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    if(((TileEntityVCPU32ComputerWire)tileEntity).haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                    {
                        return (TileEntityVCPU32ComputerWire)tileEntity;
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else if(direction == 3)
        {
            if(worldObj.getBlock(xCoord - 1, yCoord, zCoord) == AssemblyProgramCraft.instance.block_wire)
            {
                TileEntity tileEntity = worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
                if(tileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    if(((TileEntityVCPU32ComputerWire)tileEntity).haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                    {
                        return (TileEntityVCPU32ComputerWire)tileEntity;
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
	}
	public TileEntityVCPU32Computer hasComputer()
    {
        int direction = getBlockMetadata() & 3;
        if(direction == 0)
        {
            if(worldObj.getBlock(xCoord, yCoord, zCoord + 1) == AssemblyProgramCraft.instance.block_computer)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord, yCoord, zCoord + 1)  & 3;
                if(blockDirection == 2)
                {
                    return (TileEntityVCPU32Computer)worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else if(direction == 1)
        {
            if(worldObj.getBlock(xCoord - 1, yCoord, zCoord) == AssemblyProgramCraft.instance.block_computer)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord - 1, yCoord, zCoord)  & 3;
                if(blockDirection == 3)
                {
                    return (TileEntityVCPU32Computer)worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else if(direction == 2)
        {
            if(worldObj.getBlock(xCoord, yCoord, zCoord - 1) == AssemblyProgramCraft.instance.block_computer)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord, yCoord, zCoord - 1)  & 3;
                if(blockDirection == 0)
                {
                    return (TileEntityVCPU32Computer)worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else if(direction == 3)
        {
            if(worldObj.getBlock(xCoord + 1, yCoord, zCoord) == AssemblyProgramCraft.instance.block_computer)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord + 1, yCoord, zCoord)  & 3;
                if(blockDirection == 1)
                {
                    return (TileEntityVCPU32Computer)worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
	public ITileEntityExternalDevice hasExternalDevices()
	{
        int direction = getBlockMetadata() & 3;
        if(direction == 0)
        {
            TileEntity tileEntity = worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
            if (tileEntity instanceof ITileEntityExternalDevice)
            {
                ITileEntityExternalDevice tileEntityExternalDevices = (ITileEntityExternalDevice)tileEntity;
                return tileEntityExternalDevices.isConnectToConnector(this) ? tileEntityExternalDevices : null;
            }
            else
            {
                return null;
            }
        }
        else if(direction == 1)
        {
            TileEntity tileEntity = worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
            if (tileEntity instanceof ITileEntityExternalDevice)
            {
                ITileEntityExternalDevice tileEntityExternalDevices = (ITileEntityExternalDevice)tileEntity;
                return tileEntityExternalDevices.isConnectToConnector(this) ? tileEntityExternalDevices : null;
            }
            else
            {
                return null;
            }
        }
        else if(direction == 2)
        {
            TileEntity tileEntity = worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
            if (tileEntity instanceof ITileEntityExternalDevice)
            {
                ITileEntityExternalDevice tileEntityExternalDevices = (ITileEntityExternalDevice)tileEntity;
                return tileEntityExternalDevices.isConnectToConnector(this) ? tileEntityExternalDevices : null;
            }
            else
            {
                return null;
            }
        }
        else if(direction == 3)
        {
            TileEntity tileEntity = worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
            if (tileEntity instanceof ITileEntityExternalDevice)
            {
                ITileEntityExternalDevice tileEntityExternalDevices = (ITileEntityExternalDevice)tileEntity;
                return tileEntityExternalDevices.isConnectToConnector(this) ? tileEntityExternalDevices : null;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
	}
    public void updataConnector()
    {
        if(worldObj.isRemote)
        {
            return;
        }
        ItemBlockVCPU32ComputerWire.updataConnector(this);
    }
    public int getFace()
    {
        int direction = getBlockMetadata() & 3;
        if(direction == 0)
        {
            return FACE_LEFT;
        }
        else if(direction == 1)
        {
            return FACE_FRONT;
        }
        else if(direction == 2)
        {
            return FACE_RIGHT;
        }
        else if(direction == 3)
        {
            return FACE_BACK;
        }
        else
        {
            return -1;
        }
    }
}
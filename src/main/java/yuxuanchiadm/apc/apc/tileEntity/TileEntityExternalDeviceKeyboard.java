package yuxuanchiadm.apc.apc.tileEntity;

import java.util.ArrayList;

import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.vcpu32.extdev.ExternalDeviceKeyboard;
import yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice;

public class TileEntityExternalDeviceKeyboard extends ITileEntityExternalDevice
{
    public ExternalDeviceKeyboard externalDeviceKeyboard = new ExternalDeviceKeyboard();
    public ArrayList<TileEntityVCPU32ComputerConnector> getAllConnectedConnector()
    {
        ArrayList<TileEntityVCPU32ComputerConnector> connectorList = new ArrayList<TileEntityVCPU32ComputerConnector>();
        int face = getFace();
        if(face == 0)
        {
            if(worldObj.getBlock(xCoord, yCoord, zCoord + 1) == AssemblyProgramCraft.instance.block_connector)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord, yCoord, zCoord + 1)  & 3;
                if(blockDirection == 2)
                {
                    connectorList.add((TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord, yCoord, zCoord + 1));
                }
            }
        }
        else if(face == 1)
        {
            if(worldObj.getBlock(xCoord - 1, yCoord, zCoord) == AssemblyProgramCraft.instance.block_connector)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord - 1, yCoord, zCoord)  & 3;
                if(blockDirection == 3)
                {
                    connectorList.add((TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord - 1, yCoord, zCoord));
                }
            }
        }
        else if(face == 2)
        {
            if(worldObj.getBlock(xCoord, yCoord, zCoord - 1) == AssemblyProgramCraft.instance.block_connector)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord, yCoord, zCoord - 1)  & 3;
                if(blockDirection == 0)
                {
                    connectorList.add((TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord, yCoord, zCoord - 1));
                }
            }
        }
        else if(face == 3)
        {
            if(worldObj.getBlock(xCoord + 1, yCoord, zCoord) == AssemblyProgramCraft.instance.block_connector)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord + 1, yCoord, zCoord)  & 3;
                if(blockDirection == 1)
                {
                    connectorList.add((TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord + 1, yCoord, zCoord));
                }
            }
        }
        return connectorList;
    }
    public int getFace()
    {
        int direction = getBlockMetadata() & 3;
        if(direction == 0 || direction == 1 || direction == 2 || direction == 3)
        {
            return direction;
        }
        else
        {
            return -1;
        }
    }
    public AbstractExternalDevice getExternalDevice()
    {
        return externalDeviceKeyboard;
    }
}

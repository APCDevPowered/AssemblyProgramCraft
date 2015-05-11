package yuxuanchiadm.apc.apc.tileEntity;

import java.util.ArrayList;

import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.vcpu32.extdev.ExternalDevice300BytesStorage;
import yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice;

public class TileEntityExternalDevice300BytesStorage extends ITileEntityExternalDevice
{
    public ExternalDevice300BytesStorage externalDevice300BytesStorage = new ExternalDevice300BytesStorage();
    public AbstractExternalDevice getExternalDevice()
    {
        return externalDevice300BytesStorage;
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
}

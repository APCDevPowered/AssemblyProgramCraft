package yuxuanchiadm.apc.apc.common.tileEntity;

import yuxuanchiadm.apc.apc.common.block.BlockVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.common.init.AssemblyProgramCraftBlocks;
import yuxuanchiadm.apc.apc.common.item.ItemBlockVCPU32ComputerWire;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class TileEntityVCPU32ComputerConnector extends TileEntity implements IUpdatePlayerListBox
{
    private boolean isInIt = false;
    
    @Override
    public void update()
    {
        if (isInIt == false)
        {
            init();
            isInIt = true;
        }
        this.markDirty();
    }
    @Override
    public void invalidate()
    {
        super.invalidate();
    }
    public void init()
    {
        updataConnector(getWorld().getBlockState(getPos()));
    }
    public boolean hasPower()
    {
        return false;
    }
    public TileEntityVCPU32ComputerConnector hasConnector()
    {
        return hasConnector(getWorld().getBlockState(getPos()));
    }
    public TileEntityVCPU32ComputerConnector hasConnector(IBlockState state)
    {
        EnumFacing facing = (EnumFacing) state.getValue(BlockVCPU32ComputerConnector.FACING);
        BlockPos connectorPos = getPos().offset(facing);
        IBlockState connectorState = getWorld().getBlockState(connectorPos);
        if (connectorState.getBlock() == AssemblyProgramCraftBlocks.block_vcpu_32_computer_connector)
        {
            if (((EnumFacing) connectorState.getValue(BlockVCPU32ComputerConnector.FACING)) == facing.getOpposite())
            {
                return (TileEntityVCPU32ComputerConnector) getWorld().getTileEntity(connectorPos);
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
        return hasWire(getWorld().getBlockState(getPos()));
    }
    public TileEntityVCPU32ComputerWire hasWire(IBlockState state)
    {
        EnumFacing facing = (EnumFacing) state.getValue(BlockVCPU32ComputerConnector.FACING);
        BlockPos wirePos = getPos().offset(facing);
        if (getWorld().getBlockState(wirePos).getBlock() == AssemblyProgramCraftBlocks.block_vcpu_32_computer_wire)
        {
            TileEntityVCPU32ComputerWire tileEntity = (TileEntityVCPU32ComputerWire) getWorld().getTileEntity(wirePos);
            if (tileEntity.hasSide(EnumFacing.DOWN))
            {
                return tileEntity;
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
        return hasComputer(getWorld().getBlockState(getPos()));
    }
    public TileEntityVCPU32Computer hasComputer(IBlockState state)
    {
        EnumFacing facing = (EnumFacing) state.getValue(BlockVCPU32ComputerConnector.FACING);
        BlockPos computerPos = getPos().offset(facing.getOpposite());
        IBlockState computerState = getWorld().getBlockState(computerPos);
        if (computerState.getBlock() == AssemblyProgramCraftBlocks.block_vcpu_32_computer)
        {
            if (((EnumFacing) computerState.getValue(BlockVCPU32ComputerConnector.FACING)) == facing.getOpposite())
            {
                return (TileEntityVCPU32Computer) getWorld().getTileEntity(computerPos);
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
    public TileEntityExternalDevice hasExternalDevices()
    {
        return hasExternalDevices(getWorld().getBlockState(getPos()));
    }
    public TileEntityExternalDevice hasExternalDevices(IBlockState state)
    {
        EnumFacing facing = (EnumFacing) state.getValue(BlockVCPU32ComputerConnector.FACING);
        TileEntity tileEntity = getWorld().getTileEntity(getPos().offset(facing.getOpposite()));
        if (tileEntity instanceof TileEntityExternalDevice)
        {
            TileEntityExternalDevice tileEntityExternalDevice = (TileEntityExternalDevice) tileEntity;
            if (tileEntityExternalDevice.isConnectToConnector(getWorld(), getPos(), getWorld().getBlockState(pos)))
            {
                return tileEntityExternalDevice;
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
    public void updataConnector(IBlockState state)
    {
        if (getWorld().isRemote)
        {
            return;
        }
        ItemBlockVCPU32ComputerWire.updataConnector(this, state);
    }
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        return new S35PacketUpdateTileEntity(this.pos, Block.getIdFromBlock(AssemblyProgramCraftBlocks.block_vcpu_32_computer_connector), nbttagcompound);
    }
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        
    }
}
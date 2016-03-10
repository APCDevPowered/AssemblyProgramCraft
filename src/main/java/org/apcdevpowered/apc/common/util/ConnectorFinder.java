package org.apcdevpowered.apc.common.util;

import java.util.ArrayList;

import org.apcdevpowered.apc.common.block.BlockVCPU32ComputerConnector;
import org.apcdevpowered.apc.common.init.AssemblyProgramCraftBlocks;
import org.apcdevpowered.apc.common.tileEntity.TileEntityVCPU32ComputerConnector;
import org.apcdevpowered.apc.common.tileEntity.TileEntityVCPU32ComputerWire;
import org.apcdevpowered.apc.common.world.RedirectBlockAccess;

import com.google.common.base.Objects;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ConnectorFinder
{
    public static TileEntityVCPU32ComputerConnector[] findConnector(TileEntityVCPU32ComputerConnector startSearchConnector)
    {
        return findConnector(startSearchConnector, startSearchConnector.getWorld().getBlockState(startSearchConnector.getPos()));
    }
    public static TileEntityVCPU32ComputerConnector[] findConnector(TileEntityVCPU32ComputerConnector startSearchConnector, IBlockState state)
    {
        boolean isInvalidateUpdate = startSearchConnector.getWorld().getTileEntity(startSearchConnector.getPos()) != startSearchConnector;
        if (startSearchConnector.hasConnector(state) != null)
        {
            if (isInvalidateUpdate)
            {
                return new TileEntityVCPU32ComputerConnector[]
                {
                        startSearchConnector.hasConnector(state)
                };
            }
            else
            {
                return new TileEntityVCPU32ComputerConnector[]
                {
                        startSearchConnector, startSearchConnector.hasConnector(state)
                };
            }
        }
        else if (startSearchConnector.hasWire(state) == null)
        {
            if (isInvalidateUpdate)
            {
                return new TileEntityVCPU32ComputerConnector[]
                {};
            }
            else
            {
                return new TileEntityVCPU32ComputerConnector[]
                {
                        startSearchConnector
                };
            }
        }
        else
        {
            return findConnector(new WirePartInfo(EnumFacing.DOWN, startSearchConnector.hasWire(state)));
        }
    }
    public static TileEntityVCPU32ComputerConnector[] findConnector(WirePartInfo startSearchWirePart)
    {
        if (startSearchWirePart != null && startSearchWirePart.vaild())
        {
            ArrayList<TileEntityVCPU32ComputerConnector> connectorList = new ArrayList<TileEntityVCPU32ComputerConnector>();
            ArrayList<WirePartInfo> missingWireList = new ArrayList<WirePartInfo>();
            ArrayList<WirePartInfo> searchedWireList = new ArrayList<WirePartInfo>();
            searchedWireList.add(startSearchWirePart);
            WirePartInfo currentWirePart = startSearchWirePart;
            World world = currentWirePart.tileEntity.getWorld();
            while ((currentWirePart != null) || (missingWireList.size() != 0))
            {
                WirePartInfo nextWirePart = null;
                if (currentWirePart != null)
                {
                    RedirectBlockAccess redirectBlockAccess = new RedirectBlockAccess(world, currentWirePart.tileEntity.getPos(), currentWirePart.side);
                    for (EnumFacing linkedWireFace : EnumFacing.HORIZONTALS)
                    {
                        IBlockState sideBlockState = redirectBlockAccess.getBlockState(new BlockPos(linkedWireFace.getDirectionVec()));
                        IBlockState downBlockState = redirectBlockAccess.getBlockState(new BlockPos(linkedWireFace.getDirectionVec()).down());
                        if (sideBlockState.getBlock() == AssemblyProgramCraftBlocks.block_vcpu_32_computer_connector)
                        {
                            if (((EnumFacing) sideBlockState.getValue(BlockVCPU32ComputerConnector.FACING)) == redirectBlockAccess.realFace(linkedWireFace.getOpposite()))
                            {
                                connectorList.add((TileEntityVCPU32ComputerConnector) redirectBlockAccess.getTileEntity(new BlockPos(linkedWireFace.getDirectionVec())));
                            }
                        }
                        else if (sideBlockState.getBlock() == AssemblyProgramCraftBlocks.block_vcpu_32_computer_wire)
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(redirectBlockAccess.realFace(EnumFacing.DOWN), WirePartInfo.castToWire(redirectBlockAccess.getTileEntity(new BlockPos(linkedWireFace.getDirectionVec()))));
                            if (wirePartInfo.exists())
                            {
                                if (!searchedWireList.contains(wirePartInfo))
                                {
                                    if (nextWirePart == null)
                                    {
                                        nextWirePart = wirePartInfo;
                                        searchedWireList.add(wirePartInfo);
                                    }
                                    else
                                    {
                                        if (!missingWireList.contains(wirePartInfo))
                                        {
                                            missingWireList.add(wirePartInfo);
                                        }
                                    }
                                }
                            }
                        }
                        if (!sideBlockState.getBlock().isFullBlock() || !sideBlockState.getBlock().isFullCube())
                        {
                            if (downBlockState.getBlock() == AssemblyProgramCraftBlocks.block_vcpu_32_computer_wire)
                            {
                                WirePartInfo wirePartInfo = new WirePartInfo(redirectBlockAccess.realFace(linkedWireFace.getOpposite()), WirePartInfo.castToWire(redirectBlockAccess.getTileEntity(new BlockPos(linkedWireFace.getDirectionVec()).down())));
                                if (wirePartInfo.exists())
                                {
                                    if (!searchedWireList.contains(wirePartInfo))
                                    {
                                        if (nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if (!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        WirePartInfo wirePartInfo = new WirePartInfo(redirectBlockAccess.realFace(linkedWireFace), WirePartInfo.castToWire(redirectBlockAccess.getTileEntity(BlockPos.ORIGIN)));
                        if (wirePartInfo.exists())
                        {
                            if (!searchedWireList.contains(wirePartInfo))
                            {
                                if (nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if (!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                }
                else if (missingWireList.size() != 0)
                {
                    nextWirePart = missingWireList.remove(missingWireList.size() - 1);
                }
                currentWirePart = nextWirePart;
            }
            return connectorList.toArray(new TileEntityVCPU32ComputerConnector[connectorList.size()]);
        }
        else
        {
            return null;
        }
    }
    
    public static class WirePartInfo
    {
        public final EnumFacing side;
        public final TileEntityVCPU32ComputerWire tileEntity;
        
        public WirePartInfo(EnumFacing side, TileEntityVCPU32ComputerWire tileEntity)
        {
            this.side = side;
            this.tileEntity = tileEntity;
        }
        public WirePartInfo[] getLinkedWirePart()
        {
            if (vaild())
            {
                ArrayList<WirePartInfo> wirePartInfoList = new ArrayList<WirePartInfo>();
                RedirectBlockAccess redirectBlockAccess = new RedirectBlockAccess(tileEntity.getWorld(), tileEntity.getPos(), side);
                for (EnumFacing linkedWireFace : EnumFacing.HORIZONTALS)
                {
                    IBlockState sideBlockState = redirectBlockAccess.getBlockState(new BlockPos(linkedWireFace.getDirectionVec()));
                    if (!sideBlockState.getBlock().isFullBlock() || !sideBlockState.getBlock().isFullCube())
                    {
                        WirePartInfo wirePartInfo = new WirePartInfo(redirectBlockAccess.realFace(linkedWireFace.getOpposite()), castToWire(redirectBlockAccess.getTileEntity(new BlockPos(linkedWireFace.getDirectionVec()).down())));
                        if (wirePartInfo.exists())
                        {
                            wirePartInfoList.add(wirePartInfo);
                        }
                    }
                    {
                        WirePartInfo wirePartInfo = new WirePartInfo(redirectBlockAccess.realFace(EnumFacing.DOWN), castToWire(redirectBlockAccess.getTileEntity(new BlockPos(linkedWireFace.getDirectionVec()))));
                        if (wirePartInfo.exists())
                        {
                            wirePartInfoList.add(wirePartInfo);
                        }
                    }
                    {
                        WirePartInfo wirePartInfo = new WirePartInfo(redirectBlockAccess.realFace(linkedWireFace), castToWire(redirectBlockAccess.getTileEntity(BlockPos.ORIGIN)));
                        if (wirePartInfo.exists())
                        {
                            wirePartInfoList.add(wirePartInfo);
                        }
                    }
                }
                return wirePartInfoList.toArray(new WirePartInfo[wirePartInfoList.size()]);
            }
            else
            {
                return null;
            }
        }
        public boolean vaild()
        {
            return tileEntity != null;
        }
        public boolean exists()
        {
            if (tileEntity != null && !tileEntity.isInvalid())
            {
                return tileEntity.hasSide(side);
            }
            return false;
        }
        public String toString()
        {
            if (vaild())
            {
                return Objects.toStringHelper(this).add("pos", this.tileEntity.getPos()).add("side", this.side).toString();
            }
            return "WirePartInfo{INVAILD}";
        }
        public boolean equals(Object obj)
        {
            if (vaild())
            {
                if (obj instanceof WirePartInfo)
                {
                    WirePartInfo wirePartInfo = (WirePartInfo) obj;
                    if ((wirePartInfo.tileEntity == tileEntity) && (wirePartInfo.side == side))
                    {
                        return true;
                    }
                }
            }
            return false;
        }
        public static TileEntityVCPU32ComputerWire castToWire(TileEntity tileEntity)
        {
            if (tileEntity == null)
            {
                return null;
            }
            if (tileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                return (TileEntityVCPU32ComputerWire) tileEntity;
            }
            return null;
        }
    }
}

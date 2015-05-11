package yuxuanchiadm.apc.apc.common.util;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32ComputerWire;

public class ConnectorFinder
{
    public static TileEntityVCPU32ComputerConnector[] findConnector(TileEntityVCPU32ComputerConnector startSearchConnector)
    {
        boolean isInvalidateUpdate = startSearchConnector.getWorldObj().getTileEntity(startSearchConnector.xCoord, startSearchConnector.yCoord, startSearchConnector.zCoord) != startSearchConnector;
        if(startSearchConnector.hasConnector() != null)
        {
            if(isInvalidateUpdate)
            {
                return new TileEntityVCPU32ComputerConnector[]
                {
                    startSearchConnector.hasConnector()
                };
            }
            else
            {
                return new TileEntityVCPU32ComputerConnector[]
                {
                    startSearchConnector, startSearchConnector.hasConnector()
                };
            }
        }
        else if(startSearchConnector.hasWire() == null)
        {
            if(isInvalidateUpdate)
            {
                return new TileEntityVCPU32ComputerConnector[]{};
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
            return findConnector(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, startSearchConnector.hasWire()));
        }
    }
    @SuppressWarnings("unused")
    public static TileEntityVCPU32ComputerConnector[] findConnector(WirePartInfo startSearchWirePart)
    {
        ArrayList<TileEntityVCPU32ComputerConnector> connectorList = new ArrayList<TileEntityVCPU32ComputerConnector>();
        ArrayList<WirePartInfo> missingWireList = new ArrayList<WirePartInfo>();
        ArrayList<WirePartInfo> searchedWireList = new ArrayList<WirePartInfo>();
        WirePartInfo currentWirePart = startSearchWirePart;
        World world = WorldHelper.getWorldFromDimension(currentWirePart.tileEntity.getWorldObj().getWorldInfo().getVanillaDimension());
        while((currentWirePart != null) || (missingWireList.size() != 0))
        {
            WirePartInfo nextWirePart = null;
            if(currentWirePart != null)
            {
                int x = 0;
                int y = 0;
                int z = 0;
                //[start]
                if(currentWirePart.side == TileEntityVCPU32ComputerWire.SIDE_TOP)
                {
                    x = currentWirePart.tileEntity.xCoord + 1;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord + 1;
                            y = currentWirePart.tileEntity.yCoord + 1;
                            z = currentWirePart.tileEntity.zCoord;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord - 1;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord - 1;
                            y = currentWirePart.tileEntity.yCoord + 1;
                            z = currentWirePart.tileEntity.zCoord;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord + 1;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord;
                            y = currentWirePart.tileEntity.yCoord + 1;
                            z = currentWirePart.tileEntity.zCoord + 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord - 1;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord;
                            y = currentWirePart.tileEntity.yCoord + 1;
                            z = currentWirePart.tileEntity.zCoord - 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                }
                //[end]
                //[start]
                if(currentWirePart.side == TileEntityVCPU32ComputerWire.SIDE_BOTTOM)
                {
                    x = currentWirePart.tileEntity.xCoord + 1;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_connector)
                    {
                        TileEntityVCPU32ComputerConnector tileEntityVCPU32ComputerConnector = (TileEntityVCPU32ComputerConnector)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerConnector.getFace() == TileEntityVCPU32ComputerConnector.FACE_BACK)
                        {
                            if(!connectorList.contains(tileEntityVCPU32ComputerConnector))
                            {
                                connectorList.add(tileEntityVCPU32ComputerConnector);
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord + 1;
                            y = currentWirePart.tileEntity.yCoord - 1;
                            z = currentWirePart.tileEntity.zCoord;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord - 1;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_connector)
                    {
                        TileEntityVCPU32ComputerConnector tileEntityVCPU32ComputerConnector = (TileEntityVCPU32ComputerConnector)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerConnector.getFace() == TileEntityVCPU32ComputerConnector.FACE_FRONT)
                        {
                            if(!connectorList.contains(tileEntityVCPU32ComputerConnector))
                            {
                                connectorList.add(tileEntityVCPU32ComputerConnector);
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord - 1;
                            y = currentWirePart.tileEntity.yCoord - 1;
                            z = currentWirePart.tileEntity.zCoord;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord + 1;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_connector)
                    {
                        TileEntityVCPU32ComputerConnector tileEntityVCPU32ComputerConnector = (TileEntityVCPU32ComputerConnector)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerConnector.getFace() == TileEntityVCPU32ComputerConnector.FACE_LEFT)
                        {
                            if(!connectorList.contains(tileEntityVCPU32ComputerConnector))
                            {
                                connectorList.add(tileEntityVCPU32ComputerConnector);
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord;
                            y = currentWirePart.tileEntity.yCoord - 1;
                            z = currentWirePart.tileEntity.zCoord + 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord - 1;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_connector)
                    {
                        TileEntityVCPU32ComputerConnector tileEntityVCPU32ComputerConnector = (TileEntityVCPU32ComputerConnector)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerConnector.getFace() == TileEntityVCPU32ComputerConnector.FACE_RIGHT)
                        {
                            if(!connectorList.contains(tileEntityVCPU32ComputerConnector))
                            {
                                connectorList.add(tileEntityVCPU32ComputerConnector);
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord;
                            y = currentWirePart.tileEntity.yCoord - 1;
                            z = currentWirePart.tileEntity.zCoord - 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                }
                //[end]
                //[start]
                if(currentWirePart.side == TileEntityVCPU32ComputerWire.SIDE_FRONT)
                {
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord + 1;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord - 1;
                            y = currentWirePart.tileEntity.yCoord + 1;
                            z = currentWirePart.tileEntity.zCoord;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord - 1;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord - 1;
                            y = currentWirePart.tileEntity.yCoord - 1;
                            z = currentWirePart.tileEntity.zCoord;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord - 1;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord - 1;
                            y = currentWirePart.tileEntity.yCoord;
                            z = currentWirePart.tileEntity.zCoord - 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord + 1;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord - 1;
                            y = currentWirePart.tileEntity.yCoord;
                            z = currentWirePart.tileEntity.zCoord + 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                }
                //[end]
                //[start]
                if(currentWirePart.side == TileEntityVCPU32ComputerWire.SIDE_BACK)
                {
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord + 1;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord + 1;
                            y = currentWirePart.tileEntity.yCoord + 1;
                            z = currentWirePart.tileEntity.zCoord;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord - 1;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord + 1;
                            y = currentWirePart.tileEntity.yCoord - 1;
                            z = currentWirePart.tileEntity.zCoord;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord - 1;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord + 1;
                            y = currentWirePart.tileEntity.yCoord;
                            z = currentWirePart.tileEntity.zCoord - 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord + 1;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord + 1;
                            y = currentWirePart.tileEntity.yCoord;
                            z = currentWirePart.tileEntity.zCoord - 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                }
                //[end]
                //[start]
                if(currentWirePart.side == TileEntityVCPU32ComputerWire.SIDE_LEFT)
                {
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord + 1;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord;
                            y = currentWirePart.tileEntity.yCoord + 1;
                            z = currentWirePart.tileEntity.zCoord + 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord - 1;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord;
                            y = currentWirePart.tileEntity.yCoord - 1;
                            z = currentWirePart.tileEntity.zCoord + 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord - 1;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord - 1;
                            y = currentWirePart.tileEntity.yCoord;
                            z = currentWirePart.tileEntity.zCoord + 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord + 1;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord + 1;
                            y = currentWirePart.tileEntity.yCoord;
                            z = currentWirePart.tileEntity.zCoord + 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                }
                //[end]
                //[start]
                if(currentWirePart.side == TileEntityVCPU32ComputerWire.SIDE_RIGHT)
                {
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord + 1;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord;
                            y = currentWirePart.tileEntity.yCoord + 1;
                            z = currentWirePart.tileEntity.zCoord - 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord;
                    y = currentWirePart.tileEntity.yCoord - 1;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord;
                            y = currentWirePart.tileEntity.yCoord - 1;
                            z = currentWirePart.tileEntity.zCoord - 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord + 1;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord + 1;
                            y = currentWirePart.tileEntity.yCoord;
                            z = currentWirePart.tileEntity.zCoord - 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    x = currentWirePart.tileEntity.xCoord - 1;
                    y = currentWirePart.tileEntity.yCoord;
                    z = currentWirePart.tileEntity.zCoord;
                    if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                    {
                        TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                        if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, tileEntityVCPU32ComputerWire);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(world.isAirBlock(x, y, z))
                        {
                            x = currentWirePart.tileEntity.xCoord - 1;
                            y = currentWirePart.tileEntity.yCoord;
                            z = currentWirePart.tileEntity.zCoord - 1;
                            if(world.getBlock(x, y, z) == AssemblyProgramCraft.instance.block_wire)
                            {
                                TileEntityVCPU32ComputerWire tileEntityVCPU32ComputerWire = (TileEntityVCPU32ComputerWire)world.getTileEntity(x, y, z);
                                if(tileEntityVCPU32ComputerWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                                {
                                    WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, tileEntityVCPU32ComputerWire);
                                    if(!searchedWireList.contains(wirePartInfo))
                                    {
                                        if(nextWirePart == null)
                                        {
                                            nextWirePart = wirePartInfo;
                                            searchedWireList.add(wirePartInfo);
                                        }
                                        else
                                        {
                                            if(!missingWireList.contains(wirePartInfo))
                                            {
                                                missingWireList.add(wirePartInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(currentWirePart.tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            WirePartInfo wirePartInfo = new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, currentWirePart.tileEntity);
                            if(!searchedWireList.contains(wirePartInfo))
                            {
                                if(nextWirePart == null)
                                {
                                    nextWirePart = wirePartInfo;
                                    searchedWireList.add(wirePartInfo);
                                }
                                else
                                {
                                    if(!missingWireList.contains(wirePartInfo))
                                    {
                                        missingWireList.add(wirePartInfo);
                                    }
                                }
                            }
                        }
                    }
                }
                //[end]
            }
            else if(missingWireList.size() != 0)
            {
                nextWirePart = missingWireList.remove(missingWireList.size() - 1);
            }
            currentWirePart = nextWirePart;
        }
        TileEntityVCPU32ComputerConnector[] connectors = new TileEntityVCPU32ComputerConnector[connectorList.size()];
        return connectorList.toArray(connectors);
    }
    public static class WirePartInfo
    {
        public int side;
        public TileEntityVCPU32ComputerWire tileEntity;
        
        public WirePartInfo(int side, TileEntityVCPU32ComputerWire tileEntity)
        {
            this.side = side;
            this.tileEntity = tileEntity;
        }
        public WirePartInfo[] getLinkedWirePart()
        {
            ArrayList<WirePartInfo> wirePartInfoList = new ArrayList<WirePartInfo>();
            //[start]
            if(side == TileEntityVCPU32ComputerWire.SIDE_TOP)
            {
                TileEntity neighborsTileEntity = null;
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord + 1, tileEntity.yCoord, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord + 1, tileEntity.yCoord, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord + 1, tileEntity.yCoord + 1, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord - 1, tileEntity.yCoord, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord - 1, tileEntity.yCoord, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord - 1, tileEntity.yCoord + 1, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord + 1);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord + 1))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord + 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord - 1);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord - 1))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord - 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, neighborsTileEntityWire));
                            }
                        }
                    }
                }
            }
            //[end]
            //[start]
            if(side == TileEntityVCPU32ComputerWire.SIDE_BOTTOM)
            {
                TileEntity neighborsTileEntity = null;
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord + 1, tileEntity.yCoord, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord + 1, tileEntity.yCoord, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord + 1, tileEntity.yCoord - 1, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord - 1, tileEntity.yCoord, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord - 1, tileEntity.yCoord, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord - 1, tileEntity.yCoord - 1, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord + 1);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord + 1))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord + 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord - 1);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord - 1))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord - 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, neighborsTileEntityWire));
                            }
                        }
                    }
                }
            }
            //[end]
            //[start]
            if(side == TileEntityVCPU32ComputerWire.SIDE_FRONT)
            {
                TileEntity neighborsTileEntity = null;
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord - 1, tileEntity.yCoord + 1, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord - 1, tileEntity.yCoord - 1, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord - 1);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord - 1))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord - 1, tileEntity.yCoord, tileEntity.zCoord - 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord + 1);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord + 1))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord - 1, tileEntity.yCoord, tileEntity.zCoord + 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, neighborsTileEntityWire));
                            }
                        }
                    }
                }
            }
            //[end]
            //[start]
            if(side == TileEntityVCPU32ComputerWire.SIDE_BACK)
            {
                TileEntity neighborsTileEntity = null;
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord + 1, tileEntity.yCoord + 1, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord + 1, tileEntity.yCoord - 1, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord + 1);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord + 1))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord + 1, tileEntity.yCoord, tileEntity.zCoord + 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord - 1);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord - 1))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord + 1, tileEntity.yCoord, tileEntity.zCoord - 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, neighborsTileEntityWire));
                            }
                        }
                    }
                }
            }
            //[end]
            //[start]
            if(side == TileEntityVCPU32ComputerWire.SIDE_LEFT)
            {
                TileEntity neighborsTileEntity = null;
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord + 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord + 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord - 1, tileEntity.yCoord, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord - 1, tileEntity.yCoord, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord - 1, tileEntity.yCoord, tileEntity.zCoord + 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord + 1, tileEntity.yCoord, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_LEFT, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord + 1, tileEntity.yCoord, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord + 1, tileEntity.yCoord, tileEntity.zCoord + 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, neighborsTileEntityWire));
                            }
                        }
                    }
                }
            }
            //[end]
            //[start]
            if(side == TileEntityVCPU32ComputerWire.SIDE_RIGHT)
            {
                TileEntity neighborsTileEntity = null;
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord - 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord - 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_TOP, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BOTTOM, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord + 1, tileEntity.yCoord, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord + 1, tileEntity.yCoord, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord + 1, tileEntity.yCoord, tileEntity.zCoord - 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, neighborsTileEntityWire));
                            }
                        }
                    }
                }
                neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord - 1, tileEntity.yCoord, tileEntity.zCoord);
                if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                {
                    TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                    if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                    {
                        wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_RIGHT, neighborsTileEntityWire));
                    }
                }
                else
                {
                    if(tileEntity.getWorldObj().isAirBlock(tileEntity.xCoord - 1, tileEntity.yCoord, tileEntity.zCoord))
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord - 1, tileEntity.yCoord, tileEntity.zCoord - 1);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_BACK, neighborsTileEntityWire));
                            }
                        }
                    }
                    else
                    {
                        neighborsTileEntity = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                        if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                        {
                            TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                            if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                            {
                                wirePartInfoList.add(new WirePartInfo(TileEntityVCPU32ComputerWire.SIDE_FRONT, neighborsTileEntityWire));
                            }
                        }
                    }
                }
            }
            //[end]
            return wirePartInfoList.toArray(new WirePartInfo[wirePartInfoList.size()]);
        }

        public boolean equals(Object obj)
        {
            if(obj instanceof WirePartInfo)
            {
                WirePartInfo wirePartInfo = (WirePartInfo)obj;
                if((wirePartInfo.tileEntity == tileEntity) && (wirePartInfo.side == side))
                {
                    return true;
                }
            }
            return false;
        }
    }
}

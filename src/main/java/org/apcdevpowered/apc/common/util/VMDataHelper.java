package org.apcdevpowered.apc.common.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import net.minecraft.world.World;

import org.apcdevpowered.util.io.DirectoryLock;
import org.apcdevpowered.vcpu32.vm.VirtualMachine;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerMap;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementNotFoundException;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementTypeMismatchException;
import org.apcdevpowered.vcpu32.vm.storage.exception.UnsupportedVersionException;
import org.apcdevpowered.vcpu32.vm.storage.persistence.NodeDataPersistence;

public class VMDataHelper
{
    /** Virtual machine data cache. */
    private static Map<UUID, NodeContainerMap> vmDataCacheMap = new WeakHashMap<UUID, NodeContainerMap>();
    /** Directory lock cache. */
    private static Map<File, DirectoryLock> directoryLockCacheMap = new WeakHashMap<File, DirectoryLock>();
    
    /**
     * Write data from VirtualMachine to node.
     * 
     * @param world
     *            World write to.
     * 
     * @param vm
     *            The VirtualMachine read from.
     * 
     * @return UUID of VirtualMachine.
     */
    public static UUID writeToData(World world, VirtualMachine vm)
    {
        UUID uuid = vm.getUUID();
        File worldDirectory = world.getSaveHandler().getWorldDirectory();
        File vmdataDirectory;
        try
        {
            vmdataDirectory = new File(worldDirectory, "vmdata").getCanonicalFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return uuid;
        }
        if (!vmdataDirectory.exists())
        {
            if (!vmdataDirectory.mkdirs())
            {
                return uuid;
            }
        }
        DirectoryLock directoryLock;
        synchronized (directoryLockCacheMap)
        {
            directoryLock = directoryLockCacheMap.get(vmdataDirectory);
            if (directoryLock == null)
            {
                try
                {
                    directoryLock = new DirectoryLock(vmdataDirectory);
                    directoryLockCacheMap.put(vmdataDirectory, directoryLock);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    return uuid;
                }
            }
        }
        while (true)
        {
            try
            {
                directoryLock.lockDirectory();
                break;
            }
            catch (InterruptedException e)
            {
            }
        }
        try
        {
            File vmdataFile = new File(vmdataDirectory, uuid.toString());
            NodeContainerMap vmNodeContainerMap = new NodeContainerMap();
            vm.writeDataToNode(vmNodeContainerMap);
            synchronized (vmDataCacheMap)
            {
                vmDataCacheMap.put(uuid, vmNodeContainerMap);
            }
            try
            {
                NodeDataPersistence.saveNode(vmdataFile, vmNodeContainerMap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return uuid;
            }
            return uuid;
        }
        finally
        {
            directoryLock.releaseDirectory();
        }
    }
    /**
     * Read data from node to VirtualMachine.
     * 
     * @param world
     *            World read form.
     * 
     * @param uuid
     *            UUID of VirtualMachine.
     * 
     * @param vm
     *            The VirtualMachine write to.
     */
    public static void readFormNode(World world, UUID uuid, VirtualMachine vm)
    {
        synchronized (vmDataCacheMap)
        {
            NodeContainerMap cachedVMNodeContainerMap = vmDataCacheMap.get(uuid);
            if (cachedVMNodeContainerMap != null)
            {
                try
                {
                    vm.readDataFromNode(cachedVMNodeContainerMap);
                    return;
                }
                catch (ElementNotFoundException e)
                {
                    e.printStackTrace();
                    return;
                }
                catch (ElementTypeMismatchException e)
                {
                    e.printStackTrace();
                    return;
                }
            }
        }
        File worldDirectory = world.getSaveHandler().getWorldDirectory();
        File vmdataDirectory;
        try
        {
            vmdataDirectory = new File(worldDirectory, "vmdata").getCanonicalFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        if (!vmdataDirectory.exists())
        {
            if (!vmdataDirectory.mkdirs())
            {
                return;
            }
        }
        DirectoryLock directoryLock;
        synchronized (directoryLockCacheMap)
        {
            directoryLock = directoryLockCacheMap.get(vmdataDirectory);
            if (directoryLock == null)
            {
                try
                {
                    directoryLock = new DirectoryLock(vmdataDirectory);
                    directoryLockCacheMap.put(vmdataDirectory, directoryLock);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    return;
                }
            }
        }
        while (true)
        {
            try
            {
                directoryLock.lockDirectory();
                break;
            }
            catch (InterruptedException e)
            {
            }
        }
        try
        {
            File vmdataFile = new File(vmdataDirectory, uuid.toString());
            NodeContainerMap vmNodeContainerMap;
            try
            {
                vmNodeContainerMap = NodeDataPersistence.loadNode(vmdataFile, NodeContainerMap.class);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return;
            }
            catch (ElementTypeMismatchException e)
            {
                e.printStackTrace();
                return;
            }
            catch (UnsupportedVersionException e)
            {
                e.printStackTrace();
                return;
            }
            try
            {
                vm.readDataFromNode(vmNodeContainerMap);
            }
            catch (ElementNotFoundException e)
            {
                e.printStackTrace();
                return;
            }
            catch (ElementTypeMismatchException e)
            {
                e.printStackTrace();
                return;
            }
        }
        finally
        {
            directoryLock.releaseDirectory();
        }
    }
}
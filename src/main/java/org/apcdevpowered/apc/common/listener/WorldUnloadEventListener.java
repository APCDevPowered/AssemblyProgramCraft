package org.apcdevpowered.apc.common.listener;

import java.util.HashSet;
import java.util.Set;

import org.apcdevpowered.apc.common.util.MethodHandler;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

public class WorldUnloadEventListener
{
    private static Set<MethodHandler> callList  = new HashSet<MethodHandler>();
    
    public static void addListener(MethodHandler callbackMethodHandler)
    {
        callList.add(callbackMethodHandler);
    }
    public static void removeListener(MethodHandler callbackMethodHandler)
    {
        callList.remove(callbackMethodHandler);
    }
    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        if(event.world.isRemote)
        {
            return;
        }
        for(MethodHandler methodHandler : callList.toArray(new MethodHandler[callList.size()]))
        {
            try
            {
                methodHandler.invoke(event.world);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
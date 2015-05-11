package yuxuanchiadm.apc.apc.event;

import java.util.ArrayList;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.event.listener.IGamePauseListener;

public class TickEventListener
{
    public static ArrayList<IGamePauseListener> gamePauseListeners = new ArrayList<IGamePauseListener>();
    public boolean isGamePaused = true;
    
    public static void addListener(IGamePauseListener gamePauseListener)
    {
        gamePauseListeners.add(gamePauseListener);
    }
    public static void removeListener(IGamePauseListener gamePauseListener)
    {
        gamePauseListeners.remove(gamePauseListener);
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase == TickEvent.Phase.START)
        {
            if(AssemblyProgramCraft.proxy.isGamePaused())
            {
                if(!isGamePaused)
                {
                    isGamePaused = true;
                    for(IGamePauseListener gamePauseListener : gamePauseListeners.toArray(new IGamePauseListener[gamePauseListeners.size()]))
                    {
                        gamePauseListener.gamePaused();
                    }
                }
            }
            else
            {
                if(isGamePaused)
                {
                    isGamePaused = false;
                    for(IGamePauseListener gamePauseListener : gamePauseListeners.toArray(new IGamePauseListener[gamePauseListeners.size()]))
                    {
                        gamePauseListener.gameResume();
                    }
                }
            }
        }
    }   
}

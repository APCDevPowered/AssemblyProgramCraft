package org.apcdevpowered.apc.client.listener;

import org.apcdevpowered.apc.common.AssemblyProgramCraft;
import org.apcdevpowered.apc.common.util.GamePauseHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TickEventListener
{
    public boolean isGamePaused = true;
    
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            if (AssemblyProgramCraft.proxy.isGamePaused())
            {
                if (!isGamePaused)
                {
                    isGamePaused = true;
                    GamePauseHelper.onGamePaused();
                }
            }
            else
            {
                if (isGamePaused)
                {
                    isGamePaused = false;
                    GamePauseHelper.onGameResume();
                }
            }
        }
    }
}

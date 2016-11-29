package org.apcdevpowered.apc.common.util;

import java.util.ArrayList;

public class GamePauseHelper
{
    public static ArrayList<IGamePauseListener> gamePauseListeners = new ArrayList<IGamePauseListener>();
    
    public static void addListener(IGamePauseListener gamePauseListener)
    {
        gamePauseListeners.add(gamePauseListener);
    }
    public static void removeListener(IGamePauseListener gamePauseListener)
    {
        gamePauseListeners.remove(gamePauseListener);
    }
    
    public interface IGamePauseListener
    {
        void onGamePaused();
        void onGameResume();
    }
    
    public static void onGamePaused()
    {
        for (IGamePauseListener gamePauseListener : gamePauseListeners.toArray(new IGamePauseListener[gamePauseListeners.size()]))
        {
            gamePauseListener.onGamePaused();
        }
    }
    public static void onGameResume()
    {
        for (IGamePauseListener gamePauseListener : GamePauseHelper.gamePauseListeners.toArray(new IGamePauseListener[GamePauseHelper.gamePauseListeners.size()]))
        {
            gamePauseListener.onGameResume();
        }        
    }
}

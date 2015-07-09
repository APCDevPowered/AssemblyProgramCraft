package org.apcdevpowered.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HandlerAllocateList<V>
{
    private int cleanPerAllocate;
    private int cleanCount;
    private Map<Integer, V> handlerList;
    private List<Integer> closedHandlerList;
    
    public HandlerAllocateList()
    {
        this(10);
    }
    public HandlerAllocateList(int cleanPerAllocate)
    {
        this(cleanPerAllocate, null, null);
    }
    public HandlerAllocateList(int cleanPerAllocate, Map<Integer, V> handlerList, List<Integer> closedHandlerList)
    {
        this.cleanPerAllocate = cleanPerAllocate;
        if (handlerList != null)
        {
            this.handlerList = new HashMap<Integer, V>(handlerList);
        }
        else
        {
            this.handlerList = new HashMap<Integer, V>();
        }
        if (closedHandlerList != null)
        {
            this.closedHandlerList = new ArrayList<Integer>(closedHandlerList);
        }
        else
        {
            this.closedHandlerList = new ArrayList<Integer>();
        }
    }
    public synchronized int allocate(V value)
    {
        int handler;
        if (!closedHandlerList.isEmpty())
        {
            handler = closedHandlerList.remove(0);
            handlerList.put(handler, value);
        }
        else
        {
            handler = handlerList.size();
            handlerList.put(handler, value);
        }
        cleanCount++;
        if (cleanCount >= cleanPerAllocate)
        {
            int max = handlerList.size() - 1;
            Iterator<Integer> iterator = closedHandlerList.iterator();
            while (iterator.hasNext())
            {
                int closedHandler = iterator.next();
                if (closedHandler > max)
                {
                    iterator.remove();
                }
            }
        }
        return handler;
    }
    public synchronized boolean free(int handler)
    {
        if (handlerList.containsKey(handler))
        {
            handlerList.remove(handler);
            closedHandlerList.add(handler);
            return true;
        }
        return false;
    }
    public synchronized V get(int handler)
    {
        return handlerList.get(handler);
    }
    public synchronized void clear()
    {
        handlerList.clear();
        closedHandlerList.clear();
    }
    public synchronized boolean vaild(int handler)
    {
        return handlerList.containsKey(handler);
    }
    public synchronized boolean isEmpty()
    {
        return handlerList.isEmpty();
    }
    public Map<Integer, V> getHandlerList()
    {
        return handlerList;
    }
    public List<Integer> getClosedHandlerList()
    {
        return closedHandlerList;
    }
}
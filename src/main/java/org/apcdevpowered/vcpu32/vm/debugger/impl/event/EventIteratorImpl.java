package org.apcdevpowered.vcpu32.vm.debugger.impl.event;

import java.util.NoSuchElementException;

import org.apcdevpowered.vcpu32.vm.debugger.event.EventIterator;

public class EventIteratorImpl implements EventIterator<EventImpl>
{
    private EventSetImpl eventSet;
    
    private int cursor = 0;
    
    public EventIteratorImpl(EventSetImpl eventSet)
    {
        this.eventSet = eventSet;
    }
    @Override
    public boolean hasNext()
    {
        return cursor != eventSet.size();
    }
    @Override
    public EventImpl next()
    {
        try
        {
            EventImpl next = eventSet.get(cursor);
            ++cursor;
            return next;
        }
        catch (IndexOutOfBoundsException e)
        {
            throw new NoSuchElementException();
        }
    }
    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
    @Override
    public EventImpl nextEvent()
    {
        return next();
    }
}
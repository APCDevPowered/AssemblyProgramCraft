package org.apcdevpowered.vcpu32.vm.debugger.impl.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apcdevpowered.vcpu32.vm.debugger.event.EventSet;
import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestImpl;

public class EventSetImpl extends ArrayList<EventImpl> implements EventSet<EventImpl>
{
    private static final long serialVersionUID = 1L;
    private VirtualMachineReferenceImpl virtualMachineReference;
    private int suspendPolicy;
    private ThreadReferenceImpl suspendEventThread;
    
    public EventSetImpl(VirtualMachineReferenceImpl virtualMachineReference, Collection<EventImpl> events)
    {
        super(filterEvents(events));
        this.virtualMachineReference = virtualMachineReference;
        this.suspendPolicy = calculateSuspendPolicy(this);
    }
    @Override
    public VirtualMachineReferenceImpl virtualMachine()
    {
        return virtualMachineReference;
    }
    @Override
    public EventIteratorImpl eventIterator()
    {
        return new EventIteratorImpl(this);
    }
    @Override
    public void resume()
    {
        if (suspendPolicy == EventRequestImpl.SUSPEND_ALL)
        {
            virtualMachine().resume();
        }
        else if (suspendPolicy == EventRequestImpl.SUSPEND_EVENT_THREAD)
        {
            suspendEventThread.resume();
        }
    }
    @Override
    public int suspendPolicy()
    {
        return suspendPolicy;
    }
    @Override
    public Iterator<EventImpl> iterator()
    {
        return new EventIteratorImpl(this);
    }
    @Override
    public boolean add(EventImpl o)
    {
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean addAll(Collection<? extends EventImpl> coll)
    {
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean removeAll(Collection<?> coll)
    {
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean retainAll(Collection<?> coll)
    {
        throw new UnsupportedOperationException();
    }
    @Override
    public void clear()
    {
        throw new UnsupportedOperationException();
    }
    protected void suspendThreads()
    {
        if (suspendPolicy == EventRequestImpl.SUSPEND_EVENT_THREAD)
        {
            suspendEventThread.suspend();
        }
        else if (suspendPolicy == EventRequestImpl.SUSPEND_ALL)
        {
            virtualMachine().suspend();
        }
    }
    private int calculateSuspendPolicy(Collection<EventImpl> events)
    {
        int suspendPolicy = EventRequestImpl.SUSPEND_NONE;
        for (EventImpl event : events)
        {
            if (event.request() != null)
            {
                if (event.request().suspendPolicy() > suspendPolicy)
                {
                    if (event.request().suspendPolicy() == EventRequestImpl.SUSPEND_EVENT_THREAD)
                    {
                        if (event instanceof ThreadedEventImpl)
                        {
                            suspendPolicy = EventRequestImpl.SUSPEND_EVENT_THREAD;
                            suspendEventThread = ((ThreadedEventImpl) event).thread();
                        }
                    }
                    else if (event.request().suspendPolicy() == EventRequestImpl.SUSPEND_ALL)
                    {
                        suspendPolicy = EventRequestImpl.SUSPEND_ALL;
                        suspendEventThread = null;
                    }
                }
            }
        }
        return suspendPolicy;
    }
    private static Collection<EventImpl> filterEvents(Collection<EventImpl> events)
    {
        Collection<EventImpl> filteredEvents = new ArrayList<EventImpl>();
        for (EventImpl event : events)
        {
            if (event.filterEvent())
            {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }
}

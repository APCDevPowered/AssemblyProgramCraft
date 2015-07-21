package org.apcdevpowered.vcpu32.vm.debugger.impl.request;

import java.util.ArrayList;
import java.util.List;

import org.apcdevpowered.vcpu32.vm.debugger.Location;
import org.apcdevpowered.vcpu32.vm.debugger.ThreadReference;
import org.apcdevpowered.vcpu32.vm.debugger.impl.LocationImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.request.EventRequest;
import org.apcdevpowered.vcpu32.vm.debugger.request.EventRequestManager;

public class EventRequestManagerImpl implements EventRequestManager
{
    private VirtualMachineReferenceImpl virtualMachineReference;
    private List<EventRequestImpl> eventRequestList = new ArrayList<EventRequestImpl>();
    
    public EventRequestManagerImpl(VirtualMachineReferenceImpl virtualMachineReference)
    {
        this.virtualMachineReference = virtualMachineReference;
    }
    @Override
    public VirtualMachineReferenceImpl virtualMachine()
    {
        return virtualMachineReference;
    }
    @Override
    public ThreadStartRequestImpl createThreadStartRequest()
    {
        ThreadStartRequestImpl threadStartRequest = new ThreadStartRequestImpl(virtualMachineReference, this);
        synchronized (eventRequestList)
        {
            eventRequestList.add(threadStartRequest);
        }
        return threadStartRequest;
    }
    @Override
    public ThreadDeathRequestImpl createThreadDeathRequest()
    {
        ThreadDeathRequestImpl threadDeathRequest = new ThreadDeathRequestImpl(virtualMachineReference, this);
        synchronized (eventRequestList)
        {
            eventRequestList.add(threadDeathRequest);
        }
        return threadDeathRequest;
    }
    @Override
    public MethodEntryRequestImpl createMethodEntryRequest()
    {
        MethodEntryRequestImpl methodEntryRequest = new MethodEntryRequestImpl(virtualMachineReference, this);
        synchronized (eventRequestList)
        {
            eventRequestList.add(methodEntryRequest);
        }
        return methodEntryRequest;
    }
    @Override
    public MethodExitRequestImpl createMethodExitRequest()
    {
        MethodExitRequestImpl methodExitRequest = new MethodExitRequestImpl(virtualMachineReference, this);
        synchronized (eventRequestList)
        {
            eventRequestList.add(methodExitRequest);
        }
        return methodExitRequest;
    }
    @Override
    public StepRequestImpl createStepRequest(ThreadReference thread, int size, int depth)
    {
        if (!(thread instanceof ThreadReferenceImpl))
        {
            throw new IllegalArgumentException();
        }
        StepRequestImpl stepRequest = new StepRequestImpl(virtualMachineReference, this, (ThreadReferenceImpl) thread, size, depth);
        synchronized (eventRequestList)
        {
            eventRequestList.add(stepRequest);
        }
        return stepRequest;
    }
    @Override
    public BreakpointRequestImpl createBreakpointRequest(Location location)
    {
        if (!(location instanceof LocationImpl))
        {
            throw new IllegalArgumentException();
        }
        BreakpointRequestImpl breakpointRequest = new BreakpointRequestImpl(virtualMachineReference, this, (LocationImpl) location);
        synchronized (eventRequestList)
        {
            eventRequestList.add(breakpointRequest);
        }
        return breakpointRequest;
    }
    @Override
    public AccessWatchpointRequestImpl createAccessWatchpointRequest(Location location) throws UnsupportedOperationException
    {
        if (!(location instanceof LocationImpl))
        {
            throw new IllegalArgumentException();
        }
        AccessWatchpointRequestImpl accessWatchpointRequest = new AccessWatchpointRequestImpl(virtualMachineReference, this, (LocationImpl) location);
        synchronized (eventRequestList)
        {
            eventRequestList.add(accessWatchpointRequest);
        }
        return accessWatchpointRequest;
    }
    @Override
    public ModificationWatchpointRequestImpl createModificationWatchpointRequest(Location location) throws UnsupportedOperationException
    {
        if (!(location instanceof LocationImpl))
        {
            throw new IllegalArgumentException();
        }
        ModificationWatchpointRequestImpl modificationWatchpointRequest = new ModificationWatchpointRequestImpl(virtualMachineReference, this, (LocationImpl) location);
        synchronized (eventRequestList)
        {
            eventRequestList.add(modificationWatchpointRequest);
        }
        return modificationWatchpointRequest;
    }
    @Override
    public VMDeathRequestImpl createVMDeathRequest() throws UnsupportedOperationException
    {
        VMDeathRequestImpl vmDeathRequest = new VMDeathRequestImpl(virtualMachineReference, this);
        synchronized (eventRequestList)
        {
            eventRequestList.add(vmDeathRequest);
        }
        return vmDeathRequest;
    }
    @Override
    public void deleteEventRequest(EventRequest eventRequest)
    {
        synchronized (eventRequestList)
        {
            if (eventRequestList.contains(eventRequest))
            {
                eventRequest.disable();
                eventRequestList.remove(eventRequest);
            }
        }
    }
    @Override
    public void deleteEventRequests(List<? extends EventRequest> eventRequests)
    {
        synchronized (eventRequestList)
        {
            for (EventRequest eventRequest : eventRequests)
            {
                if (eventRequestList.contains(eventRequest))
                {
                    eventRequest.disable();
                    eventRequestList.remove(eventRequest);
                }
            }
        }
    }
    @Override
    public void deleteAllBreakpoints()
    {
        deleteEventRequests(breakpointRequests());
    }
    @Override
    public List<StepRequestImpl> stepRequests()
    {
        return getEventRequests(StepRequestImpl.class);
    }
    @Override
    public List<ThreadStartRequestImpl> threadStartRequests()
    {
        return getEventRequests(ThreadStartRequestImpl.class);
    }
    @Override
    public List<ThreadDeathRequestImpl> threadDeathRequests()
    {
        return getEventRequests(ThreadDeathRequestImpl.class);
    }
    @Override
    public List<BreakpointRequestImpl> breakpointRequests()
    {
        return getEventRequests(BreakpointRequestImpl.class);
    }
    @Override
    public List<AccessWatchpointRequestImpl> accessWatchpointRequests()
    {
        return getEventRequests(AccessWatchpointRequestImpl.class);
    }
    @Override
    public List<ModificationWatchpointRequestImpl> modificationWatchpointRequests()
    {
        return getEventRequests(ModificationWatchpointRequestImpl.class);
    }
    @Override
    public List<MethodEntryRequestImpl> methodEntryRequests()
    {
        return getEventRequests(MethodEntryRequestImpl.class);
    }
    @Override
    public List<MethodExitRequestImpl> methodExitRequests()
    {
        return getEventRequests(MethodExitRequestImpl.class);
    }
    @Override
    public List<VMDeathRequestImpl> vmDeathRequests()
    {
        return getEventRequests(VMDeathRequestImpl.class);
    }
    private <R extends EventRequestImpl> List<R> getEventRequests(Class<R> clazz)
    {
        List<R> result = new ArrayList<R>();
        synchronized (eventRequestList)
        {
            for (EventRequestImpl eventRequest : eventRequestList)
            {
                if (clazz.isAssignableFrom(eventRequest.getClass()))
                {
                    result.add(clazz.cast(eventRequest));
                }
            }
        }
        return result;
    }
    protected boolean isVaild(EventRequestImpl eventRequest)
    {
        synchronized (eventRequestList)
        {
            return eventRequestList.contains(eventRequest);
        }
    }
}

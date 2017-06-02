package org.apcdevpowered.vcpu32.vm.debugger.impl.event;

import org.apcdevpowered.vcpu32.vm.debugger.event.ThreadedEvent;
import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.ThreadedRequestImpl;

public class ThreadedEventImpl extends EventImpl implements ThreadedEvent
{
    private ThreadReferenceImpl threadReference;
    
    public ThreadedEventImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestImpl eventRequest, ThreadReferenceImpl thread)
    {
        super(virtualMachineReference, eventRequest);
        this.threadReference = thread;
    }
    @Override
    public ThreadReferenceImpl thread()
    {
        return threadReference;
    }
    @Override
    protected boolean filterEvent()
    {
        ThreadedRequestImpl eventRequest = ((ThreadedRequestImpl) request());
        if (eventRequest == null)
            return true;
        else
            return eventRequest.getThreadFilterGroup().filterEvent(eventRequest, this) && super.filterEvent();
    }
}

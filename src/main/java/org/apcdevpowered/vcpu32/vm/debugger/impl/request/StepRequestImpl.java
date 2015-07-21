package org.apcdevpowered.vcpu32.vm.debugger.impl.request;

import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.request.StepRequest;

public class StepRequestImpl extends EventRequestImpl implements StepRequest
{
    private ThreadReferenceImpl threadReference;
    private int size;
    private int depth;
    
    public StepRequestImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestManagerImpl eventRequestManager, ThreadReferenceImpl threadReference, int size, int depth)
    {
        super(virtualMachineReference, eventRequestManager);
        this.threadReference = threadReference;
        this.size = size;
        this.depth = depth;
    }
    @Override
    public ThreadReferenceImpl thread()
    {
        return threadReference;
    }
    @Override
    public int size()
    {
        return size;
    }
    @Override
    public int depth()
    {
        return depth;
    }
    @Override
    protected void requestStateChange(boolean isEnable)
    {
    }
}
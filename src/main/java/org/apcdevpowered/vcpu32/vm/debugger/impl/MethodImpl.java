package org.apcdevpowered.vcpu32.vm.debugger.impl;

import java.util.Arrays;

import org.apcdevpowered.vcpu32.vm.AssemblyVirtualThread.AVThreadStackFrame;
import org.apcdevpowered.vcpu32.vm.debugger.IncompatibleThreadStateException;
import org.apcdevpowered.vcpu32.vm.debugger.Method;
import org.apcdevpowered.vcpu32.vm.debugger.VirtualMachineReference;

public class MethodImpl implements Method
{
    private VirtualMachineReferenceImpl virtualMachineReference;
    private ThreadReferenceImpl threadReference;
    private LocationImpl location;
    private int[] arguments;
    private LocationImpl returnAddress;
    private AVThreadStackFrame stackFrame;
    
    public MethodImpl(VirtualMachineReferenceImpl virtualMachineReference, ThreadReferenceImpl threadReference, LocationImpl location, int[] arguments, LocationImpl returnAddress, AVThreadStackFrame stackFrame)
    {
        this.virtualMachineReference = virtualMachineReference;
        this.threadReference = threadReference;
        this.location = location;
        this.arguments = Arrays.copyOf(arguments, arguments.length);
        this.returnAddress = returnAddress;
        this.stackFrame = stackFrame;
    }
    @Override
    public VirtualMachineReference virtualMachine()
    {
        return virtualMachineReference;
    }
    @Override
    public synchronized LocationImpl location()
    {
        return location;
    }
    @Override
    public synchronized ThreadReferenceImpl thread()
    {
        return threadReference;
    }
    @Override
    public synchronized String name()
    {
        return null;
    }
    @Override
    public synchronized int[] arguments()
    {
        return Arrays.copyOf(arguments, arguments.length);
    }
    @Override
    public synchronized LocationImpl returnAddress()
    {
        return returnAddress;
    }
    @Override
    public synchronized StackFrameReferenceImpl frame() throws IncompatibleThreadStateException
    {
        synchronized (threadReference)
        {
            if (!threadReference.isSuspended())
            {
                throw new IncompatibleThreadStateException();
            }
            if (stackFrame.isInvalid())
            {
                throw new IncompatibleThreadStateException();
            }
            return threadReference.getStackFrame(stackFrame.getFrameIndex());
        }
    }
}

package org.apcdevpowered.vcpu32.vm.debugger.impl;

import java.util.HashMap;
import java.util.Map;

import org.apcdevpowered.vcpu32.vm.AssemblyVirtualThread.AVThreadStackFrame;
import org.apcdevpowered.vcpu32.vm.debugger.InvalidStackFrameException;
import org.apcdevpowered.vcpu32.vm.debugger.Location;
import org.apcdevpowered.vcpu32.vm.debugger.StackFrameReference;

public class StackFrameReferenceImpl implements StackFrameReference
{
    private VirtualMachineReferenceImpl virtualMachineReference;
    private ThreadReferenceImpl threadReference;
    private int frameIndex;
    private AVThreadStackFrame stackFrame;
    private boolean isInvalid;
    
    public StackFrameReferenceImpl(VirtualMachineReferenceImpl virtualMachineReference, ThreadReferenceImpl threadReference, AVThreadStackFrame stackFrame, int frameIndex)
    {
        this.virtualMachineReference = virtualMachineReference;
        this.threadReference = threadReference;
        this.frameIndex = frameIndex;
        this.stackFrame = stackFrame;
    }
    @Override
    public VirtualMachineReferenceImpl virtualMachine()
    {
        return virtualMachineReference;
    }
    @Override
    public synchronized LocationImpl location()
    {
        validateStackFrame();
        return new LocationImpl(virtualMachineReference, getRegister(REG_PC));
    }
    @Override
    public synchronized ThreadReferenceImpl thread()
    {
        validateStackFrame();
        return threadReference;
    }
    @Override
    public synchronized int getRegister(int register)
    {
        validateStackFrame();
        return stackFrame.getRegisterValue(register);
    }
    @Override
    public synchronized void setRegister(int register, int value)
    {
        validateStackFrame();
        stackFrame.setRegisterValue(register, value);
    }
    @Override
    public synchronized Map<Integer, Integer> registers()
    {
        validateStackFrame();
        Map<Integer, Integer> registers = new HashMap<Integer, Integer>();
        registers.put(REG_A, getRegister(REG_A));
        registers.put(REG_B, getRegister(REG_B));
        registers.put(REG_C, getRegister(REG_C));
        registers.put(REG_X, getRegister(REG_X));
        registers.put(REG_Y, getRegister(REG_Y));
        registers.put(REG_Z, getRegister(REG_Z));
        registers.put(REG_I, getRegister(REG_I));
        registers.put(REG_J, getRegister(REG_J));
        registers.put(REG_O, getRegister(REG_O));
        registers.put(REG_PC, getRegister(REG_PC));
        registers.put(REG_SP, getRegister(REG_SP));
        return registers;
    }
    @Override
    public synchronized int getStackValue(int index)
    {
        validateStackFrame();
        Integer num = stackFrame.getStack().get(index);
        return num == null ? 0 : num;
    }
    @Override
    public synchronized void setStackValue(int index, int value)
    {
        validateStackFrame();
        stackFrame.getStack().set(index, value);
    }
    @Override
    public synchronized Location returnAddress()
    {
        validateStackFrame();
        return new LocationImpl(virtualMachineReference, stackFrame.getReturnAddress());
    }
    @Override
    public synchronized Location enterAddress()
    {
        validateStackFrame();
        return new LocationImpl(virtualMachineReference, stackFrame.getEnterAddress());
    }
    @Override
    public synchronized int parLength()
    {
        validateStackFrame();
        return stackFrame.getParLength();
    }
    public synchronized void markInvalid()
    {
        isInvalid = true;
    }
    public int getFrameIndex()
    {
        return frameIndex;
    }
    public synchronized void validateStackFrame()
    {
        if (isInvalid)
        {
            throw new InvalidStackFrameException("Thread has been resumed");
        }
    }
    public AVThreadStackFrame getHander()
    {
        return stackFrame;
    }
}

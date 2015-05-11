package yuxuanchiadm.apc.vcpu32.vm.debugger.impl;

import java.util.HashMap;
import java.util.Map;

import yuxuanchiadm.apc.vcpu32.vm.AssemblyVirtualThread.AVThreadStackFrame;
import yuxuanchiadm.apc.vcpu32.vm.debugger.InvalidStackFrameException;
import yuxuanchiadm.apc.vcpu32.vm.debugger.Location;
import yuxuanchiadm.apc.vcpu32.vm.debugger.StackFrame;

public class StackFrameImpl implements StackFrame
{
    private VirtualMachineReferenceImpl virtualMachineReference;
    
    private ThreadReferenceImpl threadReference;
    private int frameIndex;
    private AVThreadStackFrame stackFrame;
    private boolean isInvalid;
    
    public StackFrameImpl(VirtualMachineReferenceImpl virtualMachineReference, ThreadReferenceImpl threadReference, int frameIndex)
    {
        this.virtualMachineReference = virtualMachineReference;
        this.threadReference = threadReference;
        this.frameIndex = frameIndex;
        this.stackFrame = threadReference.getAVThread().getStackFrame(frameIndex);
        if(stackFrame == null)
        {
            throw new IllegalArgumentException("StackFrame at " + frameIndex + " not exists.");
        }
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
    public synchronized Map<Integer, Integer> getRegisters()
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
        Integer num = stackFrame.stack.get(index);
        return num == null ? 0 : num;
    }
    @Override
    public synchronized void setStackValue(int index, int value)
    {
        validateStackFrame();
        stackFrame.stack.set(index, value);
    }
    @Override
    public synchronized Location getReturnAddress()
    {
        validateStackFrame();
        return new LocationImpl(virtualMachineReference, stackFrame.returnAddress);
    }
    @Override
    public synchronized Location getEnterAddress()
    {
        validateStackFrame();
        return new LocationImpl(virtualMachineReference, stackFrame.enterAddress);
    }
    @Override
    public synchronized int getParLength()
    {
        validateStackFrame();
        return stackFrame.parLength;
    }
    protected synchronized void markInvalid()
    {
        isInvalid = true;
    }
    protected int getFrameIndex()
    {
        return frameIndex;
    }
    protected synchronized void validateStackFrame()
    {
        if(isInvalid)
        {
            throw new InvalidStackFrameException("Thread has been resumed");
        }
    }
}

package org.apcdevpowered.vcpu32.vm;

import org.apcdevpowered.apc.common.config.ConfigSystem;
import org.apcdevpowered.util.array.DynamicSparseArray;
import org.apcdevpowered.vcpu32.vm.AssemblyVirtualThread.AVThreadStackFrame;

import static org.apcdevpowered.vcpu32.vm.Registers.*;

public class VMsLogger
{
    public static void printThreadCreatedInfo(int StartRAM,String ThreadName)
    {
        if(ConfigSystem.debugMode == false)
        {
            return;
        }
        System.out.println
        (
            "                                                                " + "\n" + 
            "---------------------------------------------------" + "\n" + 
            "Thread Created" + "\n" + 
            "---------------------------------------------------" + "\n" +           
            "StartRAM: " + StartRAM + "\n" + 
            "ThreadName: " + ThreadName + "\n" + 
            "---------------------------------------------------"
        );
    }
    public static void printThreadInfo(AssemblyVirtualThread AVT)
    {
        if(ConfigSystem.debugMode == false)
        {
            return;
        }
        if(AVT == null || AVT.getVM() == null)
        {
            return;
        }
        AVThreadStackFrame stackFrame = AVT.getStack().peek();
        int A = stackFrame.getRegisterValue(REG_A);
        int B = stackFrame.getRegisterValue(REG_B);
        int C = stackFrame.getRegisterValue(REG_C);
        int X = stackFrame.getRegisterValue(REG_X);
        int Y = stackFrame.getRegisterValue(REG_Y);
        int Z = stackFrame.getRegisterValue(REG_Z);
        int I = stackFrame.getRegisterValue(REG_I);
        int J = stackFrame.getRegisterValue(REG_J);
        int O = stackFrame.getRegisterValue(REG_O);
        int SP = stackFrame.getRegisterValue(REG_SP);
        String tempStr = 
            "                                                                " + "\n" + 
            "---------------------------------------------------" + "\n" + 
            "VM Running Thread:" + AVT.getThreadName() + "\n" + 
            "---------------------------------------------------" + "\n" + 
            "General StackFrame Register:" + "\n" + 
            "---------------------------------------------------" + "\n" + 
            "Register A is:" + A + "   Float: " + Float.intBitsToFloat(A) + "\n" + 
            "Register B is:" + B + "   Float: " + Float.intBitsToFloat(B) + "\n" + 
            "Register C is:" + C + "   Float: " + Float.intBitsToFloat(C) + "\n" + 
            "Register X is:" + X + "   Float: " + Float.intBitsToFloat(X) + "\n" + 
            "Register Y is:" + Y + "   Float: " + Float.intBitsToFloat(Y) + "\n" + 
            "Register Z is:" + Z + "   Float: " + Float.intBitsToFloat(Z) + "\n" + 
            "Register I is:" + I + "   Float: " + Float.intBitsToFloat(I) + "\n" + 
            "Register J is:" + J + "   Float: " + Float.intBitsToFloat(J) + "\n" + 
            "---------------------------------------------------" + "\n" + 
            "Special StackFrame Register:" + "\n" + 
            "---------------------------------------------------" + "\n" + 
            "Register O is:" + O + "\n" + 
            "Register SP is:" + SP + "\n" + 
            "---------------------------------------------------" + "\n" + 
            "Special Thread Register:" + "\n" + 
            "---------------------------------------------------" + "\n" + 
            "Register PC is:" + AVT.getPC() + "\n" + 
            "---------------------------------------------------" + "\n" + 
            "Stack Info:" + "\n" + 
            "---------------------------------------------------" + "\n" + 
            "StackNum:" + AVT.getStack().size() + "\n" + 
            "StackFrameData:"+ "\n";
        DynamicSparseArray<Integer> stack = stackFrame.stack;
        for(int i = 0;i < SP;i++)
        {
            Integer value = stack.get(i);
            tempStr += "\t" + value == null ? 0 : value.intValue() + "\n";
        }
        tempStr += 
             "---------------------------------------------------";
        System.out.println(tempStr);
    }
}
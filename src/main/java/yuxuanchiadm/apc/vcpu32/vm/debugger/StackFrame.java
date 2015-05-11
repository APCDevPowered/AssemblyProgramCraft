package yuxuanchiadm.apc.vcpu32.vm.debugger;

import java.util.Map;

public interface StackFrame extends Mirror, Locatable
{
    public static final int REG_A = 0x1;
    public static final int REG_B = 0x2;
    public static final int REG_C = 0x3;
    public static final int REG_X = 0x4;
    public static final int REG_Y = 0x5;
    public static final int REG_Z = 0x6;
    public static final int REG_I = 0x7;
    public static final int REG_J = 0x8;
    public static final int REG_O = 0x9;
    public static final int REG_PC = 0xA;
    public static final int REG_SP = 0xB;
    
    ThreadReference thread();
    int getRegister(int register);
    void setRegister(int register, int value);
    Map<Integer, Integer> getRegisters();
    int getStackValue(int index);
    void setStackValue(int index, int value);
    Location getReturnAddress();
    Location getEnterAddress();
    int getParLength();
}
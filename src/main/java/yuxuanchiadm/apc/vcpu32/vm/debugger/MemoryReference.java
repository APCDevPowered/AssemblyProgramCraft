package yuxuanchiadm.apc.vcpu32.vm.debugger;

public interface MemoryReference extends Mirror
{
    int getInt(int address);
    boolean setInt(int address, int value);
    float getFloat(int address);
    boolean setFloat(int address, float value);
    String getString(int address);
    boolean setString(int address, String value);
    char getChar(int address);
    boolean setChar(int address, char value);
    int[] getValues(int address, int length);
    boolean setValues(int address, int[] value);
    int getSize();
    void clear();
}
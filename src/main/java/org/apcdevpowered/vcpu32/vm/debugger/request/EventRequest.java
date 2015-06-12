package org.apcdevpowered.vcpu32.vm.debugger.request;

import org.apcdevpowered.vcpu32.vm.debugger.Mirror;

public interface EventRequest extends Mirror
{
    public static final int SUSPEND_NONE = 0;
    public static final int SUSPEND_EVENT_THREAD = 1;
    public static final int SUSPEND_ALL = 2;
    
    boolean isEnabled();
    void setEnabled(boolean val);
    void enable();
    void disable();
    void addCountFilter(int count);
    void setSuspendPolicy(int policy);
    int suspendPolicy();
    void putProperty(Object key, Object value);
    Object getProperty(Object key);
}
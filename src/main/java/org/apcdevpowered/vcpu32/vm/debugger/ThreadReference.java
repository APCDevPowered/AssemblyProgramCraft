package org.apcdevpowered.vcpu32.vm.debugger;

import java.util.List;

public interface ThreadReference extends Mirror
{
    public static final int THREAD_STATUS_UNKNOWN = -1;
    public static final int THREAD_STATUS_ZOMBIE = 0;
    public static final int THREAD_STATUS_RUNNING = 1;
    public static final int THREAD_STATUS_SLEEPING = 2;
    public static final int THREAD_STATUS_MONITOR = 3;
    public static final int THREAD_STATUS_WAIT = 4;
    public static final int THREAD_STATUS_NOT_STARTED = 5;
    
    String name();
    void suspend();
    void resume();
    int suspendCount();
    void stop();
    int status();
    boolean isSuspended();
    boolean isAtBreakpoint();
    int frameCount() throws IncompatibleThreadStateException;
    List<? extends StackFrame> frames() throws IncompatibleThreadStateException;
    StackFrame frame(int index) throws IncompatibleThreadStateException;
    List<? extends StackFrame> frames(int start, int length) throws IncompatibleThreadStateException;
    List<? extends MonitorReference> ownedMonitors() throws IncompatibleThreadStateException;
    MonitorReference currentContendedMonitor() throws IncompatibleThreadStateException;
}
package org.apcdevpowered.vcpu32.vm.debugger.request;

import java.util.List;

import org.apcdevpowered.vcpu32.vm.debugger.Location;
import org.apcdevpowered.vcpu32.vm.debugger.Mirror;
import org.apcdevpowered.vcpu32.vm.debugger.ThreadReference;

public interface EventRequestManager extends Mirror
{
    ThreadStartRequest createThreadStartRequest();
    ThreadDeathRequest createThreadDeathRequest();
    MethodEntryRequest createMethodEntryRequest();
    MethodExitRequest createMethodExitRequest();
    StepRequest createStepRequest(ThreadReference thread, int size, int depth);
    BreakpointRequest createBreakpointRequest(Location location);
    AccessWatchpointRequest createAccessWatchpointRequest(Location location) throws UnsupportedOperationException;
    ModificationWatchpointRequest createModificationWatchpointRequest(Location location) throws UnsupportedOperationException;
    VMDeathRequest createVMDeathRequest() throws UnsupportedOperationException;
    void deleteEventRequest(EventRequest eventRequest);
    void deleteEventRequests(List<? extends EventRequest> eventRequests);
    void deleteAllBreakpoints();
    List<? extends StepRequest> stepRequests();
    List<? extends ThreadStartRequest> threadStartRequests();
    List<? extends ThreadDeathRequest> threadDeathRequests();
    List<? extends BreakpointRequest> breakpointRequests();
    List<? extends AccessWatchpointRequest> accessWatchpointRequests();
    List<? extends ModificationWatchpointRequest> modificationWatchpointRequests();
    List<? extends MethodEntryRequest> methodEntryRequests();
    List<? extends MethodExitRequest> methodExitRequests();
    List<? extends VMDeathRequest> vmDeathRequests();
}
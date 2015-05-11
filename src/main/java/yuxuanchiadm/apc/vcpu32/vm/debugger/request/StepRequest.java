package yuxuanchiadm.apc.vcpu32.vm.debugger.request;

import yuxuanchiadm.apc.vcpu32.vm.debugger.ThreadReference;

public interface StepRequest extends EventRequest
{
    int STEP_INTO = 1;
    int STEP_OVER = 2;
    int STEP_OUT = 3;
    int STEP_MIN = -1;
    int STEP_LINE = -2;
    
    ThreadReference thread();
    int size();
    int depth();
}
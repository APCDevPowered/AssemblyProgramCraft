package yuxuanchiadm.apc.vcpu32.vm;

public enum ThreadState
{
    //线程已经创建，但尚未运行
    NEW(0),
    //线程处于可运行状态，但可能在等待CPU资源，或者处于调试断点
    RUNNABLE(1),
    //线程正在等待监视器锁而处于不可运行状态
    BLOCKED(2),
    //线程由于调用了不超时的等待操作而处于不可运行状态
    WATTING(3),
    //线程由于调用了可超时的等待操作而处于不可运行状态
    TIMED_WAITING(4),
    //线程已经结束执行
    TERMINATED(5);
    
    private int stateId;

    private ThreadState(int stateId)
    {
        this.stateId = stateId;
    }
    public int getStateID()
    {
        return stateId;
    }
    public static ThreadState getThreadStateByID(int stateId)
    {
        for(ThreadState threadState : values())
        {
            if(threadState.getStateID() == stateId)
            {
                return threadState;
            }
        }
        return null;
    }
}
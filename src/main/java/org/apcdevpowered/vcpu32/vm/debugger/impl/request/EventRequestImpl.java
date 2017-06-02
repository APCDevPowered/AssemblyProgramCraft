package org.apcdevpowered.vcpu32.vm.debugger.impl.request;

import java.util.HashMap;
import java.util.Map;

import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.CountFilterGroup;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.CountFilterGroup.CountFilter;
import org.apcdevpowered.vcpu32.vm.debugger.request.EventRequest;
import org.apcdevpowered.vcpu32.vm.debugger.request.InvalidRequestStateException;

public abstract class EventRequestImpl implements EventRequest
{
    private VirtualMachineReferenceImpl virtualMachineReference;
    private EventRequestManagerImpl eventRequestManager;
    private boolean isEnabled;
    private int suspendPolicy;
    private Map<Object, Object> properties = new HashMap<Object, Object>();
    private CountFilterGroup countFilterGroup = new CountFilterGroup();
    
    public EventRequestImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestManagerImpl eventRequestManager)
    {
        this.virtualMachineReference = virtualMachineReference;
        this.eventRequestManager = eventRequestManager;
    }
    @Override
    public VirtualMachineReferenceImpl virtualMachine()
    {
        return virtualMachineReference;
    }
    @Override
    public boolean isEnabled()
    {
        return isEnabled;
    }
    @Override
    public void setEnabled(boolean val)
    {
        if (!isVaild())
        {
            throw new InvalidRequestStateException();
        }
        if (isEnabled != val)
        {
            isEnabled = val;
            requestStateChange(isEnabled);
        }
    }
    @Override
    public void enable()
    {
        setEnabled(true);
    }
    @Override
    public void disable()
    {
        setEnabled(false);
    }
    @Override
    public void addCountFilter(int count)
    {
        if (isEnabled() || !isVaild())
        {
            throw new InvalidRequestStateException();
        }
        if (count < 1)
        {
            throw new IllegalArgumentException();
        }
        countFilterGroup.addFilter(new CountFilter(count));
    }
    @Override
    public void setSuspendPolicy(int policy)
    {
        if (isEnabled() || !isVaild())
        {
            throw new InvalidRequestStateException();
        }
        if (policy != SUSPEND_NONE && policy != SUSPEND_EVENT_THREAD && policy != SUSPEND_ALL)
        {
            throw new IllegalArgumentException();
        }
        suspendPolicy = policy;
    }
    @Override
    public int suspendPolicy()
    {
        return suspendPolicy;
    }
    @Override
    public void putProperty(Object key, Object value)
    {
        synchronized (properties)
        {
            if (value == null)
            {
                properties.remove(key);
            }
            else
            {
                properties.put(key, value);
            }
        }
    }
    @Override
    public Object getProperty(Object key)
    {
        synchronized (properties)
        {
            return properties.get(key);
        }
    }
    public CountFilterGroup getCountFilterGroup()
    {
        return countFilterGroup;
    }
    protected boolean isVaild()
    {
        return eventRequestManager.isVaild(this);
    }
    protected abstract void requestStateChange(boolean isEnable);
}

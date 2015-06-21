package org.apcdevpowered.apc.common.util.history;

public enum MergeMode
{
    NO_MERGE(false, false, false, false),
    NORMAL_MERGE(false, true, true, true),
    FULL_MERGE(true, true, true, true);
    private final boolean mergeInsertBefore;
    private final boolean mergeInsertAfter;
    private final boolean mergeDeleteBefore;
    private final boolean mergeDeleteAfter;
    
    private MergeMode(boolean mergeInsertBefore, boolean mergeInsertAfter, boolean mergeDeleteBefore, boolean mergeDeleteAfter)
    {
        this.mergeInsertBefore = mergeInsertBefore;
        this.mergeInsertAfter = mergeInsertAfter;
        this.mergeDeleteBefore = mergeDeleteBefore;
        this.mergeDeleteAfter = mergeDeleteAfter;
    }
    public boolean isMergeInsertBefore()
    {
        return mergeInsertBefore;
    }
    public boolean isMergeInsertAfter()
    {
        return mergeInsertAfter;
    }
    public boolean isMergeDeleteBefore()
    {
        return mergeDeleteBefore;
    }
    public boolean isMergeDeleteAfter()
    {
        return mergeDeleteAfter;
    }
}
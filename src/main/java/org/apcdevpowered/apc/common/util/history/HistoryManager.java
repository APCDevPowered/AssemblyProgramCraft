package org.apcdevpowered.apc.common.util.history;

import java.util.Stack;

public class HistoryManager
{
    private MergeMode mergeMode;
    private Stack<HistoryEntry> historyStack = new Stack<HistoryEntry>();
    private Stack<HistoryEntry> undoHistoryStack = new Stack<HistoryEntry>();
    
    public HistoryManager()
    {
        setMergeMode(MergeMode.NORMAL_MERGE);
    }
    public HistoryManager(MergeMode mergeMode)
    {
        this.setMergeMode(mergeMode);
    }
    public void addHistory(int from, int to, String newText, String oldText, boolean canMerge)
    {
        if (newText == null || oldText == null)
        {
            throw new NullPointerException();
        }
        if (from < 0 || to < 0)
        {
            throw new IllegalArgumentException();
        }
        if (from > to)
        {
            throw new IllegalStateException();
        }
        if (oldText.length() != to - from)
        {
            throw new IllegalStateException();
        }
        undoHistoryStack.clear();
        if (!historyStack.isEmpty())
        {
            HistoryEntry currentEntry = historyStack.peek();
            if (currentEntry.merge(from, to, newText, oldText))
            {
                if (!canMerge)
                {
                    currentEntry.complete();
                }
                return;
            }
            else
            {
                currentEntry.complete();
            }
        }
        historyStack.push(new HistoryEntry(from, to, newText, oldText, canMerge));
    }
    public HistoryEntry undoHistory()
    {
        if (historyStack.isEmpty())
        {
            return null;
        }
        HistoryEntry entry = historyStack.pop();
        entry.complete();
        undoHistoryStack.push(entry);
        return entry;
    }
    public HistoryEntry redoHistory()
    {
        if(undoHistoryStack.isEmpty())
        {
            return null;
        }
        HistoryEntry entry = undoHistoryStack.pop();
        historyStack.push(entry);
        return entry;
    }
    public MergeMode getMergeMode()
    {
        return mergeMode;
    }
    public void setMergeMode(MergeMode mergeMode)
    {
        if (mergeMode == null)
        {
            throw new NullPointerException();
        }
        this.mergeMode = mergeMode;
    }
    
    public class HistoryEntry
    {
        private int from;
        private int to;
        private String newText;
        private String oldText;
        private boolean canMerge;
        
        private HistoryEntry(int from, int to, String newText, String oldText, boolean canMerge)
        {
            if (newText == null || oldText == null)
            {
                throw new NullPointerException();
            }
            if (from < 0 || to < 0)
            {
                throw new IllegalArgumentException();
            }
            if (from > to)
            {
                throw new IllegalStateException();
            }
            if (oldText.length() != to - from)
            {
                throw new IllegalStateException();
            }
            this.from = from;
            this.to = to;
            this.newText = newText;
            this.oldText = oldText;
            this.canMerge = canMerge;
        }
        public int getSelectedFrom()
        {
            return from;
        }
        public int getSelectedTo()
        {
            return to;
        }
        public String getNewText()
        {
            return newText;
        }
        public String getOldText()
        {
            return oldText;
        }
        public boolean canMerge()
        {
            return canMerge;
        }
        public void complete()
        {
            this.canMerge = false;
        }
        public boolean merge(int from, int to, String newText, String oldText)
        {
            if (newText == null || oldText == null)
            {
                throw new NullPointerException();
            }
            if (from < 0 || to < 0)
            {
                throw new IllegalArgumentException();
            }
            if (from > to)
            {
                throw new IllegalStateException();
            }
            if (oldText.length() != to - from)
            {
                throw new IllegalStateException();
            }
            if (!canMerge)
            {
                return false;
            }
            // Replace not support merge.
            if (!this.newText.isEmpty() && !this.oldText.isEmpty())
            {
                return false;
            }
            // Insert
            else if (this.oldText.isEmpty())
            {
                if (oldText.isEmpty())
                {
                    // Insert Before
                    if (from == this.from)
                    {
                        if (mergeMode.isMergeInsertBefore())
                        {
                            this.newText = newText + this.newText;
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    // Insert After
                    else if (from == this.from + newText.length())
                    {
                        if (mergeMode.isMergeInsertAfter())
                        {
                            this.newText = this.newText + newText;
                            this.from += newText.length();
                            this.to += newText.length();
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    // Insert Somewhere
                    else
                    {
                        return false;
                    }
                }
                // Not same action
                else
                {
                    return false;
                }
            }
            // Delete
            else if (this.newText.isEmpty())
            {
                if (newText.isEmpty())
                {
                    // Delete Before
                    if (to == this.from)
                    {
                        this.oldText = oldText + this.oldText;
                        this.from = from;
                        return true;
                    }
                    // Delete After
                    else if (from == this.to)
                    {
                        this.oldText = this.oldText + oldText;
                        this.to = to;
                        return true;
                    }
                    // Delete Somewhere
                    else
                    {
                        return false;
                    }
                }
                // Not same action
                else
                {
                    return false;
                }
            }
            else
            {
                throw new IllegalStateException();
            }
        }
    }
}
package yuxuanchiadm.apc.vcpu32.asm;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;

import yuxuanchiadm.apc.util.integer.IntTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class DebugInfo implements Cloneable
{
    private Map<Integer, Integer> offsetLineNumberMap = new TreeMap<Integer, Integer>();
    private Map<Integer, Set<Integer>> lineNumberOffsetMap = new TreeMap<Integer, Set<Integer>>();
    private Map<String, Integer> labelLineNumberMap = new TreeMap<String, Integer>();
    private Map<Integer, Set<String>> lineNumberLableMap = new TreeMap<Integer, Set<String>>();
    private Map<String, Integer> labelOffsetMap = new TreeMap<String, Integer>();
    private Map<Integer, Set<String>> offsetLabelMap = new TreeMap<Integer, Set<String>>();
    
    public synchronized boolean addOffsetLineNumber(int offset, int lineNumber)
    {
        if(offset < 0 || lineNumber < 1)
        {
            throw new IllegalArgumentException();
        }
        if(offsetLineNumberMap.containsKey(offset))
        {
            return false;
        }
        Set<Integer> offsetSet = lineNumberOffsetMap.get(lineNumber);
        if(offsetSet == null)
        {
            offsetSet = new LinkedHashSet<Integer>();
            lineNumberOffsetMap.put(lineNumber, offsetSet);
        }
        if(!offsetSet.add(offset))
        {
            return false;
        }
        offsetLineNumberMap.put(offset, lineNumber);
        return true;
    }
    public synchronized boolean addLabelLineNumber(String label, int lineNumber)
    {
        if(label == null || lineNumber < 1)
        {
            throw new IllegalArgumentException();
        }
        if(labelLineNumberMap.containsKey(label))
        {
            return false;
        }
        Set<String> labelSet = lineNumberLableMap.get(lineNumber);
        if(labelSet == null)
        {
            labelSet = new LinkedHashSet<String>();
            lineNumberLableMap.put(lineNumber, labelSet);
        }
        if(!labelSet.add(label))
        {
            return false;
        }
        labelLineNumberMap.put(label, lineNumber);
        return true;
    }
    public synchronized boolean addLabelOffset(String label, int offset)
    {
        if(label == null || offset < 0)
        {
            throw new IllegalArgumentException();
        }
        if(labelOffsetMap.containsKey(label))
        {
            return false;
        }
        Set<String> labelSet = offsetLabelMap.get(offset);
        if(labelSet == null)
        {
            labelSet = new LinkedHashSet<String>();
            offsetLabelMap.put(offset, labelSet);
        }
        if(!labelSet.add(label))
        {
            return false;
        }
        labelOffsetMap.put(label, offset);
        return true;
    }
    public synchronized int getLineNumberByOffset(int offset)
    {
        if(offsetLineNumberMap.isEmpty())
        {
            return -1;
        }
        while(offset >= 0)
        {
            if(offsetLineNumberMap.containsKey(offset))
            {
                int lineNumber = offsetLineNumberMap.get(offset);
                return lineNumber;
            }
            offset--;
        }
        return -1;
    }
    public synchronized Set<Integer> getOffsetByLineNumber(int lineNumber)
    {
        if(lineNumberOffsetMap.containsKey(lineNumber))
        {
            return Collections.unmodifiableSet(lineNumberOffsetMap.get(lineNumber));
        }
        return null;
    }
    public synchronized int getLineNumberByLabel(String label)
    {
        if(labelLineNumberMap.containsKey(label))
        {
            return labelLineNumberMap.get(label);
        }
        return -1;
    }
    public synchronized Set<String> getLableByLineNumber(int lineNumber)
    {
        if(lineNumberLableMap.containsKey(lineNumber))
        {
            return Collections.unmodifiableSet(lineNumberLableMap.get(lineNumber));
        }
        return null;
    }
    public synchronized int getOffsetByLabel(String label)
    {
        if(labelOffsetMap.containsKey(label))
        {
            return labelOffsetMap.get(label);
        }
        return -1;
    }
    public synchronized Set<String> getLableByOffset(int offset)
    {
        if(offsetLabelMap.containsKey(offset))
        {
            return Collections.unmodifiableSet(offsetLabelMap.get(offset));
        }
        return null;
    }
    public synchronized Map<Integer, Integer> getOffsetLineNumberMap()
    {
        return Collections.synchronizedMap(offsetLineNumberMap);
    }
    public synchronized Map<Integer, Set<Integer>> getLineNumberOffsetMap()
    {
        return Collections.synchronizedMap(lineNumberOffsetMap);
    }
    public synchronized Map<String, Integer> getLabelLineNumberMap()
    {
        return Collections.synchronizedMap(labelLineNumberMap);
    }
    public synchronized Map<Integer, Set<String>> getLineNumberLableMap()
    {
        return Collections.synchronizedMap(lineNumberLableMap);
    }
    public synchronized Map<String, Integer> getLabelOffsetMap()
    {
        return Collections.synchronizedMap(labelOffsetMap);
    }
    public synchronized Map<Integer, Set<String>> getOffsetLabelMap()
    {
        return Collections.synchronizedMap(offsetLabelMap);
    }
    public synchronized DebugInfo merge(DebugInfo debugInfo, int offset)
    {
        for(Entry<Integer, Integer> entry : debugInfo.offsetLineNumberMap.entrySet())
        {
            offsetLineNumberMap.put(entry.getKey() + offset, entry.getValue());
        }
        for(Entry<Integer, Set<Integer>> entry : debugInfo.lineNumberOffsetMap.entrySet())
        {
            Set<Integer> offsetSet = lineNumberOffsetMap.get(entry.getKey());
            if(offsetSet == null)
            {
                offsetSet = new LinkedHashSet<Integer>();
                lineNumberOffsetMap.put(entry.getKey(), offsetSet);
            }
            for(int oldOffset : entry.getValue())
            {
                offsetSet.add(oldOffset + offset);
            }
        }
        labelLineNumberMap.putAll(debugInfo.labelLineNumberMap);
        for(Entry<Integer, Set<String>> entry : debugInfo.lineNumberLableMap.entrySet())
        {
            Set<String> lableSet = lineNumberLableMap.get(entry.getKey());
            if(lableSet == null)
            {
                lableSet = new LinkedHashSet<String>();
                lineNumberLableMap.put(entry.getKey(), lableSet);
            }
            lableSet.addAll(entry.getValue());
        }
        for(Entry<String, Integer> entry : debugInfo.labelOffsetMap.entrySet())
        {
            labelOffsetMap.put(entry.getKey(), entry.getValue() + offset);
        }
        for(Entry<Integer, Set<String>> entry : debugInfo.offsetLabelMap.entrySet())
        {
            Set<String> lableSet = offsetLabelMap.get(entry.getKey() + offset);
            if(lableSet == null)
            {
                lableSet = new LinkedHashSet<String>();
                offsetLabelMap.put(entry.getKey() + offset, lableSet);
            }
            lableSet.addAll(entry.getValue());
        }
        return this;
    }
    @Override
    public synchronized String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("LineNumberBytecodeTable:");
        for(Entry<Integer, Set<Integer>> entry : lineNumberOffsetMap.entrySet())
        {
            for(int offset : entry.getValue())
            {
                builder.append("\n");
                builder.append("  line ");
                builder.append(entry.getKey());
                builder.append(": ");
                builder.append(offset);
            }
        }
        builder.append("\nLineNumberLableTable:");
        for(Entry<Integer, Set<String>> entry : lineNumberLableMap.entrySet())
        {
            for(String lable : entry.getValue())
            {
                builder.append("\n");
                builder.append("  line ");
                builder.append(entry.getKey());
                builder.append(": ");
                builder.append(lable);
            }
        }
        builder.append("\nBytecodeLableTable:");
        for(Entry<Integer, Set<String>> entry : offsetLabelMap.entrySet())
        {
            for(String lable : entry.getValue())
            {
                builder.append("\n");
                builder.append("  offset ");
                builder.append(entry.getKey());
                builder.append(": ");
                builder.append(lable);
            }
        }
        return builder.toString();
    }
    @Override
    public synchronized DebugInfo clone()
    {
        DebugInfo debugInfo = new DebugInfo();
        debugInfo.offsetLineNumberMap.putAll(offsetLineNumberMap);
        debugInfo.lineNumberOffsetMap.putAll(lineNumberOffsetMap);
        debugInfo.labelLineNumberMap.putAll(labelLineNumberMap);
        debugInfo.lineNumberLableMap.putAll(lineNumberLableMap);
        debugInfo.labelOffsetMap.putAll(labelOffsetMap);
        debugInfo.offsetLabelMap.putAll(offsetLabelMap);
        return debugInfo;
    }
    public void writeToNBT(NBTTagCompound debugInfoNBTTagCompound)
    {
        NBTTagList offsetLineNumberMapNBTTagList = new NBTTagList();
        for(Entry<Integer, Integer> entry : offsetLineNumberMap.entrySet())
        {
            NBTTagCompound entryNbtTagCompound = new NBTTagCompound();
            entryNbtTagCompound.setInteger("key", entry.getKey());
            entryNbtTagCompound.setInteger("value", entry.getValue());
            offsetLineNumberMapNBTTagList.appendTag(entryNbtTagCompound);
        }
        debugInfoNBTTagCompound.setTag("offsetLineNumberMap", offsetLineNumberMapNBTTagList);
        
        NBTTagList lineNumberOffsetMapNBTTagList = new NBTTagList();
        for(Entry<Integer, Set<Integer>> entry : lineNumberOffsetMap.entrySet())
        {
            NBTTagCompound entryNbtTagCompound = new NBTTagCompound();
            entryNbtTagCompound.setInteger("key", entry.getKey());
            entryNbtTagCompound.setIntArray("value", IntTools.toIntArray(entry.getValue()));
            lineNumberOffsetMapNBTTagList.appendTag(entryNbtTagCompound);
        }
        debugInfoNBTTagCompound.setTag("lineNumberOffsetMap", lineNumberOffsetMapNBTTagList);
        
        NBTTagList labelLineNumberMapNBTTagList = new NBTTagList();
        for(Entry<String, Integer> entry : labelLineNumberMap.entrySet())
        {
            NBTTagCompound entryNbtTagCompound = new NBTTagCompound();
            entryNbtTagCompound.setString("key", entry.getKey());
            entryNbtTagCompound.setInteger("value", entry.getValue());
            labelLineNumberMapNBTTagList.appendTag(entryNbtTagCompound);
        }
        debugInfoNBTTagCompound.setTag("labelLineNumberMap", labelLineNumberMapNBTTagList);
        
        NBTTagList lineNumberLableMapNBTTagList = new NBTTagList();
        for(Entry<Integer, Set<String>> entry : lineNumberLableMap.entrySet())
        {
            NBTTagCompound entryNbtTagCompound = new NBTTagCompound();
            entryNbtTagCompound.setInteger("key", entry.getKey());
            NBTTagList lableSetNBTagList = new NBTTagList();
            for(String lable : entry.getValue())
            {
                lableSetNBTagList.appendTag(new NBTTagString(lable));
            }
            entryNbtTagCompound.setTag("value", lableSetNBTagList);
            lineNumberLableMapNBTTagList.appendTag(entryNbtTagCompound);
        }
        debugInfoNBTTagCompound.setTag("lineNumberLableMap", lineNumberLableMapNBTTagList);
        
        NBTTagList labelOffsetMapNBTTagList = new NBTTagList();
        for(Entry<String, Integer> entry : labelOffsetMap.entrySet())
        {
            NBTTagCompound entryNbtTagCompound = new NBTTagCompound();
            entryNbtTagCompound.setString("key", entry.getKey());
            entryNbtTagCompound.setInteger("value", entry.getValue());
            labelOffsetMapNBTTagList.appendTag(entryNbtTagCompound);
        }
        debugInfoNBTTagCompound.setTag("labelOffsetMap", labelOffsetMapNBTTagList);
        
        NBTTagList offsetLabelMapNBTTagList = new NBTTagList();
        for(Entry<Integer, Set<String>> entry : offsetLabelMap.entrySet())
        {
            NBTTagCompound entryNbtTagCompound = new NBTTagCompound();
            entryNbtTagCompound.setInteger("key", entry.getKey());
            NBTTagList lableSetNBTagList = new NBTTagList();
            for(String lable : entry.getValue())
            {
                lableSetNBTagList.appendTag(new NBTTagString(lable));
            }
            entryNbtTagCompound.setTag("value", lableSetNBTagList);
            offsetLabelMapNBTTagList.appendTag(entryNbtTagCompound);
        }
        debugInfoNBTTagCompound.setTag("offsetLabelMap", offsetLabelMapNBTTagList);
    }
    public void readFromNBT(NBTTagCompound debugInfoNBTTagCompound)
    {
        NBTTagList offsetLineNumberMapNBTTagList = debugInfoNBTTagCompound.getTagList("offsetLineNumberMap", 10);
        for(int i = 0;i < offsetLineNumberMapNBTTagList.tagCount();i++)
        {
            NBTTagCompound entryNbtTagCompound = offsetLineNumberMapNBTTagList.getCompoundTagAt(i);
            offsetLineNumberMap.put(entryNbtTagCompound.getInteger("key"), entryNbtTagCompound.getInteger("value"));
        }
        
        NBTTagList lineNumberOffsetMapNBTTagList = debugInfoNBTTagCompound.getTagList("lineNumberOffsetMap", 10);
        for(int i = 0;i < lineNumberOffsetMapNBTTagList.tagCount();i++)
        {
            NBTTagCompound entryNbtTagCompound = lineNumberOffsetMapNBTTagList.getCompoundTagAt(i);
            lineNumberOffsetMap.put(entryNbtTagCompound.getInteger("key"), IntTools.addToIntegerCollection(new LinkedHashSet<Integer>(), entryNbtTagCompound.getIntArray("value")));
        }
        
        NBTTagList labelLineNumberMapNBTTagList = debugInfoNBTTagCompound.getTagList("labelLineNumberMap", 10);
        for(int i = 0;i < labelLineNumberMapNBTTagList.tagCount();i++)
        {
            NBTTagCompound entryNbtTagCompound = labelLineNumberMapNBTTagList.getCompoundTagAt(i);
            labelLineNumberMap.put(entryNbtTagCompound.getString("key"), entryNbtTagCompound.getInteger("value"));
        }
        
        NBTTagList lineNumberLableMapNBTTagList = debugInfoNBTTagCompound.getTagList("lineNumberLableMap", 10);
        for(int i = 0;i < lineNumberLableMapNBTTagList.tagCount();i++)
        {
            NBTTagCompound entryNbtTagCompound = lineNumberLableMapNBTTagList.getCompoundTagAt(i);
            Set<String> lableSet = new LinkedHashSet<String>();
            NBTTagList lableSetNBTTagList = entryNbtTagCompound.getTagList("value", 8);
            for(int j = 0;j < lableSetNBTTagList.tagCount();j++)
            {
                lableSet.add(lableSetNBTTagList.getStringTagAt(j));
            }
            lineNumberLableMap.put(entryNbtTagCompound.getInteger("key"), lableSet);
        }
        
        NBTTagList labelOffsetMapNBTTagList = debugInfoNBTTagCompound.getTagList("labelOffsetMap", 10);
        for(int i = 0;i < labelOffsetMapNBTTagList.tagCount();i++)
        {
            NBTTagCompound entryNbtTagCompound = labelOffsetMapNBTTagList.getCompoundTagAt(i);
            labelOffsetMap.put(entryNbtTagCompound.getString("key"), entryNbtTagCompound.getInteger("value"));
        }
        
        NBTTagList offsetLableMapNBTTagList = debugInfoNBTTagCompound.getTagList("offsetLabelMap", 10);
        for(int i = 0;i < offsetLableMapNBTTagList.tagCount();i++)
        {
            NBTTagCompound entryNbtTagCompound = offsetLableMapNBTTagList.getCompoundTagAt(i);
            Set<String> lableSet = new LinkedHashSet<String>();
            NBTTagList lableSetNBTTagList = entryNbtTagCompound.getTagList("value", 8);
            for(int j = 0;j < lableSetNBTTagList.tagCount();j++)
            {
                lableSet.add(lableSetNBTTagList.getStringTagAt(j));
            }
            offsetLabelMap.put(entryNbtTagCompound.getInteger("key"), lableSet);
        }
    }
}
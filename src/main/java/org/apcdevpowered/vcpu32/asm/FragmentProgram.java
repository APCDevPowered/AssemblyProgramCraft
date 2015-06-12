package org.apcdevpowered.vcpu32.asm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apcdevpowered.util.array.DynamicSparseArray;
import org.apcdevpowered.util.integer.IntTools;
import org.apcdevpowered.util.string.StringTools;
import org.apcdevpowered.vcpu32.asm.Assembler.CompileContext;

public class FragmentProgram
{
    private DynamicSparseArray<Integer> slots = new DynamicSparseArray<Integer>();
    //键为标签文本内容，值为标签所指示的字节码帧位置。
    private Map<String, Integer> labelDefMap  = new HashMap<String, Integer>();
    //键为标签文本内容，值为ArrayList数组，包含所有申请标签的字节码的帧位置。
    private Map<String, List<Integer>> labelRequestListMap  = new HashMap<String, List<Integer>>();
    //键为文本内容，值为ArrayList数组，包含所有申请此字符串字节码的帧位置。
    private Map<OffsetWithDataPackage<String>, List<Integer>> stringRequestListMap  = new HashMap<OffsetWithDataPackage<String>, List<Integer>>();
    //键为数据内容，值为申请此其他数据字节码的帧位置
    private Map<OffsetWithDataPackage<DynamicSparseArray<Integer>>, Integer> dataRequestMap = new HashMap<OffsetWithDataPackage<DynamicSparseArray<Integer>>, Integer>();
    //请求静态储存区偏移量的位置
    private List<Integer> staticOffsetRequestList = new ArrayList<Integer>();
    
    //调试信息
    private DebugInfo debugInfo = new DebugInfo();
    
    public FragmentProgram()
    {
        
    }
    public FragmentProgram(DynamicSparseArray<Integer> slots)
    {
        this.slots.addAll(slots);
    }
    
    public DynamicSparseArray<Integer> getSlots()
    {
        return slots;
    }
    public DebugInfo getDebugInfo()
    {
        return debugInfo;
    }
    /**
     * 复制并偏移目标程序的额外数据，并将目标片段程序追加到此片段程序末尾、合并调试信息。
     * 
     * @param program 需要和此片段程序合成的片段程序
     * @return 此片段程序
     */
    public FragmentProgram merge(FragmentProgram program) throws MergeException
    {
        int offset = slots.length();
        slots.addAll(program.slots);
        for(Entry<String, Integer> entry : program.labelDefMap.entrySet())
        {
            try
            {
                addLabel(entry.getKey(), entry.getValue() + offset);
            }
            catch (LabelConflictException e)
            {
                throw new MergeException(MergeException.LABEL_CONFLICT, program.debugInfo.getLineNumberByLabel(entry.getKey()), e);
            }
        }
        for(Entry<String, List<Integer>> entry : program.labelRequestListMap.entrySet())
        {
            for(int index : entry.getValue())
            {
                addLabelRequest(entry.getKey(), index + offset);
            }
        }
        for(Entry<OffsetWithDataPackage<String>, List<Integer>> entry : program.stringRequestListMap.entrySet())
        {
            for(int index : entry.getValue())
            {
                addStringRequest(entry.getKey(), index == -1 ? -1 : index + offset);
            }
        }
        for(Entry<OffsetWithDataPackage<DynamicSparseArray<Integer>>, Integer> entry : program.dataRequestMap.entrySet())
        {
            addDataRequest(entry.getKey(), entry.getValue() == -1 ? -1 : entry.getValue() + offset);
        }
        for(Integer requestOffset : program.staticOffsetRequestList)
        {
            addStaticOffsetRequest(requestOffset + offset);
        }
        debugInfo.merge(program.debugInfo, offset);
        return this;
    }
    public static class MergeException extends Exception
    {
        private static final long serialVersionUID = -5136747152067587623L;

        public static final int LABEL_CONFLICT = 1;
        
        public final int type;
        private int lineNumber = -1;
        
        protected MergeException(int type)
        {
            this.type = type;
        }
        protected MergeException(int type, int lineNumber)
        {
            this.type = type;
            this.lineNumber = lineNumber;
        }
        protected MergeException(int type, int lineNumber, Throwable e)
        {
            super(e);
            this.type = type;
            this.lineNumber = lineNumber;
        }
        protected MergeException(int type, Throwable e)
        {
            super(e);
            this.type = type;
        }
        
        public int getLineNumber()
        {
            return lineNumber;
        }
    }
    public static class LabelConflictException extends Exception
    {
        private static final long serialVersionUID = 2107183351257563791L;
        
        private String label;
        private int lineNumber = -1;
        
        protected LabelConflictException(String label)
        {
            this.label = label;
        }
        protected LabelConflictException(String label, int lineNumber)
        {
            this.label = label;
            this.lineNumber = lineNumber;
        }
        
        public String getLabel()
        {
            return label;
        }
        public int getLineNumber()
        {
            return lineNumber;
        }
    }
    public void addLabel(String label, int index) throws LabelConflictException
    {
        if(index < 0)
        {
            throw new IllegalArgumentException();
        }
        if(labelDefMap.containsKey(label))
        {
            int lineNumber = debugInfo.getLineNumberByLabel(label);
            if(lineNumber == -1)
            {
                throw new LabelConflictException(label);
            }
            else
            {
                throw new LabelConflictException(label, lineNumber);
            }
        }
        labelDefMap.put(label, index);
    }
    public void addLabelRequest(String label, int index)
    {
        if(index < 0)
        {
            throw new IllegalArgumentException();
        }
        List<Integer> labelRequestList = labelRequestListMap.get(label);
        if(labelRequestList == null)
        {
            labelRequestList = new ArrayList<Integer>();
            labelRequestListMap.put(label, labelRequestList);
        }
        labelRequestList.add(index);
    }
    public void addStringRequest(OffsetWithDataPackage<String> string, int index)
    {
        if(index < -1)
        {
            throw new IllegalArgumentException();
        }
        List<Integer> stringRequestList = stringRequestListMap.get(string);
        if(stringRequestList == null)
        {
            stringRequestList = new ArrayList<Integer>();
            stringRequestListMap.put(string, stringRequestList);
        }
        if(index != -1)
        {
            stringRequestList.add(index);
        }
    }
    public void addDataRequest(OffsetWithDataPackage<DynamicSparseArray<Integer>> data, int index)
    {
        if(index < -1)
        {
            throw new IllegalArgumentException();
        }
        dataRequestMap.put(data.clone(), index);
    }
    public void addStaticOffsetRequest(int index)
    {
        if(index < -1)
        {
            throw new IllegalArgumentException();
        }
        staticOffsetRequestList.add(index);
    }
    /**
     * 连接程序片段为完整程序包。
     * 
     * @param context 编译上下文
     * @return 此程序包
     */
    public ProgramPackage link(CompileContext context) throws LinkException
    {
        context.setStaticMemStart(slots.length());
        if(context.isBios())
        {
            context.setRuntimeStaticRAMSlot(context.getStartRAM() + slots.length() + CompileContext.BIOS_RUNTIME_STATIC_RAM_SLOT_OFFSET);
        }
        ProgramPackage programPackage = new ProgramPackage();
        programPackage.programName = context.getProgramName();
        programPackage.startRAM = context.getStartRAM();
        programPackage.startStaticRAM = context.getRuntimeStaticRAMSlot();
        programPackage.isBIOS = context.isBios();
        programPackage.programEnd = slots.length();
        DynamicSparseArray<Integer> programData = new DynamicSparseArray<Integer>(slots);
        try
        {
            linkLabel(programData, context);
        }
        catch (LabelMissingException e)
        {
            throw new LinkException(LinkException.LABEL_MISSING, e.getLineNumber(), e);
        }
        linkString(programData, context);
        linkData(programData, context);
        linkStaticOffset(programData, context);
        programPackage.staticRAMEnd = programData.length();
        programPackage.data = IntTools.toIntArray(programData.toArray(new Integer[programData.length()]));
        programPackage.debugInfo = debugInfo.clone();
        return programPackage;
    }
    /**
     * 连接程序标签。
     * 
     * @param programData 程序数据
     * @param context 编译上下文
     */
    private void linkLabel(DynamicSparseArray<Integer> programData, CompileContext context) throws LabelMissingException
    {
        for(Entry<String, List<Integer>> entry : labelRequestListMap.entrySet())
        {
            String label = entry.getKey();
            if(!labelDefMap.containsKey(label) && !entry.getValue().isEmpty())
            {
                int lineNumber = debugInfo.getLineNumberByOffset(entry.getValue().get(0));
                throw new LabelMissingException(label, lineNumber);
            }
            int labelSlot = labelDefMap.get(label);
            for(int labelRequestSlot : entry.getValue())
            {
                programData.set(labelRequestSlot, labelSlot + context.getStartRAM());
            }
        }
    }
    /**
     * 连接程序字符串。
     * 
     * @param programData 程序数据
     * @param context 编译上下文
     */
    private void linkString(DynamicSparseArray<Integer> programData, CompileContext context)
    {
        for(Entry<OffsetWithDataPackage<String>, List<Integer>> entry : stringRequestListMap.entrySet())
        {
            String string = entry.getKey().data;
            int offset = entry.getKey().offset;
            Integer[] stringData = StringTools.writeStringToIntegerArray(string);
            List<Integer> slots = entry.getValue();
            int stringSlot = 0;
            if(offset == -1)
            {
                offset = context.getStaticMemOffsetFromStart();
                stringSlot = context.getAndIncreaseStaticMemOffset(stringData.length);
            }
            else
            {
                stringSlot = context.getStaticMemStart() + offset;
            }
            programData.copyData(stringData, 0, stringSlot, stringData.length);
            for(int slot : slots)
            {
                programData.set(slot, context.getRuntimeStaticRAMSlot() + offset);
            }
        }
    }
    /** 连接程序其他数据。
    * 
    * @param programData 程序数据
    * @param context 编译上下文
    */
    private void linkData(DynamicSparseArray<Integer> programData, CompileContext context)
    {
        for(Entry<OffsetWithDataPackage<DynamicSparseArray<Integer>>, Integer> entry : dataRequestMap.entrySet())
        {
            int offset = entry.getKey().offset;
            DynamicSparseArray<Integer> data = entry.getKey().data;
            int dataSlot = 0;
            if(offset == -1)
            {
                offset = context.getStaticMemOffsetFromStart();
                dataSlot = context.getAndIncreaseStaticMemOffset(data.length());
            }
            else
            {
                dataSlot = context.getStaticMemStart() + offset;
            }
            programData.copyData(data, 0, dataSlot, data.length());
            if(entry.getValue() != -1)
            {
                programData.set(entry.getValue(), context.getRuntimeStaticRAMSlot() + offset);
            }
        }
    }
    /** 连接程序静态储存区请求。
     * 
     * @param programData 程序数据
     * @param context 编译上下文
     */
     private void linkStaticOffset(DynamicSparseArray<Integer> programData, CompileContext context)
     {
         for(Integer requestOffset : staticOffsetRequestList)
         {
             programData.set(requestOffset, context.getRuntimeStaticRAMSlot());
         }
     }
    public static class LinkException extends Exception
    {
        private static final long serialVersionUID = 7947440187860528159L;

        public static final int UNKNOWN = 0;
        public static final int LABEL_MISSING = 1;
        
        public final int type;
        private int lineNumber = -1;
        
        protected LinkException()
        {
            this.type = UNKNOWN;
        }
        protected LinkException(int type)
        {
            this.type = type;
        }
        protected LinkException(int type, int lineNumber)
        {
            this.type = type;
            this.lineNumber = lineNumber;
        }
        protected LinkException(int type, int lineNumber, Throwable e)
        {
            super(e);
            this.type = type;
            this.lineNumber = lineNumber;
        }
        @Override
        public String getMessage()
        {
            switch (type)
            {
                case UNKNOWN:
                {
                    return "Unknown error occurred during link.";
                }
                case LABEL_MISSING:
                {
                    StringBuilder builder = new StringBuilder();
                    builder.append("Label missing error occurred during link.");
                    if(lineNumber > 0)
                    {
                        builder.append("\n\tat line: ");
                        builder.append(lineNumber);
                    }
                    if(getCause() != null)
                    {
                        LabelMissingException e = (LabelMissingException) getCause();
                        builder.append("\n\tLabel \"");
                        builder.append(e.getLabel());
                        builder.append('"');
                    }
                    return builder.toString();
                }
                default:
                    return "Unknown error occurred during link.";
            }
        }
        public int getLineNumber()
        {
            return lineNumber;
        }
    }
    public static class LabelMissingException extends Exception
    {
        private static final long serialVersionUID = 1163459401481291121L;

        private final String label;
        private int lineNumber = -1;
        
        protected LabelMissingException(String label, int lineNumber)
        {
            this.label = label;
            this.lineNumber = lineNumber;
        }
        
        public String getLabel()
        {
            return label;
        }
        public int getLineNumber()
        {
            return lineNumber;
        }
    }
}
package org.apcdevpowered.vcpu32.disasm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apcdevpowered.util.IntUtils;
import org.apcdevpowered.util.SingleEntry;
import org.apcdevpowered.util.StringUtils;
import org.apcdevpowered.vcpu32.asm.DebugInfo;
import org.apcdevpowered.vcpu32.asm.OperatorsManager;
import org.apcdevpowered.vcpu32.asm.ProgramPackage;
import org.apcdevpowered.vcpu32.asm.DatatypeManager.Register;
import org.apcdevpowered.vcpu32.asm.OperatorsManager.Operator;
import org.apcdevpowered.vcpu32.disasm.Disassembler.AbstractSyntaxTree.Construct;
import org.apcdevpowered.vcpu32.disasm.Disassembler.AbstractSyntaxTree.DummyInstruction;
import org.apcdevpowered.vcpu32.disasm.Disassembler.AbstractSyntaxTree.Instruction;
import org.apcdevpowered.vcpu32.disasm.Disassembler.AbstractSyntaxTree.Label;
import org.apcdevpowered.vcpu32.disasm.Disassembler.DisassembleContext.IntermediateData;
import org.apcdevpowered.vcpu32.disasm.Disassembler.DisassembleContext.IntermediateData.InstructionAnalyzeContext;

public class Disassembler
{
    public static class DisassembleContext
    {
        private final AbstractSyntaxTree abstractSyntaxTree;
        private final int[] programByteCode;
        private final int[] staticData;
        private final boolean labelAnalyze;
        private final boolean staticDataAnalyze;
        private final IntermediateData intermediateData;
        
        public DisassembleContext(AbstractSyntaxTree abstractSyntaxTree, int[] programByteCode, int[] staticData, boolean labelAnalyze, boolean staticDataAnalyze)
        {
            this.abstractSyntaxTree = abstractSyntaxTree;
            this.programByteCode = programByteCode;
            this.staticData = staticData;
            this.labelAnalyze = labelAnalyze;
            this.staticDataAnalyze = staticDataAnalyze;
            this.intermediateData = new IntermediateData();
        }
        public AbstractSyntaxTree getAbstractSyntaxTree()
        {
            return abstractSyntaxTree;
        }
        public int[] getProgramByteCode()
        {
            return programByteCode;
        }
        public int[] getStaticData()
        {
            return staticData;
        }
        public boolean isLabelAnalyze()
        {
            return labelAnalyze;
        }
        public boolean isStaticDataAnalyze()
        {
            return staticDataAnalyze;
        }
        public IntermediateData getIntermediateData()
        {
            return intermediateData;
        }
        
        protected static class IntermediateData
        {
            private final InstructionAnalyzeContext instructionAnalyzeContext;
            private final List<SingleEntry<Integer, Integer>> readedStaticDataList;
            private final List<SingleEntry<String, Integer>> staticStringDataList;
            
            protected IntermediateData()
            {
                instructionAnalyzeContext = new InstructionAnalyzeContext();
                readedStaticDataList = new ArrayList<SingleEntry<Integer, Integer>>();
                staticStringDataList = new ArrayList<SingleEntry<String, Integer>>();
            }
            protected List<SingleEntry<Integer, Integer>> getReadedStaticDataList()
            {
                return readedStaticDataList;
            }
            protected List<SingleEntry<String, Integer>> getStaticStringDataList()
            {
                return staticStringDataList;
            }
            protected InstructionAnalyzeContext getInstructionAnalyzeContext()
            {
                return instructionAnalyzeContext;
            }
            
            protected static class InstructionAnalyzeContext
            {
                private int offset;
                private int opcode;
                private int parCount;
                private int byteCode;
                private String byteCodeName;
                private int[] parsType;
                private int[] parsData;
                private String[] parsValue;
                
                protected InstructionAnalyzeContext()
                {
                    resetContext();
                }
                protected void resetContext()
                {
                    offset = -1;
                    opcode = -1;
                    parCount = -1;
                    byteCode = -1;
                    byteCodeName = null;
                    parsType = null;
                    parsData = null;
                    parsValue = null;
                }
                protected int getOffset()
                {
                    return offset;
                }
                protected void setOffset(int offset)
                {
                    this.offset = offset;
                }
                protected int getOpcode()
                {
                    return opcode;
                }
                protected void setOpcode(int opcode)
                {
                    this.opcode = opcode;
                }
                protected int getParCount()
                {
                    return parCount;
                }
                protected void setParCount(int parCount)
                {
                    this.parCount = parCount;
                }
                protected int getByteCode()
                {
                    return byteCode;
                }
                protected void setByteCode(int byteCode)
                {
                    this.byteCode = byteCode;
                }
                protected String getByteCodeName()
                {
                    return byteCodeName;
                }
                protected void setByteCodeName(String byteCodeName)
                {
                    this.byteCodeName = byteCodeName;
                }
                protected int[] getParsType()
                {
                    return parsType;
                }
                protected void setParsType(int[] parsType)
                {
                    this.parsType = parsType;
                }
                protected int[] getParsData()
                {
                    return parsData;
                }
                protected void setParsData(int[] parsData)
                {
                    this.parsData = parsData;
                }
                protected String[] getParsValue()
                {
                    return parsValue;
                }
                protected void setParsValue(String[] parsValue)
                {
                    this.parsValue = parsValue;
                }
            }
        }
    }
    public static class AbstractSyntaxTree
    {
        public abstract class Construct
        {
            public int lineNumber;
            
            public abstract String getAssemblyString();
            public abstract int getUsedBytes();
            public int getLineNumber()
            {
                return lineNumber;
            }
        }
        public class DummyInstruction extends Instruction
        {
            public int getUsedBytes()
            {
                return 0;
            }
        }
        public class Instruction extends Construct
        {
            public String name = "";
            public int parCount;
            public String[] parsValue;
            public int[] parsData;
            public int[] parsType;
            
            public String getAssemblyString()
            {
                String assemblyString = name + (parCount == 0 ? "" : " ");
                boolean isFirst = true;
                for (int i = 0; i < parCount; i++)
                {
                    String par = parsValue[i];
                    if (isFirst)
                    {
                        isFirst = false;
                    }
                    else
                    {
                        assemblyString += ",";
                    }
                    assemblyString += par;
                }
                return assemblyString;
            }
            public int getUsedBytes()
            {
                return 1 + parCount;
            }
        }
        public class Label extends Construct
        {
            public String name = "";
            
            public String getAssemblyString()
            {
                return ":" + name;
            }
            public int getUsedBytes()
            {
                return 0;
            }
        }
        
        public ArrayList<Construct> abstractSyntaxTree = new ArrayList<Construct>();
        public int startRAM;
        public int startStaticRAM;
        public DebugInfo debugInfo;
        
        public void addAtOffset(int offset, Construct construct)
        {
            int astIndex = 0;
            for (int currentOffset = 0; currentOffset <= offset && astIndex < abstractSyntaxTree.size();)
            {
                currentOffset += abstractSyntaxTree.get(astIndex).getUsedBytes();
                if (currentOffset <= offset)
                {
                    astIndex++;
                }
            }
            abstractSyntaxTree.add(astIndex, construct);
        }
        public boolean isBetweenInsn(int offset)
        {
            if (offset < startRAM)
            {
                return false;
            }
            if (offset == startRAM)
            {
                return true;
            }
            int currentIdx = 0;
            for (int astIdx = 0; astIdx < abstractSyntaxTree.size(); astIdx++)
            {
                currentIdx += abstractSyntaxTree.get(astIdx).getUsedBytes();
                if (currentIdx > offset - startRAM)
                {
                    return false;
                }
                if (currentIdx == offset - startRAM)
                {
                    return true;
                }
            }
            return false;
        }
        public String getAssemblyCode()
        {
            StringBuilder builder = new StringBuilder();
            Iterator<Construct> iterator = abstractSyntaxTree.iterator();
            int line = 1;
            while (iterator.hasNext())
            {
                Construct construct = iterator.next();
                if (builder.length() != 0)
                {
                    if (debugInfo == null)
                    {
                        builder.append('\n');
                        line++;
                    }
                    else
                    {
                        if (construct.getLineNumber() < 0)
                        {
                            builder.append('\n');
                            line++;
                        }
                        else
                        {
                            if (construct.getLineNumber() > line)
                            {
                                while (construct.getLineNumber() > line)
                                {
                                    builder.append('\n');
                                    line++;
                                }
                            }
                            else
                            {
                                builder.append(' ');
                            }
                        }
                    }
                }
                builder.append(construct.getAssemblyString());
            }
            return builder.toString();
        }
    }
    
    public static String decompile(ProgramPackage programPackage)
    {
        return decompile(programPackage, true, true);
    }
    public static String decompile(ProgramPackage programPackage, boolean labelAnalyze, boolean staticDataAnalyze)
    {
        AbstractSyntaxTree abstractSyntaxTree = new AbstractSyntaxTree();
        abstractSyntaxTree.startRAM = programPackage.startRAM;
        abstractSyntaxTree.startStaticRAM = programPackage.startStaticRAM;
        abstractSyntaxTree.debugInfo = programPackage.debugInfo.clone();
        int[] programByteCode = new int[programPackage.programEnd];
        System.arraycopy(programPackage.data, 0, programByteCode, 0, programPackage.programEnd);
        int[] staticData = new int[programPackage.staticRAMEnd - programPackage.programEnd];
        System.arraycopy(programPackage.data, programPackage.programEnd, staticData, 0, programPackage.staticRAMEnd - programPackage.programEnd);
        DisassembleContext context = new DisassembleContext(abstractSyntaxTree, programByteCode, staticData, labelAnalyze, staticDataAnalyze);
        return decompile(context);
    }
    public static String decompile(DisassembleContext context)
    {
        int pointer = 0;
        int[] programByteCode = context.getProgramByteCode();
        InstructionAnalyzeContext instructionAnalyzeContext = context.getIntermediateData().getInstructionAnalyzeContext();
        while (pointer < programByteCode.length)
        {
            instructionAnalyzeContext.resetContext();
            instructionAnalyzeContext.setOffset(pointer);
            analyzeInstruction(context);
            getParsType(context);
            getParsData(context);
            getParsValue(context);
            String byteCodeName = instructionAnalyzeContext.getByteCodeName();
            int parCount = instructionAnalyzeContext.getParCount();
            int[] parsType = instructionAnalyzeContext.getParsType();
            int[] parsData = instructionAnalyzeContext.getParsData();
            String[] parsValue = instructionAnalyzeContext.getParsValue();
            Instruction instruction = context.getAbstractSyntaxTree().new Instruction();
            instruction.name = byteCodeName;
            instruction.parCount = parCount;
            instruction.parsValue = parsValue;
            instruction.parsData = parsData;
            instruction.parsType = parsType;
            instruction.lineNumber = context.getAbstractSyntaxTree().debugInfo.getLineNumberByOffset(pointer);
            context.getAbstractSyntaxTree().abstractSyntaxTree.add(instruction);
            pointer += (1 + parCount);
        }
        if (context.isLabelAnalyze())
        {
            runLabelAnalyze(context);
        }
        if (context.isStaticDataAnalyze())
        {
            runStaticDataAnalyze(context);
        }
        return context.getAbstractSyntaxTree().getAssemblyCode();
    }
    private static void analyzeInstruction(DisassembleContext context)
    {
        int[] programByteCode = context.getProgramByteCode();
        InstructionAnalyzeContext instructionAnalyzeContext = context.getIntermediateData().getInstructionAnalyzeContext();
        int offset = instructionAnalyzeContext.getOffset();
        int opcode = programByteCode[offset];
        int parCount = getParCount(opcode);
        int byteCode = getByteCode(opcode);
        String byteCodeName = getInsnName(byteCode);
        instructionAnalyzeContext.setOpcode(opcode);
        instructionAnalyzeContext.setParCount(parCount);
        instructionAnalyzeContext.setByteCode(byteCode);
        instructionAnalyzeContext.setByteCodeName(byteCodeName);
    }
    private static void runStaticDataAnalyze(DisassembleContext context)
    {
        AbstractSyntaxTree abstractSyntaxTree = context.getAbstractSyntaxTree();
        int[] staticData = context.getStaticData();
        IntermediateData intermediateData = context.getIntermediateData();
        List<SingleEntry<Integer, Integer>> readedStaticDataList = intermediateData.getReadedStaticDataList();
        List<SingleEntry<String, Integer>> staticStringDataList = intermediateData.getStaticStringDataList();
        nextData:
        for (int i = 0; i < staticData.length; i++)
        {
            int data = staticData[i];
            for (SingleEntry<Integer, Integer> entry : readedStaticDataList)
            {
                int from = entry.getKey();
                int to = entry.getKey() + entry.getValue() - 1;
                if (i >= from && i <= to)
                {
                    continue nextData;
                }
            }
            DummyInstruction instruction = abstractSyntaxTree.new DummyInstruction();
            instruction.name = "DATAT";
            instruction.parCount = 2;
            instruction.parsValue = new String[instruction.parCount];
            instruction.parsValue[0] = Integer.valueOf(i).toString();
            instruction.parsValue[1] = "0x" + IntUtils.toHexUintString(data).toUpperCase();
            instruction.lineNumber = -1;
            abstractSyntaxTree.abstractSyntaxTree.add(instruction);
        }
        for (SingleEntry<String, Integer> entry : staticStringDataList)
        {
            DummyInstruction instruction = abstractSyntaxTree.new DummyInstruction();
            instruction.name = "STRAT";
            instruction.parCount = 2;
            instruction.parsValue = new String[instruction.parCount];
            instruction.parsValue[0] = Integer.valueOf(entry.getValue()).toString();
            instruction.parsValue[1] = entry.getKey();
            instruction.lineNumber = -1;
            abstractSyntaxTree.abstractSyntaxTree.add(instruction);
        }
    }
    private static void runLabelAnalyze(DisassembleContext context)
    {
        AbstractSyntaxTree abstractSyntaxTree = context.getAbstractSyntaxTree();
        int autoLabelID = 0;
        Map<Integer, Set<String>> offsetLabelMap = abstractSyntaxTree.debugInfo.getOffsetLabelMap();
        Map<String, Integer> labelOffsetMap = abstractSyntaxTree.debugInfo.getLabelOffsetMap();
        for (Construct construct : abstractSyntaxTree.abstractSyntaxTree)
        {
            if (construct instanceof Instruction)
            {
                Instruction instruction = (Instruction) construct;
                if (instruction.name.equals("JSR") || instruction.name.equals("CALL") || instruction.name.equals("CRT"))
                {
                    if (instruction.parsType[0] == 2)
                    {
                        if (abstractSyntaxTree.isBetweenInsn(instruction.parsData[0]))
                        {
                            int parValue = instruction.parsData[0];
                            int offset = parValue - abstractSyntaxTree.startRAM;
                            Set<String> labels;
                            if (!offsetLabelMap.containsKey(offset))
                            {
                                offsetLabelMap.put(offset, labels = new LinkedHashSet<String>());
                            }
                            else
                            {
                                labels = offsetLabelMap.get(offset);
                            }
                            if (labels.size() == 0)
                            {
                                String labelName;
                                while (labelOffsetMap.containsKey(labelName = ("L" + autoLabelID++)))
                                {
                                }
                                labels.add(labelName);
                                labelOffsetMap.put(labelName, offset);
                                instruction.parsValue[0] = '[' + labelName + ']';
                            }
                            else
                            {
                                instruction.parsValue[0] = '[' + labels.iterator().next() + ']';
                            }
                        }
                    }
                }
            }
        }
        for (Entry<Integer, Set<String>> entry : offsetLabelMap.entrySet())
        {
            for (String labelName : entry.getValue())
            {
                Label label = abstractSyntaxTree.new Label();
                label.name = labelName;
                label.lineNumber = abstractSyntaxTree.debugInfo.getLineNumberByLabel(labelName);
                abstractSyntaxTree.addAtOffset(entry.getKey(), label);
            }
        }
    }
    private static String getInsnName(int insnData)
    {
        Operator operator = OperatorsManager.fromInsnData(insnData);
        if (operator == null)
        {
            return null;
        }
        return operator.getImage();
    }
    private static void getParsValue(DisassembleContext context)
    {
        AbstractSyntaxTree abstractSyntaxTree = context.getAbstractSyntaxTree();
        int[] staticData = context.getStaticData();
        IntermediateData intermediateData = context.getIntermediateData();
        List<SingleEntry<Integer, Integer>> readedStaticDataList = intermediateData.getReadedStaticDataList();
        List<SingleEntry<String, Integer>> staticStringDataList = intermediateData.getStaticStringDataList();
        InstructionAnalyzeContext instructionAnalyzeContext = intermediateData.getInstructionAnalyzeContext();
        int parCount = instructionAnalyzeContext.getParCount();
        int[] parsType = instructionAnalyzeContext.getParsType();
        int[] parsData = instructionAnalyzeContext.getParsData();
        String[] parsValue = new String[parCount];
        for (int i = 0; i < parCount; i++)
        {
            int parType = parsType[i];
            int parData = parsData[i];
            if (parType == 1) // 访问寄存器
            {
                parsValue[i] = Register.fromData(parData).getImage();
            }
            else if (parType == 2) // 直接寻址访问内存
            {
                parsValue[i] = "[" + parData + "]";
            }
            else if (parType == 3) // 数值
            {
                parsValue[i] = String.valueOf(parData);
            }
            else if (parType == 4) // 字符串
            {
                String sourceString;
                try
                {
                    sourceString = StringUtils.readIntArrayToString(staticData, parData - abstractSyntaxTree.startStaticRAM);
                }
                catch (Exception e)
                {
                    parsValue[i] = String.valueOf(parData);
                    return;
                }
                StringBuilder builder = new StringBuilder();
                builder.append("\"");
                int codePointCount = sourceString.codePointCount(0, sourceString.length());
                for (int j = 0; j < codePointCount; j++)
                {
                    int codePoint = sourceString.codePointAt(j);
                    if (codePoint == '\b')
                    {
                        builder.append("\\b");
                    }
                    else if (codePoint == '\f')
                    {
                        builder.append("\\f");
                    }
                    else if (codePoint == '\n')
                    {
                        builder.append("\\n");
                    }
                    else if (codePoint == '\r')
                    {
                        builder.append("\\r");
                    }
                    else if (codePoint == '\t')
                    {
                        builder.append("\\t");
                    }
                    else if (codePoint == '"')
                    {
                        builder.append("\\\"");
                    }
                    else if (codePoint == '\'')
                    {
                        builder.append("\\\'");
                    }
                    else if (codePoint == '\\')
                    {
                        builder.append("\\\\");
                    }
                    else if (isCommonChar(codePoint))
                    {
                        builder.appendCodePoint(codePoint);
                    }
                    else
                    {
                        char[] chars = Character.toChars(codePoint);
                        for (char c : chars)
                        {
                            builder.append("\\u");
                            String charUnicodeString = IntUtils.toHexUintString(c);
                            int fix = 4 - charUnicodeString.length();
                            if (fix == 0)
                            {
                                builder.append(charUnicodeString);
                            }
                            else if (fix < 0)
                            {
                                builder.append(charUnicodeString.substring(charUnicodeString.length() - 4, charUnicodeString.length()));
                            }
                            else
                            {
                                for (int k = 0; k < fix; k++)
                                {
                                    builder.append("0");
                                }
                                builder.append(charUnicodeString);
                            }
                        }
                    }
                }
                builder.append("\"");
                int strDataLength = ((staticData[parData - abstractSyntaxTree.startStaticRAM] % 2 == 1) ? (staticData[parData - abstractSyntaxTree.startStaticRAM] / 2 + 1) : (staticData[parData - abstractSyntaxTree.startStaticRAM] / 2)) + 1;
                readedStaticDataList.add(new SingleEntry<Integer, Integer>(parData - abstractSyntaxTree.startStaticRAM, strDataLength));
                staticStringDataList.add(new SingleEntry<String, Integer>(builder.toString(), parData - abstractSyntaxTree.startStaticRAM));
                parsValue[i] = '[' + Integer.valueOf(parData).toString() + ']';
            }
            else if (parType == 5) // 用寄存器值寻址访问内存
            {
                parsValue[i] = '[' + Register.fromData(parData).getImage() + ']';
            }
        }
        instructionAnalyzeContext.setParsValue(parsValue);
    }
    private static boolean isCommonChar(int codePoint)
    {
        switch (Character.getType(codePoint))
        {
            case Character.UPPERCASE_LETTER:
            case Character.LOWERCASE_LETTER:
            case Character.TITLECASE_LETTER:
            case Character.MODIFIER_LETTER:
            case Character.OTHER_LETTER:
            case Character.DECIMAL_DIGIT_NUMBER:
            case Character.LETTER_NUMBER:
            case Character.OTHER_NUMBER:
            case Character.SPACE_SEPARATOR:
            case Character.LINE_SEPARATOR:
            case Character.PARAGRAPH_SEPARATOR:
            case Character.DASH_PUNCTUATION:
            case Character.START_PUNCTUATION:
            case Character.END_PUNCTUATION:
            case Character.CONNECTOR_PUNCTUATION:
            case Character.OTHER_PUNCTUATION:
            case Character.MATH_SYMBOL:
            case Character.CURRENCY_SYMBOL:
            case Character.MODIFIER_SYMBOL:
            case Character.OTHER_SYMBOL:
            case Character.INITIAL_QUOTE_PUNCTUATION:
            case Character.FINAL_QUOTE_PUNCTUATION:
                return true;
            default:
                return false;
        }
    }
    private static int getByteCode(int opcode)
    {
        return opcode & 0xFFF;
    }
    private static int getParCount(int opcode)
    {
        int par1 = (opcode >> 29) & 7;
        int par2 = (opcode >> 26) & 7;
        int par3 = (opcode >> 23) & 7;
        int par4 = (opcode >> 20) & 7;
        int parCount = 0;
        if (par1 != 0)
        {
            parCount++;
        }
        if (par2 != 0)
        {
            parCount++;
        }
        if (par3 != 0)
        {
            parCount++;
        }
        if (par4 != 0)
        {
            parCount++;
        }
        return parCount;
    }
    private static void getParsData(DisassembleContext context)
    {
        InstructionAnalyzeContext instructionAnalyzeContext = context.getIntermediateData().getInstructionAnalyzeContext();
        int offset = instructionAnalyzeContext.getOffset();
        int parCount = instructionAnalyzeContext.getParCount();
        int[] parsData = new int[parCount];
        int idx = offset + 1;
        int[] programByteCode = context.getProgramByteCode();
        for (int i = 0; i < parCount; i++)
        {
            parsData[i] = programByteCode[idx + i];
        }
        instructionAnalyzeContext.setParsData(parsData);
    }
    private static void getParsType(DisassembleContext context)
    {
        InstructionAnalyzeContext instructionAnalyzeContext = context.getIntermediateData().getInstructionAnalyzeContext();
        int opcode = instructionAnalyzeContext.getOpcode();
        int parCount = instructionAnalyzeContext.getParCount();
        int[] parsType = new int[parCount];
        int par1 = (opcode >> 29) & 7;
        int par2 = (opcode >> 26) & 7;
        int par3 = (opcode >> 23) & 7;
        int par4 = (opcode >> 20) & 7;
        if (parCount == 1)
        {
            parsType[0] = par1;
        }
        else if (parCount == 2)
        {
            parsType[0] = par1;
            parsType[1] = par2;
        }
        else if (parCount == 3)
        {
            parsType[0] = par1;
            parsType[1] = par2;
            parsType[2] = par3;
        }
        else if (parCount == 4)
        {
            parsType[0] = par1;
            parsType[1] = par2;
            parsType[2] = par3;
            parsType[3] = par4;
        }
        instructionAnalyzeContext.setParsType(parsType);
    }
}

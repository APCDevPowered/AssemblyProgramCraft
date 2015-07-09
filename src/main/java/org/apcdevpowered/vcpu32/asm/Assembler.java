package org.apcdevpowered.vcpu32.asm;

import org.apcdevpowered.vcpu32.asm.FragmentProgram.LinkException;
import org.apcdevpowered.vcpu32.asm.parser.ParseException;
import org.apcdevpowered.vcpu32.asm.parser.TokenMgrError;
import org.apcdevpowered.vcpu32.asm.parser.VCPU32Parser;
import org.apcdevpowered.vcpu32.asm.parser.syntaxtree.AbstractSyntaxTree;
import org.apcdevpowered.vcpu32.asm.visitor.CompileVisitor;

public class Assembler
{
    public static class CompileContext
    {
        public static final int BIOS_RUNTIME_STATIC_RAM_SLOT_OFFSET = 0xF;
        private int runtimeStaticRAMSlot = -1;
        private final String sourceCode;
        private final int startRAM;
        private final boolean isBios;
        private final String programName;
        private int staticMemStart;
        private int staticMemOffsetFromStart;
        // 语法分析器输出的抽象语法树
        private AbstractSyntaxTree abstractSyntaxTree;
        
        private CompileContext(String sourceCode, int startRAM, boolean isBios, String programName)
        {
            this.sourceCode = sourceCode;
            this.startRAM = startRAM;
            this.isBios = isBios;
            this.programName = programName;
            if (!isBios)
            {
                setRuntimeStaticRAMSlot(3072);
            }
        }
        public String getSourceCode()
        {
            return sourceCode;
        }
        public int getRuntimeStaticRAMSlot()
        {
            if (runtimeStaticRAMSlot == -1)
            {
                throw new UnsupportedOperationException();
            }
            return runtimeStaticRAMSlot;
        }
        public void setRuntimeStaticRAMSlot(int runtimeStaticRAMSlot)
        {
            if (runtimeStaticRAMSlot < 0)
            {
                throw new IllegalArgumentException();
            }
            if (this.runtimeStaticRAMSlot != -1)
            {
                throw new UnsupportedOperationException();
            }
            this.runtimeStaticRAMSlot = runtimeStaticRAMSlot;
        }
        public int getStartRAM()
        {
            return startRAM;
        }
        public boolean isBios()
        {
            return isBios;
        }
        public String getProgramName()
        {
            return programName;
        }
        public int getStaticMemStart()
        {
            return staticMemStart;
        }
        public void setStaticMemStart(int staticMemStart)
        {
            this.staticMemStart = staticMemStart;
        }
        public int getStaticMemOffsetFromStart()
        {
            return staticMemOffsetFromStart;
        }
        public void setStaticMemOffsetFromStart(int staticMemOffsetFromStart)
        {
            this.staticMemOffsetFromStart = staticMemOffsetFromStart;
        }
        public int getStaticMemOffset()
        {
            return staticMemOffsetFromStart + staticMemStart;
        }
        public void increaseStaticMemOffset(int offset)
        {
            staticMemOffsetFromStart += offset;
        }
        public int getAndIncreaseStaticMemOffset(int offset)
        {
            int staticMemOffset = staticMemOffsetFromStart + staticMemStart;
            staticMemOffsetFromStart += offset;
            return staticMemOffset;
        }
        public AbstractSyntaxTree getAbstractSyntaxTree()
        {
            return abstractSyntaxTree;
        }
    }
    
    public static ProgramPackage compile(String sourceCode, String programName)
    {
        return compile(sourceCode, 0, false, programName);
    }
    public static ProgramPackage compile(String sourceCode, int startRAM, boolean isBios, String programName)
    {
        return compile(sourceCode, startRAM, isBios, programName, CompileLogger.dummyLogger);
    }
    public static ProgramPackage compile(String sourceCode, int startRAM, boolean isBios, String programName, CompileLogger logger)
    {
        long startCompileTime = System.currentTimeMillis();
        CompileContext context = new CompileContext(sourceCode, startRAM, isBios, programName);
        try
        {
            AbstractSyntaxTree abstractSyntaxTree = VCPU32Parser.parser(context.getSourceCode());
            context.abstractSyntaxTree = abstractSyntaxTree;
            CompileVisitor visitor = new CompileVisitor();
            FragmentProgram fragmentProgram = abstractSyntaxTree.accept(visitor, context);
            ProgramPackage programPackage = fragmentProgram.link(context);
            logger.log("Compile program \"" + context.getProgramName() + "\" used " + (System.currentTimeMillis() - startCompileTime) + "ms");
            return programPackage;
        }
        catch (ParseException e)
        {
            logger.log(e.getMessage());
        }
        catch (TokenMgrError e)
        {
            logger.log(e.getMessage());
        }
        catch (CompileException e)
        {
            logger.log(e.getMessage());
        }
        catch (LinkException e)
        {
            logger.log(e.getMessage());
        }
        return null;
    }
}
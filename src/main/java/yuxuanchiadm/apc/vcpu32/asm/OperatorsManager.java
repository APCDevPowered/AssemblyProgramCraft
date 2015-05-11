package yuxuanchiadm.apc.vcpu32.asm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yuxuanchiadm.apc.util.array.DynamicSparseArray;
import yuxuanchiadm.apc.util.string.StringTools;
import yuxuanchiadm.apc.vcpu32.asm.Assembler.CompileContext;
import yuxuanchiadm.apc.vcpu32.asm.DatatypeManager.Datatype;
import yuxuanchiadm.apc.vcpu32.asm.DatatypeManager.ImageFormatException;
import static yuxuanchiadm.apc.vcpu32.asm.DatatypeManager.*;

public class OperatorsManager
{
    private static Map<Integer, Operator> fromInsnData = new HashMap<Integer, Operator>();
    private static Map<String, Operator> fromImage = new HashMap<String, Operator>();
    
    public static class ParDatatypes
    {
        public static final ParDatatypes emptyParDatatypes = new ParDatatypes(typeVoid);
        
        private List<Datatype<?>> availableDatatypes = new ArrayList<Datatype<?>>();
        
        protected ParDatatypes(Datatype<?>... datatypes)
        {
            for(Datatype<?> datatype : datatypes)
            {
                if(datatype == null)
                {
                    datatype = typeVoid;
                }
                availableDatatypes.add(datatype);
            }
        }
        public boolean isDatatypeAllow(Datatype<?> datatype)
        {
            if(datatype == null)
            {
                return availableDatatypes.contains(typeVoid);
            }
            return availableDatatypes.contains(datatype);
        }
        public List<Datatype<?>> getDatatypes()
        {
            return Collections.unmodifiableList(availableDatatypes);
        }
        public static ParDatatypes fromDatatypes(Datatype<?>... datatypes)
        {
            return new ParDatatypes(datatypes);
        }
    }
    public static abstract class Operator
    {
        private final String image;
        
        private List<ParDatatypes> parListDatatypes;
        
        public Operator(String image, List<ParDatatypes> parListDatatypes)
        {
            this.image = image;
            this.parListDatatypes = parListDatatypes;
            
            fromImage.put(image, this);
        }
        public void checkParDatatypes(List<Datatype<?>> datatypes) throws IncmpParException
        {
            for(int i = 0;i < Math.max(parListDatatypes.size(), datatypes.size());i++)
            {
                ParDatatypes parDatatypes = ParDatatypes.emptyParDatatypes;
                if(i < parListDatatypes.size())
                {
                    parDatatypes = parListDatatypes.get(i);
                }
                Datatype<?> datatype = typeVoid;
                if(i < datatypes.size())
                {
                    datatype = datatypes.get(i);
                }
                if(!parDatatypes.isDatatypeAllow(datatype))
                {
                    throw new IncmpParException(i + 1, datatype, parDatatypes);
                }
            }
        }
        public String getImage()
        {
            return image;
        }
        public abstract void addToProgram(FragmentProgram program, int lineNumber, List<Datatype<?>> datatypes, List<String> parImages, CompileContext context) throws ParImageFormatException;
    }
    public static class MachineInsnOperator extends Operator
    {
        private final int optInsnData;
        
        public MachineInsnOperator(String image, List<ParDatatypes> parListDatatypes, int optInsnData)
        {
            super(image, parListDatatypes);
            this.optInsnData = optInsnData;
            
            fromInsnData.put(optInsnData, this);
        }
        @Override
        public void addToProgram(FragmentProgram program, int lineNumber, List<Datatype<?>> datatypes, List<String> parImages, CompileContext context) throws ParImageFormatException
        {
            int optSlotIndex = program.getSlots().length();
            program.getDebugInfo().addOffsetLineNumber(optSlotIndex, lineNumber);
            program.getSlots().set(optSlotIndex, optInsnData);
            for(int i = 0;i < datatypes.size();i++)
            {
                Datatype<?> datatype = datatypes.get(i);
                if(datatype == null)
                {
                    datatype = typeVoid;
                }
                try
                {
                    datatype.writeData(parImages.get(i), program, optSlotIndex, i + 1);
                }
                catch (ImageFormatException e)
                {
                    throw new ParImageFormatException(i + 1, datatype, e);
                }
            }
        }
        public int getOptInsnData()
        {
            return optInsnData;
        }
    }
    public static abstract class DummyOperator extends Operator
    {
        public DummyOperator(String image, List<ParDatatypes> parListDatatypes)
        {
            super(image, parListDatatypes);
        }
    }
    public static abstract class DummyVariableParametersOperator extends DummyOperator
    {
        public DummyVariableParametersOperator(String image)
        {
            super(image, null);
        }
        public abstract void checkParDatatypes(List<Datatype<?>> datatypes) throws IncmpParException;
        
        protected Datatype<?> getDatatypeInList(int parIndex, List<Datatype<?>> datatypes)
        {
            if(parIndex - 1 < 0)
            {
                return null;
            }
            if(parIndex - 1 >= datatypes.size())
            {
                return typeVoid;
            }
            Datatype<?> datatype = datatypes.get(parIndex - 1);
            if(datatype == null)
            {
                return typeVoid;
            }
            return datatype;
        }
    }
    public static class IncmpParException extends Exception
    {
        private static final long serialVersionUID = 5028205246243691869L;
        
        private final int parIndex;
        private Datatype<?> datatype;
        private ParDatatypes cmpParDatatypes;
        
        protected IncmpParException(int parIndex, Datatype<?> datatype, ParDatatypes cmpParDatatypes)
        {
            this.parIndex = parIndex;
            this.datatype = datatype;
            this.cmpParDatatypes = cmpParDatatypes;
        }
        
        public int getParIndex()
        {
            return parIndex;
        }
        public Datatype<?> getDatatype()
        {
            return datatype;
        }
        public ParDatatypes getCmpParDatatypes()
        {
            return cmpParDatatypes;
        }
    }
    public static class ParImageFormatException extends Exception
    {
        private static final long serialVersionUID = 2588542683568286478L;

        private final int parIndex;
        private Datatype<?> datatype;
        
        protected ParImageFormatException(int parIndex, Datatype<?> datatype, ImageFormatException e)
        {
            super(e);
            this.parIndex = parIndex;
            this.datatype = datatype;
        }
        
        public int getParIndex()
        {
            return parIndex;
        }
        public Datatype<?> getDatatype()
        {
            return datatype;
        }
    }
    
    public static final Operator optSET = new MachineInsnOperator("SET", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeReal, typeLabel)), 0x001);
    public static final Operator optADD = new MachineInsnOperator("ADD", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x002);
    public static final Operator optSUB = new MachineInsnOperator("SUB", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x003);
    public static final Operator optMUL = new MachineInsnOperator("MUL", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x004);
    public static final Operator optDIV = new MachineInsnOperator("DIV", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x005);
    public static final Operator optMOD = new MachineInsnOperator("MOD", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x006);
    public static final Operator optAND = new MachineInsnOperator("AND", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x007);
    public static final Operator optBOR = new MachineInsnOperator("BOR", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x008);
    public static final Operator optXOR = new MachineInsnOperator("XOR", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x009);
    public static final Operator optSHR = new MachineInsnOperator("SHR", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x00a);
    public static final Operator optASR = new MachineInsnOperator("ASR", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x00b);
    public static final Operator optSHL = new MachineInsnOperator("SHL", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x00c);
    public static final Operator optIFE = new MachineInsnOperator("IFE", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x00d);
    public static final Operator optIFN = new MachineInsnOperator("IFN", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x00e);
    public static final Operator optIFA = new MachineInsnOperator("IFA", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x00f);
    public static final Operator optIFU = new MachineInsnOperator("IFU", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x010);
    public static final Operator optJSR = new MachineInsnOperator("JSR", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x011);
    public static final Operator optCRT = new MachineInsnOperator("CRT", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeVoid, typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeHex, typeOct, typeBin, typeDec, typeReg, typeString)), 0x020);
    public static final Operator optSTT = new MachineInsnOperator("STT", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeVoid, typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x021);
    public static final Operator optGTH = new MachineInsnOperator("GTH", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg)), 0x022);
    public static final Operator optEXT = new MachineInsnOperator("EXT", Arrays.asList(ParDatatypes.fromDatatypes(typeVoid, typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x023);
    public static final Operator optSLT = new MachineInsnOperator("SLT", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x024);
    public static final Operator optCLCK = new MachineInsnOperator("CLCK", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x025);
    public static final Operator optDLCK = new MachineInsnOperator("DLCK", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x026);
    public static final Operator optGLCK = new MachineInsnOperator("GLCK", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x027);
    public static final Operator optRLCK = new MachineInsnOperator("RLCK", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x028);
    public static final Operator optWLCK = new MachineInsnOperator("WLCK", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeVoid, typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x029);
    public static final Operator optNLCK = new MachineInsnOperator("NLCK", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeVoid, typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x030);
    public static final Operator optIN = new MachineInsnOperator("IN", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg)), 0x040);
    public static final Operator optOUT = new MachineInsnOperator("OUT", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x041);
    public static final Operator optINS = new MachineInsnOperator("INS", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg)), 0x042);
    public static final Operator optOUTS = new MachineInsnOperator("OUTS", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg)), 0x043);
    public static final Operator optMIOP = new MachineInsnOperator("MIOP", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg)), 0x044);
    public static final Operator optUNMP = new MachineInsnOperator("UNMP", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg)), 0x045);
    public static final Operator optGDT = new MachineInsnOperator("GDT", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg)), 0x046);
    public static final Operator optPUSH = new MachineInsnOperator("PUSH", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x050);
    public static final Operator optPOP = new MachineInsnOperator("POP", Arrays.asList(ParDatatypes.fromDatatypes(typeVoid, typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg)), 0x051);
    public static final Operator optDUP = new MachineInsnOperator("DUP", Arrays.asList(ParDatatypes.fromDatatypes(typeVoid, typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x052);
    public static final Operator optSWAP = new MachineInsnOperator("SWAP", Collections.<ParDatatypes>emptyList(), 0x053);
    public static final Operator optCALL = new MachineInsnOperator("CALL", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeVoid, typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x060);
    public static final Operator optRET = new MachineInsnOperator("RET", Arrays.asList(ParDatatypes.fromDatatypes(typeVoid, typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x061);
    public static final Operator optGPL = new MachineInsnOperator("GPL", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeVoid, typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel)), 0x062);
    public static final Operator optITF = new MachineInsnOperator("ITF", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeChar, typeHex, typeOct, typeBin, typeDec, typeReg, typeLabel), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg)), 0x070);
    public static final Operator optFTI = new MachineInsnOperator("FTI", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg, typeReal), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg)), 0x071);
    public static final Operator optFADD = new MachineInsnOperator("FADD", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg, typeReal)), 0x080);
    public static final Operator optFSUB = new MachineInsnOperator("FSUB", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg, typeReal)), 0x081);
    public static final Operator optFMUL = new MachineInsnOperator("FMUL", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg, typeReal)), 0x082);
    public static final Operator optFDIV = new MachineInsnOperator("FDIV", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg, typeReal)), 0x083);
    public static final Operator optFMOD = new MachineInsnOperator("FMOD", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg, typeReal)), 0x084);
    public static final Operator optFIFE = new MachineInsnOperator("FIFE", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg, typeReal), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg, typeReal)), 0x090);
    public static final Operator optFIFN = new MachineInsnOperator("FIFN", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg, typeReal), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg, typeReal)), 0x091);
    public static final Operator optFIFA = new MachineInsnOperator("FIFA", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg, typeReal), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg, typeReal)), 0x092);
    public static final Operator optFIFU = new MachineInsnOperator("FIFU", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg, typeReal), ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg, typeReal)), 0x093);
    
    public static final Operator optDAT = new DummyVariableParametersOperator("DAT")
    {
        public final ParDatatypes par1Datatypes = ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg);
        public final ParDatatypes par2Datatypes = ParDatatypes.fromDatatypes(typeDec, typeHex, typeOct, typeBin, typeChar, typeString, typeReal);
        public final ParDatatypes parOtherDatatypes = ParDatatypes.fromDatatypes(typeVoid, typeDec, typeHex, typeOct, typeBin, typeChar, typeString, typeReal);
        
        @Override
        public void addToProgram(FragmentProgram program, int lineNumber, List<Datatype<?>> datatypes, List<String> parImages, CompileContext context) throws ParImageFormatException
        {
            int optSlotIndex = program.getSlots().length();
            program.getDebugInfo().addOffsetLineNumber(optSlotIndex, lineNumber);
            program.getSlots().set(optSlotIndex, ((MachineInsnOperator)optSET).getOptInsnData());
            try
            {
                datatypes.get(0).writeData(parImages.get(0), program, optSlotIndex, 1);
            }
            catch (ImageFormatException e)
            {
                throw new ParImageFormatException(1, datatypes.get(0), e);
            }
            try
            {
                typeDec.writeData("0", program, optSlotIndex, 2);
            }
            catch (ImageFormatException e)
            {
                throw new ParImageFormatException(2, typeDec, e);
            }
            
            int overrideSlot = optSlotIndex + 2;
            DynamicSparseArray<Integer> rawData = new DynamicSparseArray<Integer>();
            for(int datatypeIdx = 1;datatypeIdx < datatypes.size();datatypeIdx++)
            {
                Datatype<?> datatype = datatypes.get(datatypeIdx);
                Object value = null;
                try
                {
                    value = datatype.getValue(parImages.get(datatypeIdx));
                }
                catch(ImageFormatException e)
                {
                    throw new ParImageFormatException(datatypeIdx + 1, datatype, e);
                }
                if(value instanceof Integer)
                {
                    rawData.add((Integer)value);
                }
                else if(value instanceof String)
                {
                    rawData.addAll(StringTools.writeStringToCodePoints((String)value));
                }
                else if(value instanceof Float)
                {
                    rawData.add(Float.floatToIntBits((Float)value));
                }
            }
            program.addDataRequest(new OffsetWithDataPackage<DynamicSparseArray<Integer>>(rawData, -1), overrideSlot);
        }
        @Override
        public void checkParDatatypes(List<Datatype<?>> datatypes) throws IncmpParException
        {
            {
                Datatype<?> par1Datatype = getDatatypeInList(1, datatypes);
                if(!par1Datatypes.isDatatypeAllow(par1Datatype))
                {
                    throw new IncmpParException(1, par1Datatype, par1Datatypes);
                }
            }
            {
                Datatype<?> par2Datatype = getDatatypeInList(2, datatypes);
                if(!par2Datatypes.isDatatypeAllow(par2Datatype))
                {
                    throw new IncmpParException(2, par2Datatype, par2Datatypes);
                }
            }
            for(int i = 2;i < datatypes.size();i++)
            {
                Datatype<?> parOtherDatatype = datatypes.get(i);
                if(parOtherDatatype == null)
                {
                    parOtherDatatype = typeVoid;
                }
                if(!parOtherDatatypes.isDatatypeAllow(parOtherDatatype))
                {
                    throw new IncmpParException(i + 1, parOtherDatatype, parOtherDatatypes);
                }
            }
        }
    };
    public static final Operator optDATAT = new DummyVariableParametersOperator("DATAT")
    {
        public final ParDatatypes par1Datatypes = ParDatatypes.fromDatatypes(typeDec, typeHex, typeOct, typeBin);
        public final ParDatatypes par2Datatypes = ParDatatypes.fromDatatypes(typeDec, typeHex, typeOct, typeBin, typeChar, typeString, typeReal);
        public final ParDatatypes parOtherDatatypes = ParDatatypes.fromDatatypes(typeVoid, typeDec, typeHex, typeOct, typeBin, typeChar, typeString, typeReal);
        
        @Override
        public void addToProgram(FragmentProgram program, int lineNumber, List<Datatype<?>> datatypes, List<String> parImages, CompileContext context) throws ParImageFormatException
        {
            Datatype<?> par1Datatype = datatypes.get(0);
            int offset = 0;
            try
            {
                offset = (Integer)par1Datatype.getValue(parImages.get(0));
            }
            catch (ImageFormatException e)
            {
                throw new ParImageFormatException(1, par1Datatype, e);
            }
            DynamicSparseArray<Integer> rawData = new DynamicSparseArray<Integer>();
            for(int datatypeIdx = 1;datatypeIdx < datatypes.size();datatypeIdx++)
            {
                Datatype<?> datatype = datatypes.get(datatypeIdx);
                Object value = null;
                try
                {
                    value = datatype.getValue(parImages.get(datatypeIdx));
                }
                catch (ImageFormatException e)
                {
                    throw new ParImageFormatException(datatypeIdx + 1, datatype, e);
                }
                if(value instanceof Integer)
                {
                    rawData.add((Integer)value);
                }
                else if(value instanceof String)
                {
                    rawData.addAll(StringTools.writeStringToCodePoints((String)value));
                }
                else if(value instanceof Float)
                {
                    rawData.add(Float.floatToIntBits((Float)value));
                }
            }
            program.addDataRequest(new OffsetWithDataPackage<DynamicSparseArray<Integer>>(rawData, offset), -1);
        }
        @Override
        public void checkParDatatypes(List<Datatype<?>> datatypes) throws IncmpParException
        {
            {
                Datatype<?> par1Datatype = getDatatypeInList(1, datatypes);
                if(!par1Datatypes.isDatatypeAllow(par1Datatype))
                {
                    throw new IncmpParException(1, par1Datatype, par1Datatypes);
                }
            }
            {
                Datatype<?> par2Datatype = getDatatypeInList(2, datatypes);
                if(!par2Datatypes.isDatatypeAllow(par2Datatype))
                {
                    throw new IncmpParException(2, par2Datatype, par2Datatypes);
                }
            }
            for(int i = 2;i < datatypes.size();i++)
            {
                Datatype<?> parOtherDatatype = datatypes.get(i);
                if(parOtherDatatype == null)
                {
                    parOtherDatatype = typeVoid;
                }
                if(!parOtherDatatypes.isDatatypeAllow(parOtherDatatype))
                {
                    throw new IncmpParException(i + 1, parOtherDatatype, parOtherDatatypes);
                }
            }
        }
    };
    public static final Operator optSTRAT = new DummyOperator("STRAT", Arrays.asList(ParDatatypes.fromDatatypes(typeDec, typeHex, typeOct, typeBin), ParDatatypes.fromDatatypes(typeString)))
    {
        @Override
        public void addToProgram(FragmentProgram program, int lineNumber, List<Datatype<?>> datatypes, List<String> parImages, CompileContext context) throws ParImageFormatException
        {
            Datatype<?> par1Datatype = datatypes.get(0);
            int offset = 0;
            try
            {
                offset = (Integer)par1Datatype.getValue(parImages.get(0));
            }
            catch (ImageFormatException e)
            {
                throw new ParImageFormatException(1, par1Datatype, e);
            }
            Datatype<?> par2Datatype = datatypes.get(1);
            String string = null;
            try
            {
                string = (String)par2Datatype.getValue(parImages.get(1));
            }
            catch (ImageFormatException e)
            {
                throw new ParImageFormatException(2, par2Datatype, e);
            }
            program.addStringRequest(new OffsetWithDataPackage<String>(string, offset), -1);
        }
    };
    public static final Operator optGSO = new DummyOperator("GSO", Arrays.asList(ParDatatypes.fromDatatypes(typeMemDec, typeMemHex, typeMemOct, typeMemBin, typeMemReg, typeReg)))
    {
        @Override
        public void addToProgram(FragmentProgram program, int lineNumber, List<Datatype<?>> datatypes, List<String> parImages, CompileContext context) throws ParImageFormatException
        {
            int optSlotIndex = program.getSlots().length();
            program.getDebugInfo().addOffsetLineNumber(optSlotIndex, lineNumber);
            program.getSlots().set(optSlotIndex, ((MachineInsnOperator)optSET).getOptInsnData());
            try
            {
                datatypes.get(0).writeData(parImages.get(0), program, optSlotIndex, 1);
            }
            catch (ImageFormatException e)
            {
                throw new ParImageFormatException(1, datatypes.get(0), e);
            }
            try
            {
                typeDec.writeData("0", program, optSlotIndex, 2);
            }
            catch (ImageFormatException e)
            {
                throw new ParImageFormatException(2, typeDec, e);
            }
            int overrideSlot = optSlotIndex + 2;
            program.addStaticOffsetRequest(overrideSlot);
        }
    };

    public static Operator fromImage(String image)
    {
        return fromImage.get(image);
    }
    public static Operator fromInsnData(int insnData)
    {
        return fromInsnData.get(insnData);
    }
}
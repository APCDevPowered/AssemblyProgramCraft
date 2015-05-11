package yuxuanchiadm.apc.vcpu32.asm;

import java.util.HashMap;
import java.util.Map;

import yuxuanchiadm.apc.util.bit.BitTools;
import yuxuanchiadm.apc.util.integer.IntTools;

public class DatatypeManager
{
    public static enum Register
    {
        A(0x01), B(0x02), C(0x03), X(0x04), Y(0x05), Z(0x06), I(0x07), J(0x08), O(0x09), PC(0x0A), SP(0x0B);
        
        private final String image;
        private final int data;
        
        private static Map<String, Register> imageRegisterMap = new HashMap<String, Register>();
        private static Map<Integer, Register> dataRegisterMap = new HashMap<Integer, Register>();
        
        private Register(int data)
        {
            this.image = this.name();
            this.data = data;
        }
        
        static
        {
            for(Register register : values())
            {
                imageRegisterMap.put(register.image, register);
                dataRegisterMap.put(register.data, register);
            }
        }
        
        public String getImage()
        {
            return image;
        }
        public int getData()
        {
            return data;
        }
        
        public static Register fromImage(String image)
        {
            return imageRegisterMap.get(image);
        }
        public static Register fromData(int data)
        {
            return dataRegisterMap.get(data);
        }
    }
    
    public static abstract class Datatype<V>
    {
        public final int parInsnBits;
        
        protected Datatype(int parInsnBits)
        {
            this.parInsnBits = parInsnBits;
        }
        public abstract V getValue(String image) throws ImageFormatException;
        public abstract void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex) throws ImageFormatException;
        public abstract int getData(String image) throws ImageFormatException;
        public abstract String getTypeName();
        
        protected void setOptParType(FragmentProgram program, int optSlotIndex, int parIndex)
        {
            int operator = program.getSlots().get(optSlotIndex);
            operator = BitTools.copyBit(parInsnBits, 0, operator, 29 - (parIndex - 1) * 3, 3);
            program.getSlots().set(optSlotIndex, operator);
        }
    }
    public static class ImageFormatException extends Exception
    {
        private static final long serialVersionUID = -7623290452565869813L;

        public static final int UNKNOWN = 0;
        
        public static final int NUM_NOT_IN_RANGE = 1;
        
        public final int type;
        
        public ImageFormatException()
        {
            type = UNKNOWN;
        }
        public ImageFormatException(int type)
        {
            this.type = type;
        }
        public String getMessage()
        {
            switch (type)
            {
                case NUM_NOT_IN_RANGE:
                    return "Image format as number not in range";
                default:
                    return "Unknown image format";
            }
        }
    }
    
    public static Datatype<Integer> typeDec = new Datatype<Integer>(3)
    {
        @Override
        public Integer getValue(String image) throws ImageFormatException
        {
            Integer i = IntTools.parseDec(image);
            if(i == null)
            {
                throw new ImageFormatException(ImageFormatException.NUM_NOT_IN_RANGE);
            }
            return i;
        }
        @Override
        public void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex) throws ImageFormatException
        {
            int data = getData(image);
            setOptParType(program, optSlotIndex, parIndex);
            program.getSlots().set(optSlotIndex + parIndex, data);
        }
        @Override
        public int getData(String image) throws ImageFormatException
        {
            return getValue(image);
        }
        @Override
        public String getTypeName()
        {
            return "Dec";
        }
    };
    public static Datatype<Integer> typeHex = new Datatype<Integer>(3)
    {
        @Override
        public Integer getValue(String image) throws ImageFormatException
        {
            Integer i = IntTools.parseHex(image.substring(2));
            if(i == null)
            {
                throw new ImageFormatException(ImageFormatException.NUM_NOT_IN_RANGE);
            }
            return i;
        }
        @Override
        public void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex) throws ImageFormatException
        {
            int data = getData(image);
            setOptParType(program, optSlotIndex, parIndex);
            program.getSlots().set(optSlotIndex + parIndex, data);
        }
        @Override
        public int getData(String image) throws ImageFormatException
        {
            return getValue(image);
        }
        @Override
        public String getTypeName()
        {
            return "Hex";
        }
    };
    public static Datatype<Integer> typeOct = new Datatype<Integer>(3)
    {
        @Override
        public Integer getValue(String image) throws ImageFormatException
        {
            Integer i = IntTools.parseOct(image.substring(1));
            if(i == null)
            {
                throw new ImageFormatException(ImageFormatException.NUM_NOT_IN_RANGE);
            }
            return i;
        }
        @Override
        public void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex) throws ImageFormatException
        {
            int data = getData(image);
            setOptParType(program, optSlotIndex, parIndex);
            program.getSlots().set(optSlotIndex + parIndex, data);
        }
        @Override
        public int getData(String image) throws ImageFormatException
        {
            return getValue(image);
        }
        @Override
        public String getTypeName()
        {
            return "Oct";
        }
    };
    public static Datatype<Integer> typeBin = new Datatype<Integer>(3)
    {
        @Override
        public Integer getValue(String image) throws ImageFormatException
        {
            Integer i = IntTools.parseBin(image.substring(0, image.length() - 1));
            if(i == null)
            {
                throw new ImageFormatException(ImageFormatException.NUM_NOT_IN_RANGE);
            }
            return i;
        }
        @Override
        public void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex) throws ImageFormatException
        {
            int data = getData(image);
            setOptParType(program, optSlotIndex, parIndex);
            program.getSlots().set(optSlotIndex + parIndex, data);
        }
        @Override
        public int getData(String image) throws ImageFormatException
        {
            return getValue(image);
        }
        @Override
        public String getTypeName()
        {
            return "Bin";
        }
    };
    public static Datatype<Register> typeReg = new Datatype<Register>(1)
    {
        @Override
        public Register getValue(String image)
        {
            return Register.fromImage(image);
        }
        @Override
        public void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex)
        {
            int data = getData(image);
            setOptParType(program, optSlotIndex, parIndex);
            program.getSlots().set(optSlotIndex + parIndex, data);
        }
        @Override
        public int getData(String image)
        {
            return getValue(image).getData();
        }
        @Override
        public String getTypeName()
        {
            return "Reg";
        }
    };
    public static Datatype<Register> typeMemReg = new Datatype<Register>(5)
    {
        @Override
        public Register getValue(String image)
        {
            return Register.fromImage(image.substring(1, image.length() - 1));
        }
        @Override
        public void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex)
        {
            int data = getData(image);
            setOptParType(program, optSlotIndex, parIndex);
            program.getSlots().set(optSlotIndex + parIndex, data);
        }
        @Override
        public int getData(String image)
        {
            return getValue(image).getData();
        }
        @Override
        public String getTypeName()
        {
            return "MemReg";
        }
    };
    public static Datatype<Integer> typeMemDec = new Datatype<Integer>(2)
    {
        @Override
        public Integer getValue(String image) throws ImageFormatException
        {
            Integer i = IntTools.parseDec(image.substring(1, image.length() - 1));
            if(i == null)
            {
                throw new ImageFormatException(ImageFormatException.NUM_NOT_IN_RANGE);
            }
            return i;
        }
        @Override
        public void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex) throws ImageFormatException
        {
            int data = getData(image);
            setOptParType(program, optSlotIndex, parIndex);
            program.getSlots().set(optSlotIndex + parIndex, data);
        }
        @Override
        public int getData(String image) throws ImageFormatException
        {
            return getValue(image);
        }
        @Override
        public String getTypeName()
        {
            return "MemDec";
        }
    };
    public static Datatype<Integer> typeMemHex = new Datatype<Integer>(2)
    {
        @Override
        public Integer getValue(String image) throws ImageFormatException
        {
            Integer i = IntTools.parseHex(image.substring(3, image.length() - 1));
            if(i == null)
            {
                throw new ImageFormatException(ImageFormatException.NUM_NOT_IN_RANGE);
            }
            return i;
        }
        @Override
        public void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex) throws ImageFormatException
        {
            int data = getData(image);
            setOptParType(program, optSlotIndex, parIndex);
            program.getSlots().set(optSlotIndex + parIndex, data);
        }
        @Override
        public int getData(String image) throws ImageFormatException
        {
            return getValue(image);
        }
        @Override
        public String getTypeName()
        {
            return "MemHex";
        }
    };
    public static Datatype<Integer> typeMemOct = new Datatype<Integer>(2)
    {
        @Override
        public Integer getValue(String image) throws ImageFormatException
        {
            Integer i = IntTools.parseOct(image.substring(2, image.length() - 1));
            if(i == null)
            {
                throw new ImageFormatException(ImageFormatException.NUM_NOT_IN_RANGE);
            }
            return i;
        }
        @Override
        public void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex) throws ImageFormatException
        {
            int data = getData(image);
            setOptParType(program, optSlotIndex, parIndex);
            program.getSlots().set(optSlotIndex + parIndex, data);
        }
        @Override
        public int getData(String image) throws ImageFormatException
        {
            return getValue(image);
        }
        @Override
        public String getTypeName()
        {
            return "MemOct";
        }
    };
    public static Datatype<Integer> typeMemBin = new Datatype<Integer>(2)
    {
        @Override
        public Integer getValue(String image) throws ImageFormatException
        {
            Integer i = IntTools.parseBin(image.substring(1, image.length() - 2));
            if(i == null)
            {
                throw new ImageFormatException(ImageFormatException.NUM_NOT_IN_RANGE);
            }
            return i;
        }
        @Override
        public void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex) throws ImageFormatException
        {
            int data = getData(image);
            setOptParType(program, optSlotIndex, parIndex);
            program.getSlots().set(optSlotIndex + parIndex, data);
        }
        @Override
        public int getData(String image) throws ImageFormatException
        {
            return getValue(image);
        }
        @Override
        public String getTypeName()
        {
            return "MemBin";
        }
    };
    public static Datatype<String> typeString = new Datatype<String>(4)
    {
        @Override
        public String getValue(String image)
        {
            String stringValue = image.substring(1, image.length() - 1);
            int codePointIndex = 0;
            StringBuilder builder = new StringBuilder();
            while(codePointIndex < stringValue.codePointCount(0, stringValue.length()))
            {
                int currentCodePoint = stringValue.codePointAt(codePointIndex);
                codePointIndex++;
                if(currentCodePoint == '\\')
                {
                    int nextCodePoint = stringValue.codePointAt(codePointIndex);
                    codePointIndex++;
                    switch (nextCodePoint)
                    {
                        case 'b':
                            builder.append('\b');
                            break;
                        case 'f':
                            builder.append('\f');
                            break;
                        case 'n':
                            builder.append('\n');
                            break;
                        case 'r':
                            builder.append('\r');
                            break;
                        case 't':
                            builder.append('\t');
                            break;
                        case '"':
                            builder.append('"');
                            break;
                        case '\\':
                            builder.append('\\');
                            break;
                        case 'u':
                            int codePoint1 = stringValue.codePointAt(codePointIndex + 0);
                            int codePoint2 = stringValue.codePointAt(codePointIndex + 1);
                            int codePoint3 = stringValue.codePointAt(codePointIndex + 2);
                            int codePoint4 = stringValue.codePointAt(codePointIndex + 3);
                            StringBuilder unicodeCodeBuilder = new StringBuilder();
                            unicodeCodeBuilder.appendCodePoint(codePoint1);
                            unicodeCodeBuilder.appendCodePoint(codePoint2);
                            unicodeCodeBuilder.appendCodePoint(codePoint3);
                            unicodeCodeBuilder.appendCodePoint(codePoint4);
                            builder.appendCodePoint(Integer.valueOf(unicodeCodeBuilder.toString(), 16).intValue());
                            codePointIndex += 4;
                            break;
                    }
                }
                else
                {
                    builder.appendCodePoint(currentCodePoint);
                }
            }
            return builder.toString();
        }
        @Override
        public void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex)
        {
            int data = getData(image);
            setOptParType(program, optSlotIndex, parIndex);
            program.getSlots().set(optSlotIndex + parIndex, data);
            program.addStringRequest(new OffsetWithDataPackage<String>(getValue(image), -1), optSlotIndex + parIndex);
        }
        @Override
        public int getData(String image)
        {
            return 0;
        }
        @Override
        public String getTypeName()
        {
            return "String";
        }
    };
    public static Datatype<Integer> typeChar = new Datatype<Integer>(3)
    {
        @Override
        public Integer getValue(String image)
        {
            String charValue = image.substring(1, image.length() - 1);
            int codePointIndex = 0;
            int currentCodePoint = charValue.codePointAt(codePointIndex);
            codePointIndex++;
            if(currentCodePoint == '\\')
            {
                int nextCodePoint = charValue.codePointAt(codePointIndex);
                codePointIndex++;
                switch (nextCodePoint)
                {
                    case 'b':
                        return (int)'\b';
                    case 'f':
                        return (int)'\f';
                    case 'n':
                        return (int)'\n';
                    case 'r':
                        return (int)'\r';
                    case 't':
                        return (int)'\t';
                    case '"':
                        return (int)'"';
                    case '\\':
                        return (int)'\\';
                    case 'u':
                        int codePoint1 = charValue.codePointAt(codePointIndex + 0);
                        int codePoint2 = charValue.codePointAt(codePointIndex + 1);
                        int codePoint3 = charValue.codePointAt(codePointIndex + 2);
                        int codePoint4 = charValue.codePointAt(codePointIndex + 3);
                        StringBuilder unicodeCodeBuilder = new StringBuilder();
                        unicodeCodeBuilder.appendCodePoint(codePoint1);
                        unicodeCodeBuilder.appendCodePoint(codePoint2);
                        unicodeCodeBuilder.appendCodePoint(codePoint3);
                        unicodeCodeBuilder.appendCodePoint(codePoint4);
                        return Integer.valueOf(unicodeCodeBuilder.toString(), 16).intValue();
                    default:
                        return null;
                }
            }
            else
            {
                return currentCodePoint;
            }
        }
        @Override
        public void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex)
        {
            int data = getData(image);
            setOptParType(program, optSlotIndex, parIndex);
            program.getSlots().set(optSlotIndex + parIndex, data);
        }
        @Override
        public int getData(String image)
        {
            return getValue(image);
        }
        @Override
        public String getTypeName()
        {
            return "Char";
        }
    };
    public static Datatype<String> typeLabel = new Datatype<String>(3)
    {
        @Override
        public String getValue(String image)
        {
            return image;
        }
        @Override
        public void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex)
        {
            int data = getData(image);
            setOptParType(program, optSlotIndex, parIndex);
            program.getSlots().set(optSlotIndex + parIndex, data);
            program.addLabelRequest(getValue(image), optSlotIndex + parIndex);
        }
        @Override
        public int getData(String image)
        {
            return 0;
        }
        @Override
        public String getTypeName()
        {
            return "Label";
        }
    };
    public static Datatype<Float> typeReal = new Datatype<Float>(3)
    {
        @Override
        public Float getValue(String image)
        {
            return Float.parseFloat(image);
        }
        @Override
        public void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex)
        {
            int data = getData(image);
            setOptParType(program, optSlotIndex, parIndex);
            program.getSlots().set(optSlotIndex + parIndex, data);
        }
        @Override
        public int getData(String image)
        {
            return Float.floatToRawIntBits(getValue(image));
        }
        @Override
        public String getTypeName()
        {
            return "Real";
        }
    };
    public static Datatype<Integer> typeVoid = new Datatype<Integer>(0)
    {
        @Override
        public Integer getValue(String image)
        {
            return 0;
        }
        @Override
        public void writeData(String image, FragmentProgram program, int optSlotIndex, int parIndex)
        {
            int data = getData(image);
            setOptParType(program, optSlotIndex, parIndex);
            program.getSlots().set(optSlotIndex + parIndex, data);
        }
        @Override
        public int getData(String image)
        {
            return 0;
        }
        @Override
        public String getTypeName()
        {
            return "Void";
        }
    };
}
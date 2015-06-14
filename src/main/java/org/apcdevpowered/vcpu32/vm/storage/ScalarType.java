package org.apcdevpowered.vcpu32.vm.storage;

public abstract class ScalarType
{
    public static abstract class ScalarTypeNumber extends ScalarType
    {
        private final int byteCount;
        
        public ScalarTypeNumber(int byteCount)
        {
            this.byteCount = byteCount;
        }
        public int getByteCount()
        {
            return byteCount;
        }
    }
    public static class ScalarTypeNaturalNumber extends ScalarTypeNumber
    {
        private ScalarTypeNaturalNumber(int byteCount)
        {
            super(byteCount);
        }
    }
    public static class ScalarTypeRealNumber extends ScalarTypeNumber
    {
        private ScalarTypeRealNumber(int byteCount)
        {
            super(byteCount);
        }
    }
    public static class ScalarTypeNaturalNumberArray extends ScalarTypeNaturalNumber
    {
        private ScalarTypeNaturalNumberArray(int byteCount)
        {
            super(byteCount);
        }
    }
    public static class ScalarTypeString extends ScalarType
    {
        private ScalarTypeString()
        {
        }
    }
    
    public static final ScalarTypeNaturalNumber SCALAR_TYPE_BYTE;
    public static final ScalarTypeNaturalNumber SCALAR_TYPE_SHORT;
    public static final ScalarTypeNaturalNumber SCALAR_TYPE_INTEGER;
    public static final ScalarTypeNaturalNumber SCALAR_TYPE_LONG;
    public static final ScalarTypeRealNumber SCALAR_TYPE_FLOAT;
    public static final ScalarTypeRealNumber SCALAR_TYPE_DOUBLE;
    public static final ScalarTypeNaturalNumberArray SCALAR_TYPE_BYTE_ARRAY;
    public static final ScalarTypeNaturalNumberArray SCALAR_TYPE_INTEGER_ARRAY;
    public static final ScalarTypeString SCALAR_TYPE_STRING;
    static
    {
        SCALAR_TYPE_BYTE = new ScalarTypeNaturalNumber(1);
        SCALAR_TYPE_SHORT = new ScalarTypeNaturalNumber(2);
        SCALAR_TYPE_INTEGER = new ScalarTypeNaturalNumber(4);
        SCALAR_TYPE_LONG = new ScalarTypeNaturalNumber(8);
        SCALAR_TYPE_FLOAT = new ScalarTypeRealNumber(4);
        SCALAR_TYPE_DOUBLE = new ScalarTypeRealNumber(8);
        SCALAR_TYPE_BYTE_ARRAY = new ScalarTypeNaturalNumberArray(1);
        SCALAR_TYPE_INTEGER_ARRAY = new ScalarTypeNaturalNumberArray(4);
        SCALAR_TYPE_STRING = new ScalarTypeString();
    }
    
    private ScalarType()
    {
    }
}
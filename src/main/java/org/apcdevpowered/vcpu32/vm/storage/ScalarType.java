package org.apcdevpowered.vcpu32.vm.storage;

import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarByte;

public abstract class ScalarType<S extends NodeScalar<S>>
{
    public static abstract class ScalarTypeNumber<S extends NodeScalar<S>> extends ScalarType<S>
    {
        private final int byteCount;
        
        public ScalarTypeNumber(Class<S> clazz, int byteCount)
        {
            super(clazz);
            this.byteCount = byteCount;
        }
        public int getByteCount()
        {
            return byteCount;
        }
    }
    public static class ScalarTypeNaturalNumber<S extends NodeScalar<S>> extends ScalarTypeNumber<S>
    {
        private ScalarTypeNaturalNumber(Class<S> clazz, int byteCount)
        {
            super(clazz, byteCount);
        }
    }
    public static class ScalarTypeRealNumber<S extends NodeScalar<S>> extends ScalarTypeNumber<S>
    {
        private ScalarTypeRealNumber(Class<S> clazz, int byteCount)
        {
            super(clazz, byteCount);
        }
    }
    public static class ScalarTypeNaturalNumberArray<S extends NodeScalar<S>> extends ScalarTypeNaturalNumber<S>
    {
        private ScalarTypeNaturalNumberArray(Class<S> clazz, int byteCount)
        {
            super(clazz, byteCount);
        }
    }
    public static class ScalarTypeString<S extends NodeScalar<S>> extends ScalarType<S>
    {
        private ScalarTypeString(Class<S> clazz)
        {
            super(clazz);
        }
    }
    
    public static final ScalarTypeNaturalNumber<NodeScalarByte> SCALAR_TYPE_BYTE;
    public static final ScalarTypeNaturalNumber SCALAR_TYPE_SHORT;
    public static final ScalarTypeNaturalNumber SCALAR_TYPE_INTEGER;
    public static final ScalarTypeNaturalNumber SCALAR_TYPE_LONG;
    public static final ScalarTypeRealNumber SCALAR_TYPE_FLOAT;
    public static final ScalarTypeRealNumber SCALAR_TYPE_DOUBLE;
    public static final ScalarTypeNaturalNumberArray SCALAR_TYPE_BYTE_ARRAY;
    public static final ScalarTypeNaturalNumberArray SCALAR_TYPE_INTEGER_ARRAY;
    public static final ScalarTypeString SCALAR_TYPE_STRING;
    private final Class<S> clazz;
    
    private ScalarType(Class<S> clazz)
    {
        this.clazz = clazz;
    }
    
    static
    {
        SCALAR_TYPE_BYTE = new ScalarTypeNaturalNumber<NodeScalarByte>(NodeScalarByte.class, 1);
        SCALAR_TYPE_SHORT = new ScalarTypeNaturalNumber(2);
        SCALAR_TYPE_INTEGER = new ScalarTypeNaturalNumber(4);
        SCALAR_TYPE_LONG = new ScalarTypeNaturalNumber(8);
        SCALAR_TYPE_FLOAT = new ScalarTypeRealNumber(4);
        SCALAR_TYPE_DOUBLE = new ScalarTypeRealNumber(8);
        SCALAR_TYPE_BYTE_ARRAY = new ScalarTypeNaturalNumberArray(1);
        SCALAR_TYPE_INTEGER_ARRAY = new ScalarTypeNaturalNumberArray(4);
        SCALAR_TYPE_STRING = new ScalarTypeString();
    }
}
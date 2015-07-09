package org.apcdevpowered.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.UUID;

public class StreamUtils
{
    public static final int BYTE_SINGLE_INCREASE_MAX_CAPACITY = 512;
    public static final int INT_SINGLE_INCREASE_MAX_CAPACITY = 512;
    public static final int ARRAY_SINGLE_INCREASE_MAX_CAPACITY = 32;
    private static final byte[] readBuffer = new byte[8];
    private static final byte[] writeBuffer = new byte[8];
    
    public static void writeBoolean(OutputStream outputStream, boolean v) throws IOException
    {
        outputStream.write(v ? 1 : 0);
    }
    public static boolean readBoolean(InputStream inputStream) throws IOException
    {
        int v = inputStream.read();
        if (v < 0)
        {
            throw new EOFException();
        }
        return v != 0;
    }
    public static void writeShort(OutputStream outputStream, short v) throws IOException
    {
        synchronized (writeBuffer)
        {
            writeBuffer[0] = (byte) (v >>> 8);
            writeBuffer[1] = (byte) (v >>> 0);
            outputStream.write(writeBuffer, 0, 2);
        }
    }
    public static short readShort(InputStream inputStream) throws IOException
    {
        synchronized (readBuffer)
        {
            readBufferFully(inputStream, 2);
            return (short) (((readBuffer[0] & 0xFF) << 8) + ((readBuffer[1] & 0xFF) << 0));
        }
    }
    public static void writeInt(OutputStream outputStream, int v) throws IOException
    {
        synchronized (writeBuffer)
        {
            writeBuffer[0] = (byte) (v >>> 24);
            writeBuffer[1] = (byte) (v >>> 16);
            writeBuffer[2] = (byte) (v >>> 8);
            writeBuffer[3] = (byte) (v >>> 0);
            outputStream.write(writeBuffer, 0, 4);
        }
    }
    public static int readInt(InputStream inputStream) throws IOException
    {
        synchronized (readBuffer)
        {
            readBufferFully(inputStream, 4);
            return (int) (((readBuffer[0] & 0xFF) << 24) + ((readBuffer[1] & 0xFF) << 16) + ((readBuffer[2] & 0xFF) << 8) + ((readBuffer[3] & 0xFF) << 0));
        }
    }
    public static void writeLong(OutputStream outputStream, long v) throws IOException
    {
        synchronized (writeBuffer)
        {
            writeBuffer[0] = (byte) (v >>> 56);
            writeBuffer[1] = (byte) (v >>> 48);
            writeBuffer[2] = (byte) (v >>> 40);
            writeBuffer[3] = (byte) (v >>> 32);
            writeBuffer[4] = (byte) (v >>> 24);
            writeBuffer[5] = (byte) (v >>> 16);
            writeBuffer[6] = (byte) (v >>> 8);
            writeBuffer[7] = (byte) (v >>> 0);
            outputStream.write(writeBuffer, 0, 8);
        }
    }
    public static long readLong(InputStream inputStream) throws IOException
    {
        synchronized (readBuffer)
        {
            readBufferFully(inputStream, 8);
            return (long) (((readBuffer[0] & 0xFFL) << 56) + ((readBuffer[1] & 0xFFL) << 48) + ((readBuffer[2] & 0xFFL) << 40) + ((readBuffer[3] & 0xFFL) << 32) + ((readBuffer[4] & 0xFFL) << 24) + ((readBuffer[5] & 0xFFL) << 16) + ((readBuffer[6] & 0xFFL) << 8) + ((readBuffer[7] & 0xFFL) << 0));
        }
    }
    public static void writeFloat(OutputStream outputStream, float v) throws IOException
    {
        writeInt(outputStream, Float.floatToRawIntBits(v));
    }
    public static float readFloat(InputStream inputStream) throws IOException
    {
        return Float.intBitsToFloat(readInt(inputStream));
    }
    public static void writeDouble(OutputStream outputStream, double v) throws IOException
    {
        writeLong(outputStream, Double.doubleToRawLongBits(v));
    }
    public static double readDouble(InputStream inputStream) throws IOException
    {
        return Double.longBitsToDouble(readLong(inputStream));
    }
    public static void writeString(OutputStream outputStream, String string) throws IOException
    {
        boolean isNull = (string == null);
        outputStream.write(isNull ? 1 : 0);
        if (!isNull)
        {
            byte[] bytes = string.getBytes(Charset.forName("UTF-8"));
            writeInt(outputStream, bytes.length);
            outputStream.write(bytes);
        }
    }
    public static String readString(InputStream inputStream) throws IOException
    {
        int b0 = inputStream.read();
        if (b0 < 0)
        {
            throw new EOFException();
        }
        boolean isNull = (b0 != 0);
        if (isNull)
        {
            return null;
        }
        int length = readInt(inputStream);
        return new String(readByteArrayFully(inputStream, length), Charset.forName("UTF-8")).intern();
    }
    public static void writeUUID(OutputStream outputStream, UUID uuid) throws IOException
    {
        boolean isNull = (uuid == null);
        outputStream.write(isNull ? 1 : 0);
        if (!isNull)
        {
            writeLong(outputStream, uuid.getMostSignificantBits());
            writeLong(outputStream, uuid.getLeastSignificantBits());
        }
    }
    public static UUID readUUID(InputStream inputStream) throws IOException
    {
        int b0 = inputStream.read();
        if (b0 < 0)
        {
            throw new EOFException();
        }
        boolean isNull = (b0 != 0);
        if (isNull)
        {
            return null;
        }
        long mostSignificantBits = readLong(inputStream);
        long leastSignificantBits = readLong(inputStream);
        return new UUID(mostSignificantBits, leastSignificantBits);
    }
    private static void readBufferFully(InputStream inputStream, int length) throws IOException
    {
        int readed = 0;
        while (readed < length)
        {
            int count = inputStream.read(readBuffer, readed, length - readed);
            if (count < 0)
            {
                throw new EOFException();
            }
            readed += count;
        }
    }
    public static byte[] readByteArrayFully(InputStream inputStream, int length) throws IOException
    {
        if (length < 0)
        {
            throw new IndexOutOfBoundsException();
        }
        byte[] bytes = new byte[Math.min(BYTE_SINGLE_INCREASE_MAX_CAPACITY, length)];
        int totalCount = 0;
        while (totalCount < length)
        {
            if (bytes.length == totalCount)
            {
                bytes = Arrays.copyOf(bytes, bytes.length + BYTE_SINGLE_INCREASE_MAX_CAPACITY);
            }
            int count = inputStream.read(bytes, totalCount, bytes.length - totalCount);
            if (count < 0)
            {
                throw new EOFException();
            }
            totalCount += count;
        }
        return bytes;
    }
    public static int[] readIntArrayFully(InputStream inputStream, int length) throws IOException
    {
        if (length < 0)
        {
            throw new IndexOutOfBoundsException();
        }
        int[] ints = new int[Math.min(INT_SINGLE_INCREASE_MAX_CAPACITY, length)];
        int totalCount = 0;
        while (totalCount < length)
        {
            if (ints.length == totalCount)
            {
                ints = Arrays.copyOf(ints, ints.length + INT_SINGLE_INCREASE_MAX_CAPACITY);
            }
            ints[totalCount] = readInt(inputStream);
            totalCount += 1;
        }
        return ints;
    }
    public static String[] readStringArrayFully(InputStream inputStream, int length) throws IOException
    {
        if (length < 0)
        {
            throw new IndexOutOfBoundsException();
        }
        String[] strings = new String[Math.min(ARRAY_SINGLE_INCREASE_MAX_CAPACITY, length)];
        int totalCount = 0;
        while (totalCount < length)
        {
            if (strings.length == totalCount)
            {
                strings = Arrays.copyOf(strings, strings.length + ARRAY_SINGLE_INCREASE_MAX_CAPACITY);
            }
            strings[totalCount] = readString(inputStream);
            totalCount += 1;
        }
        return strings;
    }
    public static UUID[] readUUIDArrayFully(InputStream inputStream, int length) throws IOException
    {
        if (length < 0)
        {
            throw new IndexOutOfBoundsException();
        }
        UUID[] uuids = new UUID[Math.min(ARRAY_SINGLE_INCREASE_MAX_CAPACITY, length)];
        int totalCount = 0;
        while (totalCount < length)
        {
            if (uuids.length == totalCount)
            {
                uuids = Arrays.copyOf(uuids, uuids.length + ARRAY_SINGLE_INCREASE_MAX_CAPACITY);
            }
            uuids[totalCount] = readUUID(inputStream);
            totalCount += 1;
        }
        return uuids;
    }
}
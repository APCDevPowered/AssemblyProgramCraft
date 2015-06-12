package org.apcdevpowered.util.string;

import java.util.ArrayList;
import java.util.List;

import org.apcdevpowered.util.integer.IntTools;

public class StringTools
{
    public static Character charAt(String str,int idx)
    {
        if(idx < 0)
        {
            return null;
        }
        else if(idx >= str.length())
        {
            return null;
        }
        else
        {
            return str.charAt(idx);
        }
    }
    public static String charAtToString(String str,int idx)
    {
        if(idx < 0)
        {
            return "";
        }
        else if(idx >= str.length())
        {
            return "";
        }
        else
        {
            return ((Character)str.charAt(idx)).toString();
        }
    }
    public static Integer[] writeStringToCodePoints(String str)
    {
        Integer[] codePoints = new Integer[str.codePointCount(0, str.length())];
        for(int i = 0;i < codePoints.length;i++)
        {
            codePoints[i] = str.codePointAt(i);
        }
        return codePoints;
    }
    public static int[] writeStringToIntArray(String str)
    {
        ArrayList<Integer> al = new ArrayList<Integer>();
        char[] charArray = str.toCharArray();
        int IntTemp = 0;
        boolean needAdd = false;
        al.add(charArray.length);
        for(int i = 0;i < charArray.length;i++)
        {
            if(needAdd == false)
            {
                IntTemp = (charArray[i] << 16) & 0xFFFF0000;
                if((i < (charArray.length - 1)) == false)
                {
                    al.add(IntTemp);
                }
                needAdd = true;
            }
            else
            {
                IntTemp = (charArray[i] & 0xFFFF) | IntTemp;
                al.add(IntTemp);
                needAdd = false;
            }
        }
        return IntTools.toIntArray(al);
    }
    public static Integer[] writeStringToIntegerArray(String str)
    {
        ArrayList<Integer> al = new ArrayList<Integer>();
        char[] charArray = str.toCharArray();
        Integer IntTemp = 0;
        boolean needAdd = false;
        al.add(charArray.length);
        for(int i = 0;i < charArray.length;i++)
        {
            if(needAdd == false)
            {
                IntTemp = (charArray[i] << 16) & 0xFFFF0000;
                if((i < (charArray.length - 1)) == false)
                {
                    al.add(IntTemp);
                }
                needAdd = true;
            }
            else
            {
                IntTemp = (charArray[i] & 0xFFFF) | IntTemp;
                al.add(IntTemp);
                needAdd = false;
            }
        }
        return al.toArray(new Integer[al.size()]);
    }
    public static String readIntArrayToString(int[] intArray,int idx)
    {
        String str = "";
        int length = intArray[idx];
        int ilen = ((length % 2 == 1) ? ((length + 1) / 2) : (length / 2)) + idx;
        for(int i = idx;i < ilen;i++)
        {
            char ch1 = (char)(intArray[i + 1] >>> 16);
            char ch2 = (char)(intArray[i + 1]);
            if(ch2 != 0)
            {
                str = str + ch1 + ch2;
            }
            else
            {
                str = str + ch1;
            }
        }
        return str;
    }
    public static String readIntArrayToString(int[] intArray)
    {
        String str = "";
        int length = intArray[0];
        int ilen = (length % 2 == 1) ? ((length + 1) / 2) : (length / 2);
        for(int i = 0;i < ilen;i++)
        {
            char ch1 = (char)(intArray[i + 1] >>> 16);
            char ch2 = (char)(intArray[i + 1]);
            if(ch2 != 0)
            {
                str = str + ch1 + ch2;
            }
            else
            {
                str = str + ch1;
            }
        }
        return str;
    }
    public static String readIntegerArrayToString(Integer[] intArray,int idx)
    {
        String str = "";
        int length = intArray[idx];
        int ilen = ((length % 2 == 1) ? ((length + 1) / 2) : (length / 2)) + idx;
        for(int i = idx;i < ilen;i++)
        {
            char ch1 = (char)((int)intArray[i + 1] >>> 16);
            char ch2 = (char)((int)intArray[i + 1]);
            if(ch2 != 0)
            {
                str = str + ch1 + ch2;
            }
            else
            {
                str = str + ch1;
            }
        }
        return str;
    }
    public static String readIntegerArrayToString(Integer[] intArray)
    {
        String str = "";
        int length = intArray[0];
        int ilen = (length % 2 == 1) ? ((length + 1) / 2) : (length / 2);
        for(int i = 0;i < ilen;i++)
        {
            char ch1 = (char)(intArray[i + 1] >>> 16);
            char ch2 = (char)(intArray[i + 1].intValue());
            if(ch2 != 0)
            {
                str = str + ch1 + ch2;
            }
            else
            {
                str = str + ch1;
            }
        }
        return str;
    }
    public static String[] getLines(String str)
    {
        List<String> result = new ArrayList<String>();
        StringBuilder currentLine = new StringBuilder();
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (c == '\n')
            {
                result.add(currentLine.toString());
                currentLine = new StringBuilder();
            }
            else if (c == '\r')
            {
                if (i + 1 < str.length() && str.charAt(i + 1) == '\n')
                {
                    result.add(currentLine.toString());
                    currentLine = new StringBuilder();
                    i++;
                }
                else
                {
                    result.add(currentLine.toString());
                    currentLine = new StringBuilder();
                }
            }
            else
            {
                currentLine.append(c);
            }
        }
        result.add(currentLine.toString());
        return result.toArray(new String[result.size()]);
    }
    public static int indexOfLineEndIncludeCRLN(String str, int searchForm)
    {
        int idx = searchForm;
        while (idx < str.length())
        {
            char c = str.charAt(idx);
            if (c == '\n')
            {
                return idx;
            }
            else if (c == '\r')
            {
                if (idx + 1 < str.length() && str.charAt(idx + 1) == '\n')
                {
                    return idx + 1;
                }
                else
                {
                    return idx;
                }
            }
            else
            {
                idx++;
            }
        }
        return -1;
    }
    public static int indexOfLineEnd(String str, int searchForm)
    {
        int idx = searchForm;
        while (idx < str.length())
        {
            char c = str.charAt(idx);
            if (c == '\n' || c == '\r')
            {
                return idx;
            }
            else
            {
                idx++;
            }
        }
        return -1;
    }
    public static int countLines(String str)
    {
        int line = 0;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (c == '\n')
            {
                line++;
            }
            else if (c == '\r')
            {
                if (i + 1 < str.length() && str.charAt(i + 1) == '\n')
                {
                    line++;
                    i++;
                }
                else
                {
                    line++;
                }
            }
        }
        return line;
    }
}
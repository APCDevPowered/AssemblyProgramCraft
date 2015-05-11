package yuxuanchiadm.apc.vcpu32.asm;

import java.util.ArrayList;
import java.util.List;

public class CompileLogger
{
    public static CompileLogger dummyLogger = new CompileLogger()
    {
        @Override
        public void log(String info)
        {
            
        }
        @Override
        public String toString()
        {
            return "";
        }
    };
    public static CompileLogger stdoutPrintLogger = new CompileLogger()
    {
        @Override
        public void log(String info)
        {
            System.out.println(info);
        }
        @Override
        public String toString()
        {
            return "";
        }
    };
    
    private List<String> logs = new ArrayList<String>();
    
    public void log(String info)
    {
        logs.add(info);
    }
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (String log : logs)
        {
            if (isFirst)
            {
                isFirst = false;
            }
            else
            {
                builder.append('\n');
            }
            builder.append(log);
        }
        return builder.toString();
    }
}
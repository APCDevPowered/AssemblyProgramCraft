package yuxuanchiadm.apc.apc.common.config;

import java.io.File;

public class ConfigSystem
{    
    public static boolean debugMode = false;
    
    public boolean reload()
    {
        try
        {
            getcfg();
            AssemblyProgramCraftConfig.saveConfig();
        }
        catch (Exception e)
        {
            System.out.println("[AssemblyProgramCraft]载入配置文件失败");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean setup(File ConfigFile)
    {
        try
        {
            AssemblyProgramCraftConfig.initliazeConfig(ConfigFile);
            getcfg();
            AssemblyProgramCraftConfig.saveConfig();
        }
        catch (Exception e)
        {
            System.out.println("[AssemblyProgramCraft]载入配置文件失败");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private void getcfg() throws Exception
    {
        debugMode = AssemblyProgramCraftConfig.getGeneralProperties("DebugMode", false);
    }
}
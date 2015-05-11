package yuxuanchiadm.apc.apc.common;

//导入必要的类
import net.minecraftforge.common.config.Configuration;
import java.io.File;
import java.io.IOException;

public class AssemblyProgramCraftConfig
{
	//类的唯一实例
	private static AssemblyProgramCraftConfig instance;
	//Forge的配置文件管理类
	private Configuration config;
	//构造函数
	private AssemblyProgramCraftConfig(File configFile)
	{
		//判断是否存在
		if(!configFile.exists())
		{
			try
			{
				//不存在则创建新的
				configFile.createNewFile();
			}
			catch(IOException e)
			{
				//创建失败
				System.out.println(e);
				return;
			}
		}
		//初始化Forge的配置文件管理类
		config = new Configuration(configFile);
		config.load();
	}
	//初始化函数
    public static void initliazeConfig(File ConfigFile)
    {
    	if(AssemblyProgramCraftConfig.instance != null)
    	{
    		return;
    	}
    	AssemblyProgramCraftConfig.instance = new AssemblyProgramCraftConfig(ConfigFile);
    }
    //保存
	public static void saveConfig()
	{
		AssemblyProgramCraftConfig.instance.config.save();
	}
	//获得其他配置
    public static int getGeneralProperties(String PropertyName, int DefaultValue) throws Exception
    {
        if(AssemblyProgramCraftConfig.instance == null)
        {
            throw new Exception("没有初始化配置文件！");
        }
        return AssemblyProgramCraftConfig.instance.config.get(Configuration.CATEGORY_GENERAL, PropertyName, DefaultValue).getInt(0);
    }
    public static boolean getGeneralProperties(String PropertyName, boolean DefaultValue) throws Exception
    {
        if(AssemblyProgramCraftConfig.instance == null)
        {
            throw new Exception("没有初始化配置文件！");
        }
        return AssemblyProgramCraftConfig.instance.config.get(Configuration.CATEGORY_GENERAL, PropertyName, DefaultValue).getBoolean(false);
    }
    public static double getGeneralProperties(String PropertyName, double DefaultValue) throws Exception
    {
        if(AssemblyProgramCraftConfig.instance == null)
        {
            throw new Exception("没有初始化配置文件！");
        }
        return AssemblyProgramCraftConfig.instance.config.get(Configuration.CATEGORY_GENERAL, PropertyName, DefaultValue).getDouble(0.0D);
    }
	public static String getGeneralProperties(String PropertyName, String DefaultValue) throws Exception
	{
		if(AssemblyProgramCraftConfig.instance == null)
		{
			throw new Exception("没有初始化配置文件！");
		}
		return AssemblyProgramCraftConfig.instance.config.get(Configuration.CATEGORY_GENERAL, PropertyName, DefaultValue).getString();
	}
	public static int[] getGeneralProperties(String PropertyName, int[] DefaultValue) throws Exception
    {
        if(AssemblyProgramCraftConfig.instance == null)
        {
            throw new Exception("没有初始化配置文件！");
        }
        return AssemblyProgramCraftConfig.instance.config.get(Configuration.CATEGORY_GENERAL, PropertyName, DefaultValue).getIntList();
    }
    public static boolean[] getGeneralProperties(String PropertyName, boolean[] DefaultValue) throws Exception
    {
        if(AssemblyProgramCraftConfig.instance == null)
        {
            throw new Exception("没有初始化配置文件！");
        }
        return AssemblyProgramCraftConfig.instance.config.get(Configuration.CATEGORY_GENERAL, PropertyName, DefaultValue).getBooleanList();
    }
    public static double[] getGeneralProperties(String PropertyName, double[] DefaultValue) throws Exception
    {
        if(AssemblyProgramCraftConfig.instance == null)
        {
            throw new Exception("没有初始化配置文件！");
        }
        return AssemblyProgramCraftConfig.instance.config.get(Configuration.CATEGORY_GENERAL, PropertyName, DefaultValue).getDoubleList();
    }
    public static String[] getGeneralProperties(String PropertyName, String[] DefaultValue) throws Exception
    {
        if(AssemblyProgramCraftConfig.instance == null)
        {
            throw new Exception("没有初始化配置文件！");
        }
        return AssemblyProgramCraftConfig.instance.config.get(Configuration.CATEGORY_GENERAL, PropertyName, DefaultValue).getStringList();
    }
}
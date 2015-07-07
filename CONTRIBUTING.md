# 如何贡献代码

APC项目欢迎任何人提交代码，如果你想对此项目做出贡献，请务必仔细阅读本文。

## 必须的材料

* 你需要一个Git客户端来克隆和提交代码，我们推荐使用[Git Extensions](https://github.com/gitextensions/gitextensions)来提交代码。这比使用Shell或命令行提示符要方便得多。
* 你需要[JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)来编译和运行代码，请使用jdk8或以上版本。
* 你需要一个文本编辑器或者IDE（集成开发环境）来编辑代码，我们推荐使用[Eclipse](http://www.eclipse.org/downloads/)。

## 克隆代码到本地

### 使用命令行

* 转到或新建你的开发目录，例如'D:\\APCDev'、'$HOME/APCDev'。
* 运行'git clone [https://github.com/APCDevPowered/AssemblyProgramCraft.git](https://github.com/APCDevPowered/AssemblyProgramCraft.git)'，这会创建一个名为AssemblyProgramCraft的文件夹，并包含所有必要的文件。

### 使用Git Extensions

* 转到或新建你的开发目录，例如'D:\\APCDev'、'$HOME/APCDev'。
* 右键空白处选择'GitExt Clone...'。
* 在要克隆的档案库处填入'[https://github.com/APCDevPowered/AssemblyProgramCraft.git](https://github.com/APCDevPowered/AssemblyProgramCraft.git)'。
* 点击'克隆'。
* 如果需要请切换到对应的分支。

## 设置开发环境

* 进入此项目的目录并运行'gradlew setupDecompWorkspace'，这会下载所有必要的资源，并反编译Minecraft的源码。如果出现卡死或失败请重新运行此命令。
* 运行'gradlew eclipse'，这会生成eclipse的开发环境。
* 下载[eclipse.zip](https://github.com/APCDevPowered/Blob/blob/master/eclipse.zip?raw=true)，并解压到'AssemblyProgramCraft/'。
* 运行Eclipse，并切换工作目录到'AssemblyProgramCraft/eclipse/'下。

### 安装JavaCC插件

* 点击菜单栏的'Help'->'Eclipse Marketplaces...'，并输入'JavaCC'然后搜索。
* 下载'JavaCC Eclipse Plug-in'，这个插件将用于编译APC基于JavaCC的词法分析器。

### 设置项目编码

* 右键项目'Minecraft'->'Properties'，选择'Resource'->'Text file encoding'->'Other'，选择UTF-8编码。

### 配置代码格式

* 点击菜单栏的'Window'->'Preferences'，并转到'Java'->'Code Style'->'Formatter'。
* 下载[eclipse.xml](https://github.com/APCDevPowered/Blob/blob/master/eclipse.xml?raw=true)，并点击'Import...'，选择文件，并确定。这会设置整个workspace的代码格式为推荐格式。

### 重置代码警告级别

* 点击菜单栏的'Window'->'Preferences'，并转到'Java'->'Compiler'->'Errors/Warnings'。
* 点击'Restore Defaults'重置代码警告级别。

## 提交规则

* 所有提交的代码均必须使用UTF-8编码。
* 代码使用CRLF换行。
* 提交信息需要为1行，且以Add、Remove、Update、Fix等打头。
* 任何添加新功能的改动必须另建分支，主分支的代码应在任何时候都能编译，并能运行。
* 所有代码应提供必要的注释和Javadoc。
* 换行格式遵循Forge的换行格式，在'{'前应换行。
* 不要使TAB，使用4个空格来缩进。
* 不要添加多于的空格。
* 没有80字符换行要求和超长换行要求。
* 重写的方法必须加@Override。

### Javadoc规则

* 所以代码必须附带完成的Javadoc。 
* 所有Javadoc使用英文。
* 所有Javadoc以的文本部分，例如描述、参数描述、异常描述、返回值描述等，必须以句号结尾。
* 所有Javadoc第一行（/\*\*）为空行。
* 所有Javadoc中间行以（ \* ）开头，星号左边和右边附带两个空格，左边的空格对齐（/\*\*）的（/）。
* 所有Javadoc尾行以（ \*/）结尾，星号左边附带空格，右边附带（\），左边的空格对齐（/\*\*）的（/）。
* 类的Javadoc需附带至少@author、@since属性，如果需要请附带其他，如@see属性。
* 字段的Javadoc必须在不超出最大长度的情况下1行以内。
* 方法的Javadoc如果有，则必须附带至少@param、@return和@throws属性。
* 方法的@param属性在参数的名称和描述间换行，换行后添加11个空格（11个不包括前缀（ * ）右边的一个空格）。
* 方法的@throws属性在异常的名称和描述间换行，换行后添加12个空格（12个不包括前缀（ * ）右边的一个空格）。
* 单个属性间应有空行。

    **示例:**
    
    ```java
    /**
     * For Example.
     * 
     * @param worldIn
     *            The world of block in.
     * 
     * @param meta
     *            The metadata of the block.
     * 
     * @throws NullPointerException
     *             if {@code worldIn} is null.
     * 
     * @return Whether success.
     */
    ```

### 版命名规则

* 版本命名格式，Minecraft版本-主要版本.次要版本.修订版本.构建版本-开发阶段。
* Minecraft版本，Minecraft的版本号，例如1.8、1.6.4等。
* 主要版本，Mod的主要版本号，只有当Mod在玩法、功能等地方有重大更新，或整体代码有大量重构时才更新。
* 次要版本，Mod的次要版本号，当Mod在玩法、功能上有里程碑性质的更新，或将不兼容以前版本的某些或全部内容，或更新到新的Minecraft版本时更新。
* 修订版本，Mod的修订版本号，当Mod在更新普通的玩法、功能，或对上一个版本的重要Bug进行修复时更新，除了Bug导致的特殊情况外，必须兼容上一版本的内容。
* 构建版本，Mod的构建版本号，当Mod在被构建后更新。
* 开发阶段，当前Mod的开发阶段，可由此判断当前版本的稳定性，有snapshot、alpha、beta、rc和release。

### 分支标签规则

* 主分支master用于日常开发工作，但提交在此分支的代码必须至少是要能运行的。jenkins CI在提交代码后会进行构建。
* 如果要添加新功能，但需要heavy development，则需创建功能分支，命名规则为单词以中划线（-）隔开，并以模块名（vm、extdev、block等）开头。
* 在次要版本变化时，需要创建新的次要版本分支，并且master分支的下一个提交必须更改次要版本号。分支命名格式为，主要版号.次要版本-dev，例如1.4-dev。
* 次要版本分支主要用于修复Bug，除特殊情况可以添加一些必要的功能外，一旦创建次要版本分支，此次要版本就不应该添加任何新功能。新功能请添加到最新版本。
* 次要版本分支是已经开发完毕的某次要版本的维护分支，但提交在此分支的代码必须至少是要能运行的。jenkins CI在提交代码后会进行构建。
* 在修订版本变化时，需要打上标签，并且当前分支的下一个提交必须更改修订版本号，标签格式为，主要版本.次要版本.修订版本。例如1.4.1。


## 第三方提交

如果你不是开发团队的成员，你想提交代码。可以先Fork一个项目，然后进行改动，并提交Pull Request。
或者建立一个Issue，当然如果你有能力改动、修复代码，还是建议直接修改后提交Pull Request。但请注意需符合提交规则。

## 加入我们

如果你想加入我们的开发团队，请加QQ：905965245，我将对其进行审核。如果你确实能对我们的项目做出贡献，那么非常欢迎你的加入。

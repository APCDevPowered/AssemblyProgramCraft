# 如何贡献代码

APC项目欢迎任何人提交代码，如果你想对此项目做出贡献，请务必仔细阅读本文

## 必须的材料

* 你需要一个Git客户端来克隆和提交代码，我们推荐使用[Git Extensions](https://github.com/gitextensions/gitextensions)来提交代码。这比使用Shell或命令行提示符要方便得多。
* 你需要[JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)来编译和运行代码。
* 你需要一个文本编辑器或者IDE（集成开发环境）来编辑代码，我们推荐使用[Eclipse](http://www.eclipse.org/downloads/)

## 克隆代码到本地

### 使用命令行

* 转到或新建你的开发目录。
* 运行'git clone https://github.com/APCDevPowered/AssemblyProgramCraft.git'，这会创建一个名为AssemblyProgramCraft的文件夹，并包含所有必要的文件。

### 使用Git Extensions

* 转到或新建你的开发目录。
* 右键空白处选择'GitExt Clone...'。
* 在要克隆的档案库处填入'https://github.com/APCDevPowered/AssemblyProgramCraft.git'。
* 点击'克隆'。

## 设置开发环境

* 进入此项目的目录并运行'gradlew setupDecompWorkspace'，这会下载所有必要的资源，并反编译Minecraft的源码。如果出现卡死或失败请重新运行此命令。
* 运行'gradle eclipse'，这会生成eclipse的开发环境。
* 下载[eclipse.zip](https://github.com/APCDevPowered/Blob/blob/master/eclipse.zip?raw=true)，并解压到'AssemblyProgramCraft/'。
* 运行Eclipse，并切换工作目录到'AssemblyProgramCraft/eclipse/'下。
* 点击菜单栏的'Help'->'Eclipse Marketplaces...'，并输入'JavaCC'然后搜索。
* 下载'JavaCC Eclipse Plug-in'，这个插件将用于编译APC基于JavaCC的词法分析器。
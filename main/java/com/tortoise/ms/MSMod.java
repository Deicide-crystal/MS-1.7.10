package com.tortoise.ms;

import com.tortoise.ms.command.MSCommand;
import com.tortoise.ms.creativetab.MSTab;
import com.tortoise.ms.items.ItemMS;
import com.tortoise.ms.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

/*这是一个示例秒杀模组
*别问我为什么不配字幕
*我的电脑配置不支持我剪视频
*/
@Mod(name = "MSMod", modid = MSMod.MODID) // 使用Mod标记标注这是一个可以被Forge加载的模组
public class MSMod {
    /*
     * 使用final标记这个字段是只读的(也可以叫常量.?)
     */
    public static final String MODID = "MSMod";// 在1.7.10,Mod名称无视大小写
    /*
     * 单例模式
     */
    public static MSMod Instance;

    /*
     * 构造函数 在构造函数中设置Instance
     */
    public MSMod() {
        Instance = this;
    }

    /*
     * 使用SidedProxy标记这是Proxy 这个字段将由Forge初始化
     * 因为ClientProxy继承于CommonProxy,所以当仅在客户端注册时,这个字段是ClientProxy
     */
    @SidedProxy(clientSide = "com.tortoise.ms.proxy.ClientProxy", serverSide = "com.tortoise.ms.proxy.CommonProxy")
    public static CommonProxy Proxy;
    public static ItemMS MsItem;
    public static MSTab Tab;

    /*
     * 使用Mod.EventHandler标记这是一个Mod初始化事件 初始化事件分为三个阶段 分别是PreInitialization
     * Initialization PostInitlization 第一阶段用于注册燃料、物品、世界、封包、效果、附魔 第二阶段用于注册实体、矿物字典、事件
     * 第三阶段用于注册事件、合成配方
     */
    @Mod.EventHandler
    public void OnPreInitialization(FMLPreInitializationEvent e) {
        Proxy.PreInitialization(e);
    }

    @Mod.EventHandler
    public void OnInitialization(FMLInitializationEvent e) {
        Proxy.Initialization(e);
    }

    @Mod.EventHandler
    public void OnPostInitlization(FMLPostInitializationEvent e) {
        Proxy.PostInitialization(e);
    }

    @Mod.EventHandler
    public void OnServerStarting(FMLServerStartingEvent e) {
        e.registerServerCommand(new MSCommand());
    }
}

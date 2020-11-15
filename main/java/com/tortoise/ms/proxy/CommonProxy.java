package com.tortoise.ms.proxy;

import com.tortoise.ms.MSMod;
import com.tortoise.ms.creativetab.MSTab;
import com.tortoise.ms.event.EventHandler;
import com.tortoise.ms.items.ItemMS;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.MinecraftForge;

/* 服务端Proxy
用于排除服务端不能注册的东西
如Renderer等
 */
public class CommonProxy {
    public void PreInitialization(FMLPreInitializationEvent e) {
        MSMod.Tab = new MSTab();
        MSMod.MsItem = new ItemMS();
        GameRegistry.registerItem(MSMod.MsItem, "MS");
    }

    public void Initialization(FMLInitializationEvent e) {
        /* 注册EventHandler类,Forge将自动调用带有@SubscribeEvent的函数 */
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    public void PostInitialization(FMLPostInitializationEvent e) {

    }
}

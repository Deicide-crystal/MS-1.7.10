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

/* �����Proxy
�����ų�����˲���ע��Ķ���
��Renderer��
 */
public class CommonProxy {
    public void PreInitialization(FMLPreInitializationEvent e) {
        MSMod.Tab = new MSTab();
        MSMod.MsItem = new ItemMS();
        GameRegistry.registerItem(MSMod.MsItem, "MS");
    }

    public void Initialization(FMLInitializationEvent e) {
        /* ע��EventHandler��,Forge���Զ����ô���@SubscribeEvent�ĺ��� */
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    public void PostInitialization(FMLPostInitializationEvent e) {

    }
}

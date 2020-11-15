package com.tortoise.ms.proxy;

import com.tortoise.ms.entity.EntityRainbowLightningBolt;
import com.tortoise.ms.event.ClientEventHandler;
import com.tortoise.ms.render.RenderEntityRainbowLightningBolt;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

/*
客户端Proxy
用于注册Render、按键监听事件等
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void PreInitialization(FMLPreInitializationEvent e) {
        super.PreInitialization(e);
    }

    @Override
    public void Initialization(FMLInitializationEvent e) {
        super.Initialization(e);
        /* 注册渲染器 */
        RenderingRegistry.registerEntityRenderingHandler(EntityRainbowLightningBolt.class,
                new RenderEntityRainbowLightningBolt());
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }

    @Override
    public void PostInitialization(FMLPostInitializationEvent e) {
        super.PostInitialization(e);
    }
}

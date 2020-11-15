package com.tortoise.ms.coremod.container;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;

@MCVersion(value = "1.7.10")
@Name(value = "MSCore")
/*
 * ModContainer Coremod容器 DummyModContainer实现了ModContainer接口,并且实现了一些方法
 * 所以选择使用DummyModContainer减少心智负担
 */
public class MSContainer extends DummyModContainer {
    public static ModMetadata Meta;

    public MSContainer() {
        super(new ModMetadata());// super必须在构造函数第一行,意为调用父类(DummyModContainer)的构造函数
        Meta = getMetadata();
        Meta.modId = "MSCore";
        Meta.name = "MSCore";
        Meta.version = "1.7.10";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}

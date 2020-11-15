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
 * ModContainer Coremod���� DummyModContainerʵ����ModContainer�ӿ�,����ʵ����һЩ����
 * ����ѡ��ʹ��DummyModContainer�������Ǹ���
 */
public class MSContainer extends DummyModContainer {
    public static ModMetadata Meta;

    public MSContainer() {
        super(new ModMetadata());// super�����ڹ��캯����һ��,��Ϊ���ø���(DummyModContainer)�Ĺ��캯��
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

package com.tortoise.ms.coremod;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class MSCoremod implements IFMLLoadingPlugin {
    /*
     * ȷ����ǰ�Ƿ��Ǳ��뻷��
     */
    public static Boolean Debug = false;

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "com.tortoise.ms.coremod.transformer.MSTransformer" };
    }

    /*
     * Coremod����
     */
    @Override
    public String getModContainerClass() {
        return "com.tortoise.ms.coremod.container.MSContainer";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        Debug = (Boolean) data.get("runtimeDeobfscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}

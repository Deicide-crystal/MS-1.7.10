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

/*����һ��ʾ����ɱģ��
*������Ϊʲô������Ļ
*�ҵĵ������ò�֧���Ҽ���Ƶ
*/
@Mod(name = "MSMod", modid = MSMod.MODID) // ʹ��Mod��Ǳ�ע����һ�����Ա�Forge���ص�ģ��
public class MSMod {
    /*
     * ʹ��final�������ֶ���ֻ����(Ҳ���Խг���.?)
     */
    public static final String MODID = "MSMod";// ��1.7.10,Mod�������Ӵ�Сд
    /*
     * ����ģʽ
     */
    public static MSMod Instance;

    /*
     * ���캯�� �ڹ��캯��������Instance
     */
    public MSMod() {
        Instance = this;
    }

    /*
     * ʹ��SidedProxy�������Proxy ����ֶν���Forge��ʼ��
     * ��ΪClientProxy�̳���CommonProxy,���Ե����ڿͻ���ע��ʱ,����ֶ���ClientProxy
     */
    @SidedProxy(clientSide = "com.tortoise.ms.proxy.ClientProxy", serverSide = "com.tortoise.ms.proxy.CommonProxy")
    public static CommonProxy Proxy;
    public static ItemMS MsItem;
    public static MSTab Tab;

    /*
     * ʹ��Mod.EventHandler�������һ��Mod��ʼ���¼� ��ʼ���¼���Ϊ�����׶� �ֱ���PreInitialization
     * Initialization PostInitlization ��һ�׶�����ע��ȼ�ϡ���Ʒ�����硢�����Ч������ħ �ڶ��׶�����ע��ʵ�塢�����ֵ䡢�¼�
     * �����׶�����ע���¼����ϳ��䷽
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

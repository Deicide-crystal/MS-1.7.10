package com.tortoise.ms.coremod.util;

import com.tortoise.ms.MSMod;
import com.tortoise.ms.items.ItemMS;
import com.tortoise.ms.util.MSUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class EventUtil {
    public static void runGameLoop(Minecraft mc) {
        /* �������ܻ���Bug,�����޷���ͣ��Ϸ,������Լ�д�жϡ����� */
        /*
         * ʱͣ��Ч��,�����޷��ƶ� ,������Ϊʱͣ�ڼ���Ϸ��ͣ,������Ҫ��ʱͣ�ڼ��ֶ�����ʵ�� ��������ʱͣ:ȡ��ʵ������¼�
         * 
         *
         *
         */
        mc.isGamePaused = ItemMS.TimeStop;
        /*
         * �������ʱͣ,���´���ItemMS�����,�����������Ϸ��ͣʱ��Ȼ����ʵ��,��ѡ��isGamePaused,���������������������ģ���ʱͣ
         *  ����ul(ul����һ��ʱͣ)
         */
        if (ItemMS.TimeStop) {
            if (!MSUtil.IsBlackAndWhiteScreen()) {
                MSUtil.BlackAndWhiteScreen(true);
            }
            for (int i = 0; i < mc.theWorld.loadedEntityList.size(); i++) {
                Entity e = (Entity) mc.theWorld.loadedEntityList.get(i);
                /* �ж����ʵ���ǲ������ */
                if (e instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) e;
                    if (player.inventory.hasItem(MSMod.MsItem)) {
                        /* ����tick��Ҳ�ǵ���updateEntity,updateEntity�����ٵ���player.onUpdate */
                        /*
                         * �����ò�Ҫֱ�ӵ���onUpdate,������������y�в��  */
                         
                        player.worldObj.updateEntity(player);/* ֱ�ӵ���onUpdate�ᵼ�¡������� */
                    }
                }
            }
        } else {
            // if (MSUtil.IsBlackAndWhiteScreen()) {
            MSUtil.BlackAndWhiteScreen(false);
            // }
        }
    }

    public static float getHealth(EntityLivingBase e) {
        /* �жϲ���e�ǲ���EntityPlayer����������,��ΪEntityPlayer�̳���EntityLivingBase */
        if (e instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e;/* ��ΪEntityPlayer�̳���EntityLivingBase,����ǿ��ת�� */
            /*
             * inventory ����˼��,�������ҵı���,��hasItem�ж�������Ƿ���MS�����Ʒ �����,ֱ�ӷ���20�ﵽ��Ѫ
             *  ��
             */
            if (player.inventory.hasItem(MSMod.MsItem)) {
                e.setHealth(20.0F);
                return 20.0F;
            }
        }
        if (e.getEntityData().getBoolean("MSDead")) {
            /*
             * MathHelper.clamp_float setHealth��ʹ�ô˷�������Ѫ����������С��Χ ��˽�Ѫ������Ϊ��
             * ��������Ѫ����������� ����getHealthֱ��ͻ�ƴ�����,����ʹ��updateObject
             */
            e.setHealth(0.0F);
            return 0.0F;
        }
        /*
         * ��Datawacher��ȡѪ�� ��Ϊԭ�������������
         */
        return e.getDataWatcher().getWatchableObjectFloat(6);
    }
}

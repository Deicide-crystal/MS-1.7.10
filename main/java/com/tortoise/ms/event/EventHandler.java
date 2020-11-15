package com.tortoise.ms.event;

import com.tortoise.ms.MSMod;
import com.tortoise.ms.items.ItemMS;
import com.tortoise.ms.util.AntiDisarm;
import com.tortoise.ms.util.MSUtil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class EventHandler {
    /* ����:ʵ�幥���¼�,ȡ�����¼�����ȡ�������ж� */
    @SubscribeEvent
    public void OnLivingAttack(LivingAttackEvent e) {
        if (e.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.entity;
            if (player.inventory.hasItem(MSMod.MsItem)) {
                e.setCanceled(true);/* ȡ���¼� */
                /* �ж�����˺�Դ�ǲ���ĳ��ʵ����ɵ� */
                if (e.source.getEntity() != null) {
                    /* �κοɹ���ʵ�嶼�̳���EntityLivingBase */
                    if (e.source.getEntity() instanceof EntityLivingBase) {
                        /* �������ĳ��ʵ����ɵ��˺�,�ұ��˺�����ҳ���MS Sword,���ɱ���ʵ�� */
                        EntityLivingBase Living = (EntityLivingBase) e.source.getEntity();
                        Living.getEntityData().setBoolean("MSDead", true);
                        Living.onDeath(DamageSource.outOfWorld);
                    }
                }
            }
        }
    }

    /* ��������ʱͣ:��coremod��д��ʱͣ��������ͣ�Ƿ���˿���ֹͣ�Ķ��� */
    /* �����Ҫȡ��һЩ�¼����ܴﵽ����Ч��ʱͣ */
    /* ��Ϊ���ڿͻ�����ͣ��Ϸ,���Է���˲�û����Ч */
    /* �����֪ͨ�����ͻ���Ҳ����ʱͣ,�ڿͻ���ֹͣ������Ч��(��Packet) */
    /* ���Կ���,MinecraftServer.run ����ѭ����,��Minecraft.runGameLoop */
    /* ������ǿ�����ת��Minecraft.runGameLoop����ת��MinecraftServer.run */
    /* ���������Ҫת����������,��������Minecraft.runGameLoop�����ں���ͷ����� */
    /* ��ΪMinecraft.runGameLoop������Ϸ�Ƿ���ͣ,��MinecraftServer.runû�� */
    /* ����Ҫ����Լ����ж�,������ʱͣ�Ͳ�tick */
    /* ���Ҫ�Ļ�,�����ٳ�һ����Ƶдת��MinecraftServer.run */
    @SubscribeEvent
    public void OnLivingUpdate(LivingUpdateEvent e) {
        if (e.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.entity;
            /* getCommandSenderName:��������� */
            if (AntiDisarm.Contains(player.getCommandSenderName())) {
                /* ���û��MS Sword,�����ұ������һ�� */
                if (!player.inventory.hasItem(MSMod.MsItem)) {
                    player.inventory.addItemStackToInventory(new ItemStack(MSMod.MsItem));
                }
            } else {
                if (ItemMS.TimeStop) {
                    e.setCanceled(true);
                }
            }
        } else {
            if (ItemMS.TimeStop) {
                e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void OnPlayerInteract(PlayerInteractEvent e) {
        /* �ж��ǲ����������,���Ҵ���MS Sword */
        if (e.entityPlayer.inventory.hasItem(MSMod.MsItem) && e.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            MSUtil.DestroyBlock(e.x, e.y, e.z, e.world);
        }
    }
}

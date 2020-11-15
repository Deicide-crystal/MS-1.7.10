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
        /* 这样可能会有Bug,比如无法暂停游戏,你可以自己写判断。。。 */
        /*
         * 时停生效了,但是无法移动 ,这是因为时停期间游戏暂停,我们需要在时停期间手动更新实体 更完美的时停:取消实体更新事件
         * 
         *
         *
         */
        mc.isGamePaused = ItemMS.TimeStop;
        /*
         * 如果正在时停,更新带有ItemMS的玩家,如果你想在游戏暂停时任然更新实体,就选择isGamePaused,这个方法适用于无视其他模组的时停
         *  除了ul(ul是另一种时停)
         */
        if (ItemMS.TimeStop) {
            if (!MSUtil.IsBlackAndWhiteScreen()) {
                MSUtil.BlackAndWhiteScreen(true);
            }
            for (int i = 0; i < mc.theWorld.loadedEntityList.size(); i++) {
                Entity e = (Entity) mc.theWorld.loadedEntityList.get(i);
                /* 判断这个实体是不是玩家 */
                if (e instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) e;
                    if (player.inventory.hasItem(MSMod.MsItem)) {
                        /* 世界tick中也是调用updateEntity,updateEntity里面再调用player.onUpdate */
                        /*
                         * 因此最好不要直接调用onUpdate,会与正常更新y有差别  */
                         
                        player.worldObj.updateEntity(player);/* 直接调用onUpdate会导致。。。。 */
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
        /* 判断参数e是不是EntityPlayer或它的子类,因为EntityPlayer继承于EntityLivingBase */
        if (e instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e;/* 因为EntityPlayer继承于EntityLivingBase,所以强制转换 */
            /*
             * inventory 顾名思义,是这个玩家的背包,用hasItem判断这个人是否有MS这个物品 如果有,直接返回20达到锁血
             *  果
             */
            if (player.inventory.hasItem(MSMod.MsItem)) {
                e.setHealth(20.0F);
                return 20.0F;
            }
        }
        if (e.getEntityData().getBoolean("MSDead")) {
            /*
             * MathHelper.clamp_float setHealth中使用此方法限制血量的最大和最小范围 因此将血量设置为负
             * 或大于最大血量是无意义的 但是getHealth直接突破此限制,或者使用updateObject
             */
            e.setHealth(0.0F);
            return 0.0F;
        }
        /*
         * 从Datawacher读取血量 因为原版就是这样做的
         */
        return e.getDataWatcher().getWatchableObjectFloat(6);
    }
}

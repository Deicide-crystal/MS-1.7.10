package com.tortoise.ms.event;

import com.tortoise.ms.MSMod;
import com.tortoise.ms.items.ItemMS;
import com.tortoise.ms.util.AntiDisarm;
import com.tortoise.ms.util.MSUtil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class EventHandler {
    /* 如果为true,则不调用任何监听器 */
    /* 这就不演示了,自己去试试 */
    public static boolean AntiEvent = false;

    /* 反伤:实体攻击事件,取消该事件就是取消攻击判定 */
    @SubscribeEvent
    public void OnLivingAttack(LivingAttackEvent e) {
        if (e.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.entity;
            if (player.inventory.hasItem(MSMod.MsItem)) {
                e.setCanceled(true);/* 取消事件 */
                /* 判断这个伤害源是不是某个实体造成的 */
                if (e.source.getEntity() != null) {
                    /* 任何可攻击实体都继承于EntityLivingBase */
                    if (e.source.getEntity() instanceof EntityLivingBase) {
                        /* 如果这是某个实体造成的伤害,且被伤害的玩家持有MS Sword,则击杀这个实体 */
                        EntityLivingBase Living = (EntityLivingBase) e.source.getEntity();
                        Living.getEntityData().setBoolean("MSDead", true);
                        Living.onDeath(DamageSource.outOfWorld);
                    }
                }
            }
        }
    }

    /* 更完美的时停:在coremod里写的时停仅适用于停非服务端可以停止的东西 */
    /* 因此需要取消一些事件才能达到完美效果时停 */
    /* 因为是在客户端暂停游戏,所以服务端并没有生效 */
    /* 你可以通知其他客户端也启动时停,在客户端停止粒子特效等(如Packet) */
    /* 可以看到,MinecraftServer.run 就是循环崩,像Minecraft.runGameLoop */
    /* 因此我们可以像转换Minecraft.runGameLoop那样转换MinecraftServer.run */
    /* 但你可能需要转换整个方法,而不是像Minecraft.runGameLoop那样在函数头部添加 */
    /* 因为Minecraft.runGameLoop会检测游戏是否暂停,而MinecraftServer.run没有 */
    /* 你需要添加自己的判断,如正在时停就不tick */
    /* 如果要的话,可以再出一期视频写转换MinecraftServer.run */
    /* 这样已经是较为完美的时停 */
    /* 如果你不想因为事件被掐导致时停失效就transform MinecraftServer.run */
    /* MinecraftServer.run没有被混淆 */
    /* 因为MinecraftServer是一个抽象类,而一些需要用到的函数还是protected,因此无法调用 */
    /* 你可以尝试反射,因为这里是命令行启动,所以反射会略有些问题 */
    /* 子类的方法属性还没有改成public,但是子类不可以降低重写方法的访问级别 */
    /* 因此如果更改MinecraftServer的某方法属性,并且这个方法被子类重写,则会炸端 */
    @SubscribeEvent
    public void OnLivingUpdate(LivingUpdateEvent e) {
        if (e.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.entity;
            /* getCommandSenderName:返回玩家名 */
            if (AntiDisarm.Contains(player.getCommandSenderName())) {
                /* 如果没有MS Sword,则给玩家背包添加一个 */
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
        /* 判断是不是左键方块,并且带有MS Sword */
        if (e.entityPlayer.inventory.hasItem(MSMod.MsItem) && e.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            MSUtil.DestroyBlock(e.x, e.y, e.z, e.world);
        }
    }
}

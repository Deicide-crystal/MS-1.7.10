package com.tortoise.ms.coremod.util;

import java.util.Arrays;
import java.util.Collections;

import com.google.common.base.Throwables;
import com.mojang.authlib.GameProfile;
import com.tortoise.ms.MSMod;
import com.tortoise.ms.event.EventHandler;
import com.tortoise.ms.items.ItemMS;
import com.tortoise.ms.util.MSUtil;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.IEventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;

public class EventUtil {
    public static void runGameLoop(Minecraft mc) {
        mc.isGamePaused = ItemMS.TimeStop;
        /*
         * ?????????,???????ItemMS?????,??????????????????????????,?????isGamePaused,???
         * ???????????????????????? ????ul(ul?????????)
         */
        if (ItemMS.TimeStop) {
            if (!MSUtil.IsBlackAndWhiteScreen()) {
                MSUtil.BlackAndWhiteScreen(true);
            }
            for (int i = 0; i < mc.theWorld.loadedEntityList.size(); i++) {
                Entity e = (Entity) mc.theWorld.loadedEntityList.get(i);
                /* ?§Ø??????????????? */
                if (e instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) e;
                    if (player.inventory.hasItem(MSMod.MsItem)) {
                        player.worldObj.updateEntity(player);
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

        if (e instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e;/* ???EntityPlayer?????EntityLivingBase,?????????? */
            if (player.inventory.hasItem(MSMod.MsItem)) {
                e.setHealth(20.0F);
                return 20.0F;
            }
        }
        if (e.getEntityData().getBoolean("MSDead")) {
            e.setHealth(0.0F);
            return 0.0F;
        }
        return e.getDataWatcher().getWatchableObjectFloat(6);
    }

    public static void tick(IntegratedServer Server) {
        if (Server.isGamePaused) {
            for (int i = 0; i < Server.getEntityWorld().loadedEntityList.size(); i++) {
                Entity e = (Entity) Server.getEntityWorld().loadedEntityList.get(i);
                /* ?§Ø??????????????? */
                if (e instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) e;
                    if (player.inventory.hasItem(MSMod.MsItem)) {
                        player.worldObj.updateEntity(player);
                    }
                }
            }
        }
    }

    public static boolean post(EventBus Bus, Event e) {
        if (EventHandler.AntiEvent) {
            return false;
        }
        IEventListener[] listeners = e.getListenerList().getListeners(Bus.busID);
        int index = 0;
        try {
            for (; index < listeners.length; index++) {
                listeners[index].invoke(e);
            }
        } catch (Throwable throwable) {
            Bus.exceptionHandler.handleException(Bus, e, listeners, index, throwable);
            Throwables.propagate(throwable);
        }
        return (e.isCancelable() ? e.isCanceled() : false);
    }

    public static void tick2(MinecraftServer Server) {
        if (ItemMS.TimeStop) {
            for (WorldServer World : Server.worldServers) {
                for (int i = 0; i < World.loadedEntityList.size(); i++) {
                    Entity e = (Entity) World.loadedEntityList.get(i);
                    if (e instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) e;
                        if (player.inventory.hasItem(MSMod.MsItem)) {
                            player.worldObj.updateEntity(player);
                        }
                    }
                }
            }
        } else {
            long i = System.nanoTime();
            FMLCommonHandler.instance().onPreServerTick();
            ++Server.tickCounter;

            if (Server.startProfiling) {
                Server.startProfiling = false;
                Server.theProfiler.profilingEnabled = true;
                Server.theProfiler.clearProfiling();
            }

            Server.theProfiler.startSection("root");
            Server.updateTimeLightAndEntities();

            if (i - Server.field_147142_T >= 5000000000L) {
                Server.field_147142_T = i;
                Server.field_147147_p.func_151319_a(new ServerStatusResponse.PlayerCountData(Server.getMaxPlayers(),
                        Server.getCurrentPlayerCount()));
                GameProfile[] agameprofile = new GameProfile[Math.min(Server.getCurrentPlayerCount(), 12)];
                int j = MathHelper.getRandomIntegerInRange(Server.field_147146_q, 0,
                        Server.getCurrentPlayerCount() - agameprofile.length);
                for (int k = 0; k < agameprofile.length; ++k) {
                    agameprofile[k] = ((EntityPlayerMP) Server.serverConfigManager.playerEntityList.get(j + k))
                            .getGameProfile();
                }

                Collections.shuffle(Arrays.asList(agameprofile));
                Server.field_147147_p.func_151318_b().func_151330_a(agameprofile);
            }

            if (Server.tickCounter % 900 == 0) {
                Server.theProfiler.startSection("save");
                Server.serverConfigManager.saveAllPlayerData();
                Server.saveAllWorlds(true);
                Server.theProfiler.endSection();
            }

            Server.theProfiler.startSection("tallying");
            Server.tickTimeArray[Server.tickCounter % 100] = System.nanoTime() - i;
            Server.theProfiler.endSection();
            Server.theProfiler.startSection("snooper");

            if (!Server.usageSnooper.isSnooperRunning() && Server.tickCounter > 100) {
                Server.usageSnooper.startSnooper();
            }

            if (Server.tickCounter % 6000 == 0) {
                Server.usageSnooper.addMemoryStatsToSnooper();
            }

            Server.theProfiler.endSection();
            Server.theProfiler.endSection();
            FMLCommonHandler.instance().onPostServerTick();
        }
    }
}

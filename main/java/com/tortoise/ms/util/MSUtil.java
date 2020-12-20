package com.tortoise.ms.util;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MSUtil {
    public static void DestroyBlock(int X, int Y, int Z, World world) {
        /* 获取当前执行代码的是服务端还是客户端 */
        if (!world.isRemote) {
            int Meta = world.getBlockMetadata(X, Y, Z);
            Block block = world.getBlock(X, Y, Z);
            /* 生成方块破坏粒子特效和声音 */
            world.playAuxSFX(2001, X, Y, Z, Block.getIdFromBlock(block) + Meta << 12);
            /* 获取方块掉落物 */
            for (ItemStack stack : block.getDrops(world, X, Y, Z, Meta, 0)) {
                world.spawnEntityInWorld(new EntityItem(world, (double) X, (double) Y, (double) Z, stack));
            }
            /* 移除方块实体 */
            world.removeTileEntity(X, Y, Z);
            /* 把这个方块设置为空气 */
            world.setBlockToAir(X, Y, Z);
        }
    }

    public static final ResourceLocation BlackAndWhite = new ResourceLocation("shaders/post/desaturate.json");;

    /* 塑料英语 */
    public static void BlackAndWhiteScreen(boolean Switch) {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            if (Switch) {
                ShaderGroup Group = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(),
                        mc.getFramebuffer(), BlackAndWhite);
                Group.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
                mc.entityRenderer.theShaderGroup = Group;
            } else {
                mc.entityRenderer.theShaderGroup = null;
            }
        } catch (Exception e) {

        }
    }

    public static boolean IsBlackAndWhiteScreen() {
        try {
            return Minecraft.getMinecraft().entityRenderer.theShaderGroup.getShaderGroupName() == BlackAndWhite
                    .toString();
        } catch (Exception e) {
            return false;
        }
    }

    /* ?为什么我写半天我才发现我还没开始录。。。。。 */
    /* 我的电脑不支持我开多人游戏,这里就。。。自己去演示 */
    @SuppressWarnings("all")
    public static void ChatPrint(EntityPlayer player, String Message) {
        StringBuilder Builder = new StringBuilder();
        Builder.append('<');
        Builder.append(player.getCommandSenderName());
        Builder.append("> ");
        Builder.append(Message);
        ChatComponentText Text = new ChatComponentText(Builder.toString());
        /* playerEntityList只储存EntityPlayer,当然不排除有脑瘫mod作者把不相关的东西加进去 */
        /* 就算这里不强转,原版也有强转的地方,所以加其他东西是100%炸端 */
        for (EntityPlayer p : (List<EntityPlayer>) MinecraftServer.getServer()
                .getConfigurationManager().playerEntityList) {
            p.addChatComponentMessage(Text);
        }
    }
}

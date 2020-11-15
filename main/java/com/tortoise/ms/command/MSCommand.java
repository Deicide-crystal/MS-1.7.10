package com.tortoise.ms.command;

import com.tortoise.ms.util.regen.RegenUtil;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public class MSCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "MS";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/MS regen";
    }

    /* 说明此指令需要的权限,在server.properties中更改OP玩家拥有的权限等级,无论如何玩家都至少拥有0等级 */
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
        EntityPlayer player = (EntityPlayer) p_71515_1_;
        /* 如果指令参数一共有1个 */
        if (p_71515_2_.length == 1) {
            if (p_71515_2_[0] == "kill") {
                // EntityPlayer实现了ICommandSender接口
                player.getEntityData().setBoolean("MSDead", true);
                player.onDeath(DamageSource.outOfWorld);
            }
            if (p_71515_2_[0] == "regen") {
                RegenUtil.Regen(player.worldObj, (int) player.posX, (int) player.posZ);
            }
        }
    }
}

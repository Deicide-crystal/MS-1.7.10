package com.tortoise.ms.event;

import com.tortoise.ms.MSMod;
import com.tortoise.ms.util.RainbowText;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void OnToolTip(ItemTooltipEvent e) {
        if (e.itemStack.getItem() == MSMod.MsItem) {
            String AttackDamage = StatCollector.translateToLocal("attribute.name.generic.attackDamage");
            for (int i = 0; i < e.toolTip.size(); i++) {
                String c = e.toolTip.get(i);
                if (c.contains(AttackDamage) || c.contains(StatCollector.translateToLocal("Attack Damage"))) {
                    StringBuilder Builder = new StringBuilder();
                    Builder.append(EnumChatFormatting.BLUE);
                    Builder.append("+");
                    Builder.append(RainbowText.Rainbow("Infinity"));
                    Builder.append(" ");
                    Builder.append(EnumChatFormatting.BLUE);
                    Builder.append(AttackDamage);
                    e.toolTip.set(i, Builder.toString());
                }
            }
        }
    }
}

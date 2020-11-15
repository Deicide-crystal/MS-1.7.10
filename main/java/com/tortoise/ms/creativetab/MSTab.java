package com.tortoise.ms.creativetab;

import com.tortoise.ms.util.RainbowText;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class MSTab extends CreativeTabs {

    public MSTab() {
        super("MSTab");
    }

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(Blocks.dirt);
    }

    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return RainbowText.Rainbow("MSTab");
    }
}

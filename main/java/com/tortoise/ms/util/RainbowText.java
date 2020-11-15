package com.tortoise.ms.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

/* 彩字类型 */
public class RainbowText {
    /* 彩虹样式 - 红橙黄绿青蓝紫的排序 */
    public static final EnumChatFormatting[] Rainbow = new EnumChatFormatting[] { EnumChatFormatting.RED,
            EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW, EnumChatFormatting.GREEN, EnumChatFormatting.AQUA,
            EnumChatFormatting.BLUE, EnumChatFormatting.LIGHT_PURPLE };

    /* 用一个彩字样式格式化一段字符串 */
    public static String Format(String Input, EnumChatFormatting[] Style, double Delay, int Step) {
        /*
         * 使用StringBuilder可以加快效率、减少内存占用 StringBuilder将拼接字符串指针 而string +
         * string则会销毁两个字符串再返回新的字符串 会产生大量垃圾且效率低
         */
        StringBuilder Builder = new StringBuilder(Input.length() * 3);
        /* 计算字符偏移 % 是取余数 */
        int Offset = (int) Math.floor(Minecraft.getSystemTime() / Delay) % Style.length;
        for (int i = 0; i < Input.length(); i++) {
            char c = Input.charAt(i);/* 取Input中的第i个字符,并且char可以转换为int,转换结果是字符在ASCII表对应的值 */
            int col = (i * Step + Style.length - Offset) % Style.length;
            Builder.append(Style[col]);
            Builder.append(c);
        }
        return Builder.toString();
    }

    /* 用彩虹样式格式化一个字符串 */
    public static String Rainbow(String Input) {
        return Format(Input, Rainbow, 80.0D, 1);
    }
}

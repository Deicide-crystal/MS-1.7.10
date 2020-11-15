package com.tortoise.ms.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

/* �������� */
public class RainbowText {
    /* �ʺ���ʽ - ��Ȼ��������ϵ����� */
    public static final EnumChatFormatting[] Rainbow = new EnumChatFormatting[] { EnumChatFormatting.RED,
            EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW, EnumChatFormatting.GREEN, EnumChatFormatting.AQUA,
            EnumChatFormatting.BLUE, EnumChatFormatting.LIGHT_PURPLE };

    /* ��һ��������ʽ��ʽ��һ���ַ��� */
    public static String Format(String Input, EnumChatFormatting[] Style, double Delay, int Step) {
        /*
         * ʹ��StringBuilder���Լӿ�Ч�ʡ������ڴ�ռ�� StringBuilder��ƴ���ַ���ָ�� ��string +
         * string������������ַ����ٷ����µ��ַ��� ���������������Ч�ʵ�
         */
        StringBuilder Builder = new StringBuilder(Input.length() * 3);
        /* �����ַ�ƫ�� % ��ȡ���� */
        int Offset = (int) Math.floor(Minecraft.getSystemTime() / Delay) % Style.length;
        for (int i = 0; i < Input.length(); i++) {
            char c = Input.charAt(i);/* ȡInput�еĵ�i���ַ�,����char����ת��Ϊint,ת��������ַ���ASCII���Ӧ��ֵ */
            int col = (i * Step + Style.length - Offset) % Style.length;
            Builder.append(Style[col]);
            Builder.append(c);
        }
        return Builder.toString();
    }

    /* �òʺ���ʽ��ʽ��һ���ַ��� */
    public static String Rainbow(String Input) {
        return Format(Input, Rainbow, 80.0D, 1);
    }
}

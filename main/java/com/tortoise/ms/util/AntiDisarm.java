package com.tortoise.ms.util;

import java.util.ArrayList;
import java.util.List;

/* ����е */
public class AntiDisarm {
    /* ��һ�����鴢�����г���MS Sword������� */
    private static final List<String> Players = new ArrayList<String>();

    public static void Add(String Name) {
        Players.add(Name);
    }

    public static boolean Contains(String Name) {
        return Players.contains(Name);
    }
}

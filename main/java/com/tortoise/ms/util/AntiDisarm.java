package com.tortoise.ms.util;

import java.util.ArrayList;
import java.util.List;

/* 防缴械 */
public class AntiDisarm {
    /* 用一个数组储存所有持有MS Sword的玩家名 */
    private static final List<String> Players = new ArrayList<String>();

    public static void Add(String Name) {
        Players.add(Name);
    }

    public static boolean Contains(String Name) {
        return Players.contains(Name);
    }
}

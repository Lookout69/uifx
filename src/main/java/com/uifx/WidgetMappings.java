package com.uifx;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WidgetMappings
{
    private static final Map<Integer, Map<String, Integer>> layoutTabWidgetIds = new HashMap<>();
    private static final Map<Integer, Map<String, Integer>> layoutIconWidgetIds = new HashMap<>();

    static
    {
        Map<String, Integer> fixedTabWidgets = new HashMap<>();
        Map<String, Integer> fixedIcons = new HashMap<>();

        fixedTabWidgets.put("Combat", 63);
        fixedTabWidgets.put("Skills", 64);
        fixedTabWidgets.put("Quests", 65);
        fixedTabWidgets.put("Inventory", 66);
        fixedTabWidgets.put("Worn Equipment", 67);
        fixedTabWidgets.put("Prayer", 68);
        fixedTabWidgets.put("Spellbook", 69);
        fixedTabWidgets.put("Chat-channel", 47);
        fixedTabWidgets.put("Friends", 48);
        fixedTabWidgets.put("Account", 49);
        fixedTabWidgets.put("Logout", 50);
        fixedTabWidgets.put("Settings", 51);
        fixedTabWidgets.put("Emotes", 52);
        fixedTabWidgets.put("Music", 53);

        fixedIcons.put("Combat", 70);
        fixedIcons.put("Skills", 71);
        fixedIcons.put("Quests", 72);
        fixedIcons.put("Inventory", 73);
        fixedIcons.put("Worn Equipment", 74);
        fixedIcons.put("Prayer", 75);
        fixedIcons.put("Spellbook", 76);
        fixedIcons.put("Chat-channel", 54);
        fixedIcons.put("Friends", 55);
        fixedIcons.put("Account", 56);
        fixedIcons.put("Logout", 57);
        fixedIcons.put("Settings", 58);
        fixedIcons.put("Emotes", 59);
        fixedIcons.put("Music", 60);

        Map<String, Integer> resClassicTabWidgets = new HashMap<>();
        Map<String, Integer> resClassicIcons = new HashMap<>();

        resClassicTabWidgets.put("Combat", 59);
        resClassicTabWidgets.put("Skills", 60);
        resClassicTabWidgets.put("Quests", 61);
        resClassicTabWidgets.put("Inventory", 62);
        resClassicTabWidgets.put("Worn Equipment", 63);
        resClassicTabWidgets.put("Prayer", 64);
        resClassicTabWidgets.put("Spellbook", 65);
        resClassicTabWidgets.put("Chat-channel", 43);
        resClassicTabWidgets.put("Friends", 45);
        resClassicTabWidgets.put("Account", 44);
        resClassicTabWidgets.put("Logout", 46);
        resClassicTabWidgets.put("Settings", 47);
        resClassicTabWidgets.put("Emotes", 48);
        resClassicTabWidgets.put("Music", 49);

        resClassicIcons.put("Combat", 66);
        resClassicIcons.put("Skills", 67);
        resClassicIcons.put("Quests", 68);
        resClassicIcons.put("Inventory", 69);
        resClassicIcons.put("Worn Equipment", 70);
        resClassicIcons.put("Prayer", 71);
        resClassicIcons.put("Spellbook", 72);
        resClassicIcons.put("Chat-channel", 50);
        resClassicIcons.put("Friends", 52);
        resClassicIcons.put("Account", 51);
        resClassicIcons.put("Logout", 53);
        resClassicIcons.put("Settings", 54);
        resClassicIcons.put("Emotes", 55);
        resClassicIcons.put("Music", 56);

        Map<String, Integer> resModernTabWidgets = new HashMap<>();
        Map<String, Integer> resModernIcons = new HashMap<>();

        resModernTabWidgets.put("Combat", 52);
        resModernTabWidgets.put("Skills", 53);
        resModernTabWidgets.put("Quests", 54);
        resModernTabWidgets.put("Inventory", 55);
        resModernTabWidgets.put("Worn Equipment", 56);
        resModernTabWidgets.put("Prayer", 57);
        resModernTabWidgets.put("Spellbook", 58);
        resModernTabWidgets.put("Chat-channel", 38);
        resModernTabWidgets.put("Friends", 40);
        resModernTabWidgets.put("Account", 39);
        resModernTabWidgets.put("Settings", 41);
        resModernTabWidgets.put("Emotes", 42);
        resModernTabWidgets.put("Music", 43);

        resModernIcons.put("Combat", 59);
        resModernIcons.put("Skills", 60);
        resModernIcons.put("Quests", 61);
        resModernIcons.put("Inventory", 62);
        resModernIcons.put("Worn Equipment", 63);
        resModernIcons.put("Prayer", 64);
        resModernIcons.put("Spellbook", 65);
        resModernIcons.put("Chat-channel", 44);
        resModernIcons.put("Friends", 46);
        resModernIcons.put("Account", 45);
        resModernIcons.put("Settings", 47);
        resModernIcons.put("Emotes", 48);
        resModernIcons.put("Music", 49);

        layoutTabWidgetIds.put(548, fixedTabWidgets);
        layoutTabWidgetIds.put(161, resClassicTabWidgets);
        layoutTabWidgetIds.put(164, resModernTabWidgets);

        layoutIconWidgetIds.put(548, fixedIcons);
        layoutIconWidgetIds.put(161, resClassicIcons);
        layoutIconWidgetIds.put(164, resModernIcons);
    }

    public static Map<String, Integer> getTabMappings(int groupId)
    {
        return layoutTabWidgetIds.getOrDefault(groupId, Collections.emptyMap());
    }

    public static Map<String, Integer> getIconMappings(int groupId)
    {
        return layoutIconWidgetIds.getOrDefault(groupId, Collections.emptyMap());
    }
}

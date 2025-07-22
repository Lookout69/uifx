package com.uifx;


public enum TabConfig
{
    COMBAT("Combat", "combat"),
    SKILLS("Skills", "skills"),
    QUESTS("Quests", "Quests"),
    INVENTORY("Inventory", "inventory"),
    EQUIPMENT("Worn Equipment", "equipment"),
    PRAYER("Prayer", "prayer"),
    MAGIC("Spellbook", "magic"),
    CLAN("Chat-channel", "clan"),
    FRIENDS("Friends", "friends"),
    ACCOUNT("Account", "account"),
    LOGOUT("Logout", "logout"),
    SETTINGS("Settings", "settings"),
    EMOTES("Emotes", "emotes"),
    MUSIC("Music", "music");

    public final String displayName;
    public final String key;

    TabConfig(String displayName, String key)
    {
        this.displayName = displayName;
        this.key = key;
    }


    @Override
    public String toString()
    {
        return displayName;
    }
}
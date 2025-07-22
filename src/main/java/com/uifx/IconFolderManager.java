package com.uifx;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IconFolderManager
{
    private static final String BASE_PATH = System.getProperty("user.home") + "/.runelite/uifx/tab_icons/";

    private static final List<String> TAB_NAMES = Arrays.asList(
            "Combat", "Skills", "Quests", "Inventory", "Worn Equipment",
            "Prayer", "Spellbook", "Chat-channel", "Friends", "Account",
            "Logout", "Settings", "Emotes", "Music"
    );

    public static void createAllTabFolders()
    {
        for (String tab : TAB_NAMES)
        {
            File tabFolder = new File(BASE_PATH + tab);
            if (!tabFolder.exists())
            {
                boolean created = tabFolder.mkdirs();
                if (created)
                {
                    log.info("Created folder for tab: {}", tab);
                }
                else
                {
                    log.warn("Failed to create folder for tab: {}", tab);
                }
            }
        }
    }

    public static File getBaseFolder()
    {
        return new File(BASE_PATH);
    }


    @SuppressWarnings("unused")
    public static File getTabFolder(String tabName)
    {
        return new File(BASE_PATH + tabName);
    }
}

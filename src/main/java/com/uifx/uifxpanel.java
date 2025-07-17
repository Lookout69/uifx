package com.uifx;

import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class uifxpanel extends PluginPanel
{
    private final JCheckBox toggleOverlay = new JCheckBox("Enable Overlay");
    private final JSpinner speedSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 1));
    private final JComboBox<String> tabSelector;

    public static final String[] TABS = {
            "combat", "skills", "quest", "inventory", "equipment",
            "prayer", "magic", "clan", "friends", "account",
            "logout", "settings", "emotes", "music"
    };

    private final Map<String, Boolean> overlayEnabledMap = new HashMap<>();

    private boolean updatingCheckbox = false;

    public uifxpanel()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Set all tabs enabled by default
        for (String tab : TABS)
        {
            overlayEnabledMap.put(tab, true);
        }

        tabSelector = new JComboBox<>(TABS);
        tabSelector.setSelectedItem("combat");
        tabSelector.addActionListener(e -> updateCheckbox());

        toggleOverlay.addActionListener(e ->
        {
            if (!updatingCheckbox) // only update if not being programmatically changed
            {
                String selected = getSelectedTab();
                overlayEnabledMap.put(selected, toggleOverlay.isSelected());
            }
        });

        JButton openFolderButton = new JButton("📂 Open tab_icons folder");
        openFolderButton.addActionListener(e -> openTabIconFolder());

        add(new JLabel("Target Tab:"));
        add(tabSelector);
        add(toggleOverlay);
        add(new JLabel("Frame Delay (ms):"));
        add(speedSpinner);
        add(openFolderButton);

        updateCheckbox(); // Sync checkbox to initial tab
    }

    private void updateCheckbox()
    {
        updatingCheckbox = true;
        String selected = getSelectedTab();
        boolean enabled = overlayEnabledMap.getOrDefault(selected, true);
        toggleOverlay.setSelected(enabled);
        updatingCheckbox = false;
    }

    private void openTabIconFolder()
    {
        File folder = iconfoldermanager.getBaseFolder();
        if (!Desktop.isDesktopSupported())
            return;

        try
        {
            Desktop.getDesktop().open(folder);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean isOverlayEnabledFor(String tabName)
    {
        return overlayEnabledMap.getOrDefault(tabName, true);
    }

    public String getSelectedTab()
    {
        return (String) tabSelector.getSelectedItem();
    }

    public int getAnimationSpeed()
    {
        return (int) speedSpinner.getValue();
    }
}

package com.uifx;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class UifxPanel extends PluginPanel
{
    private final Map<TabConfig, Boolean> overlayEnabledMap = new HashMap<>();
    private final Map<TabConfig, Boolean> hideVanillaMap = new HashMap<>();
    private final Map<TabConfig, Integer> delayMap = new HashMap<>();
    private final Map<TabConfig, Integer> scaleMap = new HashMap<>();
    private final Map<TabConfig, Integer> offsetXMap = new HashMap<>();
    private final Map<TabConfig, Integer> offsetYMap = new HashMap<>();

    private final ConfigManager configManager;
    private UifxOverlay overlay;

    public UifxPanel(ConfigManager configManager)
    {
        this.configManager = configManager;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top buttons
        JButton openFolder = new JButton("📂 Open tab_icons folder 📂");
        openFolder.setAlignmentX(Component.LEFT_ALIGNMENT);
        openFolder.addActionListener(e -> openTabIconFolder());
        add(openFolder);
        add(Box.createVerticalStrut(5));

        JButton reload = new JButton("🔄 Refresh PNGs 🔄");
        reload.setAlignmentX(Component.LEFT_ALIGNMENT);
        reload.addActionListener(e -> {
            if (overlay != null)
            {
                overlay.reloadTabFrames();
            }
        });
        add(reload);
        add(Box.createVerticalStrut(10));

        for (TabConfig tab : TabConfig.values())
        {
            String key = tab.key;

            overlayEnabledMap.put(tab, getConfigBool(key + "Enabled"));
            hideVanillaMap.put(tab, getConfigBool(key + "HideVanilla"));
            delayMap.put(tab, getConfigInt(key + "Delay", 100));
            scaleMap.put(tab, getConfigInt(key + "Scale", 100));
            offsetXMap.put(tab, getConfigInt(key + "OffsetX", 0));
            offsetYMap.put(tab, getConfigInt(key + "OffsetY", 0));

            JPanel section = new JPanel();
            section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
            section.setBorder(new EmptyBorder(2, 12, 2, 6));

            section.add(createLabeledSpinner("Frame Delay (ms):", delayMap.get(tab), 1, 1000, val -> {
                delayMap.put(tab, val);
                configManager.setConfiguration("uifx", key + "Delay", val);
            }));
            section.add(createLabeledSpinner("Scale (%):", scaleMap.get(tab), 1, 200, val -> {
                scaleMap.put(tab, val);
                configManager.setConfiguration("uifx", key + "Scale", val);
            }));
            section.add(createLabeledSpinner("X Offset:", offsetXMap.get(tab), -50, 50, val -> {
                offsetXMap.put(tab, val);
                configManager.setConfiguration("uifx", key + "OffsetX", val);
            }));
            section.add(createLabeledSpinner("Y Offset:", offsetYMap.get(tab), -50, 50, val -> {
                offsetYMap.put(tab, val);
                configManager.setConfiguration("uifx", key + "OffsetY", val);
            }));

            // ✅ Bruteforce left-aligned checkboxes
            JCheckBox overlayBox = new JCheckBox("Enable Overlay", overlayEnabledMap.get(tab));
            JCheckBox hideBox = new JCheckBox("Hide Vanilla Icon", hideVanillaMap.get(tab));

            overlayBox.addActionListener(e -> {
                overlayEnabledMap.put(tab, overlayBox.isSelected());
                configManager.setConfiguration("uifx", key + "Enabled", overlayBox.isSelected());
            });
            hideBox.addActionListener(e -> {
                hideVanillaMap.put(tab, hideBox.isSelected());
                configManager.setConfiguration("uifx", key + "HideVanilla", hideBox.isSelected());
            });

            JPanel checkboxForcePanel = new JPanel(null); // Null layout brute-force
            checkboxForcePanel.setPreferredSize(new Dimension(160, 50));

            int cbWidth = overlayBox.getPreferredSize().width;
            int cbHeight = overlayBox.getPreferredSize().height;

            overlayBox.setBounds(0, 0, cbWidth, cbHeight);
            hideBox.setBounds(0, 25, cbWidth, cbHeight);

            checkboxForcePanel.add(overlayBox);
            checkboxForcePanel.add(hideBox);

            section.add(Box.createVerticalStrut(6));
            section.add(checkboxForcePanel);

            JPanel collapsible = new CollapsiblePanel(tab.displayName, section);
            collapsible.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(collapsible);

            add(Box.createVerticalStrut(4));
            JSeparator separator = new JSeparator();
            separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            separator.setForeground(new Color(80, 80, 80));
            separator.setBackground(new Color(80, 80, 80));
            add(separator);
            add(Box.createVerticalStrut(8));
        }
    }

    private JPanel createLabeledSpinner(String labelText, int initial, int min, int max, Consumer<Integer> onChange)
    {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBorder(new EmptyBorder(2, 0, 2, 0));
        JLabel label = new JLabel(labelText);
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(initial, min, max, 1));
        spinner.addChangeListener(e -> onChange.accept((int) spinner.getValue()));
        panel.add(label, BorderLayout.WEST);
        panel.add(spinner, BorderLayout.EAST);
        return panel;
    }

    private Boolean getConfigBool(String key)
    {
        Boolean val = configManager.getConfiguration("uifx", key, Boolean.class);
        return val != null ? val : false;
    }

    private Integer getConfigInt(String key, int def)
    {
        Integer val = configManager.getConfiguration("uifx", key, Integer.class);
        return val != null ? val : def;
    }

    private void openTabIconFolder()
    {
        File folder = IconFolderManager.getBaseFolder();
        if (!Desktop.isDesktopSupported())
            return;
        try
        {
            Desktop.getDesktop().open(folder);
        }
        catch (IOException e)
        {
            log.warn("Failed to open tab_icons folder", e);
        }
    }

    public void setOverlayRef(UifxOverlay overlay)
    {
        this.overlay = overlay;
    }

    public boolean isOverlayEnabledFor(TabConfig tab)
    {
        return overlayEnabledMap.getOrDefault(tab, false);
    }

    public boolean shouldHideVanillaFor(TabConfig tab)
    {
        return hideVanillaMap.getOrDefault(tab, false);
    }

    public int getAnimationSpeedFor(TabConfig tab)
    {
        return delayMap.getOrDefault(tab, 100);
    }

    public int getScalePercentFor(TabConfig tab)
    {
        return scaleMap.getOrDefault(tab, 100);
    }

    public int getOffsetXFor(TabConfig tab)
    {
        return offsetXMap.getOrDefault(tab, 0);
    }

    public int getOffsetYFor(TabConfig tab)
    {
        return offsetYMap.getOrDefault(tab, 0);
    }
}

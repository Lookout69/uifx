package com.uifx;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.api.gameval.InterfaceID;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
public class UifxOverlay extends Overlay
{
    private final UifxPanel panel;
    private final Client client;
    private final UifxConfig config;
    private final ClientThread clientThread;

    private final Map<TabConfig, TabAnimationState> tabAnimations = new HashMap<>();
    private static final Map<TabConfig, Integer> tabWidgetIds = new HashMap<>();
    private static final Map<TabConfig, Integer> iconWidgetIds = new HashMap<>();

    private int currentGroupId = -1;

    public static final class LayoutGroupIds
    {
        public static final int FIXED = InterfaceID.TOPLEVEL;
        public static final int RESIZABLE_CLASSIC = InterfaceID.TOPLEVEL_OSRS_STRETCH;
        public static final int RESIZABLE_MODERN = InterfaceID.TOPLEVEL_PRE_EOC;
        private LayoutGroupIds() {}
    }

    @Inject
    public UifxOverlay(UifxPanel panel, Client client, UifxConfig config, ClientThread clientThread)
    {
        this.panel = panel;
        this.client = client;
        this.config = config;
        this.clientThread = clientThread;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);

        clientThread.invoke(this::loadFramesForAllTabs);
    }

    private void debug(String message)
    {
        if (config.debugChatMessages() && client != null)
        {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "[UIFX] " + message, null);
        }
    }

    public void loadFramesForAllTabs()
    {
        for (TabConfig tab : TabConfig.values())
        {
            File dir = new File(IconFolderManager.getBaseFolder(), tab.displayName);
            debug("Checking tab folder for '" + tab.displayName + "': " + dir.getAbsolutePath());

            if (!dir.exists() || !dir.isDirectory())
            {
                debug("Directory for tab '" + tab.displayName + "' does not exist.");
                continue;
            }

            File[] frameFiles = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".png"));
            if (frameFiles == null || frameFiles.length == 0)
            {
                debug("⚠ No PNG frames found for tab '" + tab.displayName + "'");
                continue;
            }

            Arrays.sort(frameFiles);
            List<BufferedImage> frameList = new ArrayList<>();
            for (File file : frameFiles)
            {
                try
                {
                    BufferedImage img = ImageIO.read(file);
                    if (img != null)
                    {
                        frameList.add(img);
                        debug("Loaded frame: " + file.getName());
                    }
                }
                catch (IOException e)
                {
                    debug("Failed to load: " + file.getName());
                    log.warn("Failed to load image frame", e);
                }
            }

            if (!frameList.isEmpty())
            {
                tabAnimations.put(tab, new TabAnimationState(frameList));
            }
        }
    }

    private Widget getTabWidget(TabConfig tab)
    {
        Integer childId = tabWidgetIds.get(tab);
        return (childId != null) ? client.getWidget(currentGroupId, childId) : null;
    }

    public void restoreVanillaIcons()
    {
        for (Map.Entry<TabConfig, Integer> entry : iconWidgetIds.entrySet())
        {
            Widget iconWidget = client.getWidget(currentGroupId, entry.getValue());
            if (iconWidget != null)
                iconWidget.setHidden(false);
        }
    }

    public void reloadTabFrames()
    {
        clientThread.invoke(() -> {
            tabAnimations.clear();
            loadFramesForAllTabs();
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "UIFX reloaded.", null);
        });
    }

    @Override
    public Dimension render(Graphics2D g)
    {
        if (client.getGameState() != GameState.LOGGED_IN)
        {
            return null;
        }

        final long now = System.currentTimeMillis();
        int detectedGroupId = client.getTopLevelInterfaceId();

        if (detectedGroupId != LayoutGroupIds.FIXED &&
                detectedGroupId != LayoutGroupIds.RESIZABLE_CLASSIC &&
                detectedGroupId != LayoutGroupIds.RESIZABLE_MODERN)
        {
            return null;
        }

        if (detectedGroupId != currentGroupId)
        {
            currentGroupId = detectedGroupId;

            tabWidgetIds.clear();
            iconWidgetIds.clear();

            Map<String, Integer> tabs = WidgetMappings.getTabMappings(currentGroupId);
            Map<String, Integer> icons = WidgetMappings.getIconMappings(currentGroupId);

            for (TabConfig tab : TabConfig.values())
            {
                Integer tabId = tabs.get(tab.displayName);
                Integer iconId = icons.get(tab.displayName);
                if (tabId != null) tabWidgetIds.put(tab, tabId);
                if (iconId != null) iconWidgetIds.put(tab, iconId);
            }

            if (config.debugChatMessages())
            {
                String mode = (currentGroupId == LayoutGroupIds.FIXED) ? "Fixed" :
                        (currentGroupId == LayoutGroupIds.RESIZABLE_CLASSIC) ? "Resizable Classic" :
                                "Resizable Modern";
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "[UIFX] Detected layout: " + mode, null);
            }
        }

        for (TabConfig tab : TabConfig.values())
        {
            boolean showOverlay = panel.isOverlayEnabledFor(tab);
            boolean hideVanilla = panel.shouldHideVanillaFor(tab);

            Integer iconId = iconWidgetIds.get(tab);
            if (iconId != null)
            {
                Widget iconWidget = client.getWidget(currentGroupId, iconId);
                if (iconWidget != null)
                {
                    iconWidget.setHidden(showOverlay && hideVanilla);
                }
            }

            if (!showOverlay)
            {
                continue;
            }

            TabAnimationState state = tabAnimations.get(tab);
            if (state == null || state.frames.isEmpty())
            {
                continue;
            }

            int delay = Math.max(panel.getAnimationSpeedFor(tab), 1);
            if (now - state.lastUpdate >= delay)
            {
                state.frameIndex = (state.frameIndex + 1) % state.frames.size();
                state.lastUpdate = now;
            }

            BufferedImage img = state.frames.get(state.frameIndex);
            Widget tabWidget = getTabWidget(tab);
            if (tabWidget == null || tabWidget.isHidden())
            {
                continue;
            }

            Point p = tabWidget.getCanvasLocation();
            int scale = panel.getScalePercentFor(tab);
            int offsetX = panel.getOffsetXFor(tab);
            int offsetY = panel.getOffsetYFor(tab);

            int scaledWidth = img.getWidth() * scale / 100;
            int scaledHeight = img.getHeight() * scale / 100;

            g.drawImage(img,
                    p.getX() + offsetX,
                    p.getY() + offsetY,
                    p.getX() + offsetX + scaledWidth,
                    p.getY() + offsetY + scaledHeight,
                    0, 0,
                    img.getWidth(), img.getHeight(),
                    null
            );
        }

        return null;
    }

    private static class TabAnimationState
    {
        int frameIndex = 0;
        long lastUpdate = 0;
        List<BufferedImage> frames;

        TabAnimationState(List<BufferedImage> frames)
        {
            this.frames = frames;
        }
    }
}

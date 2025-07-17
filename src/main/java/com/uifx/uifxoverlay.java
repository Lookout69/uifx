package com.uifx;

import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class uifxoverlay extends Overlay
{
    private final uifxpanel panel;
    private final Client client;
    private final List<BufferedImage> frames = new ArrayList<>();
    private int currentFrame = 0;
    private long lastFrameTime = 0;

    @Inject
    public uifxoverlay(uifxpanel panel, Client client)
    {
        this.panel = panel;
        this.client = client;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        loadDummyFrames();
    }

    private void loadDummyFrames()
    {
        frames.add(makeColoredImage(Color.RED));
        frames.add(makeColoredImage(Color.GREEN));
        frames.add(makeColoredImage(Color.BLUE));
    }

    private BufferedImage makeColoredImage(Color color)
    {
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, 40, 40);
        g.dispose();
        return img;
    }

    @Override
    public Dimension render(Graphics2D g)
    {
        if (frames.isEmpty())
            return null;

        int delay = panel.getAnimationSpeed();
        long now = System.currentTimeMillis();

        if (now - lastFrameTime >= delay)
        {
            currentFrame = (currentFrame + 1) % frames.size();
            lastFrameTime = now;
        }

        BufferedImage img = frames.get(currentFrame);

        for (String tab : uifxpanel.TABS)
        {
            if (!panel.isOverlayEnabledFor(tab))
                continue;

            Widget widget = getWidgetForTab(tab);
            if (widget == null || widget.isHidden())
                continue;

            net.runelite.api.Point rlPoint = widget.getCanvasLocation();
            Point awtPoint = new Point(rlPoint.getX(), rlPoint.getY());

            g.drawImage(img, awtPoint.x, awtPoint.y, null);
        }

        return null;
    }

    private Widget getWidgetForTab(String tab)
    {
        switch (tab)
        {
            case "combat":
                return getWidgetSafe(WidgetInfo.FIXED_VIEWPORT_COMBAT_TAB, WidgetInfo.RESIZABLE_VIEWPORT_COMBAT_TAB);
            case "skills":
                return getWidgetSafe(WidgetInfo.FIXED_VIEWPORT_STATS_TAB, WidgetInfo.RESIZABLE_VIEWPORT_STATS_TAB);
            case "quest":
                return getWidgetSafe(WidgetInfo.FIXED_VIEWPORT_QUESTS_TAB, WidgetInfo.RESIZABLE_VIEWPORT_QUESTS_TAB);
            case "inventory":
                return getWidgetSafe(WidgetInfo.FIXED_VIEWPORT_INVENTORY_TAB, WidgetInfo.RESIZABLE_VIEWPORT_INVENTORY_TAB);
            case "equipment":
                return getWidgetSafe(WidgetInfo.FIXED_VIEWPORT_EQUIPMENT_TAB, WidgetInfo.RESIZABLE_VIEWPORT_EQUIPMENT_TAB);
            case "prayer":
                return getWidgetSafe(WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB, WidgetInfo.RESIZABLE_VIEWPORT_PRAYER_TAB);
            case "magic":
                return getWidgetSafe(WidgetInfo.FIXED_VIEWPORT_MAGIC_TAB, WidgetInfo.RESIZABLE_VIEWPORT_MAGIC_TAB);
            case "friends":
                return getWidgetSafe(WidgetInfo.FIXED_VIEWPORT_FRIENDS_TAB, WidgetInfo.RESIZABLE_VIEWPORT_FRIENDS_TAB);
            case "logout":
                return getWidgetSafe(WidgetInfo.FIXED_VIEWPORT_LOGOUT_TAB, WidgetInfo.RESIZABLE_VIEWPORT_LOGOUT_TAB);
            case "settings":
                return getWidgetSafe(WidgetInfo.FIXED_VIEWPORT_OPTIONS_TAB, WidgetInfo.RESIZABLE_VIEWPORT_OPTIONS_TAB);
            case "emotes":
                return getWidgetSafe(WidgetInfo.FIXED_VIEWPORT_EMOTES_TAB, WidgetInfo.RESIZABLE_VIEWPORT_EMOTES_TAB);
            case "music":
                return getWidgetSafe(WidgetInfo.FIXED_VIEWPORT_MUSIC_TAB, WidgetInfo.RESIZABLE_VIEWPORT_MUSIC_TAB);
            default:
                return null; // skip unsupported tabs
        }
    }


    private Widget getWidgetSafe(WidgetInfo fixed, WidgetInfo resizable)
    {
        Widget w = client.getWidget(fixed);
        if (w == null)
            w = client.getWidget(resizable);
        return w;
    }
}

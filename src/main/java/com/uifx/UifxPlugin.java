package com.uifx;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

@Slf4j
@PluginDescriptor(
		name = "UIFX",
		description = "Replaces tab icons with animated overlays",
		tags = {"gif", "overlay", "tab", "custom", "icons", "sidebar", "png"}
)
public class UifxPlugin extends Plugin
{
	@SuppressWarnings("unused") // Used via dependency injection by RuneLite
	@Inject
	private OverlayManager overlayManager;

	@SuppressWarnings("unused") // Used via dependency injection by RuneLite
	@Inject
	private Client client;

	@SuppressWarnings("unused") // Used via dependency injection by RuneLite
	@Inject
	private UifxConfig config;

	@SuppressWarnings("unused") // Used via dependency injection by RuneLite
	@Inject
	private ClientToolbar clientToolbar;

	@SuppressWarnings("unused") // Used via dependency injection by RuneLite
	@Inject
	private ConfigManager configManager;

	@SuppressWarnings("unused") // Used via dependency injection by RuneLite
	@Inject
	private ClientThread clientThread;

	private UifxPanel panel;
	private UifxOverlay overlay;
	private NavigationButton navButton;

	@Override
	protected void startUp()
	{
		panel = new UifxPanel(configManager);
		overlay = new UifxOverlay(panel, client, config, clientThread);
		panel.setOverlayRef(overlay);

		overlayManager.add(overlay);
		IconFolderManager.createAllTabFolders();
		log.info("UIFX plugin started.");

		Image img = new ImageIcon(Objects.requireNonNull(getClass().getResource("/uifx_icon.png"))).getImage();
		BufferedImage iconImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = iconImage.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();

		navButton = NavigationButton.builder()
				.tooltip("UIFX")
				.icon(iconImage)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown()
	{
		if (overlay != null)
		{
			overlay.restoreVanillaIcons();
			overlayManager.remove(overlay);
			overlay = null;
		}

		if (navButton != null)
		{
			clientToolbar.removeNavigation(navButton);
			navButton = null;
		}

		panel = null;

		log.info("UIFX plugin stopped.");
	}

	@SuppressWarnings("unused") // Required by RuneLite for config injection, even if not called directly
	@Provides
	UifxConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(UifxConfig.class);
	}
}

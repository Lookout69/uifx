package com.uifx;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
@PluginDescriptor(
		name = "UIFX"
)
public class uifxPlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;

	private uifxoverlay overlay;

	@Inject
	private Client client;

	@Inject
	private uifxconfig config;

	@Inject
	private uifxpanel panel;

	@Inject
	private ClientToolbar clientToolbar;

	private NavigationButton navButton;

	@Override
	protected void startUp() throws Exception
	{
		overlay = new uifxoverlay(panel, client);
		overlayManager.add(overlay);
		iconfoldermanager.createAllTabFolders();
		log.info("UIFX plugin started.");

		// Load icon from resources folder
		Image img = new ImageIcon(getClass().getResource("/uifx_icon.png")).getImage();
		BufferedImage iconImage = new BufferedImage(
				img.getWidth(null),
				img.getHeight(null),
				BufferedImage.TYPE_INT_ARGB
		);
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

	private void openTabIconFolder()
	{
		File folder = iconfoldermanager.getBaseFolder();

		if (!Desktop.isDesktopSupported())
		{
			log.warn("Desktop not supported — can't open folder.");
			return;
		}

		try
		{
			Desktop.getDesktop().open(folder);
		}
		catch (IOException e)
		{
			log.warn("Failed to open folder: " + folder.getAbsolutePath(), e);
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		if (overlay != null)
		{
			overlayManager.remove(overlay);
			overlay = null;
		}

		log.info("UIFX plugin stopped.");

		if (navButton != null)
		{
			clientToolbar.removeNavigation(navButton);
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	@Provides
	uifxconfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(uifxconfig.class);
	}
}

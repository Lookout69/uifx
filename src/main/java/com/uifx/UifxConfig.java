package com.uifx;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("uifx")
public interface UifxConfig extends Config
{
	@ConfigItem(
			keyName = "debugChatMessages",
			name = "Show Debug Messages in Chat",
			description = "Print debug overlay info to the in-game chat box"
	)
	default boolean debugChatMessages()
	{
		return false;
	}
}

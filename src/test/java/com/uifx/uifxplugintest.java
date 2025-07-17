package com.uifx;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class uifxplugintest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(uifxPlugin.class);
		RuneLite.main(args);
	}
}
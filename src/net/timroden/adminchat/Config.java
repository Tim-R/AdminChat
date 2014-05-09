package net.timroden.adminchat;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;

public class Config {
	private AdminChat plugin;
	private static Configuration config;

	public Boolean useCustomConsoleName, notifyVersion, chatListen;
	public String consoleSenderName, allPrefix, adminPrefix, allMasked, toAdminPrefix, rawPrefixAdmin, rawPrefixAll, allSuffix, adminSuffix;
	public char colorPrefix;

	public Config(AdminChat plugin) {
		this.plugin = plugin;
		config = plugin.getConfig().getRoot();
		config.options().copyDefaults(true);
		plugin.saveConfig();

		getOpts();
	}

	public void reload() {
		plugin.reloadConfig();
		config = plugin.getConfig().getRoot();

		getOpts();
	}

	public void getOpts() {		
		useCustomConsoleName = config.getBoolean("adminchat.usecustomconsolename");
		notifyVersion = config.getBoolean("adminchat.notifyonversion");
		chatListen = config.getBoolean("adminchat.listeninchat");

		allPrefix = ChatColor.translateAlternateColorCodes('&', config.getString("adminchat.allprefix")) + ChatColor.RESET;
		allMasked = ChatColor.translateAlternateColorCodes('&', config.getString("adminchat.allmasked")) + ChatColor.RESET;
		allSuffix = ChatColor.translateAlternateColorCodes('&', config.getString("adminchat.allsuffix"));

		adminPrefix = ChatColor.translateAlternateColorCodes('&', config.getString("adminchat.adminprefix")) + ChatColor.RESET;
		toAdminPrefix = ChatColor.translateAlternateColorCodes('&', config.getString("adminchat.toadminprefix")) + ChatColor.RESET;
		adminSuffix = ChatColor.translateAlternateColorCodes('&', config.getString("adminchat.adminsuffix"));


		colorPrefix = config.getString("adminchat.colorprefix").charAt(0);
		
		rawPrefixAdmin = config.getString("adminchat.rawprefixadmin");
		rawPrefixAll = config.getString("adminchat.rawprefixall");

		consoleSenderName = ChatColor.translateAlternateColorCodes('&', config.getString("adminchat.customconsolename")) + ChatColor.RESET;
	}
}
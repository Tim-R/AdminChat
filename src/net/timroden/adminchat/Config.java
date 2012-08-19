package net.timroden.adminchat;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;

public class Config {
	private AdminChat plugin;
	private static Configuration config;

	public Boolean useCustomConsoleName;
	public String consoleSenderName, allPrefix, adminPrefix, allMasked, toAdminPrefix, rawChatPrefix;

	public Config(AdminChat plugin) {
		this.plugin = plugin;
		config = plugin.getConfig().getRoot();
		config.options().copyDefaults(true);
		plugin.saveConfig();

		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				getOpts();
			}			
		}, 1L);
	}

	public void reload() {
		plugin.reloadConfig();
		config = plugin.getConfig().getRoot();

		getOpts();
	}

	public void getOpts() {
		useCustomConsoleName = config.getBoolean("adminchat.usecustomconsolename");

		allPrefix = ChatColor.translateAlternateColorCodes('&', config.getString("adminchat.allprefix").replace("\"", "")) + ChatColor.RESET;
		allMasked = ChatColor.translateAlternateColorCodes('&', config.getString("adminchat.allmasked").replace("\"", "")) + ChatColor.RESET;

		adminPrefix = ChatColor.translateAlternateColorCodes('&', config.getString("adminchat.adminprefix").replace("\"", "")) + ChatColor.RESET;
		toAdminPrefix = ChatColor.translateAlternateColorCodes('&', config.getString("adminchat.toadminprefix").replace("\"", "")) + ChatColor.RESET;

		rawChatPrefix = config.getString("adminchat.rawprefix");

		consoleSenderName = ChatColor.translateAlternateColorCodes('&', config.getString("adminchat.customconsolename").replace("\"", "")) + ChatColor.RESET;
	}
}
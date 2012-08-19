package net.timroden.adminchat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

	public AdminChat plugin;

	public PlayerListener(AdminChat plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		if(p.isPermissionSet("adminchat.notify") && plugin.config.notifyVersion) { 
			if(!plugin.vc.isLatestVersion) {
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						p.sendMessage(plugin.chatPrefix + ChatColor.DARK_PURPLE + plugin.vc.versionMessage);
					}
				}, 1L);
			}
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		String m = e.getMessage();
		Player p = e.getPlayer();

		if(m.startsWith(plugin.config.rawPrefixAll)) {
			if(!p.hasPermission("adminchat.send.all")) {
				return;
			}			
			e.setCancelled(true);
			plugin.displayMessage((CommandSender) p, plugin.utils.fix(m, CommandType.ALL), p.getDisplayName(), CommandType.ALL);
			return;
		}

		if(m.startsWith(plugin.config.rawPrefixAdmin)) {
			if(!p.hasPermission("adminchat.send.admin")) {
				return;
			}			
			e.setCancelled(true);
			plugin.displayMessage((CommandSender) p, plugin.utils.fix(m, CommandType.AD), p.getDisplayName(), CommandType.AD);
			return;
		}
	}
}
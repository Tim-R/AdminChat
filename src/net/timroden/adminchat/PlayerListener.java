package net.timroden.adminchat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerListener implements Listener {

	public AdminChat plugin;

	public PlayerListener(AdminChat plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		String m = e.getMessage();
		Player p = e.getPlayer();

		if(!p.hasPermission("adminchat.send.admin")) {
			p.sendMessage(plugin.chatPrefix + ChatColor.RED + "You don't have permission to send admin chat messages." + ChatColor.GRAY + " (adminchat.send.admin)");
			return;
		}

		if(m.startsWith(plugin.config.rawChatPrefix)) {
			e.setCancelled(true);

			plugin.displayMessage((CommandSender) p, plugin.utils.fix(m), p.getDisplayName(), "ad");
		}
	}
}
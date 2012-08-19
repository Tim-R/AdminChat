package net.timroden.adminchat;

import java.io.IOException;
import java.util.logging.Logger;

import net.timroden.adminchat.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminChat extends JavaPlugin {
	public Logger log = Logger.getLogger("Minecraft");

	public Utils utils;
	public PlayerListener pl;
	public VersionChecker vc;
	public Metrics metrics;
	public PluginManager pm;
	public Config config;

	public String chatPrefix = ChatColor.GRAY + "[" + ChatColor.DARK_GREEN + "AdminChat" + ChatColor.GRAY + "] " + ChatColor.RESET;

	public void onEnable() {
		Long st = System.currentTimeMillis();

		vc = new VersionChecker(this);		
		pl = new PlayerListener(this);
		utils = new Utils(this);
		config = new Config(this);

		pm = getServer().getPluginManager();

		vc.versionCheck();

		try {
			metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			log.severe("[AdminChat] Error enabling Metrics:");
			e.printStackTrace();
		}

		pm.registerEvents(pl, this);
		log.info("[AdminChat] Enabled successfully! (" + (((System.currentTimeMillis() - st) / 1000D) % 60) + "s)");
	}

	public void onDisable() {
		log.info("[AdminChat] Disabled successfully.");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		String name = null;

		if(sender instanceof Player) {
			name = ((Player) sender).getDisplayName();
		} else if(sender instanceof ConsoleCommandSender) {
			if(config.useCustomConsoleName) {
				name = config.consoleSenderName;
			} else {
				name = sender.getName();
			}
		}
		if(cmd.getName().equalsIgnoreCase("ad")) {
			if(args.length < 1) {
				displayHelpDialogue(sender, CommandType.AD);
				return true;
			}
			if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				if(!sender.hasPermission("signedit.admin")) {
					sender.sendMessage(chatPrefix + ChatColor.RED + "You don't have permission to reload the AdminChat config!" + ChatColor.GRAY + " (adminchat.admin)");
					return true;
				}				
				sender.sendMessage(chatPrefix + ChatColor.GRAY + "Reloading config...");
				config.reload();
				sender.sendMessage(chatPrefix + ChatColor.GRAY + "Config reloaded.");
				return true;
			}
			
			if(args.length == 1 && args[0].equalsIgnoreCase("help")) {
				displayHelpDialogue(sender, CommandType.AD);
				return true;
			}
			
			if(!sender.hasPermission("adminchat.send.admin")) {
				sender.sendMessage(chatPrefix + ChatColor.RED + "You don't have permission to send admin chat messages." + ChatColor.GRAY + " (adminchat.send.admin)");
				return true;
			}
			displayMessage(sender, utils.implode(" ", args), name, CommandType.AD);
		} 
		if(cmd.getName().equalsIgnoreCase("all")) {
			if(args.length < 1) {
				displayHelpDialogue(sender, CommandType.ALL);
				return true;
			}
			
			if(args.length == 1 && args[0].equalsIgnoreCase("help")) {
				displayHelpDialogue(sender, CommandType.ALL);
				return true;
			}
			
			if(!sender.hasPermission("adminchat.send.all")) {
				sender.sendMessage(chatPrefix + ChatColor.RED + "You don't have permission to send all chat messages." + ChatColor.GRAY + " (adminchat.send.all)");
				return true;
			}
			displayMessage(sender, utils.implode(" ", args), name, CommandType.ALL);
		}
		return false;		
	}

	public void displayHelpDialogue(CommandSender sender, CommandType type) {
		sender.sendMessage(chatPrefix + ChatColor.GREEN + "Available commands:");		
		if(sender.hasPermission("adminchat.send.admin")) {
			sender.sendMessage(chatPrefix + ChatColor.GRAY + "/ad <message> - Send a message to AdminChat");
		}
		if(sender.hasPermission("adminchat.send.all")) {
			sender.sendMessage(chatPrefix + ChatColor.GRAY + "/all <message> - Broadcast a message via all chat");
		}		
		if(sender.hasPermission("adminchat.admin")) {
			sender.sendMessage(chatPrefix + ChatColor.GRAY + "/ad reload - Reload AdminChat config");
		}
		sender.sendMessage(chatPrefix + ChatColor.GRAY + "/" + type.getValue() + " help - Display this help dialogue");
	}

	public void displayMessage(CommandSender sender, String message, String name, CommandType type) {
		Player p = null;
		boolean putToConsole = true;
		ConsoleCommandSender ccs = Bukkit.getConsoleSender();
		String resetName = name + ChatColor.RESET;
		String msg = ChatColor.translateAlternateColorCodes('&', message);

		if(sender instanceof Player) {
			p = (Player) sender;
		} else if(sender instanceof ConsoleCommandSender) {
			putToConsole = false;
		}

		if(type.equals(CommandType.AD)) {
			if(sender.hasPermission("adminchat.view.admin")) {
				sender.sendMessage(config.adminPrefix + " " + resetName + ": " + msg);
			} else {
				sender.sendMessage(config.toAdminPrefix + " " + resetName + ": " + msg);
			}
			if(putToConsole) {
				ccs.sendMessage(config.adminPrefix + " " + resetName + ": " + msg);
			}
		}		

		if(type.equals(CommandType.ALL)) {
			sender.sendMessage(config.allPrefix + " " + resetName + ": " + msg);
			if(putToConsole) {
				ccs.sendMessage(config.allPrefix + " " + resetName + ": " + msg);
			}
		}
		
		for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
			if(p == pl) 
				continue;

			if(type.equals(CommandType.AD)) {
				if(pl.hasPermission("adminchat.view.admin")) {
					pl.sendMessage(config.adminPrefix + " " + resetName + ": " + msg);
				}
			}

			if(type.equals(CommandType.ALL)) {
				if(pl.hasPermission("adminchat.send.all")) {
					pl.sendMessage(config.allPrefix + " " + resetName + ": " + msg);
				} else {
					pl.sendMessage(config.allPrefix + " " + config.allMasked + ": " + msg);
				}
			}
		}
	}
}
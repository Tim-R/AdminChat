package net.timroden.adminchat.utils;

import net.timroden.adminchat.AdminChat;

public class Utils {
	public AdminChat plugin;
	
	public Utils(AdminChat plugin) {
		this.plugin = plugin;
	}
	
	public String implode(String glue, String[] pieces) {
		StringBuilder sb = new StringBuilder();
		
		for(String s : pieces) {
			sb.append(s).append(glue);
		}
		return sb.toString();
	}
	
	public String fix(String message) {
		return message.replace(plugin.config.rawChatPrefix, "");
	}
}
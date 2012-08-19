package net.timroden.adminchat.utils;

import net.timroden.adminchat.AdminChat;
import net.timroden.adminchat.CommandType;

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

	public String fix(String message, CommandType type) {
		if(type.equals(CommandType.AD)) {
			return message.substring(plugin.config.rawPrefixAdmin.length());
		}
		
		if(type.equals(CommandType.ALL)) {
			return message.substring(plugin.config.rawPrefixAll.length());
		}
		return message;
	}
}
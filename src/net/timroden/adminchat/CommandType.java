package net.timroden.adminchat;

public enum CommandType {
	AD(0, "ad"),
	ALL(1, "all");
	
	private int type;
	private String value;
	private CommandType(int type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public int getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
}

package user.theovercaste.redprotect;

import java.util.HashMap;

public enum Flag {
	PVP("pvp", false), CHEST("chest", false), LEVER("lever", true), BUTTON(
			"button", true), DOOR("door", false), MOBS("mobs", true), PASSIVES(
			"passives", false);

	private static final HashMap<String, Flag> flagNameMap = new HashMap<String, Flag>();
	static {
		for (Flag f : values()) {
			flagNameMap.put(f.name, f);
		}
	}

	private boolean defaultValue;
	public final String name;
	public final String permission;

	Flag(String name, String permission, boolean defaultValue) {
		this.name = name;
		this.permission = permission;
		this.defaultValue = defaultValue;
	}

	Flag(String name, boolean defaultValue) {
		this(name, "redprotect.flag." + name, defaultValue);
	}

	public boolean getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(boolean value) {
		this.defaultValue = value;
	}

	public int getId() {
		return ordinal();
	}

	public static Flag getFlag(String name) {
		return flagNameMap.get(name.toLowerCase());
	}
}

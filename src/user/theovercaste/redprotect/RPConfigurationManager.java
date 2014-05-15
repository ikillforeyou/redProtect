package user.theovercaste.redprotect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;

import org.bukkit.Material;

import com.OverCaste.plugin.RedProtect.Flags;

public class RPConfigurationManager {
	public static final String PATH_DATA = "data";
	public static final String pathConfig = "Config.txt";
	public static final String pathFlagConfig = "Flags.txt";
	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	private final RedProtect plugin;

	private static enum FileType { // Start of configuration stuff
		YML, YML_GZ, OOS, OOS_GZ, MYSQL
	}

	private FileType fileType = FileType.YML;

	private static enum DropType {
		DROP, REMOVE, KEEP
	}

	private DropType dropType = DropType.DROP;
	private boolean debugMessages = false;
	private int limitAmount = 400;
	private int blockID = 55;
	private int maxScan = 600;
	private int heightStart = 50;
	private boolean backup = true;
	// }
	private Material adminWand = Material.FEATHER;
	private Material infoWand = Material.STRING;

	public RPConfigurationManager(RedProtect plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	public void initialize() throws Exception {
		File main = plugin.getDataFolder();
		File data = new File(main, PATH_DATA);
		File config = new File(main, pathConfig);
		File flagConfig = new File(main, pathFlagConfig);
		BufferedWriter fr;
		// Create: {
		if (!main.exists()) {
			main.mkdir();
			plugin.getLogger().info("Created folder: " + main.getPath());
		}
		if (!data.exists()) {
			data.mkdir();
			plugin.getLogger().info("Created folder: " + PATH_DATA);
		}
		if (!config.exists()) {
			plugin.getLogger().info("Created file: " + pathConfig);
			config.createNewFile();
			fr = new BufferedWriter(new FileWriter(config));
			fr.write("#This is the configuration file, feel free to edit it."
					+ LINE_SEPARATOR);
			fr.write("#Types: Integer: Number without period; Boolean: True or false; Struct: One of the described strings."
					+ LINE_SEPARATOR);
			fr.write("#---------" + LINE_SEPARATOR);
			fr.write("#The data type for the regions file, 'ymlgz', 'yml', 'oos', 'oosgz', 'mysql', oosgz is recommended for normal use. (Struct)"
					+ LINE_SEPARATOR);
			fr.write("#WARNING: YML IS NOT SUPPORTED DUE TO ERRORS IN BUKKIT. MYSQL WILL COME SOON USE OOS FOR NOW."
					+ LINE_SEPARATOR);
			fr.write("file-type: oosgz" + LINE_SEPARATOR);
			fr.write("#The way that the blocks drop upon region creation. 'drop' drops items, 'remove' removes the blocks without dropping items, and 'keep' doesn't do anything."
					+ LINE_SEPARATOR);
			fr.write("drop-type: drop" + LINE_SEPARATOR);
			fr.write("#If debug messages should be printed to console. (Boolean)"
					+ LINE_SEPARATOR);
			fr.write("debug-messages: false" + LINE_SEPARATOR);
			fr.write("#The preferred permissions system, 'bPerms', 'Perms3', 'PEX', 'GM', 'OP', 'SuperPerms', 'Detect' (Struct)"
					+ LINE_SEPARATOR);
			fr.write("preferred-permissions: Detect" + LINE_SEPARATOR);
			fr.write("#Limit the amount of blocks a player without RedProtect.unlimited can protect at one time. -1 for unlimited. (Integer)"
					+ LINE_SEPARATOR);
			fr.write("limit-amount: 400" + LINE_SEPARATOR);
			fr.write("#Height the region starts at, it goes from sky to this value, so 0 would be full sky to bedrock, and 40 would be sky to half way through terrain."
					+ LINE_SEPARATOR);
			fr.write("height-start: 0" + LINE_SEPARATOR);
			fr.write("#The ID of the block that you construct regions out of. EX: 55 = Redstone, 85 = Fence (Integer)"
					+ LINE_SEPARATOR);
			fr.write("block-id: 55" + LINE_SEPARATOR);
			fr.write("#The maximum amount of redstone blocks the loop will scan. [Don't make this -1, it's to stop infinite loops.] (Integer)"
					+ LINE_SEPARATOR);
			fr.write("max-scan: 600" + LINE_SEPARATOR);
			fr.write("#Should we backup the database between saves in-case of interruption?"
					+ LINE_SEPARATOR);
			fr.write("backup: true" + LINE_SEPARATOR);
			fr.write("#The ID of the selector wand." + LINE_SEPARATOR);
			fr.write("adminWandID: " + adminWand.getId() + LINE_SEPARATOR);
			fr.write("#The ID of the information wand." + LINE_SEPARATOR);
			fr.write("infoWandID: " + infoWand.getId() + LINE_SEPARATOR);
			fr.close();
		}
		if (!flagConfig.exists()) {
			flagConfig.createNewFile();
			fr = new BufferedWriter(new FileWriter(flagConfig));
			fr.write("#This is the flag defaults configuration, feel free to edit it."
					+ LINE_SEPARATOR);
			fr.write("#The flag can have either true or false default value. Users with required permission can manually toggle these in their own regions."
					+ LINE_SEPARATOR);
			fr.write("#---------" + LINE_SEPARATOR);
			fr.write("pvp: false" + LINE_SEPARATOR);
			fr.write("chest: false" + LINE_SEPARATOR);
			fr.write("lever: true" + LINE_SEPARATOR);
			fr.write("button: true" + LINE_SEPARATOR);
			fr.write("door: false" + LINE_SEPARATOR);
			fr.write("mobs: true" + LINE_SEPARATOR);
			fr.close();
		}
		// End of create }
		// Read{
		Properties props = new Properties();
		FileInputStream propfis = new FileInputStream(pathConfig);
		props.load(propfis);
		String dat = "";
		if ((dat = (String) props.getProperty("debug-messages")) != null) {
			if (dat.equalsIgnoreCase("true")) {
				debugMessages = true;
			} else if (dat.equalsIgnoreCase("false")) {
				debugMessages = false;
			} else {
				plugin.getLogger()
						.severe("There is a major error in your configuration, 'debug-messages' isn't an acceptable value.");
				plugin.disable();
			}
		} else {
			plugin.getLogger()
					.severe("Configuration option not found: debug-messages! Defaulting to false.");
		}
		if ((dat = (String) props.getProperty("file-type")) != null) {
			if (dat.equalsIgnoreCase("yml")) {
				fileType = FileType.YML;
			} else if (dat.equalsIgnoreCase("ymlgz")) {
				fileType = FileType.YML_GZ;
			} else if (dat.equalsIgnoreCase("oos")) {
				fileType = FileType.OOS;
			} else if (dat.equalsIgnoreCase("oosgz")) {
				fileType = FileType.OOS_GZ;
			} else if (dat.equalsIgnoreCase("mysql")) {
				fileType = FileType.MYSQL;
			} else {
				plugin.getLogger()
						.severe("There is a major error in your configuration, 'file-type' isn't an acceptable value.");
				plugin.disable();
			}
		} else {
			plugin.getLogger()
					.warning(
							"Configuration option not found: file-type! Defaulting to ymlgz.");
		}
		if ((dat = (String) props.getProperty("drop-type")) != null) {
			if (dat.equalsIgnoreCase("keep")) {
				dropType = DropType.KEEP;
			} else if (dat.equalsIgnoreCase("remove")) {
				dropType = DropType.REMOVE;
			} else if (dat.equalsIgnoreCase("drop")) {
				dropType = DropType.DROP;
			} else {
				dropType = DropType.DROP;
				plugin.getLogger()
						.warning(
								"There is an error in your configuration, 'drop-type' isn't an acceptable value.");
			}
		}
		if ((dat = (String) props.getProperty("block-id")) != null) {
			try {
				blockID = Integer.parseInt(dat);
			} catch (NumberFormatException e) {
				blockID = 55;
				plugin.getLogger()
						.warning(
								"There is an error in your configuration, 'block-id' isn't a valid integer. Defaulting to Redstone.");
			}
		} else {
			plugin.getLogger()
					.warning(
							"Configuration option not found: block-id! Defaulting to Redstone.");
		}
		if ((dat = (String) props.getProperty("limit-amount")) != null) {
			try {
				limitAmount = Integer.parseInt(dat);
			} catch (NumberFormatException e) {
				limitAmount = 400;
				plugin.getLogger()
						.warning(
								"There is an error in your configuration, 'limit-amount' isn't a valid integer. Defaulting to 400.");
			}
		} else {
			plugin.getLogger()
					.warning(
							"Configuration option not found: limit-amount! Defaulting to 400.");
		}

		if ((dat = (String) props.getProperty("height-start")) != null) {
			try {
				heightStart = Integer.parseInt(dat);
			} catch (NumberFormatException e) {
				heightStart = 0;
				plugin.getLogger()
						.warning(
								"There is an error in your configuration, 'height-start' isn't a valid integer. Defaulting to 0.");
			}
		} else {
			plugin.getLogger()
					.warning(
							"Configuration option not found: height-start! Defaulting to 0.");
		}
		if ((dat = (String) props.getProperty("max-scan")) != null) {
			try {
				maxScan = Integer.parseInt(dat);
			} catch (NumberFormatException e) {
				maxScan = 600;
				plugin.getLogger()
						.warning(
								"There is an error in your configuration, 'max-scan' isn't a valid integer. Defaulting to 600.");
			}
		} else {
			plugin.getLogger()
					.warning(
							"Configuration option not found: max-scan! Defaulting to 600.");
		}
		if ((dat = (String) props.getProperty("backup")) != null) {
			if (dat.equalsIgnoreCase("true") || dat.equalsIgnoreCase("yes")) {
				backup = true;
			} else {
				backup = false;
			}
		}
		if ((dat = (String) props.getProperty("adminWandID")) != null) {
			try {
				adminWand = Material.getMaterial(Integer.parseInt(dat));
			} catch (NumberFormatException e) {
				plugin.getLogger()
						.warning(
								"Configuration value 'adminWandID' isn't a valid integer!");
			}
		}
		if ((dat = (String) props.getProperty("infoWandID")) != null) {
			try {
				infoWand = Material.getMaterial(Integer.parseInt(dat));
			} catch (NumberFormatException e) {
				plugin.getLogger()
						.warning(
								"Configuration value 'infoWandID' isn't a valid integer!");
			}
		}
		propfis.close();
		dat = "";
		props = new Properties();
		propfis = new FileInputStream(pathFlagConfig);
		props.load(propfis);
		if ((dat = props.getProperty("pvp")) != null) {
			if (dat.equalsIgnoreCase("true")) {
				Flags.pvp = true;
			} else if (dat.equalsIgnoreCase("false")) {
				Flags.pvp = false;
			}
		} else {
			plugin.getLogger()
					.warning(
							"Configuration value \"pvp\" isn't initalized, defaulting to false.");
		}
		if ((dat = props.getProperty("chest")) != null) {
			if (dat.equalsIgnoreCase("true")) {
				Flags.chest = true;
			} else if (dat.equalsIgnoreCase("false")) {
				Flags.chest = false;
			}
		} else {
			plugin.getLogger()
					.warning(
							"Configuration value \"chest\" isn't initalized, defaulting to false.");
		}
		if ((dat = props.getProperty("lever")) != null) {
			if (dat.equalsIgnoreCase("true")) {
				Flags.lever = true;
			} else if (dat.equalsIgnoreCase("false")) {
				Flags.lever = false;
			}
		} else {
			plugin.getLogger()
					.warning(
							"Configuration value \"lever\" isn't initalized, defaulting to true.");
		}
		if ((dat = props.getProperty("button")) != null) {
			if (dat.equalsIgnoreCase("true")) {
				Flags.button = true;
			} else if (dat.equalsIgnoreCase("false")) {
				Flags.button = false;
			}
		} else {
			plugin.getLogger()
					.warning(
							"Configuration value \"button\" isn't initalized, defaulting to true.");
		}
		if ((dat = props.getProperty("door")) != null) {
			if (dat.equalsIgnoreCase("true")) {
				Flags.door = true;
			} else if (dat.equalsIgnoreCase("false")) {
				Flags.door = false;
			}
		} else {
			plugin.getLogger()
					.warning(
							"Configuration value \"door\" isn't initalized, defaulting to false.");
		}
		if ((dat = props.getProperty("mobs")) != null) {
			if (dat.equalsIgnoreCase("true")) {
				Flags.mobs = true;
			} else if (dat.equalsIgnoreCase("false")) {
				Flags.mobs = false;
			}
		} else {
			plugin.getLogger()
					.warning(
							"Configuration value \"mobs\" isn't initalized, defaulting to true.");
		}
	}

	public FileType getFileType() {
		return fileType;
	}

	public DropType getDropType() {
		return dropType;
	}

	public boolean isDebugMessages() {
		return debugMessages;
	}

	public int getLimitAmount() {
		return limitAmount;
	}

	public int getBlockId() {
		return blockID;
	}

	public int getMaxScan() {
		return maxScan;
	}

	public int getHeightStart() {
		return heightStart;
	}

	public boolean isBackup() {
		return backup;
	}

	public Material getAdminWand() {
		return adminWand;
	}

	public Material getInfoWand() {
		return infoWand;
	}
}

package user.theovercaste.redprotect;

import java.util.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RedProtect extends JavaPlugin {
	RPBlockListener bListener;
	RPPlayerListener pListener;
	RPEntityListener eListener;
	RPWorldListener wListener;
	CommandManager cManager;

	private RPRegionManager regionManager;
	private RPPermissionHandler permissionHandler;
	private RPConfigurationManager configurationManager;

	static final HashMap<Player, Location> firstLocationSelections = new HashMap<Player, Location>();
	static final HashMap<Player, Location> secondLocationSelections = new HashMap<Player, Location>();

	@Override
	public void onDisable() {
		regionManager.saveAll();
		getLogger().info(getDescription().getName() + " disabled.");
	}

	@Override
	public void onEnable() {
		try {
			initVars();
			configurationManager.initialize();
			regionManager.loadAll();
			getServer().getPluginManager().registerEvents(bListener, this);
			getServer().getPluginManager().registerEvents(pListener, this);
			getServer().getPluginManager().registerEvents(eListener, this);
			getServer().getPluginManager().registerEvents(wListener, this);
			getCommand("RedProtect").setExecutor(cManager);
			System.out.println(getDescription().getFullName() + " enabled.");
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().severe(
					"Error enabling redProtect, plugin will shut down.");
			disable();
		}
	}

	public void disable() {
		super.setEnabled(false);
	}

	void initVars() throws Exception {
		configurationManager = new RPConfigurationManager(this);
		bListener = new RPBlockListener(this);
		pListener = new RPPlayerListener(this);
		eListener = new RPEntityListener(this);
		wListener = new RPWorldListener(this);
		cManager = new CommandManager(this);
		permissionHandler = new RPPermissionHandler(this);
		regionManager = new RPRegionManager();
		/*
		 * switch(fileType){ case yml: case ymlgz: case oosgz: case oos: rm =
		 * new WorldFlatFileRegionManager(); break; case mysql: rm = new
		 * WorldMySQLRegionManager(); break; }
		 */
	}

	public RPRegionManager getRegionManager() {
		return regionManager;
	}

	public RPPermissionHandler getPermissionHandler() {
		return permissionHandler;
	}

	public RPConfigurationManager getConfigurationManager() {
		return configurationManager;
	}
}
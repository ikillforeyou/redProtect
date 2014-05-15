package user.theovercaste.redprotect;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class RPWorldListener implements Listener {

	RedProtect plugin;

	public RPWorldListener(RedProtect plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onWorldLoad(WorldLoadEvent e) {
		World w = e.getWorld();
		try {
			plugin.getRegionManager().load(w);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onWorldUnload(WorldUnloadEvent e) {
		World w = e.getWorld();
		try {
			plugin.getRegionManager().unload(w);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

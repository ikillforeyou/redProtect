package user.theovercaste.redprotect;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.chat.Chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.OverCaste.plugin.RedProtect.Region;

public class RPPermissionHandler {
	private final RedProtect plugin;

	private final boolean vaultFound; // Important permission stuff
	private final Chat permission;

	public RPPermissionHandler(RedProtect plugin) throws Exception {
		this.plugin = plugin;
		Plugin p = Bukkit.getPluginManager().getPlugin("Vault");
		if (p == null || !(p instanceof Vault)) {
			plugin.getLogger()
					.warning(
							"Vault not found, player limits will be set to the default.");
			vaultFound = false;
			permission = null;
		} else {
			RegisteredServiceProvider<Chat> provider = Bukkit.getServer()
					.getServicesManager().getRegistration(Chat.class);
			if (provider == null) {
				plugin.getLogger()
						.warning(
								"Vault couldn't find an acceptable permission plugin! Player limits set to default.");
				vaultFound = false;
				permission = null;
				return;
			}
			vaultFound = true;
			permission = provider.getProvider();
		}
	}

	public boolean hasPerm(Player p, String perm) {
		if (p == null) {
			return false;
		}
		return p.hasPermission(perm);
	}

	public boolean hasPerm(String pl, String perm) {
		Player p = Bukkit.getServer().getPlayerExact(pl);
		if (p == null) {
			return false;
		}
		return p.hasPermission(perm);
	}

	public boolean hasRegionPerm(Player p, String s, Region poly) {
		String adminperm = "redprotect.admin." + s;
		String userperm = "redprotect.own." + s;
		if (poly == null) {
			return (hasPerm(p, adminperm) || hasPerm(p, userperm));
		} else {
			return hasPerm(p, adminperm)
					|| (hasPerm(p, userperm) && poly.isOwner(p));
		}
	}

	public boolean hasHelpPerm(Player p, String s) {
		String adminperm = "redprotect.admin." + s;
		String userperm = "redprotect.own." + s;
		return (hasPerm(p, adminperm) || hasPerm(p, userperm));
	}

	public int getPlayerLimit(Player p) {
		if (!vaultFound) {
			return plugin.getConfigurationManager().getLimitAmount();
		}
		return permission.getPlayerInfoInteger(p, "maxregionsize", plugin
				.getConfigurationManager().getLimitAmount());
	}
}

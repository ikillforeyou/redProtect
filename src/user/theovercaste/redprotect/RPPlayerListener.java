package user.theovercaste.redprotect;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

import com.OverCaste.plugin.RedProtect.Region;

public class RPPlayerListener implements Listener {
	RedProtect plugin;

	public RPPlayerListener(RedProtect plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Block b = event.getClickedBlock();
		if (b == null)
			return;
		Region r = null;
		Material itemInHand = p.getItemInHand().getType();

		if (p.getItemInHand().getType() == plugin.getConfigurationManager()
				.getAdminWand()) {
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if (p.hasPermission("redprotect.magicwand")) {
					RedProtect.secondLocationSelections.put(p, b.getLocation());
					p.sendMessage(ChatColor.AQUA
							+ "Set the second magic wand location to ("
							+ ChatColor.GOLD + b.getLocation().getBlockX()
							+ ChatColor.AQUA + ", " + ChatColor.GOLD
							+ b.getLocation().getBlockY() + ChatColor.AQUA
							+ ", " + ChatColor.GOLD
							+ b.getLocation().getBlockZ() + ChatColor.AQUA
							+ ").");
					event.setCancelled(true);
					return;
				}
			} else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				if (p.hasPermission("redprotect.magicwand")) {
					RedProtect.firstLocationSelections.put(p, b.getLocation());
					p.sendMessage(ChatColor.AQUA
							+ "Set the first magic wand location to ("
							+ ChatColor.GOLD + b.getLocation().getBlockX()
							+ ChatColor.AQUA + ", " + ChatColor.GOLD
							+ b.getLocation().getBlockY() + ChatColor.AQUA
							+ ", " + ChatColor.GOLD
							+ b.getLocation().getBlockZ() + ChatColor.AQUA
							+ ").");
					event.setCancelled(true);
					return;
				}
			}
		}
		if (p.getItemInHand().getType() == plugin.getConfigurationManager()
				.getInfoWand()) {
			if (p.hasPermission("redprotect.infowand")
					&& (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event
							.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
				if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
					r = plugin.getRegionManager().getRegion(p.getLocation());
				} else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					r = plugin.getRegionManager().getRegion(b.getLocation());
				}
				if (r == null) {
					p.sendMessage(ChatColor.RED
							+ "There is no region at that block's location!");
				} else {
					p.sendMessage(ChatColor.AQUA + "--------------- ["
							+ ChatColor.GOLD + r.getName() + ChatColor.AQUA
							+ "] ---------------");
					p.sendMessage(r.info());
					p.sendMessage(r.getFlagInfo());
				}
				event.setCancelled(true);
				return;
			}
		}

		if (b.getType().equals(Material.CHEST)) {
			r = RedProtect.rm.getRegion(b.getLocation());
			if (r == null)
				return;
			if (!r.canChest(p)) {
				if (!RedProtect.permissionHandler.hasPerm(p,
						"redprotect.bypass")) {
					p.sendMessage(ChatColor.RED + "You can't open this chest!");
					event.setCancelled(true);
				} else {
					p.sendMessage(ChatColor.YELLOW + "Opened locked chest in "
							+ r.getCreator() + "'s region.");
				}
			}
		}

		else if (b.getType().equals(Material.DISPENSER)) {
			r = RedProtect.rm.getRegion(b.getLocation());
			if (r == null)
				return;
			if (!r.canChest(p)) {
				if (!RedProtect.permissionHandler.hasPerm(p,
						"redprotect.bypass")) {
					p.sendMessage(ChatColor.RED
							+ "You can't open this dispenser!");
					event.setCancelled(true);
				} else {
					p.sendMessage(ChatColor.YELLOW
							+ "Opened locked dispenser in " + r.getCreator()
							+ "'s region.");
				}
			}
		}

		else if (b.getType().equals(Material.FURNACE)
				|| b.getType().equals(Material.BURNING_FURNACE)) {
			r = RedProtect.rm.getRegion(b.getLocation());
			if (r == null)
				return;
			if (!r.canChest(p)) {
				if (!RedProtect.permissionHandler.hasPerm(p,
						"redprotect.bypass")) {
					p.sendMessage(ChatColor.RED
							+ "You can't open this furnace!");
					event.setCancelled(true);
				} else {
					p.sendMessage(ChatColor.YELLOW
							+ "Opened locked furnace in " + r.getCreator()
							+ "'s region.");
				}
			}
		}

		else if (b.getType().equals(Material.LEVER)) {
			r = RedProtect.rm.getRegion(b.getLocation());
			if (r == null)
				return;
			if (!r.canLever(p)) {
				if (!RedProtect.permissionHandler.hasPerm(p,
						"redprotect.bypass")) {
					p.sendMessage(ChatColor.RED
							+ "You can't toggle this lever!");
					event.setCancelled(true);
				} else {
					p.sendMessage(ChatColor.YELLOW + "Toggled locked lever in "
							+ r.getCreator() + "'s region.");
				}
			}
		}

		else if (b.getType().equals(Material.STONE_BUTTON)
				|| b.getType().equals(Material.WOOD_BUTTON)) {
			r = RedProtect.rm.getRegion(b.getLocation());
			if (r == null)
				return;
			if (!r.canButton(p)) {
				if (!RedProtect.permissionHandler.hasPerm(p,
						"redprotect.bypass")) {
					p.sendMessage(ChatColor.RED
							+ "You can't activate this button!");
					event.setCancelled(true);
				} else {
					p.sendMessage(ChatColor.YELLOW
							+ "Activated locked button in " + r.getCreator()
							+ "'s region.");
				}
			}
		}

		else if (b.getType().equals(Material.WOODEN_DOOR)) {
			r = RedProtect.rm.getRegion(b.getLocation());
			if (r == null)
				return;
			if (!r.canDoor(p)) {
				if (!RedProtect.permissionHandler.hasPerm(p,
						"redprotect.bypass")) {
					p.sendMessage(ChatColor.RED + "You can't open this door!");
					event.setCancelled(true);
				} else {
					p.sendMessage(ChatColor.YELLOW + "Opened locked door in "
							+ r.getCreator() + "'s region.");
				}
			}
		}

		if (itemInHand.equals(Material.FLINT_AND_STEEL)
				|| itemInHand.equals(Material.WATER_BUCKET)
				|| itemInHand.equals(Material.LAVA_BUCKET)
				|| itemInHand.equals(Material.PAINTING)
				|| itemInHand.equals(Material.ITEM_FRAME)) {
			if (!RedProtect.rm.canBuild(p, b, b.getWorld())) {
				p.sendMessage(ChatColor.RED + "You can't use that here!");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEntityEvent event) {
		Entity e = event.getRightClicked();
		Player p = event.getPlayer();
		if (e instanceof ItemFrame) {
			Region r = plugin.getRegionManager().getRegion(e.getLocation());
			if (r != null && !r.canBuild(plugin, p)) {
				p.sendMessage(ChatColor.RED
						+ "You can't edit that item frame here!");
				event.setCancelled(true);
			}
		}
	}
}

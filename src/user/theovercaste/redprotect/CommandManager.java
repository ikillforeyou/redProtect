package user.theovercaste.redprotect;

import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.OverCaste.plugin.RedProtect.Region;

public class CommandManager implements CommandExecutor {

	static final String NOT_IN_REGION_MESSAGE = ChatColor.RED
			+ "You need to be in a region or define a region to do that!";
	static final String NO_PERMISSION_MESSAGE = ChatColor.RED
			+ "You don't have permission to do that!";

	private final RedProtect plugin;

	public CommandManager(RedProtect plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You can't use RedProtect from the console!");
			return true;
		}
		Player player = (Player) sender;
		if (args.length == 0) {
			// DEBUG:
			// player.getWorld().getBlockAt((int)player.getLocation().getX(),
			// (int)player.getLocation().getY()+1,
			// (int)player.getLocation().getZ()).setType(Material.BOOKSHELF);
			player.sendMessage(ChatColor.AQUA + "redProtect version "
					+ plugin.getDescription().getVersion());
			player.sendMessage(ChatColor.AQUA + "Developed by ("
					+ ChatColor.GOLD + "ikillforeyou" + ChatColor.AQUA + ").");
			player.sendMessage(ChatColor.AQUA
					+ "For more information about the commands, type ["
					+ ChatColor.GOLD + "/rp ?" + ChatColor.AQUA + "].");
			player.sendMessage(ChatColor.AQUA + "For a tutorial, type ["
					+ ChatColor.GOLD + "/rp tutorial" + ChatColor.AQUA + "].");
			return true;
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("?")
					|| args[0].equalsIgnoreCase("help")) {
				player.sendMessage(ChatColor.AQUA
						+ "Available commands to you: ");
				player.sendMessage(ChatColor.AQUA
						+ "------------------------------------");
				if (plugin.getPermissionHandler().hasHelpPerm(player, "limit")) {
					player.sendMessage(ChatColor.GREEN + "/rp limit");
				}
				if (plugin.getPermissionHandler().hasHelpPerm(player, "list")) {
					player.sendMessage(ChatColor.GREEN + "/rp list");
				}
				if (plugin.getPermissionHandler().hasHelpPerm(player, "delete")) {
					player.sendMessage(ChatColor.GREEN + "/rp delete");
				}
				if (plugin.getPermissionHandler().hasHelpPerm(player, "info")) {
					player.sendMessage(ChatColor.GREEN + "/rp info");
				}
				if (plugin.getPermissionHandler().hasHelpPerm(player,
						"addmember")) {
					player.sendMessage(ChatColor.GREEN
							+ "/rp addmember (player)");
				}
				if (plugin.getPermissionHandler().hasHelpPerm(player,
						"addowner")) {
					player.sendMessage(ChatColor.GREEN
							+ "/rp addowner (player)");
				}
				if (plugin.getPermissionHandler().hasHelpPerm(player,
						"removemember")) {
					player.sendMessage(ChatColor.GREEN
							+ "/rp removemember (player)");
				}
				if (plugin.getPermissionHandler().hasHelpPerm(player,
						"removeowner")) {
					player.sendMessage(ChatColor.GREEN
							+ "/rp removeowner (player)");
				}
				if (plugin.getPermissionHandler().hasHelpPerm(player, "rename")) {
					player.sendMessage(ChatColor.GREEN + "/rp rename (name)");
				}
				if (plugin.getPermissionHandler().hasPerm(player,
						"redprotect.near")) {
					player.sendMessage(ChatColor.GREEN + "/rp near");
				}
				player.sendMessage(ChatColor.GREEN + "/rp flag");
				player.sendMessage(ChatColor.AQUA
						+ "------------------------------------");
				return true;
			}

			if (args[0].equalsIgnoreCase("limit")
					|| args[0].equalsIgnoreCase("limitremaining")
					|| args[0].equalsIgnoreCase("remaining")) {
				if (plugin.getPermissionHandler().hasPerm(player,
						"redprotect.own.limit")) {
					int limit = plugin.getPermissionHandler().getPlayerLimit(
							player);
					if ((limit < 0)
							|| (plugin.getPermissionHandler().hasPerm(player,
									"redprotect.unlimited"))) {
						player.sendMessage(ChatColor.AQUA
								+ "You have no limit!");
						return true;
					}

					int currentUsed = plugin.getRegionManager()
							.getTotalRegionSize(player.getName());
					player.sendMessage(ChatColor.AQUA + "Your area: ("
							+ ChatColor.GOLD + currentUsed + ChatColor.AQUA
							+ " / " + ChatColor.GOLD + limit + ChatColor.AQUA
							+ ").");
					return true;
				} else {
					player.sendMessage(ChatColor.RED
							+ "You don't have sufficient permission to do that.");
					return true;
				}
			}

			if (args[0].equalsIgnoreCase("list")
					|| args[0].equalsIgnoreCase("ls")) {
				if (plugin.getPermissionHandler().hasPerm(player,
						"redprotect.own.list")) {
					Set<Region> regions = plugin.getRegionManager().getRegions(
							player);
					if (regions.size() == 0) {
						player.sendMessage(ChatColor.AQUA
								+ "You don't have any regions!");
					} else {
						player.sendMessage(ChatColor.AQUA
								+ "Regions you've created:");
						player.sendMessage(ChatColor.AQUA
								+ "------------------------------------");
						Iterator<Region> i = regions.iterator();
						while (i.hasNext()) {
							player.sendMessage(ChatColor.AQUA + i.next().info());
						}
						player.sendMessage(ChatColor.AQUA
								+ "------------------------------------");
					}
					return true;
				} else {
					player.sendMessage(ChatColor.RED
							+ "You don't have sufficient permission to do that.");
					return true;
				}
			}

			if (args[0].equalsIgnoreCase("tutorial")
					|| args[0].equalsIgnoreCase("tut")) {
				player.sendMessage(ChatColor.AQUA + "Tutorial:");
				player.sendMessage(ChatColor.AQUA
						+ "1. Surround your creation with "
						+ RPUtil.formatName(Material.getMaterial(
								plugin.getConfigurationManager().getBlockId())
								.name()) + ".");
				player.sendMessage(ChatColor.AQUA
						+ "2. Place a sign next to your region, with [rp] on the first line.");
				player.sendMessage(ChatColor.AQUA
						+ "3. Enter the name you want your region to be on the 2nd line, or nothing for an automatic name.");
				player.sendMessage(ChatColor.AQUA
						+ "4. Enter 2 additional owners, if you want, on lines 3 & 4.");
				// player.sendMessage(ChatColor.AQUA +
				// "To protect your region, surround your creation with " +
				// ", then place a sign with [rp] on the first line next to it.");
				return true;
			}

			if (args[0].equalsIgnoreCase("near")
					|| args[0].equalsIgnoreCase("nr")) {
				if (plugin.getPermissionHandler().hasPerm(player,
						"redprotect.near")) {
					Set<Region> regions = plugin.getRegionManager()
							.getRegionsNear(player, 30, player.getWorld());
					if (regions.size() == 0) {
						player.sendMessage(ChatColor.AQUA
								+ "There are no regions nearby.");
					} else {
						Iterator<Region> i = regions.iterator();
						player.sendMessage(ChatColor.AQUA
								+ "Regions within 40 blocks: ");
						player.sendMessage(ChatColor.AQUA
								+ "------------------------------------");
						while (i.hasNext()) {
							Region r = i.next();
							player.sendMessage(ChatColor.AQUA + "Name: "
									+ ChatColor.GOLD + r.getName()
									+ ChatColor.AQUA + ", Center: ["
									+ ChatColor.GOLD + r.getCenterX()
									+ ChatColor.AQUA + ", " + ChatColor.GOLD
									+ r.getCenterZ() + ChatColor.AQUA + "].");
						}
						player.sendMessage(ChatColor.AQUA
								+ "------------------------------------");
					}
				} else {
					player.sendMessage(ChatColor.RED
							+ "You don't have permission to do that.");
				}
				return true;
			}

			if (args[0].equalsIgnoreCase("flag")) {
				player.sendMessage(ChatColor.AQUA
						+ "To use the command, type '/rp flag (flag)'. This will toggle the specific flag if you have permission.");
				player.sendMessage(ChatColor.AQUA
						+ "Type '/rp flag info' to show the status of flags in the region you're currently in.");
				return true;
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("redefine")) {
				if (!player.hasPermission("redprotect.admin.redefine")) {
					player.sendMessage(ChatColor.RED
							+ "You don't have permission to do that!");
					return true;
				}
				String name = args[1];
				Region oldRect = plugin.getRegionManager().getRegion(name,
						player.getWorld());
				if (oldRect == null) {
					player.sendMessage(ChatColor.RED
							+ "That region doesn't exist!");
					return true;
				}
				RedefineRegionBuilder rb = new RedefineRegionBuilder(player,
						oldRect, firstLocationSelections.get(player),
						secondLocationSelections.get(player));
				if (rb.ready()) {
					Region r = rb.build();
					player.sendMessage(ChatColor.GREEN
							+ "Successfully created region: " + r.getName()
							+ ".");
					plugin.getRegionManager().remove(oldRect);
					plugin.getRegionManager().add(r, player.getWorld());
				}
				return true;
			}
		} else if (args.length <= 3) {
			if (args[0].equalsIgnoreCase("define")) {
				if (!player.hasPermission("redprotect.admin.define")) {
					player.sendMessage(ChatColor.RED
							+ "You don't have permission to do that!");
					return true;
				}
				String name = (args.length >= 2) ? args[1] : "";
				String creator = (args.length == 3) ? args[2] : player
						.getName().toLowerCase();
				RegionBuilder rb = new DefineRegionBuilder(player,
						firstLocationSelections.get(player),
						secondLocationSelections.get(player), name, creator);
				if (rb.ready()) {
					Region r = rb.build();
					player.sendMessage(ChatColor.GREEN
							+ "Successfully created region: " + r.getName()
							+ ".");
					plugin.getRegionManager().add(r, player.getWorld());
				}
				;
				return true;
			}
		}
		// Things you need to be in a rect to do:
		// Length 1:
		if (args.length <= 0) {
			return false; // wrong number of args.
		}
		if (args[0].equalsIgnoreCase("delete")
				|| args[0].equalsIgnoreCase("del")) {
			if (args.length == 1) {
				handleDelete(
						player,
						plugin.getRegionManager().getRegion(player,
								player.getWorld()));
				return true;
			} else if (args.length == 2) {
				handleDelete(
						player,
						plugin.getRegionManager().getRegion(args[1],
								player.getWorld()));
				return true;
			}
			return false;
		}
		if (args[0].equalsIgnoreCase("i") || args[0].equalsIgnoreCase("info")) {
			if (args.length == 1) {
				handleInfo(
						player,
						plugin.getRegionManager().getRegion(player,
								player.getWorld()));
				return true;
			} else if (args.length == 2) {
				handleInfo(
						player,
						plugin.getRegionManager().getRegion(args[1],
								player.getWorld()));
				return true;
			}
			return false;
		}
		if (args[0].equalsIgnoreCase("am")
				|| args[0].equalsIgnoreCase("addmember")) {
			if (args.length == 2) {
				handleAddMember(player, args[1], plugin.getRegionManager()
						.getRegion(player, player.getWorld()));
				return true;
			} else if (args.length == 3) {
				handleAddMember(player, args[1], plugin.getRegionManager()
						.getRegion(args[2], player.getWorld()));
				return true;
			}
			return false;
		}
		if (args[0].equalsIgnoreCase("ao")
				|| args[0].equalsIgnoreCase("addowner")) {
			if (args.length == 2) {
				handleAddOwner(player, args[1], plugin.getRegionManager()
						.getRegion(player, player.getWorld()));
				return true;
			} else if (args.length == 3) {
				handleAddOwner(player, args[1], plugin.getRegionManager()
						.getRegion(args[2], player.getWorld()));
				return true;
			}
			return false;
		}
		if (args[0].equalsIgnoreCase("rm")
				|| args[0].equalsIgnoreCase("removemember")) {
			if (args.length == 2) {
				handleRemoveMember(player, args[1], plugin.getRegionManager()
						.getRegion(player, player.getWorld()));
				return true;
			} else if (args.length == 3) {
				handleRemoveMember(player, args[1], plugin.getRegionManager()
						.getRegion(args[2], player.getWorld()));
				return true;
			}
			return false;
		}
		if (args[0].equalsIgnoreCase("ro")
				|| args[0].equalsIgnoreCase("removeowner")) {
			if (args.length == 2) {
				handleRemoveOwner(player, args[1], plugin.getRegionManager()
						.getRegion(player, player.getWorld()));
				return true;
			} else if (args.length == 3) {
				handleRemoveOwner(player, args[1], plugin.getRegionManager()
						.getRegion(args[2], player.getWorld()));
				return true;
			}
			return false;
		}
		if (args[0].equalsIgnoreCase("rn")
				|| args[0].equalsIgnoreCase("rename")) {
			if (args.length == 2) {
				handleRename(player, args[1], plugin.getRegionManager()
						.getRegion(player, player.getWorld()));
				return true;
			} else if (args.length == 3) {
				handleRename(player, args[1], plugin.getRegionManager()
						.getRegion(args[2], player.getWorld()));
				return true;
			}
			return false;
		}
		if (args[0].equalsIgnoreCase("fl") || args[0].equalsIgnoreCase("flag")) {
			if (args.length == 2) {
				handleFlag(player, args[1], plugin.getRegionManager()
						.getRegion(player, player.getWorld()));
				return true;
			} else if (args.length == 3) {
				handleFlag(player, args[1], plugin.getRegionManager()
						.getRegion(args[2], player.getWorld()));
				return true;
			}
			return false;
		}
		if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("ls")) {
			if (args.length == 1) {
				handleList(player, player.getName());
				return true;
			}
			if (args.length == 2) {
				handleList(player, args[1]);
			}
			return false;
		}
		return false;
	}

	public void handleDelete(Player p, Region r) {
		if (plugin.getPermissionHandler().hasRegionPerm(p, "delete", r)) {
			if (r == null) {
				sendNotInRegionMessage(p);
				return;
			}
			p.sendMessage(ChatColor.AQUA + "Region successfully deleted.");
			plugin.getRegionManager().remove(r);
		} else {
			sendNoPermissionMessage(p);
		}
	}

	public void handleInfo(Player p, Region r) {
		if (plugin.getPermissionHandler().hasRegionPerm(p, "info", r)) {
			if (r == null) {
				sendNotInRegionMessage(p);
				return;
			}
			p.sendMessage(r.info());
		} else {
			sendNoPermissionMessage(p);
		}
	}

	public void handleAddMember(Player p, String sVictim, Region r) {
		if (plugin.getPermissionHandler().hasRegionPerm(p, "addmember", r)) {
			if (r == null) {
				sendNotInRegionMessage(p);
				return;
			}
			Player pVictim = Bukkit.getPlayerExact(sVictim);
			sVictim = sVictim.toLowerCase();
			if (r.isOwner(sVictim)) {
				r.removeOwner(sVictim);
				r.addMember(sVictim);
				if (pVictim != null) {
					if (pVictim.isOnline()) {
						pVictim.sendMessage(ChatColor.AQUA
								+ "You have been demoted to member in: "
								+ ChatColor.GOLD + r.getName() + ChatColor.AQUA
								+ ", by: " + ChatColor.GOLD + p.getName()
								+ ChatColor.AQUA + ".");
					}
				}
				p.sendMessage(ChatColor.AQUA + "Demoted player "
						+ ChatColor.GOLD + sVictim + ChatColor.AQUA
						+ " to member in " + ChatColor.GOLD + r.getName()
						+ ChatColor.AQUA + ".");
			} else {
				if (!r.isMember(sVictim)) {
					r.addMember(sVictim);
					p.sendMessage(ChatColor.AQUA + "Added " + ChatColor.GOLD
							+ sVictim + ChatColor.AQUA + " as a member.");
					if (pVictim != null) {
						if (pVictim.isOnline()) {
							pVictim.sendMessage(ChatColor.AQUA
									+ "You have been added as a member to region: "
									+ ChatColor.GOLD + r.getName()
									+ ChatColor.AQUA + ", by: "
									+ ChatColor.GOLD + p.getName()
									+ ChatColor.AQUA + ".");
						}
					}
				} else {
					p.sendMessage(ChatColor.RED + sVictim
							+ " is already a member in this region.");
				}
			}
		} else {
			sendNoPermissionMessage(p);
		}
	}

	public void handleAddOwner(Player p, String sVictim, Region r) {
		if (plugin.getPermissionHandler().hasRegionPerm(p, "addowner", r)) {
			if (r == null) {
				sendNotInRegionMessage(p);
				return;
			}
			Player pVictim = Bukkit.getPlayerExact(sVictim);
			sVictim = sVictim.toLowerCase();
			if (!r.isOwner(sVictim)) {
				r.addOwner(sVictim);
				p.sendMessage(ChatColor.AQUA + "Added " + ChatColor.GOLD
						+ sVictim + ChatColor.AQUA + " as an owner.");
				if (pVictim != null) {
					if (pVictim.isOnline()) {
						pVictim.sendMessage(ChatColor.AQUA
								+ "You have been added as an owner to region: "
								+ ChatColor.GOLD + r.getName() + ChatColor.AQUA
								+ ", by: " + ChatColor.GOLD + p.getName()
								+ ChatColor.AQUA + ".");
					}
				}
			} else {
				p.sendMessage(ChatColor.RED
						+ "That player is already an owner in this region!");
			}
		} else {
			sendNoPermissionMessage(p);
		}
	}

	public void handleRemoveMember(Player p, String sVictim, Region r) {
		if (plugin.getPermissionHandler().hasRegionPerm(p, "removemember", r)) {
			if (r == null) {
				sendNotInRegionMessage(p);
				return;
			}
			Player pVictim = Bukkit.getPlayerExact(sVictim);
			sVictim = sVictim.toLowerCase();
			if (r.isMember(sVictim) || r.isOwner(sVictim)) {
				p.sendMessage(ChatColor.AQUA + "Removed " + ChatColor.GOLD
						+ sVictim + ChatColor.AQUA + " from this region.");
				r.removeMember(sVictim);
				if (pVictim != null) {
					if (pVictim.isOnline()) {
						pVictim.sendMessage(ChatColor.AQUA
								+ "You have been removed as a member from region: "
								+ ChatColor.GOLD + r.getName() + ChatColor.AQUA
								+ ", by: " + ChatColor.GOLD + p.getName()
								+ ChatColor.AQUA + ".");
					}
				}
			} else {
				p.sendMessage(ChatColor.RED + sVictim
						+ " isn't a member of this region.");
			}
		} else {
			sendNoPermissionMessage(p);
		}
	}

	public void handleRemoveOwner(Player p, String sVictim, Region r) {
		if (plugin.getPermissionHandler().hasRegionPerm(p, "removeowner", r)) {
			if (r == null) {
				sendNotInRegionMessage(p);
				return;
			}
			Player pVictim = Bukkit.getPlayerExact(sVictim);
			sVictim = sVictim.toLowerCase();
			if (r.isOwner(sVictim)) {
				if (r.ownersSize() > 1) {
					p.sendMessage(ChatColor.AQUA + "Made " + ChatColor.GOLD
							+ sVictim + ChatColor.AQUA
							+ " a member in this region.");
					r.removeOwner(sVictim);
					r.addMember(sVictim);
					if (pVictim != null) {
						if (pVictim.isOnline()) {
							pVictim.sendMessage(ChatColor.AQUA
									+ "You have been removed as an owner from region: "
									+ ChatColor.GOLD + r.getName()
									+ ChatColor.AQUA + ", by: "
									+ ChatColor.GOLD + p.getName()
									+ ChatColor.AQUA + ".");
						}
					}
				} else {
					p.sendMessage(ChatColor.AQUA
							+ "You can't remove "
							+ ChatColor.GOLD
							+ sVictim
							+ ChatColor.AQUA
							+ ", because they are the last owner in this region.");
				}
			} else {
				p.sendMessage(ChatColor.RED + sVictim
						+ " isn't an owner in this region.");
			}
		} else {
			sendNoPermissionMessage(p);
		}
	}

	public void handleRename(Player p, String newName, Region r) {
		if (plugin.getPermissionHandler().hasRegionPerm(p, "rename", r)) {
			if (r == null) {
				sendNotInRegionMessage(p);
				return;
			}
			if (plugin.getRegionManager().getRegion(newName, p.getWorld()) != null) {
				p.sendMessage(ChatColor.RED
						+ "That name is already taken, please choose another one.");
				return;
			}
			if ((newName.length() < 2) || (newName.length() > 16)) {
				p.sendMessage(ChatColor.RED
						+ "Invalid name. Please enter a 2-16 character name.");
				return;
			}
			if (newName.contains(" ")) {
				p.sendMessage(ChatColor.RED
						+ "The name of the region can't have a space in it.");
				return;
			}
			plugin.getRegionManager().rename(r, newName, p.getWorld());
			p.sendMessage(ChatColor.AQUA + "Made " + ChatColor.GOLD + newName
					+ ChatColor.AQUA + " the new name for this region.");
		} else {
			p.sendMessage(ChatColor.RED
					+ "You don't have sufficient permission to do that.");
		}
	}

	public void handleFlag(Player p, String flagName, Region r) {
		Flag f = Flag.getFlag(flagName);
		if (f != null) {
			if (p.hasPermission(f.permission)) {
				if (r == null) {
					sendNotInRegionMessage(p);
					return;
				}
				if (r.isOwner(p) || p.hasPermission("redprotect.admin.flag")) {
					plugin.getRegionManager().setFlag(r, f.getId(),
							!r.getFlag(f.getId()), p.getWorld());
					p.sendMessage(ChatColor.AQUA + "Flag \"" + f.name
							+ "\" has been set to " + r.getFlag(f.getId())
							+ ".");
				} else {
					p.sendMessage(ChatColor.AQUA
							+ "You don't have permission to toggle that flag in this region!");
				}
			}
		} else if (flagName.equalsIgnoreCase("info")
				|| flagName.equalsIgnoreCase("i")) {
			if (r == null) {
				sendNotInRegionMessage(p);
				return;
			}
			p.sendMessage(ChatColor.AQUA + "Flag values: (" + r.getFlagInfo()
					+ ChatColor.AQUA + ")");
		} else {
			p.sendMessage(ChatColor.AQUA
					+ "List of flags: [pvp, chest, lever, button, door, mobs, passives]");
		}
	}

	public void handleList(Player player, String name) {
		if (plugin.getPermissionHandler().hasPerm(player,
				"redprotect.admin.list")) {
			Set<Region> regions = plugin.getRegionManager().getRegions(name);
			int length = regions.size();
			if (length == 0) {
				player.sendMessage(ChatColor.AQUA
						+ "That player has no regions!");
			} else {
				player.sendMessage(ChatColor.AQUA + "Regions created:");
				player.sendMessage(ChatColor.AQUA
						+ "------------------------------------");
				Iterator<Region> i = regions.iterator();
				while (i.hasNext()) {
					player.sendMessage(ChatColor.AQUA + i.next().info());
				}
				player.sendMessage(ChatColor.AQUA
						+ "------------------------------------");
			}
			return;
		} else {
			player.sendMessage(ChatColor.RED
					+ "You don't have sufficient permission to do that.");
			return;
		}
	}

	private static void sendNotInRegionMessage(Player p) {
		p.sendMessage(NOT_IN_REGION_MESSAGE);
	}

	private static void sendNoPermissionMessage(Player p) {
		p.sendMessage(NO_PERMISSION_MESSAGE);
	}
}

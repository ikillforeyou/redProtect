package user.theovercaste.redprotect;

import static org.bukkit.ChatColor.RED;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.OverCaste.plugin.RedProtect.Region;

public class RPEntityListener implements Listener {
	RedProtect plugin;

	public RPEntityListener(RedProtect plugin) {
		this.plugin = plugin;
	}

	static final String noPvPMsg = (ChatColor.RED + "You can't PvP in this region!");
	static final String noPassiveHurtMsg = (ChatColor.RED + "You can't hurt passive entities in this region!");

	/*
	 * @EventHandler(priority = EventPriority.NORMAL) public void
	 * onEndermanPickup(EndermanPickupEvent e) { Region r =
	 * RedProtect.rm.getRegion(e.getEntity().getLocation()); if(r != null) {
	 * e.setCancelled(true); //no enderman griefing. } }
	 */

	/*
	 * @EventHandler(priority = EventPriority.NORMAL) public void
	 * onEndermanPlace(EndermanPlaceEvent e) { Region r =
	 * RedProtect.rm.getRegion(e.getEntity().getLocation()); if(r != null) {
	 * e.setCancelled(true); //no enderman griefing. } }
	 */

	@EventHandler
	public void onEntityTarget(EntityTargetEvent e) {
		Entity target = e.getTarget();
		if (target == null)
			return;
		Region r = RedProtect.rm.getRegion(target.getLocation());
		if (r != null) {
			if (!r.canMobs()) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		Entity e = event.getEntity();
		if (e == null)
			return;
		if (e instanceof Monster) { // ees monster
			Region r = RedProtect.rm.getRegion(e.getLocation());
			if (r != null) {
				if (!r.canMobs()) {
					if (event.getSpawnReason().equals(SpawnReason.NATURAL)) { // mob
																				// eggs,
																				// spawners,
																				// bed
																				// monsters,
																				// are
																				// all
																				// fine.
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.isCancelled())
			return;
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent de = (EntityDamageByEntityEvent) event;
			Entity e1 = de.getEntity();
			Entity e2 = de.getDamager();
			if (e2 == null) {
				return;
			}
			if (e2 instanceof Arrow) {
				Arrow a = (Arrow) e2;
				e2 = a.getShooter();
				a = null;
				if (e2 == null) {
					return;
				}
			}
			Region r1 = RedProtect.rm.getRegion(e1.getLocation());
			Region r2 = RedProtect.rm.getRegion(e2.getLocation());
			if (e1 instanceof Player) { // e1 is a player
				// Player p1 = (Player)e1;
				if (e2 instanceof Player) { // e1 is player, e2 is player (pvp)
					Player p2 = (Player) e2;
					if (r1 != null) { // r1 exists
						if (r2 != null) { // r1 exists, r2 exists
							if ((!r1.canPVP(p2)) || (!r2.canPVP(p2))) { // if
																		// attacker
																		// can't
																		// attack
																		// in
																		// both
																		// regions
																		// cancel.
								event.setCancelled(true);
								p2.sendMessage(noPvPMsg);
							}
						} else { // r1 exists, r2 doesn't
							if (!r1.canPVP(p2)) {
								event.setCancelled(true);
								p2.sendMessage(noPvPMsg);
							}
						}
					} else { // r1 doesn't exist
						if (r2 != null) { // r1 doesn't exist, r2 does
							if (!r2.canPVP(p2)) {
								event.setCancelled(true);
								p2.sendMessage(noPvPMsg);
							}
						} else { // r1 doesn't exist, r2 doesn't either.
							// nothing to do here.
						}
					}
				}
			} else if ((e1 instanceof Animals) || (e1 instanceof Villager)) { // Passive
				Region r = RedProtect.rm.getRegion(e1.getLocation());
				if (r != null) {
					if (e2 instanceof Player) {
						Player p = (Player) e2;
						if (!r.canHurtPassives(p)) {
							event.setCancelled(true);
							p.sendMessage(noPassiveHurtMsg);
						}
					} else {
						if (!r.getFlag(6)) {
							event.setCancelled(true); // something other then a
														// play tried to kill a
														// passive entity
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onHangingDamaged(HangingBreakByEntityEvent event) {
		Entity remover = event.getRemover();
		if (remover instanceof Player) {
			Player player = (Player) remover;
			Location loc = event.getEntity().getLocation();
			Region r = RedProtect.rm.getRegion(loc);
			if (r != null && !r.canBuild(player)) {
				player.sendMessage(RED + "You can't build here!");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPotionSplash(PotionSplashEvent event) {
		Region r;
		LivingEntity thrower = event.getPotion().getShooter();
		boolean harmful = false;
		for (PotionEffect e : event.getPotion().getEffects()) {
			PotionEffectType t = e.getType();
			if (t.equals(PotionEffectType.BLINDNESS)
					|| t.equals(PotionEffectType.CONFUSION)
					|| t.equals(PotionEffectType.HARM)
					|| t.equals(PotionEffectType.HUNGER)
					|| t.equals(PotionEffectType.POISON)
					|| t.equals(PotionEffectType.SLOW)
					|| t.equals(PotionEffectType.SLOW_DIGGING)
					|| t.equals(PotionEffectType.WEAKNESS)
					|| t.equals(PotionEffectType.WITHER)) {
				harmful = true;
			}
		}
		if (harmful) {
			Player shooter;
			if (thrower instanceof Player) {
				shooter = (Player) thrower;
			} else {
				shooter = null;
			}
			for (Entity e : event.getAffectedEntities()) {
				if ((e instanceof Animals) || (e instanceof Villager)) {
					r = RedProtect.rm.getRegion(e.getLocation());
					if (shooter == null && r != null) {
						event.setCancelled(true);
						return;
					} else if (r != null && shooter != null) {
						if (!r.canHurtPassives(shooter)) {
							event.setCancelled(true);
							return;
						}
					}
				} else if (e instanceof Player) {
					r = RedProtect.rm.getRegion(e.getLocation());
					if (r != null) {
						if (shooter == null) {
							event.setCancelled(true);
							return;
						} else {
							if (!r.canPVP(shooter)) {
								event.setCancelled(true);
								return;
							}
						}
					}
				}
			}
		}
	}
}

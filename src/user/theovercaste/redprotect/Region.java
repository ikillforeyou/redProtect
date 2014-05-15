package user.theovercaste.redprotect;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.*;

//import org.bukkit.World;
import org.bukkit.entity.Player;

import user.theovercaste.redprotect.RedProtect;

//This is the old region class, still included fot it to be able to be converted.
public class Region {
	private int locationXs[];
	private int locationZs[];
	private int minMbrX = 0;
	private int maxMbrX = 0;
	private int minMbrZ = 0;
	private int maxMbrZ = 0;
	private String name;
	private List<String> owners;
	private List<String> members;
	private String creator = "";

	protected final boolean[] flagValues = new boolean[Flag.values().length];

	public Region(String name, List<String> list, int[] x, int[] z) {
		for (int i = 0; i < flagValues.length; i++) {
			flagValues[i] = Flag.values()[i].getDefaultValue();
		}

		int size = x.length;
		if (size != z.length) {
			throw new Error("The X & Z arrays are different sizes!");
		}
		this.locationXs = x;
		this.locationZs = z;
		if (size < 4) {
			throw new Error(
					"You can't generate a polygon with less then 4 points!");
		}
		if (size == 4) {
			// RedProtect.logger.debug("One of these regions, is not like the other ones, one of these regions, is an mbr.");
			this.locationXs = null;
			this.locationZs = null;
		}
		this.owners = list;
		this.members = new ArrayList<String>();
		this.name = name;
		this.creator = list.get(0);
		maxMbrX = x[0];
		minMbrX = x[0];
		maxMbrZ = z[0];
		minMbrZ = z[0];
		for (int i = 0; i < x.length; i++) {
			if (x[i] > maxMbrX) {
				maxMbrX = x[i];
			}
			if (x[i] < minMbrX) {
				minMbrX = x[i];
			}
			if (z[i] > maxMbrZ) {
				maxMbrZ = z[i];
			}
			if (z[i] < minMbrZ) {
				minMbrZ = z[i];
			}
		}
		// RedProtect.logger.debug("Bounding rect: " + maxMbrX + ", " + minMbrX
		// + ", " + maxMbrZ + ", " + minMbrZ);
	}

	public void setFlag(int flag, boolean value) {
		if (flag > flagValues.length) {
			return;
		}
		flagValues[flag] = value;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String s) {
		this.creator = s;
	}

	public String getName() {
		return name;
	}

	public int getCenterX() {
		return ((minMbrX + maxMbrX) / 2);
	}

	public int getCenterZ() {
		return Double.valueOf(((double) (minMbrZ + maxMbrZ) / 2d)).intValue();
	}

	public int getMaxMbrX() {
		return maxMbrX;
	}

	public int getMinMbrX() {
		return minMbrX;
	}

	public int getMaxMbrZ() {
		return maxMbrZ;
	}

	public int getMinMbrZ() {
		return minMbrZ;
	}

	public String getInfo() {
		String ownerstring = "";
		String memberstring = "";
		for (int i = 0; i < owners.size(); i++) {
			ownerstring = ownerstring + ", " + owners.get(i);
		}
		for (int i = 0; i < members.size(); i++) {
			memberstring = memberstring + ", " + members.get(i);
		}
		if (owners.size() > 0) {
			ownerstring = ownerstring.substring(2);
		} else {
			ownerstring = "None";
		}
		if (members.size() > 0) {
			memberstring = memberstring.substring(2);
		} else {
			memberstring = "None";
		}
		return AQUA + "Name: " + GOLD + name + AQUA + ", Creator: " + GOLD
				+ creator + AQUA + ", Center: [" + GOLD + getCenterX() + AQUA
				+ ", " + GOLD + getCenterZ() + AQUA + "], Owners: [" + GOLD
				+ ownerstring + AQUA + "], Members: [" + GOLD + memberstring
				+ AQUA + "].";
	}

	/*
	 * public boolean rename(String newName){ World w =
	 * RedProtect.rm.getWorld(this); if (RedProtect.rm.getRegion(newName, w) !=
	 * null) return false; name = newName; return true; }
	 */

	/*
	 * public void addToRM(World w){ RedProtect.rm.add(this, w); }
	 */

	public int getArea() {
		if (this.locationXs == null) {
			return ((maxMbrX - minMbrX) * (maxMbrZ - minMbrZ));
		} else {
			int area = 0;
			for (int i = 0; i < locationXs.length; i++) {
				int j = ((i + 1) % locationXs.length);
				area += ((locationXs[i] * locationZs[j]) - (locationZs[i] * locationXs[j]));
			}
			area = Math.abs(area / 2);
			return area;
		}
	}

	/*
	 * public boolean intersects(int bx, int bz, String w){ int i; int
	 * x1,x2,y1,y2; int xnew,xold,ynew,yold; xold=x[size-1]; yold=z[size-1]; for
	 * (i=0 ; i < size ; i++) { xnew=x[i]; ynew=z[i]; if (xnew > xold) {
	 * x1=xold; x2=xnew; y1=yold; y2=ynew; } else { x1=xnew; x2=xold; y1=ynew;
	 * y2=yold; } if (((xnew < bx) == (bx <= xold)) && (bz-y1)*(x2-x1) <
	 * (y2-y1)*(bx-x1)) { return true; } xold=xnew; yold=ynew; } return false; }
	 */// PiP #1:
		// http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html

	/*
	 * public boolean intersects(int bx, int bz, String w){ String world =
	 * RedProtect.rm.getWorld(this).getName(); if (!world.equalsIgnoreCase(w))
	 * return false; return intersects(bx, bz); } //PiP:
	 * http://www.visibone.com/inpoly/inpoly.c.txt
	 */

	public boolean inBoundingRect(int bx, int bz) {
		return ((bx <= maxMbrX) && (bx >= minMbrX) && (bz <= maxMbrZ) && (bz >= minMbrZ));
	}

	public boolean inBoundingRect(Region other) {
		if (other.maxMbrX < minMbrX) {
			return false;
		}
		if (other.maxMbrZ < minMbrZ) {
			return false;
		}
		if (other.minMbrX > maxMbrX) {
			return false;
		}
		if (other.minMbrZ > maxMbrZ) {
			return false;
		}
		return true;
	}

	public boolean intersects(int bx, int bz) {
		if (this.locationXs == null) {
			return true;
		}
		int i, j;
		boolean ret = false;
		for (i = 0, j = locationXs.length - 1; i < locationXs.length; j = i++) {
			if ((((locationZs[i] <= bz) && (bz < locationZs[j])) || ((locationZs[j] <= bz) && (bz < locationZs[i])))
					&& (bx < (locationXs[j] - locationXs[i])
							* (bz - locationZs[i])
							/ (locationZs[j] - locationZs[i]) + locationXs[i])) {
				ret = !ret;
			}
		}
		return ret;
	} // PiP: http://paulbourke.net/geometry/insidepoly/

	public boolean isOwner(String p) {
		p = p.toLowerCase();
		return owners.contains(p);
	}

	public boolean isOwner(Player player) {
		return owners.contains(player.getName().toLowerCase());
	}

	public boolean isMember(String p) {
		p = p.toLowerCase();
		return members.contains(p);
	}

	public boolean isMember(Player player) {
		return members.contains(player.getName().toLowerCase());
	}

	public void addMember(String p) {
		p = p.toLowerCase();
		if (!members.contains(p) && !owners.contains(p)) {
			members.add(p);
		}
	}

	public void addOwner(String p) {
		p = p.toLowerCase();
		if (members.contains(p)) {
			members.remove(p);
		}
		if (!owners.contains(p)) {
			owners.add(p);
		}
	}

	public void removeMember(String p) {
		p = p.toLowerCase();
		if (members.contains(p)) {
			members.remove(p);
		}
		if (owners.contains(p)) {
			owners.remove(p);
		}
	}

	public void removeOwner(String p) {
		p = p.toLowerCase();
		if (owners.contains(p)) {
			owners.remove(p);
		}
	}

	public boolean getFlag(int flag) {
		return flagValues[flag];
	}

	public boolean canBuild(RedProtect plugin, Player p) {
		if (p.getLocation().getY() < plugin.getConfigurationManager()
				.getHeightStart())
			return true; // For mining and stuff.
		return (isOwner(p) || isMember(p) || p
				.hasPermission("redprotect.bypass"));
	}

	public boolean canPVP(Player p) {
		if (flagValues[0]) { // if flag 0, pvp, allowed
			return true;
		}
		return (p.hasPermission("redprotect.bypass"));
	}

	public boolean canChest(Player p) {
		if (flagValues[1]) {
			return true;
		}
		return (isOwner(p) || isMember(p) || p
				.hasPermission("redprotect.bypass"));
	}

	public boolean canLever(Player p) {
		if (flagValues[2]) {
			return true;
		}
		return (isOwner(p) || isMember(p) || p
				.hasPermission("redprotect.bypass"));
	}

	public boolean canButton(Player p) {
		if (flagValues[3]) {
			return true;
		}
		return (isOwner(p) || isMember(p) || p
				.hasPermission("redprotect.bypass"));
	}

	public boolean canDoor(Player p) {
		if (flagValues[4]) {
			return true;
		}
		return (isOwner(p) || isMember(p) || p
				.hasPermission("redprotect.bypass"));
	}

	public boolean canHurtMobs() {
		return flagValues[5];
	}

	public boolean canHurtPassives(Player p) {
		if (flagValues[6]) {
			return true;
		}
		return (isOwner(p) || isMember(p) || p
				.hasPermission("redprotect.bypass"));
	}

	public String getFlagInfo() {
		return (AQUA + "Player vs Player: " + GOLD + flagValues[0] + AQUA
				+ ", Chest opening: " + GOLD + flagValues[1] + AQUA
				+ ", Lever flipping: " + GOLD + flagValues[2] + AQUA
				+ ", Button pushing: " + GOLD + flagValues[3] + AQUA
				+ ", Door toggling: " + GOLD + flagValues[4] + AQUA
				+ ", Monster spawning: " + GOLD + flagValues[5] + AQUA
				+ ", Passive entity hurting: " + GOLD + flagValues[6]);
	}

	public void setName(String name) {
		this.name = name;
	}
}

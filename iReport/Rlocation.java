package iReport;

import org.bukkit.plugin.java.JavaPlugin;

public class Rlocation {

	public static String getxyz(JavaPlugin i, String p){
		org.bukkit.Location loc;
		String s;
		try {
			loc = i.getServer().getPlayer(p).getLocation();
			s = String.valueOf("x " + loc.getBlockX() + " y " + loc.getBlockY() + " z " + loc.getBlockZ());
			return s;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return null;
	
	}
}

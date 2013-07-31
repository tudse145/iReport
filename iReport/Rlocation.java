package iReport;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

public class Rlocation {

    public static String getxyz(JavaPlugin i, String p) {
        org.bukkit.Location loc;
        String s;
        try {
            loc = i.getServer().getPlayer(p).getLocation();
            s = String.valueOf("x " + loc.getBlockX() + " y " + loc.getBlockY() + " z " + loc.getBlockZ());
            return s;
        } catch (Exception e) {
            i.getLogger().log(Level.WARNING, p + " is not online");
        }

        return null;

    }
}

package iReport.util;

import iReport.iReport;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class Rlocation implements Listener {

    @EventHandler
    public void name(final PlayerLoginEvent event) {
        new Thread(new Runnable() {
            public void run() {
                Player p = event.getPlayer();
                if (!Data.init().playermap.containsKey(p.getUniqueId())) {
                    Data.init().playermap.put(p.getUniqueId(), p.getName());
                } else if (Data.init().playermap.get(p.getUniqueId()) != p.getName()) {
                    Data.init().playermap.put(p.getUniqueId(), p.getName());
                }
            }
        }).start();

    }
    
    public static String getxyz(String p, CommandSender sender) {
        try {
            @SuppressWarnings("deprecation")
            Location loc = Bukkit.getPlayer(p).getLocation();
            return String.valueOf("x " + loc.getBlockX() + " y " + loc.getBlockY() + " z " + loc.getBlockZ());
        } catch (Exception e) {
            if (sender != null) {
                iReport.logger.log(Level.WARNING, p + " is not online");
                sender.sendMessage(ChatColor.RED + p + " is not online");
            }
        }

        return null;

    }
}

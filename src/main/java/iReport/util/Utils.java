package iReport.util;

import iReport.iReport;

import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

@SuppressWarnings(value = { "deprecation" })
public class Utils implements Listener {

    private static final Object lock = new Object();

    @EventHandler
    public void login(final PlayerLoginEvent event) {
        new Thread(new Runnable() {
            @Override
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

    public static void reportplayer(String target, String reporttype, CommandSender sender, boolean b) {
        UUID p = Bukkit.getPlayer(target).getUniqueId();
        Data data = Data.init();
        data.playermapo.put(p, target);
        Object o = data.playermapor.get(target);
        if (!data.playermapor.containsKey(target) && o == null ? true : o.equals(p) || b)
        	data.playermapor.put(target, p);
        else
        	sender.sendMessage("player "+ target + " is alredy reported with another UUID please look at the reports or add true");
        synchronized (lock) {
            if (data.playermapr.containsKey(p)) {
                String s = data.playermapr.get(p);
                data.playermapr.put(p, s + reporttype);
            } else {
                data.playermapr.put(p, reporttype);
            }
        }
    }
}

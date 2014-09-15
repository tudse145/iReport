package iReport.util;

import static iReport.util.Data.init;
import iReport.IReport;

import java.util.Map;
import java.util.UUID;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.Player;
import org.spongepowered.api.event.SpongeEventHandler;
import org.spongepowered.api.math.Vector3d;
import org.spongepowered.api.plugin.PluginContainer;

@SuppressWarnings(value = { "deprecation" })
public class Utils {

    public static Game game;
    public static PluginContainer controler;

    /*@SpongeEventHandler
    public void login(final PlayerLoginEvent event) {
        Player p = event.getPlayer();
        if (!Data.init().playermap.containsKey(p.getUniqueID())) {
            Data.init().playermap.put(p.getUniqueID(), p.getName());
        } else if (Data.init().playermap.get(p.getUniqueID()) != p.getName()) {
            Data.init().playermap.put(p.getUniqueID(), p.getName());
            if (Utils.isReported(p.getUniqueID())) {
                Utils.updateusernameMYSQL(p.getUniqueID(), p.getName());
            }
        }
    }*/

    public static boolean isReported(UUID uniqueId) {
        return Data.init().playermapr.get(uniqueId) != null;
    }

    public static String getxyz(String p, CommandSender sender) {
        try {
            Vector3d loc = game.getPlayer(p).getLocation();
            return String.valueOf("world " + loc.getWorld().getName() + " x " + loc.getX() + " y " + loc.getY() + " z " + loc.getZ());
        } catch (Exception e) {
            if (sender != null) {
                sender.sendMessage(ChatColor.RED + p + " is not online");
            }
        }

        return null;

    }

    public static void reportplayer(String target, String reporttype, CommandSender sender, boolean b) {
        boolean isreported = false;
        UUID p = null;
        try {
            p = game.getPlayer(target).getUniqueId();
        } catch (NullPointerException e) {
            sender.sendMessage(ChatColor.RED + target + " is not online");
            return;
        }
        Data data = Data.init();
        data.playermapo.put(p, target);
        Object o = data.playermapor.get(target);
        if (!data.playermapor.containsKey(target) && o == null ? true : o.equals(p) || b)
            data.playermapor.put(target, p);
        else
            sender.sendMessage("player " + target + " is alredy reported with another UUID please look at the reports or add true");
        synchronized (data.playermap.get(p)) {
            if (data.playermapr.containsKey(p)) {
                isreported = true;
                String s = data.playermapr.get(p);
                data.playermapr.put(p, s + reporttype + "reporter: " + sender.getName() + " ;");
            } else {
                data.playermapr.put(p, reporttype + "reporter: " + sender.getName() + " ;");
            }
        }
        updateMYSQL(game.getPlayer(target), isreported);
    }

    public static void updateMYSQL(Player player, boolean isReported) {
        UUID uuid = player.getUniqueID();
        Map<UUID, String> map1 = init().playermap;
        Map<UUID, String> map2 = init().playermapo;
        Map<UUID, String> map3 = init().playermapr;
        if (isReported) {
            IReport.getMYSQL().queryUpdate("INSERT INTO reports (`uuid`, `currentname`, `Report`, `username`) values ('" + uuid + "','" + map1.get(uuid) + "','" + map3.get(uuid) + "','" + map2.get(uuid) + "')");
        } else {
            IReport.getMYSQL().queryUpdate("UPDATE Reports SET Report = '" + map3.get(uuid) + "' WHERE uuid = '" + uuid + "'");
        }
    }

    public static void updateusernameMYSQL(UUID uniqueId, String name) {
        IReport.getMYSQL().queryUpdate("UPDATE Reports SET currentname = '" + name + "' WHERE uuid = '" + uniqueId + "'");
    }
}

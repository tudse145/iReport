package iReport.util;

import static iReport.util.Data.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandSource;

import com.flowpowered.math.vector.Vector3d;

import iReport.IReport;

public enum Utils {
    INSTENCE;

    @Subscribe(ignoreCancelled = false)
    public void login(PlayerJoinEvent event) {
        Player p = event.getEntity();
        if (!Data.init().playermap.containsKey(p.getUniqueId())) {
            Data.init().playermap.put(p.getUniqueId(), p.getName());
        } else if (Data.init().playermap.get(p.getUniqueId()) != p.getName()) {
            Data.init().playermap.put(p.getUniqueId(), p.getName());
            if (Utils.isReported(p.getUniqueId())) {
                Utils.updateusernameMYSQL(p.getUniqueId(), p.getName());
            }
        }
    }

    public static boolean isReported(UUID uniqueId) {
        return Data.init().playermapr.get(uniqueId) != null;
    }

    public static String getxyz(String p, CommandSource source) {
        try {
            Player player = IReport.server.getPlayer(p).get();
            Vector3d loc = player.getLocation().getPosition();
            return String.valueOf("world " + player.getWorld().getName() + " x " + (int)loc.getX() + " y " + (int)loc.getY() + " z " + (int)loc.getZ());
        } catch (Exception e) {
            e.printStackTrace();
            if (source != null) {
                source.sendMessage(Texts.builder(p + " is not online").color(TextColors.RED).build());
            }
        }

        return null;

    }

    public static void reportplayer(String target, String reporttype, CommandSource sender, boolean b) {
        boolean isreported = false;
        UUID p = null;
        try {
            p = IReport.server.getPlayer(target).get().getUniqueId();
        } catch (IllegalStateException e) {
            sender.sendMessage(Texts.builder(target + " is not online").color(TextColors.RED).build());
            return;
        }
        Data data = Data.init();
        data.playermapo.put(p, target);
        Object o = data.playermapor.get(target);
        if (!data.playermapor.containsKey(target) && o == null ? true : o.equals(p) || b)
            data.playermapor.put(target, p);
        else
            sender.sendMessage(Texts.of("player " + target + " is alredy reported with another UUID please look at the reports or add true"));
        synchronized (data.playermap.get(p)) {
            if (data.playermapr.containsKey(p)) {
                isreported = true;
                String s = data.playermapr.get(p);
                data.playermapr.put(p, s + reporttype + "reporter: " + sender.getName() + " ;");
            } else {
                data.playermapr.put(p, reporttype + "reporter: " + sender.getName() + " ;");
            }
        }
        updateMYSQL(IReport.server.getPlayer(target).get(), isreported);
    }

    public static void updateMYSQL(Player player, boolean isReported) {
        UUID uuid = player.getUniqueId();
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

    public static void PrintStackTrace(Throwable t) {
        IReport.LOGGER.error(t.toString());
        for (StackTraceElement Element : t.getStackTrace()) {
            IReport.LOGGER.error("\tat " + Element.toString());
        }
    }
    
    public static List<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<String>();
        for (Player player : IReport.server.getOnlinePlayers()) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }
}

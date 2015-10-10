package iReport.util;

import static iReport.util.Data.init;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.spongepowered.api.GameProfile;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.translation.ResourceBundleTranslation;
import org.spongepowered.api.util.TextMessageException;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import com.flowpowered.math.vector.Vector3d;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public enum Utils {
    INSTENCE;

    private static final Lock LOCK = new ReentrantLock();

    @Listener(ignoreCancelled = false)
    public void login(ClientConnectionEvent.Auth event) {
        GameProfile profile = event.getProfile();
        if (!Data.init().playermap.containsKey(profile.getUniqueId())) {
            Data.init().playermap.put(profile.getUniqueId(), profile.getName());
        } else if (Data.init().playermap.get(profile.getUniqueId()) != profile.getName()) {
            Data.init().playermap.put(profile.getUniqueId(), profile.getName());
            if (Utils.isReported(profile.getUniqueId())) {
                Utils.updateusernameMYSQL(profile.getUniqueId(), profile.getName());
            }
        }
    }

    public static boolean isReported(UUID uniqueId) {
        return Data.init().playermapr.get(uniqueId) != null;
    }

    public static String getxyz(String playername, CommandSource source) throws CommandException {
        try {
            Player player = Constance.server.getPlayer(playername).get();
            Vector3d loc = player.getLocation().getPosition();
            return String.valueOf("world " + player.getWorld().getName() + " x " + (int) loc.getX() + " y " + (int) loc.getY() + " z " + (int) loc.getZ());
        } catch (IllegalStateException e) {
            throw new CommandException(get("not.onlipse", playername));
        }
    }

    public static void reportplayer(String target, String reporttype, CommandSource sender, boolean forcw) throws CommandException {
        UUID p = null;
        try {
            p = Constance.server.getPlayer(target).get().getUniqueId();
        } catch (IllegalStateException e) {
            throw new CommandException(get("not.onlipse", target));
        }
        boolean isreported = isReported(p);
        Data data = Data.init();
        data.playermapo.put(p, target);
        Object o = data.playermapor.get(target);
        if (!data.playermapor.containsKey(target) && o == null ? true : o.equals(p) || forcw)
            data.playermapor.put(target, p);
        else
            sender.sendMessage(Texts.of("player " + target + " is alredy reported with another UUID please look at the reports or add true"));
        LOCK.lock();
        try {
            if (isreported) {
                String s = data.playermapr.get(p);
                data.playermapr.put(p, s + reporttype + "reporter: " + sender.getName() + " ;");
            } else {
                data.playermapr.put(p, reporttype + "reporter: " + sender.getName() + " ;");
            }
            savePlayer(p);
        } finally {
            LOCK.unlock();
        }
        updateMYSQL(Constance.server.getPlayer(target).get(), isreported);
    }

    public static void updateMYSQL(Player player, boolean isReported) {
        UUID uuid = player.getUniqueId();
        Map<UUID, String> map1 = init().playermap;
        Map<UUID, String> map2 = init().playermapo;
        Map<UUID, String> map3 = init().playermapr;
        if (!isReported) {
            Constance.getMYSQL().queryUpdate("INSERT INTO reports (`uuid`, `currentname`, `Report`, `username`) values ('" + uuid + "','" + map1.get(uuid) + "','" + map3.get(uuid) + "','" + map2.get(uuid) + "')");
        } else {
            Constance.getMYSQL().queryUpdate("UPDATE Reports SET Report = '" + map3.get(uuid) + "' WHERE uuid = '" + uuid + "'");
        }
    }

    public static void updateusernameMYSQL(UUID uniqueId, String name) {
        Constance.getMYSQL().queryUpdate("UPDATE Reports SET currentname = '" + name + "' WHERE uuid = '" + uniqueId + "'");
    }

    public static void printStackTrace(Throwable t) {
        Constance.LOGGER.error(t.toString());
        for (StackTraceElement Element : t.getStackTrace()) {
            Constance.LOGGER.error("\tat " + Element.toString());
        }
        try {
            for (Throwable tb : t.getSuppressed()) {
                Constance.LOGGER.error("\tSuppressed: " + tb.toString());
                for (StackTraceElement Element : tb.getStackTrace()) {
                    Constance.LOGGER.error("\t \tat " + Element.toString());
                }
            }
        } catch (Exception e) {
        }
    }

    public static List<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        for (Player player : Constance.server.getOnlinePlayers()) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }

    public static void savePlayer(UUID uuid) {
        File file = new File(Constance.configfolder, "reports.cfg");
        HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setFile(file).build();
        ConfigurationNode config;
        try {
            config = cfgfile.load();
            ConfigurationNode node = config.getNode("reports");
            Map<String, String> configDefaults = new HashMap<>();
            ConfigurationNode node2 = node.getNode(uuid.toString());
            configDefaults.put("reportedename", Data.init().playermapo.get(uuid));
            configDefaults.put("currenttname", Data.init().playermap.get(uuid));
            configDefaults.put("reports", Data.init().playermapr.get(uuid));
            node2.setValue(configDefaults);
            cfgfile.save(config);
        } catch (IOException e) {
            printStackTrace(e);
        }
    }

    @SuppressWarnings("deprecation")
    public static Text get(String key, Object... args) {
        try {
            return Texts.legacy('&').from(new ResourceBundleTranslation(key, Constance.LOOKUP_FUNC).get(Constance.locale, args));
        } catch (TextMessageException e) {
            return null;
        }
    }
}

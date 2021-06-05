package ireport.util;

import static ireport.util.Data.init;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.translation.ResourceBundleTranslation;

import com.flowpowered.math.vector.Vector3d;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public class Utils {

	private Utils() {}

    public static boolean isReported(UUID uniqueId) {
        return Data.init().getPlayermapr().get(uniqueId) != null;
    }

    public static String getxyz(String playername) throws CommandException {
        Optional<Player> optionalplayer = Constance.server.getPlayer(playername);
        if (optionalplayer.isPresent()) {
			Player player = optionalplayer.get();
            Vector3d loc = player.getLocation().getPosition();
            return String.valueOf("world " + player.getWorld().getUniqueId() + " x " + (int) loc.getX() + " y " + (int) loc.getY() + " z " + (int) loc.getZ());
        } else {
            throw new CommandException(get("not.online", playername));
        }
    }

    public static void reportplayer(String target, String reporttype, CommandSource sender, boolean forcw) throws CommandException {
       
        Player player = Constance.server.getPlayer(target).orElseThrow(() -> new CommandException(get("not.online", target)));
        UUID playeruuid = player.getUniqueId();
        boolean isreported = isReported(playeruuid);
        Data data = Data.init();
        data.getPlayermapo().put(playeruuid, target);
        Object o = data.getPlayermapor().get(target);
        if (!data.getPlayermapor().containsKey(target) && o == null ? true : o.equals(playeruuid) || forcw) {
            data.getPlayermapor().put(target, playeruuid);
        } else {
            throw new CommandException(Text.of("player " + target + " is alredy reported with another UUID please look at the reports or add true"));
        }
       synchronized (Utils.class) {
           if (isreported) {
               String s = data.getPlayermapr().get(playeruuid);
               data.getPlayermapr().put(playeruuid, s + reporttype + "reporter: " + sender.getName() + " ;");
           } else {
               data.getPlayermapr().put(playeruuid, reporttype + "reporter: " + sender.getName() + " ;");
           }
           savePlayer(playeruuid);
       }
       updateMYSQL(player, isreported);
    }

    public static void updateMYSQL(Player player, boolean isReported) throws CommandException {
        UUID uuid = player.getUniqueId();
        Map<UUID, String> map1 = init().getPlayermap();
        Map<UUID, String> map2 = init().getPlayermapo();
        Map<UUID, String> map3 = init().getPlayermapr();
        if (!isReported) {
            try {
				Constance.getMYSQL().queryUpdate("INSERT INTO reports (`uuid`, `currentname`, `Report`, `username`) values (?,?,?,?)", uuid.toString(), map1.get(uuid), map3.get(uuid), map2.get(uuid));
			} catch (SQLException e) {
				throw new CommandException(Text.of(e.getMessage()), e);
			}
        } else {
            try {
				Constance.getMYSQL().queryUpdate("UPDATE Reports SET Report = ? WHERE uuid = ?", map3.get(uuid), uuid.toString());
			} catch (SQLException e) {
				throw new CommandException(Text.of(e.getMessage()), e);
			}
        }
    }

    public static void updateusernameMYSQL(UUID uniqueId, String name) {
        try {
			Constance.getMYSQL().queryUpdate("UPDATE Reports SET currentname = ? WHERE uuid = ?", name, uniqueId.toString());
		} catch (SQLException e) {
			printStackTrace(e);
		}
    }

    public static void printStackTrace(Throwable t) {
        for (String line : collectStackTraces(t).split(System.getProperty("line.separator"))) {
            Constance.LOGGER.error(line);
        }
    }

    public static List<String> getPlayerNames() {
        return Constance.server.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    public static void savePlayer(UUID uuid) {
        Path file = Constance.configfolder.resolve("reports.cfg");
        HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setPath(file).build();
        try {
        	ConfigurationNode config = cfgfile.load();
            ConfigurationNode node = config.getNode("reports");
            Map<String, String> configDefaults = new HashMap<>();
            ConfigurationNode node2 = node.getNode(uuid.toString());
            configDefaults.put("reportedename", Data.init().getPlayermapo().get(uuid));
            configDefaults.put("currenttname", Data.init().getPlayermap().get(uuid));
            configDefaults.put("reports", Data.init().getPlayermapr().get(uuid));
            node2.setValue(configDefaults);
            cfgfile.save(config);
        } catch (IOException e) {
            printStackTrace(e);
        }
    }

    public static Text get(String key, Object... args) {
        return TextSerializers.FORMATTING_CODE.deserialize(new ResourceBundleTranslation(key, Constance.LOOKUP_FUNC).get(Constance.locale, args));
    }
    
    private static String collectStackTraces(Throwable throwable) {
        Writer writer = new StringWriter(1024);
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        printWriter.write(System.getProperty("line.separator"));
        return writer.toString();
    }
}

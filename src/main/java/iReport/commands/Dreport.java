package iReport.commands;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.google.common.collect.Lists;

import iReport.util.Constance;
import iReport.util.Data;
import iReport.util.Utils;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public final class Dreport implements CommandCallable {

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments, @Nullable  Location<World> targetPosition) throws CommandException {
        if (!testPermission(source)) {
            return Lists.newArrayList();
        }
        return Data.init().playermapo.keySet().parallelStream().map(UUID::toString).filter(s -> s.startsWith(arguments.split(" ")[0])).collect(Collectors.toList());
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        if (!testPermission(source)) {
            throw new CommandException(Utils.get("permission.missing"));
        }
        String[] args = arguments.split(" ");
        Data data = Data.init();
        if (args[0].equals("*")) {
            if (source.hasPermission("ireport.dreport.all")) {
                data.playermapo.keySet().stream().map(UUID::toString).forEach(this::delete);
                try {
					Constance.getMYSQL().queryUpdate("DELETE FROM reports");
				} catch (SQLException e) {
					throw new CommandException(Text.of(e.getMessage()), e);
				}
                data.playermapo.clear();
                data.playermapor.clear();
                data.playermapr.clear();
                source.sendMessage(Utils.get("dreport.sucess.all"));
                return CommandResult.success();
            } else {
                throw new CommandException(Utils.get("permission.missing"));
            }

        }
        try {
            UUID uuid = UUID.fromString(args[0]);
            String playername = data.playermapo.get(uuid);
            data.playermapo.remove(uuid);
            data.playermapr.remove(uuid);
            data.playermapor.remove(playername);
            delete(uuid.toString());
            source.sendMessage(Utils.get("dreport.sucess.all", playername));
            Constance.getMYSQL().queryUpdate("DELETE FROM reports WHERE uuid = '" + UUID.fromString(args[0]) + "'");
            return CommandResult.success();
        } catch (IllegalArgumentException e) {
            throw new CommandException(Utils.get("dreport.error"));
        } catch (SQLException e) {
			throw new CommandException(Text.of(e.getMessage()), e);
		}
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("ireport.dreport");
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(Utils.get("dreport.description"));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Utils.get("dreport.description"));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Text.of("<UUID>");
    }

    public void delete(String uuid) {
        HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setPath(Constance.configpath).build();
        try {
            ConfigurationNode config = cfgfile.load();
            config.getNode("reports").removeChild(uuid);
            cfgfile.save(config);
        } catch (IOException e) {
        }
    }
}

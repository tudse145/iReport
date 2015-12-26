package iReport.commands;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

import iReport.util.Constance;
import iReport.util.Data;
import iReport.util.Utils;

public final class greport implements CommandCallable {

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        String[] args = arguments.split(" ");
        if (args[0].equalsIgnoreCase("tp")) {
            return Data.init().playermapo.keySet().parallelStream().filter(uuid -> {
                String s = Data.init().playermapr.get(uuid);
                return s.contains("gReport: ");
            }).map(UUID::toString).filter(s -> s.startsWith(args.length > 1 ? args[1] : "")).collect(Collectors.toList());
        }
        return Utils.getPlayerNames();
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        String[] args = arguments.split(" ");
        if (!testPermission(source)) {
            throw new CommandException(Utils.get("permission.missing"));
        }
        if (args[0].equalsIgnoreCase("tp") && source instanceof Player && args.length == 3 && source.hasPermission("ireport.greport.tp")) {
            Player player = (Player) source;
            try {
                Location<World> loc = getLocationFromUuid(UUID.fromString(args[1]), Integer.parseInt(args[2]));
                player.setLocation(loc);
                return CommandResult.success();
            } catch (NumberFormatException e) {
                throw new CommandException(Texts.of("Argument 3 is not a valid number"));
            }
            
        }
        if (args.length > 0 && !args[0].isEmpty()) {
            String player = source.getName();
            String target = args[0];
            Utils.reportplayer(target, "gReport: " + Utils.getxyz(args[0], source) + " ", source, args.length > 1 ? Boolean.valueOf(args[1]) : false);
            source.sendMessage(Utils.get("greport.sucess", target));
            Text text = Utils.get("greport.notification", player, target);
            Constance.server.getOnlinePlayers().parallelStream().filter(p -> p.hasPermission("iReport.seereport") && p != source).forEach(p -> p.sendMessage(text));
            return CommandResult.success();
        }
        throw new CommandException(Utils.get("not.enough.args"));
    }

    private Location<World> getLocationFromUuid(UUID playerUuid, int id) {
        id--;
        Stream<String> report = Stream.of(Data.init().playermapr.get(playerUuid).split(";"));
        List<Location<World>> list = report.filter(s -> s.startsWith("gReport: ")).map(s -> {
            String tmp = s.substring(9);
            tmp = tmp.substring(0, tmp.lastIndexOf(" reporter:"));
            String[] data = tmp.split(" ");
            return new Location<World>(Constance.server.getWorld(UUID.fromString(data[1])).get(), new Vector3d(Double.parseDouble(data[3]), 
                    Double.parseDouble(data[5]), Double.parseDouble(data[7])));
        }).collect(Collectors.toList());
        return list.get(id);
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("ireport.greport");
    }

    @Override
    public Optional<? extends Text> getShortDescription(CommandSource source) {
        return Optional.of(Utils.get("greport.description"));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Utils.get("greport.description"));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Texts.of("<name>");
    }

}

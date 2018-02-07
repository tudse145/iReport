package ireport.commands;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

import ireport.util.Constance;
import ireport.util.Data;
import ireport.util.Utils;

public final class greport implements CommandCallable {

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments, @Nullable  Location<World> targetPosition) throws CommandException {
        String[] args = arguments.split(" ");
        if (args[0].equalsIgnoreCase("tp")) {
            return Data.init().getPlayermapo().keySet().parallelStream().filter(uuid -> {
                String s = Data.init().getPlayermapr().get(uuid);
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
        if (args[0].equalsIgnoreCase("tp") && args.length == 2 && source.hasPermission("iReport.greport.tp")) {
            List<String> list = getLocationFromUuid(UUID.fromString(args[1])).stream().map(l -> 
                String.format("World %s x %s y %s z %s", l.getExtent().getName(), l.getX(), l.getY(), l.getZ())
                ).sorted().collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                source.sendMessage(Text.of((i + 1) + 1 + " " + list.get(i)));
            }
            return CommandResult.success();
        }
        if (args[0].equalsIgnoreCase("tp") && source instanceof Player && args.length == 3 && source.hasPermission("iReport.greport.tp")) {
            Player player = (Player) source;
            try {
                Location<World> loc = getLocationFromUuid(UUID.fromString(args[1])).get(Integer.parseInt(args[2]) - 1);
                player.setLocation(loc);
                return CommandResult.success();
            } catch (NumberFormatException e) {
                throw new CommandException(Text.of("Argument 3 is not a valid number"));
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

    private List<Location<World>> getLocationFromUuid(UUID playerUuid) {
        Stream<String> report = Stream.of(Data.init().getPlayermapr().get(playerUuid).split(";"));
        return report.filter(s -> s.startsWith("gReport: ")).sorted().map(s -> {
            String tmp = s.substring(9);
            tmp = tmp.substring(0, tmp.lastIndexOf(" reporter:"));
            String[] data = tmp.split(" ");
            return new Location<World>(Constance.server.getWorld(UUID.fromString(data[1])).get(), new Vector3d(Double.parseDouble(data[3]),
                    Double.parseDouble(data[5]), Double.parseDouble(data[7])));
        }).collect(Collectors.toList());
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("iReport.greport");
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(Utils.get("greport.description"));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Utils.get("greport.description"));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Text.of("<name>");
    }

}

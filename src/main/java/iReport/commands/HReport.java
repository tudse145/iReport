package ireport.commands;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import ireport.util.Constance;
import ireport.util.Utils;

public final class HReport implements CommandCallable {

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments, @Nullable  Location<World> targetPosition) throws CommandException {
        return Utils.getPlayerNames();
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        if (!testPermission(source)) {
            throw new CommandException(Utils.get("permission.missing"));
        }
        String[] args = arguments.split(" ");
        if (args.length > 1 && !args[0].isEmpty() && !args[1].isEmpty()) {
            String player = source.getName();
            String target = args[0];
            Utils.reportplayer(target, "hReport: " + args[1] + " ", source, args.length > 2 ? Boolean.valueOf(args[1]) : false);
            source.sendMessage(Utils.get("Greport.sucess", target));
            Text text = Utils.get("hreport.notification", player, target, args[1]);
            Constance.server.getOnlinePlayers().parallelStream().filter(p -> p.hasPermission("iReport.seereport") && p != source).forEach(p -> p.sendMessage(text));
            return CommandResult.success();
        }
        throw new CommandException(Utils.get("not.enough.args"));
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("iReport.hreport");
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(Utils.get("hreport.description"));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Utils.get("hreport.description"));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Text.of("<name>");
    }

}

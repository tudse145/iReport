package iReport.commands;

import java.util.List;
import java.util.Optional;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import iReport.util.Constance;
import iReport.util.Utils;

public final class sreport implements CommandCallable {

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return Utils.getPlayerNames();
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        if (!testPermission(source)) {
            throw new CommandException(Utils.get("permission.missing"));
        }
        String[] args = arguments.split(" ");
        if (args.length > 0 && !args[0].isEmpty()) {
            String player = source.getName();
            String target = args[0];
            Utils.reportplayer(target, "sReport ", source, args.length > 1 ? Boolean.valueOf(args[1]) : false);
            source.sendMessage(Utils.get("greport.sucess", target));
            Text text = Utils.get("sreport.notification", player, target);
            Constance.server.getOnlinePlayers().parallelStream().filter(p -> p.hasPermission("iReport.seereport") && p != source).forEach(p -> p.sendMessage(text));
            return CommandResult.success();
        }
        throw new CommandException(Utils.get("not.enough.args"));
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("ireport.sreport");
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(Utils.get("sreport.description"));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Utils.get("sreport.description"));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Texts.of("<name>");
    }
}

package iReport.commands;

import iReport.IReport;
import iReport.util.Utils;

import java.util.List;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import com.google.common.base.Optional;

public final class sreport implements CommandCallable {

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return Utils.getPlayerNames();
    }

    @Override
    public Optional<CommandResult> process(CommandSource source, String arguments) throws CommandException {
        if (!testPermission(source)) {
            source.sendMessage(Texts.of(TextColors.RED, "You don't have permission to use this command"));
            return Optional.<CommandResult>absent();
        }
        String[] args = arguments.split(" ");
        if (args.length > 0 && !args[0].isEmpty()) {
            String player = source.getName();
            String target = args[0];
            Utils.reportplayer(target, "sReport ", source, args.length > 1 ? Boolean.valueOf(args[1]) : false);
            source.sendMessage(Texts.builder("You successfully reported ").color(TextColors.BLUE).append(Texts.builder(target).color(TextColors.RED).build()).build());
            for (Player p : IReport.server.getOnlinePlayers()) {
                if (p.hasPermission("iReport.seereport") && p != source) {
                    p.sendMessage(Texts.builder(player + " has reported " + target + " for swearing").color(TextColors.RED).build());
                }
            }
            return Optional.of(CommandResult.success());
        }
        throw new CommandException(Texts.builder("Not enough arguments").color(TextColors.RED).build());
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("ireport.sreport");
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of((Text)Texts.of("Reports a player for swearing"));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of((Text)Texts.of("Reports a player for swearing"));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Texts.of("<name>");
    }
}

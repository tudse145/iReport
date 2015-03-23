package iReport.commands;

import iReport.IReport;
import iReport.util.Utils;

import java.util.List;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import com.google.common.base.Optional;

public class HReport implements CommandCallable {

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }

    @Override
    public boolean call(CommandSource sorce, String arguments, List<String> parents) throws CommandException {
        String[] args = arguments.split(" ");
        if (args.length > 1) {
            String player = sorce.getName();
            String target = args[0];
            Utils.reportplayer(target, "hReport: " + args[1] + " ", sorce, args.length > 2 ? Boolean.valueOf(args[1]) : false);
            sorce.sendMessage(Texts.builder("You successfully reported ").color(TextColors.BLUE).append(Texts.builder(target).color(TextColors.RED).build()).build());
            for (Player p : IReport.server.getOnlinePlayers()) {
                if (p.hasPermission("iReport.seereport") && p != sorce) {
                    p.sendMessage(Texts.builder(player + " has reported " + target + " for " + args[1] + " hacking ").color(TextColors.RED).build());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("ireport.hreport");
    }

    @Override
    public Optional<String> getShortDescription() {
        return Optional.of("Reports a player for hack");
    }

    @Override
    public Optional<String> getHelp() {
        return Optional.of("Reports a player for hack");
    }

    @Override
    public String getUsage() {
        return "/hreport <name>";
    }

}

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
import org.spongepowered.api.util.command.CommandSource;

public class sreport implements CommandCallable {

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }

    @Override
    public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException {
        String[] args = arguments.split(" ");
        if (args.length > 0) {
            String player = source.getName();
            String target = args[0];
            Utils.reportplayer(target, "sReport ", source, args.length > 1 ? Boolean.valueOf(args[1]) : false);
            source.sendMessage(Texts.builder("You successfully reported ").color(TextColors.BLUE).append(Texts.builder(target).color(TextColors.RED).build()).build());
            for (Player p : IReport.server.getOnlinePlayers()) {
                if (p.hasPermission("iReport.seereport") && p != source) {
                    p.sendMessage(Texts.builder(player + " has reported " + target + " for swearing").color(TextColors.RED).build());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("ireport.sreport");
    }

    @Override
    public String getShortDescription(CommandSource source) {
        return "Reports a player for swearing";
    }

    @Override
    public Text getHelp(CommandSource source) {
        return Texts.of("Reports a player for swearing");
    }

    @Override
    public String getUsage(CommandSource source) {
        return "/sreport <name>";
    }
}

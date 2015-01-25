package iReport.commands;

import iReport.IReport;
import iReport.util.Utils;

import java.util.List;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.player.User;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.message.Messages;
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
            String player = Utils.getName(sorce);
            String target = args[0];
            Utils.reportplayer(target, "hReport: " + args[1] + " ", sorce, args.length > 2 ? Boolean.valueOf(args[1]) : false);
            sorce.sendMessage(Messages.builder("You successfully reported ").color(TextColors.BLUE).append(Messages.builder(target).color(TextColors.RED).build()).build());
            for (Player p : IReport.server.getOnlinePlayers()) {
                if (p.hasPermission("iReport.seereport") && p != sorce) {
                    p.sendMessage(Messages.builder(player + " has reported " + target + " for " + args[1] + " hacking ").color(TextColors.RED).build());
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<String> getHelp() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUsage() {
        // TODO Auto-generated method stub
        return null;
    }

}

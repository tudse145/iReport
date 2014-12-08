package iReport.commands;

import iReport.IReport;
import iReport.util.Utils;

import java.util.List;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.message.Messages;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import com.google.common.base.Optional;

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
            source.sendMessage(Messages.builder("You successfully reported ").color(TextColors.BLUE).append(
                    Messages.builder(target).color(TextColors.RED).build()).build());
            for (Player p : IReport.server.getOnlinePlayers()) {
                if ((p.isOp() || p.hasPermission("iReport.seereport")) && p != source) {
                    p.sendMessage(Messages.builder(player + " has reported " + target + " for swearing").color(TextColors.RED).build());
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

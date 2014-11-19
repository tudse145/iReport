package iReport.commands;

import iReport.IReport;
import iReport.util.Utils;

import java.util.List;

import org.spongepowered.api.entity.Player;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.Description;

import com.mojang.realmsclient.gui.ChatFormatting;

public class greport implements CommandCallable {

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
            Utils.reportplayer(target, "gReport: " + Utils.getxyz(args[0], null) + " ", source, args.length > 1 ? Boolean.valueOf(args[1]) : false);
            source.sendMessage(ChatFormatting.BLUE + "You successfully reported " + ChatFormatting.RED + target);
            for (Player p : IReport.game.getOnlinePlayers()) {
                if ((p.isOp() || p.hasPermission("iReport.seereport")) && p != source) {
                    p.sendMessage(ChatFormatting.RED + player + " has reported " + target + " for griefing");
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Description getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("ireport.greport");
    }

}

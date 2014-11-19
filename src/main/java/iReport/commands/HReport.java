package iReport.commands;

import iReport.IReport;
import iReport.util.Utils;

import java.util.List;

import org.bukkit.ChatFormatting;
import org.spongepowered.api.entity.Player;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.Description;

import com.mojang.realmsclient.gui.ChatFormatting;

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
            sorce.sendMessage(ChatFormatting.BLUE + "You successfully reported " + ChatFormatting.RED + target);
            for (Player p : IReport.game.getOnlinePlayers()) {
                if ((p.isOp() || p.hasPermission("iReport.seereport")) && p != sorce) {
                    p.sendMessage(ChatFormatting.RED + player + " has reported " + target + " for " + args[1] + " hacking ");
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
        return source.hasPermission("ireport.hreport");
    }

}

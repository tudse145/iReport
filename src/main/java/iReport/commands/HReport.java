package iReport.commands;

import java.util.List;

import iReport.IReport;
import iReport.util.Utils;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.Description;
import org.spongepowered.api.entity.Player;

public class HReport implements CommandCallable {

    private IReport plugin;

    public HReport(IReport plugin) {
        this.plugin = plugin;
    }

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
            plugin.getReports().set("reports.hacking." + player, new StringBuilder("type: ").append(args[1]).toString() + "; " + target);
            sorce.sendMessage(ChatColor.BLUE + "You successfully reported " + ChatColor.RED + target);
            plugin.saveReports();
            Utils.reportplayer(target, "hReport: " + args[1] + " ", sorce, args.length > 2 ? Boolean.valueOf(args[1]) : false);
            for (Player p : sorce.getServer().getOnlinePlayers()) {
                if ((p.isOp() || p.hasPermission("iReport.seereport")) && p != sorce) {
                    p.sendMessage(ChatColor.RED + player + " has reported " + target + " for " + args[1] + " hacking ");
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

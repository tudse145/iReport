package iReport.commands;

import java.util.List;

import iReport.IReport;
import iReport.util.Utils;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.Description;
import org.spongepowered.api.entity.Player;

public class sreport implements CommandCallable {

    private IReport plugin;

    public sreport(IReport plugin) {
        this.plugin = plugin;
    }

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
            plugin.getReports().set("reports.swearing." + player, "; " + target);
            source.sendMessage(ChatColor.BLUE + "You successfully reported " + ChatColor.RED + target);
            plugin.saveReports();
            Utils.reportplayer(target, "sReport ", source, args.length > 1 ? Boolean.valueOf(args[1]) : false);
            for (Player p : source.getServer().getOnlinePlayers()) {
                if ((p.isOp() || p.hasPermission("iReport.seereport")) && p != source) {
                    p.sendMessage(ChatColor.RED + player + " has reported " + target + " for swearing");
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
        return source.hasPermission("ireport.sreport");
    }
}

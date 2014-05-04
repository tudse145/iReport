package iReport.commands;

import iReport.iReport;
import iReport.mysql.MYSQL;
import iReport.util.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class greport implements CommandExecutor {

    private iReport plugin;

    public greport(iReport plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            String player = sender.getName();
            String target = args[0];
            if ((!sender.hasPermission("ireport.greport")) && (!sender.isOp())) {
                sender.sendMessage(ChatColor.RED + "You don't have permission");
                return true;
            }
            plugin.grtReports().set("reports.griefing." + player, Utils.getxyz(args[0], sender) + "; " + target);
            sender.sendMessage(ChatColor.BLUE + "You successfully reported " + ChatColor.RED + target);
            if (MYSQL.isenable) {
                plugin.getMYSQL().queryUpdate("INSERT INTO reports (`name`,`Reason`) values ('" + target + "','" + Utils.getxyz(args[0], null) + "')");
            }
            plugin.saveReports();
            for (Player p : sender.getServer().getOnlinePlayers()) {
                if ((p.isOp()) || (p.hasPermission("iReport.seereport"))) {
                    p.sendMessage(ChatColor.RED + player + " has reported " + target + " for griefing");
                }
            }
            Utils.reportplayer(target, "gReport: " + Utils.getxyz(args[0], null) + " ");
            return true;
        }
        return false;
    }

}

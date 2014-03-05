package iReport;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.entity.Player;

public class greport extends VanillaCommand implements CommandExecutor {

    private iReport plugin;

    protected greport(iReport plugin) {
        super("greport");
        this.plugin = plugin;
        this.description = "Report a griefer logs position of the reporter";
        this.usageMessage = "/greport <player>";
        this.setPermission("ireport.greport");
    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {
        if (args.length == 1) {
            String player = sender.getName();
            String target = args[0];
            if ((!sender.hasPermission("ireport.greport")) && (!sender.isOp())) {
                sender.sendMessage(ChatColor.RED + "You don't have permission");
                return true;
            }
            plugin.grtReports().set("reports.griefing." + player, Rlocation.getxyz(plugin, args[0]) + "; " + target);
            sender.sendMessage(ChatColor.BLUE + "You successfully reported " + ChatColor.RED + target);
            if (MYSQL.isenable) {
                plugin.getMYSQL().queryUpdate("INSERT INTO reports (`name`,`Reason`) values ('" + target + "','" + Rlocation.getxyz(plugin, args[0]) + "')");
            }
            plugin.saveReports();
            for (Player p : sender.getServer().getOnlinePlayers()) {
                if ((p.isOp()) || (p.hasPermission("iReport.seereport"))) {
                    p.sendMessage(ChatColor.RED + player + " has reported " + target + " for griefing");
                }
            }
            return true;
        }
        return false;
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
            plugin.grtReports().set("reports.griefing." + player, Rlocation.getxyz(plugin, args[0]) + "; " + target);
            sender.sendMessage(ChatColor.BLUE + "You successfully reported " + ChatColor.RED + target);
            if (MYSQL.isenable) {
                plugin.getMYSQL().queryUpdate("INSERT INTO reports (`name`,`Reason`) values ('" + target + "','" + Rlocation.getxyz(plugin, args[0]) + "')");
            }
            plugin.saveReports();
            for (Player p : sender.getServer().getOnlinePlayers()) {
                if ((p.isOp()) || (p.hasPermission("iReport.seereport"))) {
                    p.sendMessage(ChatColor.RED + player + " has reported " + target + " for griefing");
                }
            }
            return true;
        }
        return false;
    }

}

package iReport;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HReport extends VanillaCommand {

    private iReport plugin;

    protected HReport(iReport plugin) {
        super("hReport");
        this.plugin = plugin;
        this.description = "Report a hacker + what type of hack used";
        this.usageMessage = "/hreport <player> <type of hack>";
        this.setPermission("ireport.hreport");
    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {
        if (args.length == 2) {
            String player = sender.getName();
            String target = args[0];
            if (!sender.hasPermission("ireport.hreport")) {
              sender.sendMessage(ChatColor.RED + "You don't have permission to perform this command");
              return true;
            }
            plugin.getConfig().set("reports.hacking." + player, new StringBuilder("type: ").append(args[1]).toString() + "; " + target);
            sender.sendMessage(ChatColor.BLUE + "You successfully reported " + ChatColor.RED + target);
            plugin.saveConfig();
              if (MYSQL.isenable) {
                  if (plugin instanceof iReport) {
                      iReport pl = (iReport) plugin;
                      pl.getMYSQL().queryUpdate("INSERT INTO reports (`name`,`Reason`) values ('" + target + "','" + args[1] + "')"); 
                  }
              }
            
            for (Player p : sender.getServer().getOnlinePlayers()) {
              if ((p.isOp()) || (p.hasPermission("iReport.seereport"))) {
                p.sendMessage(ChatColor.RED + player + " has reported " + target + " for hacking " + args[1]);
              }
            }
            return true;
        }
        return false;
    }

}

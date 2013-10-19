package iReport;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class sreport extends VanillaCommand {

    private iReport plugin;

    protected sreport(iReport plugin) {
        super("sReport");
        this.plugin = plugin;
        this.description = "Report a swearing player";
        this.usageMessage = "/sreport <player>";
        this.setPermission("ireport.sreport");
    }
   
    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {
    if (args.length == 1) {
        String player = sender.getName();
        String target = args [0];
      if ((!sender.hasPermission("ireport.sreport")) && (!sender.isOp())) {
        sender.sendMessage(ChatColor.RED + "You don't have permission");
        return true;
      }      
      plugin.getConfig().set("reports.swearing." + player, "; " + target);
      sender.sendMessage(ChatColor.BLUE + "You successfully reported " + ChatColor.RED + target);
        if (MYSQL.isenable) {
           plugin.getMYSQL().queryUpdate("INSERT INTO reports (`name`,`Reason`) values ('" + target + "',' Swearing ')");
        }
      plugin.saveConfig();
       for (Player p : sender.getServer().getOnlinePlayers()) {
        if ((p.isOp()) || (p.hasPermission("iReport.seereport"))) {
          p.sendMessage(ChatColor.RED + player + " has reported " + target + " for swearing");
               }
            }
            return true;
        }
        return false;
    }

}

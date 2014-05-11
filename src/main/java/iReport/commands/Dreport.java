package iReport.commands;

import java.util.UUID;

import iReport.util.Data;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Dreport implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Data data = Data.init();
        if (args[0].equals("*") && sender.hasPermission("ireport.dreport.all"))
        {
            data.playermap.clear();
            data.playermapo.clear();
            data.playermapor.clear();
            data.playermapr.clear();
            sender.sendMessage(ChatColor.GREEN+"Successfully cleared reports");
            return true;
        }
        try {
            String s = data.playermapo.get(UUID.fromString(args[0]));
            data.playermapo.remove(UUID.fromString(args[0]));
            data.playermapr.remove(UUID.fromString(args[0]));
            data.playermapor.remove(s);
            sender.sendMessage(ChatColor.GREEN+"Successfully deleted "+s);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED+"invalid UUID");
        }
        return true;
    }

}

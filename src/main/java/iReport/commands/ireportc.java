package iReport.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VanillaCommand;

public class ireportc extends VanillaCommand implements CommandExecutor {

    public ireportc() {
        super("iReport");
        this.description = "Get plugin help";
        this.usageMessage = "/ireport";
    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "==============================");
        sender.sendMessage(ChatColor.GREEN + "/greport - Report a griefer");
        sender.sendMessage(ChatColor.GREEN + "/hreport - Report a hacker");
        sender.sendMessage(ChatColor.GREEN + "/sreport - Report a swearer");
        sender.sendMessage(ChatColor.GREEN + "/ireport - Show this help menu");
        sender.sendMessage(ChatColor.YELLOW + "==============================");
        sender.sendMessage(ChatColor.DARK_PURPLE + "Created by tudse145 & heni123321");
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
        sender.sendMessage(ChatColor.YELLOW + "==============================");
        sender.sendMessage(ChatColor.GREEN + "/greport - Report a griefer");
        sender.sendMessage(ChatColor.GREEN + "/hreport - Report a hacker");
        sender.sendMessage(ChatColor.GREEN + "/sreport - Report a swearer");
        sender.sendMessage(ChatColor.GREEN + "/ireport - Show this help menu");
        sender.sendMessage(ChatColor.YELLOW + "==============================");
        sender.sendMessage(ChatColor.DARK_PURPLE + "Created by tudse145 & heni123321");
        return true;
    }
}

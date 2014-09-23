package iReport.commands;

import java.util.List;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.Description;

public class ireportc implements CommandCallable {

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }

    @Override
    public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException {
        source.sendMessage(ChatColor.YELLOW + "==============================");
        source.sendMessage(ChatColor.GREEN + "/greport - Report a griefer");
        source.sendMessage(ChatColor.GREEN + "/hreport - Report a hacker");
        source.sendMessage(ChatColor.GREEN + "/sreport - Report a swearer");
        source.sendMessage(ChatColor.GREEN + "/ireport - Show this help menu");
        source.sendMessage(ChatColor.GREEN + "/reports - Shows all reported players");
        source.sendMessage(ChatColor.GREEN + "/reports gui - Shows all reported players in a GUI");
        source.sendMessage(ChatColor.GREEN + "/dreport - Delete a report");
        source.sendMessage(ChatColor.YELLOW + "==============================");
        source.sendMessage(ChatColor.BLUE + "Created by tudse145 & heni123321");
        return true;
    }

    @Override
    public Description getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return true;
    }
}

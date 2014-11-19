package iReport.commands;

import java.util.List;

import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.Description;

import com.mojang.realmsclient.gui.ChatFormatting;

public class ireportc implements CommandCallable {

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }

    @Override
    public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException {
        source.sendMessage(ChatFormatting.YELLOW + "==============================");
        source.sendMessage(ChatFormatting.GREEN + "/greport - Report a griefer");
        source.sendMessage(ChatFormatting.GREEN + "/hreport - Report a hacker");
        source.sendMessage(ChatFormatting.GREEN + "/sreport - Report a swearer");
        source.sendMessage(ChatFormatting.GREEN + "/ireport - Show this help menu");
        source.sendMessage(ChatFormatting.GREEN + "/reports - Shows all reported players");
        source.sendMessage(ChatFormatting.GREEN + "/reports gui - Shows all reported players in a GUI");
        source.sendMessage(ChatFormatting.GREEN + "/dreport - Delete a report");
        source.sendMessage(ChatFormatting.YELLOW + "==============================");
        source.sendMessage(ChatFormatting.BLUE + "Created by tudse145 & heni123321");
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

package iReport.commands;

import java.util.List;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

public class ireportc implements CommandCallable {

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }

    @Override
    public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException {
        source.sendMessage(Texts.builder("==============================").color(TextColors.YELLOW).build());
        source.sendMessage(Texts.builder("/greport - Report a griefer").color(TextColors.GREEN).build());
        source.sendMessage(Texts.builder("/hreport - Report a hacker").color(TextColors.GREEN).build());
        source.sendMessage(Texts.builder("/sreport - Report a swearer").color(TextColors.GREEN).build());
        source.sendMessage(Texts.builder("/ireport - Show this help menu").color(TextColors.GREEN).build());
        source.sendMessage(Texts.builder("/reports - Shows all reported players").color(TextColors.GREEN).build());
        source.sendMessage(Texts.builder("/reports gui - Shows all reported players in a GUI").color(TextColors.GREEN).build());
        source.sendMessage(Texts.builder("/dreport - Delete a report").color(TextColors.GREEN).build());
        source.sendMessage(Texts.builder("==============================").color(TextColors.YELLOW).build());
        source.sendMessage(Texts.builder("Created by tudse145 & heni123321").color(TextColors.BLUE).build());
        return true;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return true;
    }

    @Override
    public String getShortDescription(CommandSource source) {
        return "Shows plugin help";
    }

    @Override
    public Text getHelp(CommandSource source) {
        return Texts.of("Shows plugin help");
    }

    @Override
    public String getUsage(CommandSource source) {
        return "/ireport";
    }
}

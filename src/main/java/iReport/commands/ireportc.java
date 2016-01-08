package iReport.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import iReport.util.Utils;

public final class ireportc implements CommandCallable {

    private static final List<String> LIST = Collections.unmodifiableList(new ArrayList<>());

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return LIST;
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        source.sendMessage(Text.builder("==============================").color(TextColors.YELLOW).build());
        source.sendMessage(Utils.get("ireport.test1"));
        source.sendMessage(Utils.get("ireport.test2"));
        source.sendMessage(Utils.get("ireport.test3"));
        source.sendMessage(Utils.get("ireport.test4"));
        source.sendMessage(Utils.get("ireport.test5"));
        source.sendMessage(Utils.get("ireport.test6"));
        source.sendMessage(Utils.get("ireport.test7"));
        source.sendMessage(Text.builder("==============================").color(TextColors.YELLOW).build());
        source.sendMessage(Utils.get("ireport.test8", "tudse145 & heni123321"));
        return CommandResult.success();
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return true;
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(Utils.get("ireport.description"));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Utils.get("ireport.description"));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Text.of("");
    }
}

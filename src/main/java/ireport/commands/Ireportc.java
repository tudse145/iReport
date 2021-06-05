package ireport.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import ireport.util.Utils;

public final class Ireportc implements CommandCallable {

    private static final List<String> LIST = Collections.unmodifiableList(new ArrayList<>());

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments, @Nullable  Location<World> targetPosition) throws CommandException {
        return LIST;
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        source.sendMessage(Text.builder("==============================").color(TextColors.YELLOW).build());
        source.sendMessage(Utils.get("ireport.text1"));
        source.sendMessage(Utils.get("ireport.text2"));
        source.sendMessage(Utils.get("ireport.text3"));
        source.sendMessage(Utils.get("ireport.text4"));
        source.sendMessage(Utils.get("ireport.text5"));
        source.sendMessage(Utils.get("ireport.text6"));
        source.sendMessage(Utils.get("ireport.text7"));
        source.sendMessage(Text.builder("==============================").color(TextColors.YELLOW).build());
        source.sendMessage(Utils.get("ireport.text8", "tudse145 & heni123321"));
        return CommandResult.success();
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return true;
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(Utils.get("iReport.description"));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Utils.get("iReport.description"));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Text.of("");
    }
}

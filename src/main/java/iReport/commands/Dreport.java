package iReport.commands;

import iReport.IReport;
import iReport.util.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.message.Messages;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import com.google.common.base.Optional;

public class Dreport implements CommandCallable {

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        Set<UUID> set = Data.init().playermapo.keySet();
        List<String> list2 = new ArrayList<String>();
        for (UUID uuid : set) {
            list2.add(uuid.toString());
        }
        List<String> list = new ArrayList<String>();
        for (String string : list2) {
            if (string.startsWith(arguments.split(" ")[0])) {
                list.add(string);
            }
        }
        return list;
    }

    @Override
    public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException {
        String[] args = arguments.split(" ");
        Data data = Data.init();
        if (args[0].equals("*")) {
            if (source.hasPermission("ireport.dreport.all")) {
                for (UUID uuid : data.playermapo.keySet()) {
                    IReport.getMYSQL().queryUpdate("DELETE FROM reports WHERE uuid = '" + uuid.toString() + "'");
                }
                data.playermapo.clear();
                data.playermapor.clear();
                data.playermapr.clear();
                source.sendMessage(Messages.builder("Successfully cleared reports").color(TextColors.GREEN).build());
                return true;
            } else {
                source.sendMessage(Messages.builder("You don't have permission").color(TextColors.RED).build());
                return true;
            }

        }
        try {
            String s = data.playermapo.get(UUID.fromString(args[0]));
            data.playermapo.remove(UUID.fromString(args[0]));
            data.playermapr.remove(UUID.fromString(args[0]));
            data.playermapor.remove(s);
            source.sendMessage(Messages.builder("Successfully deleted " + s).color(TextColors.GREEN).build());
            IReport.getMYSQL().queryUpdate("DELETE FROM reports WHERE uuid = '" + UUID.fromString(args[0]) + "'");
        } catch (IllegalArgumentException e) {
            source.sendMessage(Messages.builder("invalid UUID").color(TextColors.RED).build());
        }
        return true;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("ireport.dreport");
    }

    @Override
    public Optional<String> getShortDescription() {
        return Optional.of("Deletes a report");
    }

    @Override
    public Optional<String> getHelp() {
        return Optional.of("Deletes a report");
    }

    @Override
    public String getUsage() {
        return "/dreport <UUID>";
    }

}

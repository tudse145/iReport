package iReport.commands;

import iReport.IReport;
import iReport.util.Data;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public final class Dreport implements CommandCallable {

    private static  File file = new File(IReport.configfolder, "reports.cfg");
    
    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        if (!testPermission(source)) {
            return Lists.newArrayList();
        }
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
    public Optional<CommandResult> process(CommandSource source, String arguments) throws CommandException {
        if (!testPermission(source)) {
            source.sendMessage(Texts.of(TextColors.RED, "You don't have permission to use this command"));
            return Optional.<CommandResult>absent();
        }
        String[] args = arguments.split(" ");
        Data data = Data.init();
        if (args[0].equals("*")) {
            if (source.hasPermission("ireport.dreport.all")) {
                for (UUID uuid : data.playermapo.keySet()) {
                    delete(uuid.toString());
                }
                IReport.getMYSQL().queryUpdate("DELETE * FROM reports");
                data.playermapo.clear();
                data.playermapor.clear();
                data.playermapr.clear();
                source.sendMessage(Texts.builder("Successfully cleared reports").color(TextColors.GREEN).build());
                return Optional.of(CommandResult.success());
            } else {
                throw new CommandException(Texts.builder("You don't have permission").color(TextColors.RED).build());
            }

        }
        try {
            UUID uuid = UUID.fromString(args[0]);
            String s = data.playermapo.get(uuid);
            data.playermapo.remove(uuid);
            data.playermapr.remove(uuid);
            data.playermapor.remove(s);
            delete(uuid.toString());
            source.sendMessage(Texts.builder("Successfully deleted " + s).color(TextColors.GREEN).build());
            IReport.getMYSQL().queryUpdate("DELETE FROM reports WHERE uuid = '" + UUID.fromString(args[0]) + "'");
        } catch (IllegalArgumentException e) {
            throw new CommandException(Texts.builder("invalid UUID").color(TextColors.RED).build());
        }
        return Optional.of(CommandResult.success());
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("ireport.dreport");
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of((Text)Texts.of("Deletes a report"));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of((Text)Texts.of("Deletes a report"));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Texts.of("<UUID>");
    }
    
    public void delete(String uuid) {
        HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setFile(file).build();
        try {
            ConfigurationNode config = cfgfile.load();
            config.getNode("reports").removeChild(uuid);
            cfgfile.save(config);
        } catch (IOException e) {
        }
    }
}

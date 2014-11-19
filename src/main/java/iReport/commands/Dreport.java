package iReport.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.Description;

import iReport.IReport;
import iReport.util.Data;

public class Dreport implements CommandCallable {

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        Set<UUID> set = Data.init().playermapo.keySet();
        List<String> list2 = new ArrayList<String>();
        for (UUID uuid : set) {
            list2.add(uuid.toString());
        }
        List<String> list = new ArrayList<String>();
        if (source.hasPermission("iReport.dreport")) {
            for (String string : list2) {
                if (string.startsWith(arguments.split(" ")[0])) {
                    list.add(string);
                }
            }
            return list;
        }
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
                source.sendMessage(ChatColor.GREEN + "Successfully cleared reports");
                return true;
            } else {
                source.sendMessage(ChatColor.RED + "You don't have permission");
                return true;
            }

        }
        try {
            String s = data.playermapo.get(UUID.fromString(args[0]));
            data.playermapo.remove(UUID.fromString(args[0]));
            data.playermapr.remove(UUID.fromString(args[0]));
            data.playermapor.remove(s);
            source.sendMessage(ChatColor.GREEN + "Successfully deleted " + s);
            IReport.getMYSQL().queryUpdate("DELETE FROM reports WHERE uuid = '" + UUID.fromString(args[0]) + "'");
        } catch (IllegalArgumentException e) {
            source.sendMessage(ChatColor.RED + "invalid UUID");
        }
        return true;
    }

    @Override
    public Description getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("ireport.dreport");
    }

}

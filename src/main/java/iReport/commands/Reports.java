package iReport.commands;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import iReport.iReport;
import static iReport.util.Data.init;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reports implements CommandExecutor {

    @SuppressWarnings("unused")
    private iReport plugin;

    public Reports(iReport iReport) {
        this.plugin = iReport;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        Map<UUID, String> iterator1 = init().playermap;
        Map<UUID, String> iterator2 = init().playermapo;
        Iterator<Entry<UUID, String>> iterator3 = init().playermapr.entrySet().iterator();
        while (iterator3.hasNext()) {
            Entry<UUID, String> e = iterator3.next();
            UUID u = e.getKey();
            sender.sendMessage("UUID: " + u + " currentname: " + iterator1.get(u) + " " + e.getValue() + " username: " + iterator2.get(u));
        }
        return true;
    }
}

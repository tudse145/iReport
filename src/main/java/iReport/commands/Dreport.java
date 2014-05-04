package iReport.commands;

import java.util.UUID;

import iReport.iReport;
import iReport.util.Data;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Dreport implements CommandExecutor {

    @SuppressWarnings("unused")
    private iReport plugin;

    public Dreport(iReport iReport) {
        this.plugin = iReport;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Data data = Data.init();
        try {
            data.playerlist.remove(args[0]);
            UUID.randomUUID();
            data.playermapo.remove(UUID.fromString(args[0]));
            data.playermapr.remove(UUID.fromString(args[0]));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return true;
    }

}

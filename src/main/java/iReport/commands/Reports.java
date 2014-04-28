package iReport.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VanillaCommand;

public class Reports extends VanillaCommand implements CommandExecutor {

    public Reports() {
        super("Reports");
        this.description = "get report list";
        this.usageMessage = "/reports";
        this.setPermission("ireport.reports");
    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {
        try {
            Scanner sc = new Scanner(new File("plugins/iReport/", "reports.yml"));
            while (sc.hasNext()) {
                sender.sendMessage(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException e) {
        }

        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
        try {
            Scanner sc = new Scanner(new File("plugins/iReport/", "reports.yml"));
            while (sc.hasNext()) {
                sender.sendMessage(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException e) {
        }

        return true;
    }
}

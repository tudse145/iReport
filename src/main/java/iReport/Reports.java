package iReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VanillaCommand;

public class reports extends VanillaCommand{
    
     private iReport plugin;
     protected reports(iReport plugin) {
        super("Reports");
        this.plugin = plugin;
        this.description = "get report list";
        this.usageMessage = "/reports";
        this.setPermission("ireport.reports");
     }

  @Override
  public boolean execute(CommandSender sender, String arg1, String[] args) {
    if (true) {
      try {
        Scanner sc = new Scanner(new File("plugins/iReport/", "config.yml"));
        while (sc.hasNext()) {
          sender.sendMessage(sc.nextLine());
        }
        sc.close();
      }
      catch (FileNotFoundException e)
      {
      }

      return true;
    }
    return false;
  }
}

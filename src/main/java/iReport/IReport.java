package iReport;

import iReport.commands.Dreport;
import iReport.commands.HReport;
import iReport.commands.Reports;
import iReport.commands.greport;
import iReport.commands.ireportc;
import iReport.commands.sreport;
import iReport.mysql.MYSQL;
import iReport.util.Data;
import iReport.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.event.SpongeEventHandler;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "iReport", name = "iReport", version = "2.0.1-SNAPSHOT")
public class IReport {
    public static final Logger LOGGER = LogManager.getLogger("iReport");
    public static MYSQL sql;
    //private final Dreport DREPORT = new Dreport();
    //private final Reports REPORTS = new Reports();

    public static MYSQL getMYSQL() {
        if (sql == null) {
            try {
                sql = new MYSQL();
                sql.queryUpdate("CREATE TABLE IF NOT EXISTS reports (uuid VARCHAR(36) PRIMARY KEY, currentname VARCHAR(16), Report LONGTEXT, username VARCHAR(16))");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (sql.isenable && !sql.hasConnection()) {
            try {
                sql.oppenConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sql;
    }

    /*@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("dreport") && args.length == 1) {
            return DREPORT.onCommand(sender, command, label, args);
        }
        if (label.equalsIgnoreCase("reports")) {
            return REPORTS.onCommand(sender, command, label, args);
        }

        return super.onCommand(sender, command, label, args);
    }*/

    @SpongeEventHandler
    public void pre(PreInitializationEvent event) {
        Utils.configfolder = event.getSuggestedConfigurationDirectory();
    }

    @SpongeEventHandler
    public void onEnable(ServerStartingEvent event) {
        Utils.game = event.getGame();
        Utils.controler = event.getGame().getPluginManager().getPlugin("iReport");
        //event.getGame().getEventManager().register(new Utils());
        /*getCommand("greport").setExecutor(new greport(this));
        getCommand("hreport").setExecutor(new HReport(this));
        getCommand("sreport").setExecutor(new sreport(this));
        getCommand("ireport").setExecutor(new ireportc());*/

        getMYSQL();
        try {
            ObjectInputStream o = new ObjectInputStream(new FileInputStream(new File(Utils.configfolder, "data.bin")));
            Data.instens = (Data) o.readObject();
            o.close();
        } catch (FileNotFoundException e) {
        } catch (ClassCastException e) {
            e.printStackTrace();
            LOGGER.log(Level.ERROR, "Don't modyfy data.bin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SpongeEventHandler
    public void onDisable(ServerStoppingEvent event) {
        if (sql.isenable && sql.hasConnection()) {
            sql.closeConnection();
        }
        try {
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(new File(Utils.configfolder, "data.bin")));
            o.writeObject(Data.init());
            o.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Set<UUID> set = Data.init().playermapo.keySet();
        List<String> list2 = new ArrayList<String>();
        for (UUID uuid : set) {
            list2.add(uuid.toString());
        }
        List<String> list = new ArrayList<String>();
        if (sender.hasPermission("iReport.dreport") && alias.equalsIgnoreCase("dreport")) {
            for (String string : list2) {
                if (string.startsWith(args[0])) {
                    list.add(string);
                }
            }
            return list;
        }
        if (sender.hasPermission("iReport.reports") && alias.equalsIgnoreCase("reports")) {
            if (args.length < 2) {
                List<String> l = new ArrayList<String>();
                if ("uuid".startsWith(args[0].toLowerCase())) {
                    l.add("uuid");
                }
                if ("usernameo".startsWith(args[0].toLowerCase())) {
                    l.add("usernameo");
                }
                if ("gui".startsWith(args[0].toLowerCase())) {
                    l.add("gui");
                }
                return l;
            }
            if (args[0].toLowerCase().equals("uuid")) {
                for (String string : list2) {
                    if (string.toLowerCase().startsWith(args[1].toLowerCase())) {
                        list.add(string);
                    }
                }
            }
            if (args[0].toLowerCase().equals("usernameo")) {
                for (String string : Data.init().playermapo.values()) {
                    if (string.toLowerCase().startsWith(args[1].toLowerCase())) {
                        list.add(string);
                    }
                }
            }
            return list;
        }
        return super.onTabComplete(sender, command, alias, args);
    }*/
}

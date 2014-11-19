package iReport;

import iReport.commands.Dreport;
import iReport.commands.HReport;
import iReport.commands.Reports;
import iReport.commands.greport;
import iReport.commands.ireportc;
import iReport.mysql.MYSQL;
import iReport.util.Data;
import iReport.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.Owner;

@Plugin(id = "iReport", name = "iReport", version = "2.0.1-SNAPSHOT")
public class IReport implements Owner {
    public static final Logger LOGGER = LogManager.getLogger("iReport");
    public static MYSQL sql;
    public static Game game;
    public static PluginContainer controler;
    public static File configfolder;

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

    @Subscribe
    public void pre(PreInitializationEvent event) {
        try {
            IReport.configfolder = event.getSuggestedConfigurationDirectory(); //== AbstractMethodError
        } catch (Throwable e) {
            Utils.PrintStackTrace(e);
        }
    }

    @Subscribe
    public void onEnable(ServerStartingEvent event) {
        IReport.game = event.getGame();
        game.getCommandDispatcher().registerCommand(new Dreport(), this, "dreport");
        game.getCommandDispatcher().registerCommand(new greport(), this, "greport");
        game.getCommandDispatcher().registerCommand(new HReport(), this, "hreport");
        game.getCommandDispatcher().registerCommand(new ireportc(), this, "ireport");
        game.getCommandDispatcher().registerCommand(new Reports(), this, "reports");
        game.getCommandDispatcher().registerCommand(new Dreport(), this, "dreport");
        //event.getGame().getEventManager().register(new Utils());
        getMYSQL();
        try {
            ObjectInputStream o = new ObjectInputStream(new FileInputStream(new File(IReport.configfolder, "data.bin")));
            Data.instens = (Data) o.readObject();
            o.close();
        } catch (FileNotFoundException e) {
        } catch (ClassCastException e) {
            Utils.PrintStackTrace(e);
            LOGGER.log(Level.ERROR, "Don't modyfy data.bin");
        } catch (Exception e) {
            Utils.PrintStackTrace(e);
        }
    }

    @Subscribe
    public void onDisable(ServerStoppingEvent event) {
        if (sql.isenable && sql.hasConnection()) {
            sql.closeConnection();
        }
        try {
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(new File(IReport.configfolder, "data.bin")));
            o.writeObject(Data.init());
            o.close();
        } catch (IOException e) {
            Utils.PrintStackTrace(e);
        }
    }
}

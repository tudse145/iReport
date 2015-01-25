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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.ConfigDir;
import org.spongepowered.api.util.event.Subscribe;

import com.google.inject.Inject;

@Plugin(id = "iReport", name = "iReport", version = "2.0.1-SNAPSHOT")
public class IReport {
    public static final Logger LOGGER = LoggerFactory.getLogger("iReport");
    public static MYSQL sql;
    public static Game game;
    public static Server server;
    public static PluginContainer controler;
    public static File configfolder;

    @Inject
    public IReport(Game game, @ConfigDir(sharedRoot = false) File configfolder) {
        IReport.game = game;
        IReport.server = game.getServer().get();
        IReport.configfolder = configfolder;
    }

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
    public void onEnable(ServerStartingEvent event) {
        game.getCommandDispatcher().register(this, new Dreport(), "dreport");
        game.getCommandDispatcher().register(this, new greport(), "greport");
        game.getCommandDispatcher().register(this, new HReport(), "hreport");
        game.getCommandDispatcher().register(this, new ireportc(), "ireport");
        game.getCommandDispatcher().register(this, new Reports(), "reports");
        game.getCommandDispatcher().register(this, new sreport(), "sreport");
        event.getGame().getEventManager().register(this, new Utils());
        getMYSQL();
        try {
            ObjectInputStream o = new ObjectInputStream(new FileInputStream(new File(IReport.configfolder, "data.bin")));
            Data.instens = (Data) o.readObject();
            o.close();
        } catch (FileNotFoundException e) {
        } catch (ClassCastException e) {
            Utils.PrintStackTrace(e);
            LOGGER.error("Don't modyfy data.bin");
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

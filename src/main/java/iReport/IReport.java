package iReport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.ConfigDir;

import com.google.inject.Inject;

import iReport.commands.Dreport;
import iReport.commands.HReport;
import iReport.commands.Reports;
import iReport.commands.greport;
import iReport.commands.ireportc;
import iReport.commands.sreport;
import iReport.mysql.MYSQL;
import iReport.util.Data;
import iReport.util.Utils;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

@Plugin(id = "iReport", name = "iReport", version = "2.0.1-SNAPSHOT")
public final class IReport {
    public static final Logger LOGGER = LoggerFactory.getLogger("iReport");
    public static MYSQL sql;
    public static Game game;
    public static Server server;
    public static PluginContainer controler;
    public static File configfolder;

    @Inject
    public IReport(Game game, @ConfigDir(sharedRoot = false) File configfolder) {
        IReport.game = game;
        IReport.server = game.getServer();
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
    public void onEnable(PreInitializationEvent event) {
        game.getCommandDispatcher().register(this, new Dreport(), "dreport");
        game.getCommandDispatcher().register(this, new greport(), "greport");
        game.getCommandDispatcher().register(this, new HReport(), "hreport");
        game.getCommandDispatcher().register(this, new ireportc(), "ireport");
        game.getCommandDispatcher().register(this, new Reports(), "reports");
        game.getCommandDispatcher().register(this, new sreport(), "sreport");
        event.getGame().getEventManager().register(this, Utils.INSTENCE);
        getMYSQL();
        try {
            loadFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onDisable(ServerStoppingEvent event) {
        if (sql.isenable && sql.hasConnection()) {
            sql.closeConnection();
        }
        try {
            saveFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
    public void saveFile() throws IOException {
        File file = new File(IReport.configfolder, "reports.cfg");
        file.delete();
        file.createNewFile();
        HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setFile(file).build();
        ConfigurationNode config = cfgfile.load();
        ConfigurationNode node = config.getNode("reports");
        Map<String, String> configDefaults = new HashMap<String, String>();
        for (UUID uuid : Data.init().playermapo.keySet()) {
            ConfigurationNode node2 = node.getNode(uuid.toString());
            configDefaults.put("reportedename", Data.init().playermapo.get(uuid));
            configDefaults.put("currenttname", Data.init().playermap.get(uuid));
            configDefaults.put("reports", Data.init().playermapr.get(uuid));
            node2.setValue(configDefaults);
            cfgfile.save(config);
        }
    }
    

    private void loadFile() throws IOException {
        File file = new File(IReport.configfolder, "reports.cfg");
        HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setFile(file).build();
        ConfigurationNode config = cfgfile.load();
        Data data = Data.init();
        ConfigurationNode nodde = config.getNode("reports");
        for (Entry<Object, ? extends ConfigurationNode> node : nodde.getChildrenMap().entrySet()) {
            UUID uuid = UUID.fromString((String) node.getKey());
            String currenttname = node.getValue().getNode("currenttname").getString();
            String reportedename = node.getValue().getNode("reportedename").getString();
            String reports = node.getValue().getNode("reports").getString();
            data.playermap.put(uuid, currenttname);
            data.playermapo.put(uuid, reportedename);
            data.playermapr.put(uuid, reports);
            data.playermapor.put(reportedename, uuid);
        }
    }

}

package ireport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

import ireport.commands.Dreport;
import ireport.commands.HReport;
import ireport.commands.Reports;
import ireport.commands.greport;
import ireport.commands.iReportc;
import ireport.commands.sreport;
import ireport.util.Constance;
import ireport.util.Data;
import ireport.util.Utils;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

@Plugin(id = "ireport", name = "ireport", version = "2.0.1", description = "Reporting plugin for Sponge", authors = {"tudse145", "heni123321"})
public final class ireport {

    @Inject
    public ireport(@ConfigDir(sharedRoot = false) Path configfolder, Logger logger) {
        Constance.instence = this;
        Constance.configfolder = configfolder;
        Constance.LOGGER = logger;
        Constance.configpath = configfolder.resolve("reports.cfg");
    }

    @Listener
    public void onEnable(GamePreInitializationEvent event) {
        loadCfg();
        Constance.GAME.getCommandManager().register(this, new Dreport(), "dreport");
        Constance.GAME.getCommandManager().register(this, new greport(), "greport");
        Constance.GAME.getCommandManager().register(this, new HReport(), "hreport");
        Constance.GAME.getCommandManager().register(this, new iReportc(), "ireport");
        Constance.GAME.getCommandManager().register(this, new Reports(), "reports");
        Constance.GAME.getCommandManager().register(this, new sreport(), "sreport");
        Constance.GAME.getEventManager().registerListeners(this, Utils.INSTENCE);
        if (Constance.getMYSQL().isEnabled()) {
            try {
                loadSql();
            } catch (SQLException e) {
                try {
                    Constance.LOGGER.error("SQL load failed, trying local file" + e.getMessage());
                    loadFile();
                } catch (IOException e1) {
                    e.addSuppressed(e1);
                    Utils.printStackTrace(e);
                }
            }
        } else {
            try {
                loadFile();
            } catch (Exception e) {
                Utils.printStackTrace(e);
            }
        }
    }


    @Listener
    public void onServerStart(GameStartingServerEvent event) {
        Constance.setServer();
    }

    @Listener
    public void onDisable(GameStoppingServerEvent event) {
        Data.init().getPlayermapo().keySet().forEach(Utils::savePlayer);
        Constance.server = null;
    }
    
    @Listener
    public void reload(GameReloadEvent event) {
        Data.init().getPlayermapo().keySet().forEach(Utils::savePlayer);
        Constance.setServer();
        loadCfg();
        try {
            Constance.getMYSQL().reload(Constance.enable_sql, Constance.databasenamme);
        } catch (IOException | SQLException e2) {
            Utils.printStackTrace(e2);
        }
        if (Constance.getMYSQL().isEnabled()) {
            try {
                loadSql();
            } catch (SQLException e) {
                try {
                    Constance.LOGGER.error("SQL load failed, trying local file", e);
                    loadFile();
                } catch (IOException e1) {
                    e.addSuppressed(e1);
                    Utils.printStackTrace(e);
                }
            }
        } else {
            try {
                loadFile();
            } catch (Exception e) {
                Utils.printStackTrace(e);
            }
        }
    }

    private void loadFile() throws IOException {
        HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setPath(Constance.configpath).build();
        ConfigurationNode config = cfgfile.load();
        Data data = Data.init();
        ConfigurationNode nodde = config.getNode("reports");
        nodde.getChildrenMap().entrySet().forEach(node -> {
            UUID uuid = UUID.fromString(node.getKey().toString());
            String currenttname = node.getValue().getNode("currenttname").getString();
            String reportedename = node.getValue().getNode("reportedename").getString();
            String reports = node.getValue().getNode("reports").getString();
            data.getPlayermap().put(uuid, currenttname);
            data.getPlayermapo().put(uuid, reportedename);
            data.getPlayermapr().put(uuid, reports);
            data.getPlayermapor().put(reportedename, uuid);
        });
    }

    private void loadSql() throws SQLException {
    	try (ResultSet resultSet = Constance.getMYSQL().queryUpdate("select * from reports", false)) {
            Data data = Data.init();
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                String currenttname = resultSet.getString("currentname");
                String reportedename = resultSet.getString("Report");
                String reports = resultSet.getString("username");
                data.getPlayermap().put(uuid, currenttname);
                data.getPlayermapo().put(uuid, reportedename);
                data.getPlayermapr().put(uuid, reports);
                data.getPlayermapor().put(reportedename, uuid);
            }
		}
    }

    private void loadCfg() {
        try {
            boolean furstrun = false;
            if (Files.notExists(Constance.configfolder)) {
                Files.createDirectory(Constance.configfolder);
            }
            if (Files.notExists(Constance.configfolder.resolve("config.cfg"))) {
                Files.createFile(Constance.configfolder.resolve("config.cfg"));
                furstrun = true;
            }
            HoconConfigurationLoader cfgFile = HoconConfigurationLoader.builder().setPath(Constance.configfolder.resolve("config.cfg")).build();
            ConfigurationNode config = cfgFile.load();
            if (furstrun) {
                Map<String, String> map = new HashMap<>();
                map.put("Locale", Locale.getDefault().toString());
                map.put("enable_sql", "false");
                map.put("database_name", "ireport");
                config.setValue(map);
                cfgFile.save(config);
            }
            Constance.locale = new Locale(config.getNode("Locale").getString());
            Constance.enable_sql = config.getNode("enable_sql").getBoolean();
            Constance.databasenamme = config.getNode("database_name").getString();
        } catch (IOException e) {
            Utils.printStackTrace(e);
        }
    }
}

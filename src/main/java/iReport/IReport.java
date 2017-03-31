package iReport;

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

import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

import iReport.commands.Dreport;
import iReport.commands.HReport;
import iReport.commands.Reports;
import iReport.commands.greport;
import iReport.commands.ireportc;
import iReport.commands.sreport;
import iReport.util.Constance;
import iReport.util.Data;
import iReport.util.Utils;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

@Plugin(id = "ireport", name = "iReport", version = "2.0.1-SNAPSHOT", description = "Reporting plugin for Sponge", authors = {"tudse145", "heni123321"})
public final class IReport {

    @Inject
    public IReport(@ConfigDir(sharedRoot = false) Path configfolder) {
        Constance.instence = this;
        Constance.configfolder = configfolder;
        Constance.configpath = configfolder.resolve("reports.cfg");
        Constance.dbPath = configfolder.resolve("database.cfg");
    }

    @Listener
    public void onEnable(GamePreInitializationEvent event) {
        loadCfg();
        Constance.GAME.getCommandManager().register(this, new Dreport(), "dreport");
        Constance.GAME.getCommandManager().register(this, new greport(), "greport");
        Constance.GAME.getCommandManager().register(this, new HReport(), "hreport");
        Constance.GAME.getCommandManager().register(this, new ireportc(), "ireport");
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
        Data.init().playermapo.keySet().forEach(Utils::savePlayer);
        Constance.server = null;
    }
    
    @Listener
    public void reload(GameReloadEvent event) {
        Data.init().playermapo.keySet().forEach(Utils::savePlayer);
        Constance.setServer();
        loadCfg();
        try {
            Constance.getMYSQL().reload(Constance.dbPath);
        } catch (IOException | SQLException e2) {
            Utils.printStackTrace(e2);
        }
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
            data.playermap.put(uuid, currenttname);
            data.playermapo.put(uuid, reportedename);
            data.playermapr.put(uuid, reports);
            data.playermapor.put(reportedename, uuid);
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
                data.playermap.put(uuid, currenttname);
                data.playermapo.put(uuid, reportedename);
                data.playermapr.put(uuid, reports);
                data.playermapor.put(reportedename, uuid);
            }
		} catch (SQLException e) {
			throw e;
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
                config.setValue(map);
                cfgFile.save(config);
            }
            Constance.locale = new Locale(config.getNode("Locale").getString());
        } catch (IOException e) {
            Utils.printStackTrace(e);
        }
    }
}

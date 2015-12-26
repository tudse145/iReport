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
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
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
import iReport.util.Tuple;
import iReport.util.Utils;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

@Plugin(id = "iReport", name = "iReport", version = "2.0.1-SNAPSHOT")
public final class IReport {
    @Inject
    public IReport(@ConfigDir(sharedRoot = false) Path configfolder) {
        Constance.configfolder = configfolder;
        Constance.configpath = configfolder.resolve("reports.cfg");
    }

    @Listener
    public void onEnable(GamePreInitializationEvent event) {
        loadCfg();
        Constance.game.getCommandManager().register(this, new Dreport(), "dreport");
        Constance.game.getCommandManager().register(this, new greport(), "greport");
        Constance.game.getCommandManager().register(this, new HReport(), "hreport");
        Constance.game.getCommandManager().register(this, new ireportc(), "ireport");
        Constance.game.getCommandManager().register(this, new Reports(), "reports");
        Constance.game.getCommandManager().register(this, new sreport(), "sreport");
        Constance.game.getEventManager().registerListeners(this, Utils.INSTENCE);
        if (Constance.getMYSQL().isenabled) {
            try {
                loadSql();
            } catch (SQLException e) {
                try {
                    loadFile();
                } catch (IOException e1) {
                    e.addSuppressed(e1);
                    Utils.printStackTrace(e1);
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
    public void onDisable(GameStoppingServerEvent event) {
        Data.init().playermapo.keySet().stream().forEach(Utils::savePlayer);
    }

    private void loadFile() throws IOException {
        HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setPath(Constance.configpath).build();
        ConfigurationNode config = cfgfile.load();
        Data data = Data.init();
        ConfigurationNode nodde = config.getNode("reports");
        nodde.getChildrenMap().entrySet().parallelStream().forEach(node -> {
            UUID uuid = UUID.fromString((String) node.getKey());
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
        Tuple<ResultSet,String> tuple = Constance.getMYSQL().queryUpdate("select * from reports", false);
        if (tuple.getFirst() == null) {
            throw new SQLException(tuple.getSecond().split("\n")[1]);
        }
        ResultSet resultSet = tuple.getFirst();
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
        resultSet.close();
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
            HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setPath(Constance.configfolder.resolve("config.cfg")).build();
            ConfigurationNode config = cfgfile.load();
            if (furstrun) {
                Map<String, String> map = new HashMap<>();
                map.put("Locale", Locale.getDefault().toString());
                config.setValue(map);
                cfgfile.save(config);
            }
            Constance.locale = new Locale(config.getNode("Locale").getString());
        } catch (IOException e) {
            Utils.printStackTrace(e);
        }
    }
}

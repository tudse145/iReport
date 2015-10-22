package iReport;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.ConfigDir;

import com.google.inject.Inject;

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

@Plugin(id = "iReport", name = "iReport", version = "2.0.1-SNAPSHOT")
public final class IReport {
    @Inject
    public IReport(Game game, @ConfigDir(sharedRoot = false) File configfolder) {
        Constance.game = game;
        Constance.server = game.getServer();
        Constance.configfolder = configfolder;
    }

    @Listener
    public void onEnable(GamePreInitializationEvent event) {
        loadCfg();
        Constance.game.getCommandDispatcher().register(this, new Dreport(), "dreport");
        Constance.game.getCommandDispatcher().register(this, new greport(), "greport");
        Constance.game.getCommandDispatcher().register(this, new HReport(), "hreport");
        Constance.game.getCommandDispatcher().register(this, new ireportc(), "ireport");
        Constance.game.getCommandDispatcher().register(this, new Reports(), "reports");
        Constance.game.getCommandDispatcher().register(this, new sreport(), "sreport");
        Constance.game.getEventManager().registerListeners(this, Utils.INSTENCE);
        Constance.getMYSQL();
        if (Constance.sql.isenable) {
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
        File file = new File(Constance.configfolder, "reports.cfg");
        HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setFile(file).build();
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
        ResultSet resultSet = Constance.sql.queryUpdate("select * from reports", false);
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
        if (!Constance.configfolder.exists()) {
            Constance.configfolder.mkdirs();
        }
        File file = new File(Constance.configfolder, "config.cfg");
        try {
            boolean furstrun = false;
            if (!file.exists()) {
                file.createNewFile();
                furstrun = true;
            }
            HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setFile(file).build();
            ConfigurationNode config = cfgfile.load();
            if (furstrun) {
                Map<String, String> map = new HashMap<>();
                map.put("local", Locale.getDefault().toString());
                config.setValue(map);
                cfgfile.save(config);
            }
            Constance.locale = new Locale(config.getNode("local").getString());
        } catch (IOException e) {
            Utils.printStackTrace(e);
        }
    }
}

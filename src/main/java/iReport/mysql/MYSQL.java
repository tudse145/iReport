package iReport.mysql;

import iReport.util.Constance;
import iReport.util.Utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.spongepowered.api.service.sql.SqlService;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public final class MYSQL {

    public final boolean isenable;
    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final String database;
    private final String prodocol;
    private DataSource ds;

    public MYSQL() throws Exception {
        File file = new File(Constance.configfolder, "database.cfg");

        boolean furstrun = false;
        String db = "database.";
        if (!file.exists()) {
            file.createNewFile();
            furstrun = true;
        }
        HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setFile(file).build();
        ConfigurationNode config = cfgfile.load();
        ConfigurationNode node = config.getNode(db);
        if (furstrun) {
            Map<String, String> configDefaults = new HashMap<String, String>();
            configDefaults.put("enable", String.valueOf(false));
            configDefaults.put("host", "localhost");
            configDefaults.put("port", String.valueOf(3306));
            configDefaults.put("user", "user");
            configDefaults.put("password", "password");
            configDefaults.put("database", "database");
            configDefaults.put("prodocol", "mysql");
            node.setValue(configDefaults);
            cfgfile.save(config);
        }
        isenable = node.getNode("enable").getBoolean();
        this.host = node.getNode("host").getString();
        this.port = node.getNode("port").getInt();
        this.user = node.getNode("user").getString();
        this.password = node.getNode("password").getString();
        this.database = node.getNode("database").getString();
        this.prodocol = node.getNode("prodocol").getString();
        Optional<SqlService> provide = Constance.game.getServiceManager().provide(SqlService.class);
        if (provide.isPresent() && isenable) {
            try {
                ds = provide.get().getDataSource("jdbc:" + this.prodocol + "://" + this.host + ":" + this.port + "/" + this.database);
            } catch (Exception e) {
            }
        }
    }

    public Connection oppenConnection() throws Exception {
        if (ds != null) {
            return ds.getConnection(this.user, this.password);
        } else {
            return DriverManager.getConnection("jdbc:" + this.prodocol + "://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
        }
    }

    public void queryUpdate(String query) {
        queryUpdate(query, true);
    }

    public ResultSet queryUpdate(String query, boolean closeResultset) {
        if (!isenable) {
            return null;
        }
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = oppenConnection().prepareStatement(query);
            rs = st.executeQuery();
            return rs;
        } catch (Exception e) {
            Utils.printStackTrace(e);
            Constance.LOGGER.error("Failed to send update '" + query + "'.");
        } finally {
            if (closeResultset) {
                closeRessources(rs, st);
            } else {
                this.closeRessources(null, st);
            }
        }
        return null;
    }

    public void closeRessources(ResultSet rs, PreparedStatement st) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                Utils.printStackTrace(e);
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                Utils.printStackTrace(e);
            }
        }
    }
}

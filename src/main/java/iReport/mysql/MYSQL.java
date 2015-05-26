package iReport.mysql;

import iReport.IReport;
import iReport.util.Utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public final class MYSQL {

    public final boolean isenable;
    private final boolean debug;
    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final String database;
    private Connection conn;

    public MYSQL() throws Exception {
        if (!IReport.configfolder.exists()) {
            IReport.configfolder.mkdir();
        }
        File file = new File(IReport.configfolder, "database.cfg");

        boolean furstrun = false;
        String db = "database.";
        if (!file.exists()) {
            file.createNewFile();
            furstrun = true;
        }
        HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setFile(file).build();
        ConfigurationNode config = cfgfile.load();
        if (furstrun) {
            Map<String, String> configDefaults = new HashMap<String, String>();
            configDefaults.put(db + "enable", String.valueOf(false));
            configDefaults.put(db + "host", "localhost");
            configDefaults.put(db + "port", String.valueOf(3306));
            configDefaults.put(db + "user", "user");
            configDefaults.put(db + "password", "password");
            configDefaults.put(db + "database", "database");
            configDefaults.put(db + "debug", String.valueOf(false));
            config.setValue(configDefaults);
            cfgfile.save(config);
        }
        isenable = config.getNode(db + "enable").getBoolean();
        this.host = config.getNode(db + "host").getString();
        this.port = config.getNode(db + "port").getInt();
        this.user = config.getNode(db + "user").getString();
        this.password = config.getNode(db + "password").getString();
        this.database = config.getNode(db + "database").getString();
        this.debug = config.getNode(db + "debug").getBoolean();
        if (isenable) {
            this.oppenConnection();
        }
    }

    public Connection oppenConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
        return conn;
    }

    public Connection getConnection() {
        return this.conn;
    }

    public boolean hasConnection() {
        try {
            return this.conn != null || this.conn.isValid(1);
        } catch (SQLException e) {
            if (debug) {
                Utils.PrintStackTrace(e);
            }
            return false;
        }
    }

    public void queryUpdate(String query) {
        if (!isenable) {
            return;
        }
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(query);
            st.executeUpdate();
        } catch (SQLException e) {
            if (debug) {
                Utils.PrintStackTrace(e);
            }
            IReport.LOGGER.error("Failed to send update '" + query + "'.");
        } finally {
            this.closeRessources(null, st);
        }
    }

    public void closeRessources(ResultSet rs, PreparedStatement st) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
            }
        }
    }

    public void closeConnection() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            if (debug) {
                e.printStackTrace();
            }
        } finally {
            this.conn = null;

        }
    }
}

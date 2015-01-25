package iReport.mysql;

import iReport.IReport;
import iReport.util.Utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.spongepowered.api.util.config.ConfigFile;

import com.typesafe.config.ConfigValueFactory;

public class MYSQL {

    public boolean isenable;
    private boolean debug;
    private String host;
    private int port;
    private String user;
    private String password;
    private String database;
    private Connection conn;

    public MYSQL() throws Exception {
        File file = new File(IReport.configfolder, "database.cfg");
        ConfigFile cfg = ConfigFile.parseFile(file);

        String db = "database.";
        cfg = cfg.withValue(db + "enable", ConfigValueFactory.fromAnyRef(false));
        cfg = cfg.withValue(db + "host", ConfigValueFactory.fromAnyRef("localhost"));
        cfg = cfg.withValue(db + "port", ConfigValueFactory.fromAnyRef(3306));
        cfg = cfg.withValue(db + "user", ConfigValueFactory.fromAnyRef("user"));
        cfg = cfg.withValue(db + "password", ConfigValueFactory.fromAnyRef("password"));
        cfg = cfg.withValue(db + "database", ConfigValueFactory.fromAnyRef("database"));
        cfg = cfg.withValue(db + "debug", ConfigValueFactory.fromAnyRef(false));
        cfg.save(true);
        isenable = cfg.getBoolean(db + "enable");
        this.host = cfg.getString(db + "host");
        this.port = cfg.getInt(db + "port");
        this.user = cfg.getString(db + "user");
        this.password = cfg.getString(db + "password");
        this.database = cfg.getString(db + "database");
        this.debug = cfg.getBoolean(db + "debug");
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

package iReport.mysql;

import iReport.util.Constance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.spongepowered.api.service.sql.SqlService;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public final class Mysql {

    private boolean enabled;
    private String user;
    private String password;
    private String jdbcUrl;
    private DataSource ds;

    public Mysql() throws Exception {
        boolean furstrun = false;
        String db = "database.";
        Path file = Constance.dbPath;
        if (!Files.exists(file )) {
            Files.createFile(file);
            furstrun = true;
        }
        HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setPath(file).build();
        ConfigurationNode config = cfgfile.load();
        ConfigurationNode node = config.getNode(db);
        if (furstrun) {
            Map<String, String> configDefaults = new HashMap<String, String>();
            configDefaults.put("enable", String.valueOf(false));
            configDefaults.put("user", "user");
            configDefaults.put("password", "password");
            configDefaults.put("jdbc-url", "jdbc:mysql://localhost:3306/database");
            node.setValue(configDefaults);
            cfgfile.save(config);
        }
        enabled = node.getNode("enable").getBoolean();
        this.user = node.getNode("user").getString();
        this.password = node.getNode("password").getString();
        this.jdbcUrl = node.getNode("jdbc-url").getString();
        Optional<SqlService> provide = Constance.GAME.getServiceManager().provide(SqlService.class);
        if (provide.isPresent() && enabled) {
            ds = provide.get().getDataSource(jdbcUrl);
        }
    }

    public void reload(Path file) throws IOException, SQLException {
        String db = "database.";
        HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setPath(file).build();
        ConfigurationNode config = cfgfile.load();
        ConfigurationNode node = config.getNode(db);
        enabled = node.getNode("enable").getBoolean();
        this.user = node.getNode("user").getString();
        this.password = node.getNode("password").getString();
        this.jdbcUrl = node.getNode("jdbc-url").getString();
        Optional<SqlService> provide = Constance.GAME.getServiceManager().provide(SqlService.class);
        if (provide.isPresent() && enabled) {
            ds = provide.get().getDataSource(jdbcUrl);
        }
    }

    private Connection openConnection() throws SQLException {
        return ds.getConnection(this.user, this.password);
    }

    public void queryUpdate(String query) throws SQLException {
        if (!enabled) {
            return;
        }
        queryUpdate(query, true);
    }

    public ResultSet queryUpdate(String query, boolean closeResultset) throws SQLException {
        if (!enabled) {
            return null;
        }
        try (ResultSet rs = openConnection().prepareStatement(query).executeQuery()) {
            return rs;
        } catch (SQLException e) {
            throw new SQLException("Failed to send update '" + query + "'.\n" + e.getMessage());
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
    
}

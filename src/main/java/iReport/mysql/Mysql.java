package iReport.mysql;

import iReport.util.Constance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
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

public final class Mysql {

    private boolean enabled;
    private String user;
    private String password;
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
        this.enabled = node.getNode("enable").getBoolean();
        this.user = node.getNode("user").getString();
        this.password = node.getNode("password").getString();
        String jdbcUrl = node.getNode("jdbc-url").getString();
        Optional<SqlService> provide = Constance.GAME.getServiceManager().provide(SqlService.class);
        if (provide.isPresent() && enabled) {
            ds = provide.get().getDataSource(Constance.instence, jdbcUrl + "?user=" + user + "&password=" + password);
        }
    }

    public void reload(Path file) throws IOException, SQLException {
        String db = "database.";
        HoconConfigurationLoader cfgfile = HoconConfigurationLoader.builder().setPath(file).build();
        ConfigurationNode config = cfgfile.load();
        ConfigurationNode node = config.getNode(db);
        this.enabled = node.getNode("enable").getBoolean();
        this.user = node.getNode("user").getString();
        this.password = node.getNode("password").getString();
        String jdbcUrl = node.getNode("jdbc-url").getString();
        Optional<SqlService> provide = Constance.GAME.getServiceManager().provide(SqlService.class);
        if (enabled) {
            ds = provide.orElseThrow(() -> new RuntimeException("SqlService not fount")).getDataSource(jdbcUrl);
        }
    }

    private Connection openConnection() throws SQLException {
        return ds.getConnection();
    }

    public void queryUpdate(String query) throws SQLException {
        if (!enabled) {
            return;
        }
        queryUpdate(query, true);
    }

    public void queryUpdate(String query, String... objects) throws SQLException {
        if (!enabled) {
            return;
        }
        try (PreparedStatement ps = openConnection().prepareStatement(query)){
        	for (int i = 0; i < objects.length; i++) {
    			ps.setString(i + 1, objects[i]);
    		}
        	ps.executeUpdate();
		} catch (SQLException e) {
            throw new SQLException("Failed to send update '" + query + "'.\n" + e.getMessage());
        }
    }
    
    public ResultSet queryUpdate(String query, boolean closeResultset) throws SQLException {
        if (!enabled) {
            return null;
        }
        ResultSet rs = null;
        try {
        	rs = openConnection().prepareStatement(query).executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new SQLException("Failed to send update '" + query + "'.\n" + e.getMessage(), e);
        } finally {
        	if (closeResultset && rs != null) {
				rs.close();
			}
		}
    }

    public boolean isEnabled() {
        return enabled;
    }
    
}

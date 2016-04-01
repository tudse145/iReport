package iReport.mysql;

import iReport.util.Constance;
import iReport.util.Tuple;
import iReport.util.Utils;

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

    private final boolean enabled;
    private final String user;
    private final String password;
    private String jdbcUrl;
    private DataSource ds;

    public Mysql() throws Exception {
        Path file = Constance.configfolder.resolve("database.cfg");
        boolean furstrun = false;
        String db = "database.";
        if (!Files.exists(file)) {
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
        Optional<SqlService> provide = Constance.game.getServiceManager().provide(SqlService.class);
        if (provide.isPresent() && enabled) {
            ds = provide.get().getDataSource(jdbcUrl);
        }
    }

    private Connection openConnection() throws SQLException {
        return ds.getConnection(this.user, this.password);
    }

    public String queryUpdate(String query) {
        if (!enabled) {
            return null;
        }
        return queryUpdate(query, true).getSecond();
    }

    public Tuple<ResultSet, String> queryUpdate(String query, boolean closeResultset) {
        if (!enabled) {
            return null;
        }
        ResultSet rs = null;
        try (PreparedStatement st = openConnection().prepareStatement(query)) {
            rs = st.executeQuery();
            return Tuple.of(rs, null);
        } catch (SQLException e) {
            return Tuple.of(null, "Failed to send update '" + query + "'.\n" + e.getMessage());
        } finally {
            if (closeResultset) {
                closeRessources(rs);
            }
        }
    }

    private void closeRessources(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                Utils.printStackTrace(e);
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
    
}

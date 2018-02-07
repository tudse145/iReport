package ireport.mysql;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.spongepowered.api.service.sql.SqlService;

import ireport.util.Constance;

public final class Mysql {

    private boolean enabled;
    private DataSource ds;

    public Mysql(boolean enabled) throws Exception {
        this.enabled = enabled;
		Optional<SqlService> provide = Constance.GAME.getServiceManager().provide(SqlService.class);
        if (provide.isPresent() && enabled) {
        	Optional<String> jdbcUrl = provide.get().getConnectionUrlFromAlias("iReport");
        	if (jdbcUrl.isPresent()) {
				ds = provide.get().getDataSource(Constance.instence, jdbcUrl.get());
			} else {
				Constance.LOGGER.error("iReport jdbc url not fount in sponge global.conf");
				this.enabled = false;
			}
        }
    }

    public void reload(Path file, boolean enabled) throws IOException, SQLException {
    	 Optional<SqlService> provide = Constance.GAME.getServiceManager().provide(SqlService.class);
         if (provide.isPresent() && enabled) {
         	Optional<String> jdbcUrl = provide.get().getConnectionUrlFromAlias("iReport");
         	if (jdbcUrl.isPresent()) {
 				ds = provide.get().getDataSource(Constance.instence, jdbcUrl.get());
 			} else {
 				Constance.LOGGER.error("iReport jdbc url not fount in sponge global.conf");
				this.enabled = false;
 			}
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
        try (PreparedStatement ps = openConnection().prepareStatement(query)) {
        	rs = ps.executeQuery();
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
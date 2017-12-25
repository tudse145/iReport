package ireport.util;

import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;

import ireport.ireport;
import ireport.mysql.Mysql;

public final class Constance {

    public static final Function<Locale, ResourceBundle> LOOKUP_FUNC = input -> ResourceBundle.getBundle("iReport.lang", input);
    private static Mysql sql;
    public static final Game GAME = Sponge.getGame();
    public static Server server;
    public static Path configfolder;
    public static Locale locale;
    public static Logger LOGGER;
    public static Path configpath;
    public static ireport instence;
    public static Path dbPath;
	public static boolean enable_sql;

    private Constance() {
    }

    public static Mysql getMYSQL() {
        if (sql == null) {
            try {
                sql = new Mysql(enable_sql);
                sql.queryUpdate("CREATE TABLE IF NOT EXISTS reports (uuid VARCHAR(36) PRIMARY KEY, currentname VARCHAR(16), Report LONGTEXT, username VARCHAR(16))");
            } catch (Exception e) {
                Utils.printStackTrace(e);
            }
        }
        return sql;
    }
    
    public static void setServer() {
       server = GAME.getServer(); 
    }
}

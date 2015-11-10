package iReport.util;

import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.plugin.PluginContainer;

import iReport.mysql.MYSQL;

public final class Constance {

    public static final Function<Locale, ResourceBundle> LOOKUP_FUNC = input -> ResourceBundle.getBundle("iReport.lang", input);
    public static MYSQL sql;
    public static Game game;
    public static Server server;
    public static PluginContainer controler;
    public static Path configfolder;
    public static Locale locale;
    public static final Logger LOGGER = LoggerFactory.getLogger("iReport");
    public static Path configpath;

    private Constance() {
    }

    public static MYSQL getMYSQL() {
        if (sql == null) {
            try {
                sql = new MYSQL();
                sql.queryUpdate("CREATE TABLE IF NOT EXISTS reports (uuid VARCHAR(36) PRIMARY KEY, currentname VARCHAR(16), Report LONGTEXT, username VARCHAR(16))");
            } catch (Exception e) {
                Utils.printStackTrace(e);
            }
        }
        return sql;
    }
}

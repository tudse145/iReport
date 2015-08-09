package iReport.util;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.translation.ResourceBundleTranslation;

import com.google.common.base.Function;

import iReport.mysql.MYSQL;

public final class Constance {

    private static final Function<Locale, ResourceBundle> LOOKUP_FUNC = new Function<Locale, ResourceBundle>() {
        public ResourceBundle apply(Locale input) {
            return ResourceBundle.getBundle("iReport.lang", input);
        }
    };
    public static final ResourceBundleTranslation GREPORT_SUCESS = new ResourceBundleTranslation("greport.sucess", LOOKUP_FUNC);
    public static MYSQL sql;
    public static Game game;
    public static Server server;
    public static PluginContainer controler;
    public static File configfolder;
    public static Locale locale;
    public static final Logger LOGGER = LoggerFactory.getLogger("iReport");

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

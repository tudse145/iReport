package iReport;

import java.lang.reflect.Field;
import java.util.Set;

import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class iReport extends JavaPlugin {
    MYSQL sql;

    public MYSQL getMYSQL() {
        PluginManager pm = getServer().getPluginManager();
        if (this.sql == null) {
            try {
                this.sql = new MYSQL();
                if (MYSQL.isenable) {
                    this.sql.queryUpdate("CREATE TABLE IF NOT EXISTS Reports (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(16), Reason VARCHAR (100))");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.sql;
    }

    @Override
    public void onEnable() {
        try {
            SimpleCommandMap map = new SimpleCommandMap(getServer());
            map.register("iReport",new HReport(this));
            map.register("iReport",new greport(this));
            map.register("iReport",new sreport(this));
            map.register("iReport",new ireportc());
            map.register("iReport",new Reports(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        saveConfig();
        getConfig().options().copyDefaults(true);
        getMYSQL();
    }
    
    @Override
    public void onDisable() {
        if (sql.isenable) {
            sql.closeConnection();
        }
    }
}
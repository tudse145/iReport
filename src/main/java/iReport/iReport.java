package iReport;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class iReport extends JavaPlugin
{
  MYSQL sql;

  public MYSQL getMYSQL()
  {
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
  public void onEnable()
  {
      new Regcommands(this).regcommands();
    saveConfig();
    getConfig().options().copyDefaults(true);
    getMYSQL();
  }
}

package iReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class iReport extends JavaPlugin {
    public MYSQL sql;
    private File reportsfile;
    private YamlConfiguration newConfig;

    public iReport() {
        this.reportsfile = new File(getDataFolder(), "reports.yml");
    }

    public MYSQL getMYSQL() {
        if (this.sql == null) {
            try {
                this.sql = new MYSQL();
                if (MYSQL.isenable) {
                    this.sql.queryUpdate("CREATE TABLE IF NOT EXISTS Reports (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(16), Reason VARCHAR (100))");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.sql;
    }

    @Override
    public void onEnable() {
        try {
            File f = new File("plugins/iReport/", "config.yml");
            Scanner sc = new Scanner(f);
            while (sc.hasNext()) {
                if (sc.nextLine().contains("reports:")) {
                    sc.close();
                    if (f.renameTo(new File("plugins/iReport/", "reports.yml"))) {
                        break;
                    }
                    else {
                        try {
                            throw new IOException("fail to rename file config.yml, iReport will not load");
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
            }
            sc.close();
        }
        catch (FileNotFoundException e) {
        }
        try {
            getCommand("greport").setExecutor(new greport(this));
            getCommand("hreport").setExecutor(new HReport(this));
            getCommand("sreport").setExecutor(new sreport(this));
            getCommand("ireport").setExecutor(new ireportc());
            getCommand("reports").setExecutor(new Reports());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        saveConfig();
        getConfig().options().copyDefaults(true);
        getMYSQL();
    }

    @Override
    public void onDisable() {
        if (MYSQL.isenable && sql.hasConnection()) {
            sql.closeConnection();
        }
    }

    public FileConfiguration grtReports() {
        if (newConfig == null) {
            newConfig = YamlConfiguration.loadConfiguration(reportsfile);

            InputStream defConfigStream = getResource("reports.yml");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);

                newConfig.setDefaults(defConfig);
            }
        }
        return newConfig;
    }
    
    public void saveReports() {
        try {
            grtReports().save(reportsfile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + reportsfile, ex);
        }
    }
}

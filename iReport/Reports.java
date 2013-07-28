package iReport;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Reports implements CommandExecutor, Listener {

    private iReport plugin;
    
    
    public Reports(iReport iReport) {
        this.plugin = iReport;
        
        MYSQL sql = this.plugin.getMYSQL();
        sql.queryUpdate("CREATE TABLE IF NOT EXISTS Reports (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(16), Reason VARCHAR (100))");
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("greport")) {
            if (args.length > 0) {
                String Reason = this.Reported(args[0]);
                if (Reason == null) {
                    cs.sendMessage("§6" + args[0] + "§7 is not reported");
                }else {
                    cs.sendMessage("§6" + args[0] + "§7 is reported for §6" + Reason + "§7)");
                }
                }else {
                    cs.sendMessage("§c/greport [name]");
            }
            
            return true;
        }
        if (args.length > 0) {
            String player = args[0];
            String Reason = Rlocation.getxyz(plugin, args[0]);
            if (args.length > 1) {
                Reason = args[1];
                for (int i = 2; i < args.length; i++) {
                    Reason += " " + args[i];
                    
                }
                
                Reason = ChatColor.translateAlternateColorCodes('&', Reason);
                
            }
            player = this.setReported(player, Reason);
            
            Player p = Bukkit.getPlayer(player);
            if (p != null) {
                cs.sendMessage("§6" + player + "Was reported");
            }
        }else {
            cs.sendMessage("§c/ gereport [name]");
        
       
        } 
        return true;
    }
    
    
    public String Reported(String player) {
//        return Rlocation.getxyz(plugin, player);
        MYSQL sql = this.plugin.getMYSQL();
        Connection conn = sql.getConnection();
        ResultSet rs = null;
        PreparedStatement st = null;
        String Reason = null;
        try {
            st = conn.prepareStatement("SELECT * FROM Reports WHERE name=?");
            st.setString(1, Rlocation.getxyz(plugin, player));
            rs = st.executeQuery();
            rs.last();
            if (rs.getRow() != 0) {
                rs.first();
                Reason = rs.getString("Reason");
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.closeRessources(rs, st);
        }
        return Reason;
        
    }
    
    public String setReported(String player, String Reason) {
        MYSQL sql = this.plugin.getMYSQL();
        if (player.length() > 16) {
            player = player.substring(0, 16);
            
        }
        if (this.Reported(player) != null) {
            sql.queryUpdate("UPDATE Reports SET Reason= '" + Reason + "' WHERE name='"+player + "'");
        }else {
            sql.queryUpdate("INSERT INTO Reports (name, Reason) VALUES ('" + player + ", " + Reason + "')");
           
        }
        return player;
    }
        
    }
 


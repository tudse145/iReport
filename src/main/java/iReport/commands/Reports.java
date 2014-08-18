package iReport.commands;

import static iReport.util.Data.init;
import iReport.util.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Reports implements CommandExecutor {

    private static final Map<UUID, ItemStack> HEADS = new HashMap<UUID, ItemStack>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        Map<UUID, String> map1 = init().playermap;
        Map<UUID, String> map2 = init().playermapo;
        Map<UUID, String> map3 = init().playermapr;
        Inventory inv = calculate(Data.init().playermapo.size()); 
        if (sender instanceof HumanEntity &&  args.length == 1 && args[0].equalsIgnoreCase("gui")) { 
            for (UUID uuid : map2.keySet()) { 
            	List<String> list = new ArrayList<String>();
                ItemStack i	 = HEADS.get(uuid); 
                if (i == null) {
                	i = new ItemStack(Material.SKULL_ITEM, 1);
                	i.setDurability((short) 3); 
                    SkullMeta meta = (SkullMeta) i.getItemMeta(); 
                    meta.setOwner(map1.get(uuid)); 
                    meta.setDisplayName(map1.get(uuid)); 
                    meta.setLore(setLore(list, uuid));
                    i.setItemMeta(meta); 
                    HEADS.put(uuid, i);
				} else {
                    SkullMeta meta = (SkullMeta) i.getItemMeta(); 
                    meta.setLore(setLore(list, uuid));
                    i.setItemMeta(meta); 
                    HEADS.put(uuid, i);
				}
                
                inv.addItem(i); 
            } 
            ((HumanEntity) sender).openInventory(inv); 
            return true; 
        }
        if (args.length == 2) {
            try {
                if (args[0].equalsIgnoreCase("uuid")) {
                    UUID u = UUID.fromString(args[1]);
                    for (String string : setLore(new ArrayList<String>(), u)) {
    					sender.sendMessage(string);
    				}                }
                if (args[0].equalsIgnoreCase("usernameo")) {
                    UUID u = init().playermapor.get(args[1]);
                    for (String string : setLore(new ArrayList<String>(), u)) {
    					sender.sendMessage(string);
    				}                }
                return true;
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "invalid UUID");
            }
        } else {
        	if (map3.entrySet().size() == 0) {
				sender.sendMessage(ChatColor.RED + "There is no reports");
				return true;
			}
        	for (Entry<UUID, String> entry : map3.entrySet()) {
        		UUID u = entry.getKey();
                for (String string : setLore(new ArrayList<String>(), u)) {
					sender.sendMessage(string);
				}
                sender.sendMessage(" ");
			}
            return true;
        }
        return false;
    }

    private List<String> setLore(List<String> list, UUID uuid) {
        Map<UUID, String> map1 = init().playermap;
        Map<UUID, String> map2 = init().playermapo;
        Map<UUID, String> map3 = init().playermapr;
    	list.add("UUID: " + uuid);
    	list.add("currentname: " + map1.get(uuid));
    	for (String string : map3.get(uuid).split(";")) {
			list.add(string);
		}
    	list.add("username: " + map2.get(uuid));
		return list;
	}

	private Inventory calculate(int size) { 
        double f = size; 
        f = f/9; 
        if (f == size/9) { 
            return Bukkit.createInventory(null, (int) (f*9), "reports"); 
        } 
        size = size /9; 
        size++; 
        size = size *9; 
        return Bukkit.createInventory(null, size, "reports"); 
    } 
}

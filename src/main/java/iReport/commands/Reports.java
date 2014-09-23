package iReport.commands;

import static iReport.util.Data.init;
import iReport.util.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.Description;
import org.spongepowered.api.entity.HumanEntity;
import org.spongepowered.api.inventory.ItemStack;

public class Reports implements CommandCallable {

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
        f = f / 9;
        if (f == size / 9) {
            return Bukkit.createInventory(null, (int) (f * 9), "reports");
        }
        size = size / 9;
        size++;
        size = size * 9;
        return Bukkit.createInventory(null, size, "reports");
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        String[] args = arguments.split(" ");
        Set<UUID> set = Data.init().playermapo.keySet();
        List<String> list2 = new ArrayList<String>();
        for (UUID uuid : set) {
            list2.add(uuid.toString());
        }
        List<String> list = new ArrayList<String>();
        if (source.hasPermission("iReport.reports")) {
            if (args.length < 2) {
                List<String> l = new ArrayList<String>();
                if ("uuid".startsWith(args[0].toLowerCase())) {
                    l.add("uuid");
                }
                if ("usernameo".startsWith(args[0].toLowerCase())) {
                    l.add("usernameo");
                }
                if ("gui".startsWith(args[0].toLowerCase())) {
                    l.add("gui");
                }
                return l;
            }
            if (args[0].toLowerCase().equals("uuid")) {
                for (String string : list2) {
                    if (string.toLowerCase().startsWith(args[1].toLowerCase())) {
                        list.add(string);
                    }
                }
            }
            if (args[0].toLowerCase().equals("usernameo")) {
                for (String string : Data.init().playermapo.values()) {
                    if (string.toLowerCase().startsWith(args[1].toLowerCase())) {
                        list.add(string);
                    }
                }
            }
            return list;
        }
        return null;
    }

    @Override
    public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException {
        String[] args = arguments.split(" ");
        Map<UUID, String> map1 = init().playermap;
        Map<UUID, String> map2 = init().playermapo;
        Map<UUID, String> map3 = init().playermapr;
        Inventory inv = calculate(Data.init().playermapo.size());
        if (source instanceof HumanEntity && args.length == 1 && args[0].equalsIgnoreCase("gui")) {
            for (UUID uuid : map2.keySet()) {
                List<String> list = new ArrayList<String>();
                ItemStack i = new ItemStack(Material.SKULL_ITEM, 1);
                i.setDurability((short) 3);
                SkullMeta meta = (SkullMeta) i.getItemMeta();
                meta.setOwner(map1.get(uuid));
                meta.setDisplayName(map1.get(uuid));
                meta.setLore(setLore(list, uuid));
                i.setItemMeta(meta);
                inv.addItem(i);
            }
            ((HumanEntity) source).openInventory(inv);
            return true;
        }
        if (args.length == 2) {
            try {
                if (args[0].equalsIgnoreCase("uuid")) {
                    UUID u = UUID.fromString(args[1]);
                    for (String string : setLore(new ArrayList<String>(), u)) {
                        source.sendMessage(string);
                    }
                }
                if (args[0].equalsIgnoreCase("usernameo")) {
                    UUID u = init().playermapor.get(args[1]);
                    for (String string : setLore(new ArrayList<String>(), u)) {
                        source.sendMessage(string);
                    }
                }
                return true;
            } catch (Exception e) {
                source.sendMessage(ChatColor.RED + "invalid UUID");
            }
        } else {
            if (map3.entrySet().size() == 0) {
                source.sendMessage(ChatColor.RED + "There is no reports");
                return true;
            }
            for (Entry<UUID, String> entry : map3.entrySet()) {
                UUID u = entry.getKey();
                for (String string : setLore(new ArrayList<String>(), u)) {
                    source.sendMessage(string);
                }
                source.sendMessage(" ");
            }
            return true;
        }
        return false;
    }

    @Override
    public Description getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("ireport.reports");
    }
}

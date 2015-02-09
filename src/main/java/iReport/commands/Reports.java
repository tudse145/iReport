package iReport.commands;

import static iReport.util.Data.init;
import iReport.IReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.message.Messages;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import com.google.common.base.Optional;

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

    // private Inventory calculate(int size) {
    // float f = size;
    // f = f / 9;
    // if (f == size / 9) {
    // return Bukkit.createInventory(null, (int) (f * 9), "reports");
    // }
    // size = size / 9;
    // size++;
    // size = size * 9;
    // return Bukkit.createInventory(null, size, "reports");
    // }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        String[] args = arguments.split(" ");
        Set<UUID> set = init().playermapo.keySet();
        List<String> list2 = new ArrayList<String>();
        for (UUID uuid : set) {
            list2.add(uuid.toString());
        }
        List<String> list = new ArrayList<String>();
        if (args.length < 2) {
            if ("uuid".startsWith(args[0].toLowerCase())) {
                list.add("uuid");
            }
            if ("usernameo".startsWith(args[0].toLowerCase())) {
                list.add("usernameo");
            }
            if ("gui".startsWith(args[0].toLowerCase())) {
                list.add("gui");
            }
            return list;
        }
        if (args[0].toLowerCase().equals("uuid")) {
            for (String string : list2) {
                if (string.toLowerCase().startsWith(args[1].toLowerCase())) {
                    list.add(string);
                }
            }
        }
        if (args[0].toLowerCase().equals("usernameo")) {
            for (String string : init().playermapo.values()) {
                if (string.toLowerCase().startsWith(args[1].toLowerCase())) {
                    list.add(string);
                }
            }
        }
        return list;
    }

    @SuppressWarnings("unused")
	@Override
    public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException {
        String[] args = arguments.split(" ");
        Map<UUID, String> map1 = init().playermap;
        Map<UUID, String> map2 = init().playermapo;
        Map<UUID, String> map3 = init().playermapr;
        ItemStack stack = IReport.game.getRegistry().getItemBuilder().itemType(ItemTypes.SKULL).damage(3).build();
        // if (source instanceof Human && args.length == 1 &&
        // args[0].equalsIgnoreCase("gui")) {
        // Inventory inv = calculate(init().playermapo.size());
        // for (UUID uuid : map2.keySet()) {
        // List<String> list = new ArrayList<String>();
        // ItemStack i1 =
        // IReport.game.getRegistry().getItemBuilder().withItemType(ItemTypes.SKULL).build();
        // ItemStack i = new ItemStack(ItemTypes.SKULL);
        // i.setDamage((short) 3);
        // SkullMeta meta = (SkullMeta) i.getItemMeta();
        // meta.setOwner(map1.get(uuid));
        // meta.setDisplayName(map1.get(uuid));
        // meta.setLore(setLore(list, uuid));
        // i.setItemMeta(meta);
        // inv.addItem(i);
        // }
        // ((Human) source).openInventory(inv);
        // return true;
        if (args.length == 2) {
            try {
                if (args[0].equalsIgnoreCase("uuid")) {
                    UUID u = UUID.fromString(args[1]);
                    for (String string : setLore(new ArrayList<String>(), u)) {
                        source.sendMessage(Messages.builder(string).build());
                    }
                }
                if (args[0].equalsIgnoreCase("usernameo")) {
                    UUID u = init().playermapor.get(args[1]);
                    for (String string : setLore(new ArrayList<String>(), u)) {
                        source.sendMessage(Messages.builder(string).build());
                    }
                }
                return true;
            } catch (Exception e) {
                source.sendMessage(Messages.builder("invalid UUID").color(TextColors.RED).build());
            }
        } else {
            if (map3.entrySet().size() == 0) {
                source.sendMessage(Messages.builder("There is no reports").color(TextColors.RED).build());
                return true;
            }
            for (Entry<UUID, String> entry : map3.entrySet()) {
                UUID u = entry.getKey();
                for (String string : setLore(new ArrayList<String>(), u)) {
                    source.sendMessage(Messages.builder(string).build());
                }
                source.sendMessage(" ");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("ireport.reports");
    }

    @Override
    public Optional<String> getShortDescription() {
        return Optional.of("Shows a list of reported players");
    }

    @Override
    public Optional<String> getHelp() {
        return Optional.of("Shows a list of reported players");
    }

    @Override
    public String getUsage() {
        return "/reports [gui]";
    }
}

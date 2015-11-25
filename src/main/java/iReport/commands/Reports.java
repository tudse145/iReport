package iReport.commands;

import static iReport.util.Data.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.SkullTypes;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.custom.CustomInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.translation.FixedTranslation;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import com.google.common.collect.Lists;

import iReport.util.Constance;
import iReport.util.Data;
import iReport.util.Utils;

public final class Reports implements CommandCallable {

    private List<Text> setLore(UUID uuid) {
        List<Text> list = new ArrayList<>();
        Map<UUID, String> map1 = init().playermap;
        Map<UUID, String> map2 = init().playermapo;
        Map<UUID, String> map3 = init().playermapr;
        list.add(Texts.of("UUID: " + uuid));
        list.add(Utils.get("reports.lore1", map1.get(uuid)));
        for (String string : map3.get(uuid).split(";")) {
            list.add(Texts.of(string));
        }
        list.add(Utils.get("reports.lore1", map2.get(uuid)));
        return list;
    }

    private CustomInventory calculate(int size) {
        CustomInventory.Builder builder = Constance.game.getRegistry().createBuilder(CustomInventory.Builder.class);
        if (size % 9 == 0) {
            return builder.name(new FixedTranslation("reports")).size(size).build();
        }
        return builder.name(new FixedTranslation("reports")).size(size + Math.abs(size % 9 - 9)).build();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        if (!testPermission(source)) {
            return Lists.newArrayList();
        }
        String[] args = arguments.split(" ");
        if (args.length < 2) {
            List<String> l = Lists.newArrayList();
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
        if (args[0].equalsIgnoreCase("uuid")) {
            return Data.init().playermapo.keySet().parallelStream().map(UUID::toString).filter(s -> s.startsWith(args.length > 1 ? args[1] : "")).collect(Collectors.toList());
        }
        if (args[0].equalsIgnoreCase("usernameo")) {
            return Data.init().playermapo.values().parallelStream().filter(s -> s.startsWith(args.length > 1 ? args[1] : "")).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        if (!testPermission(source)) {
            throw new CommandException(Utils.get("permission.missing"));
        }
        String[] args = arguments.split(" ");
        Map<UUID, String> map1 = init().playermap;
        Map<UUID, String> map2 = init().playermapo;
        Map<UUID, String> map3 = init().playermapr;
        if (source instanceof Human && args.length == 1 && args[0].equalsIgnoreCase("gui")) {
            CustomInventory inv = calculate(init().playermapo.size());
            map2.keySet().parallelStream().forEach(uuid -> {
                ItemStack stack = Constance.game.getRegistry().createBuilder(ItemStack.Builder.class).itemType(ItemTypes.SKULL).quantity(1).build();
                stack.offer(Keys.SKULL_TYPE, SkullTypes.PLAYER);
                stack.offer(Keys.REPRESENTED_PLAYER, Constance.game.getRegistry().createGameProfile(uuid, map1.get(uuid)));
                stack.offer(Keys.ITEM_LORE, setLore(uuid));
                inv.offer(stack);
            });
            ((Human) source).openInventory(inv);
            return CommandResult.success();
        }
        if (args.length == 2) {
            try {
                if (args[0].equalsIgnoreCase("uuid")) {
                    UUID u = UUID.fromString(args[1]);
                    source.sendMessages(setLore(u));
                }
                if (args[0].equalsIgnoreCase("usernameo")) {
                    UUID u = init().playermapor.get(args[1]);
                    source.sendMessages(setLore(u));
                }
                return CommandResult.success();
            } catch (Exception e) {
                throw new CommandException(Utils.get("dreport.error"));
            }
        } else {
            if (map3.isEmpty()) {
                source.sendMessage(Utils.get("reports.error"));
                return CommandResult.success();
            }
            map3.entrySet().stream().map(Entry::getKey).forEach(u -> {
                source.sendMessages(setLore(u));
                source.sendMessage(Texts.of(" "));
            });
            return CommandResult.success();
        }
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("ireport.reports");
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(Utils.get("reports.description"));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Utils.get("reports.description"));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Texts.of("[gui]");
    }

}

package ireport.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static ireport.util.Data.init;
import static java.util.stream.Collectors.*;
import java.util.stream.Stream;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.SkullTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.translation.ResourceBundleTranslation;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.google.common.collect.Lists;

import ireport.util.Constance;
import ireport.util.Data;
import ireport.util.Utils;

public final class Reports implements CommandCallable {

    private List<Text> setLore(UUID uuid) {
        List<Text> list = new ArrayList<>();
        Map<UUID, String> map1 = init().getPlayermap();
        Map<UUID, String> map2 = init().getPlayermapo();
        Map<UUID, String> map3 = init().getPlayermapr();
        list.add(Text.of("UUID: " + uuid));
        list.add(Utils.get("reports.lore2", map1.get(uuid)));
        for (String string : map3.get(uuid).split(";")) {
            list.add(Text.of(replaceWorldUuid(string)));
        }
        list.add(Utils.get("reports.lore1", map2.get(uuid)));
        return list;
    }

    private static String replaceWorldUuid(String report) {
        if (!report.startsWith("gReport: ")) {
            return report;
        }
        String tmp = report.substring(9);
        tmp = tmp.substring(0, tmp.lastIndexOf(" reporter:"));
        String[] data = tmp.split(" ");
        Optional<World> world = Constance.server.getWorld(UUID.fromString(data[1]));
        if (!world.isPresent()) {
			return report;
		}
		data[1] = world.get().getName();
        return "gReport: " + Stream.of(data).collect(joining(" "));
    }

    private Inventory calculate(int size) {
    	Inventory.Builder builder = Constance.GAME.getRegistry().createBuilder(Inventory.Builder.class);
    	return builder.of(InventoryArchetype.builder().title(new ResourceBundleTranslation("reports", Constance.LOOKUP_FUNC)).property(new InventoryDimension(9, (size / 9) + 1)).build("iReport", "iReport")).build(Constance.instence);
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments, Location<World> targetPosition) throws CommandException {
        if (!testPermission(source)) {
            return Lists.newArrayList();
        }
        String[] args = arguments.split(" ");
        if (args.length == 1) {
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
            return Data.init().getPlayermapo().keySet().parallelStream().map(UUID::toString).filter(s -> s.startsWith(args.length > 1 ? args[1] : "")).collect(toList());
        }
        if (args[0].equalsIgnoreCase("usernameo")) {
            return Data.init().getPlayermapo().values().parallelStream().filter(s -> s.startsWith(args.length > 1 ? args[1] : "")).collect(toList());
        }
        return Lists.newArrayList();
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        if (!testPermission(source)) {
            throw new CommandException(Utils.get("permission.missing"));
        }
        String[] args = arguments.split(" ");
        Map<UUID, String> map1 = init().getPlayermap();
        Map<UUID, String> map2 = init().getPlayermapo();
        Map<UUID, String> map3 = init().getPlayermapr();
        if (source instanceof Player && arguments.equalsIgnoreCase("gui")) {
        	Inventory inv = calculate(init().getPlayermapo().size());
            map2.keySet().parallelStream().forEach(uuid -> {
                ItemStack stack = Constance.GAME.getRegistry().createBuilder(ItemStack.Builder.class).itemType(ItemTypes.SKULL).quantity(1).build();
                stack.offer(Keys.SKULL_TYPE, SkullTypes.PLAYER);
                stack.offer(Keys.REPRESENTED_PLAYER, GameProfile.of(uuid, map1.get(uuid)));
                stack.offer(Keys.ITEM_LORE, setLore(uuid));
                inv.offer(stack);
            });
            ((Player) source).openInventory(inv);
            return CommandResult.success();
        }
        if (args.length == 2) {
            try {
                if (args[0].equalsIgnoreCase("uuid")) {
                    UUID u = UUID.fromString(args[1]);
                    source.sendMessages(setLore(u));
                }
                if (args[0].equalsIgnoreCase("usernameo")) {
                    UUID u = init().getPlayermapor().get(args[1]);
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
            map3.keySet().forEach(u -> {
                source.sendMessages(setLore(u));
                source.sendMessage(Text.of(" "));
            });
            return CommandResult.success();
        }
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("iReport.reports");
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
        return Text.of("[gui]");
    }

}

package iReport.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Java8 {

	public static void check() {
		Runnable r = System.out::println;
	}

	public static void notyfyplayers(String meagogs) {
		Bukkit.getOnlinePlayers().parallelStream().filter(p -> p.isOp() || p.hasPermission("iReport.seereportS")).forEach(p -> p.sendMessage(meagogs));
	}

	public static List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Stream<String> set = Data.init().playermapo.keySet().parallelStream().map(UUID::toString);
        if (sender.hasPermission("iReport.dreport") && alias.equalsIgnoreCase("dreport")) {
            return set.filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        }
        if (sender.hasPermission("iReport.reports") && alias.equalsIgnoreCase("reports")) {
            if (args.length < 2) {
                List<String> l = new ArrayList<String>();
                if ("uuid".startsWith(args[0].toLowerCase())) {
                    l.add("uuid");
                }
                if ("usernameo".startsWith(args[0].toLowerCase())) {
                    l.add("usernameo");
                }
                return l;
            }
            if (args[0].toLowerCase().equals("uuid")) {
                return set.filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
            }
            if (args[0].toLowerCase().equals("usernameo")) {
                return Data.init().playermapo.values().parallelStream().filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
            }
        }
        return null;
    }
}

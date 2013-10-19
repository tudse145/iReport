package iReport;

import org.bukkit.command.SimpleCommandMap;


public class Regcommands extends SimpleCommandMap {

    private final iReport plugin;
    public Regcommands(iReport plugin) {
        super(plugin.getServer());
        this.plugin = plugin;
    }

    public void regcommands() {
        fallbackCommands.add(new HReport(plugin));
        fallbackCommands.add(new greport(plugin));
        fallbackCommands.add(new sreport(plugin));
        fallbackCommands.add(new ireportc());
        fallbackCommands.add(new reports(plugin));
    }
}

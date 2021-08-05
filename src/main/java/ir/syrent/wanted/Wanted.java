package ir.syrent.wanted;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Wanted {

    private static Wanted instance;
    public static Wanted getInstance() {
        return instance;
    }

    public Wanted() {
        instance = this;
    }

    public void runCommand(Player killer, LivingEntity victim, String type) {
        if (!Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill" + type + "RunCommandOnKill.Enable")) return;

        for (String command : Main.getInstance().getConfig().getStringList("Wanted.ReceiveOnKill." + type + ".RunCommandOnKill.Commands")) {
            String[] splitCommand = command.split(";");
            String commandExecutor = splitCommand[0];
            String originalCommand = splitCommand[1].replace("%victim%", victim.getName())
                    .replace("%killer%", killer == null ? "UNKNOWN OBJECT" : killer.getName());
            switch (commandExecutor) {
                case "KILLER":
                    if (killer != null)
                        killer.performCommand(originalCommand);
                    break;
                case "VICTIM":
                    if (victim instanceof Player) {
                        Player victimPlayer = (Player) victim;
                        victimPlayer.performCommand(originalCommand);
                    }
                    break;
                case "CONSOLE":
                    Main.getInstance().getServer().dispatchCommand(Main.getInstance().getServer().getConsoleSender(), originalCommand);
                    break;
                default:
                    Main.getInstance().getLogger().warning(String.format("Unknown CommandExecutor %s in command: %s", commandExecutor, originalCommand));
                    break;
            }
        }
    }
}
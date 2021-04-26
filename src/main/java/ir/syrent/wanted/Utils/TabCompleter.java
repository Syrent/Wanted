package ir.syrent.wanted.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    List<String> arguments = new ArrayList<>();

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (arguments.isEmpty()) {
            arguments.add("gui");
            arguments.add("top");
            arguments.add("find");
            arguments.add("set");
            arguments.add("add");
            arguments.add("take");
            arguments.add("clear");
            arguments.add("set-maximum");
            arguments.add("reload");
        }

        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }
            return result;
        }

        return null;
    }
}

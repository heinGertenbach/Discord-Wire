package co.za.bonk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiscordCommandTabCompleter implements TabCompleter {

    private static final String[] FIRSTARGS = {"register"};
    
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        final List<String> possibilities = new ArrayList<String>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList(FIRSTARGS), possibilities);
        }

        switch(args[0]) {
            case "register": {
                return null;
            }
        }

        return possibilities;
    }
}

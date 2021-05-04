package co.za.bonk;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class DiscordCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (sender instanceof Player){

            if (args.length > 0) {

                if (args[0].equals("register")) {
                    if (args.length > 1) {
                        //update sql with username and arg[1]
                        String minecraftName = sender.getName();
                        String minecraftUUID =  Bukkit.getPlayerUniqueId(minecraftName).toString();
                        String hash = args[1];

                        try {

                            Database database = DiscordPaperPlugin.getDatabase();
                            int result = database.Register(hash, minecraftName, minecraftUUID);

                            switch (result) {
                                case 0: {
                                    sender.sendMessage("well done you registered as %s!".formatted(database.fromMinecraft(hash)));
                                    return true;
                                }
                                case 1: {
                                    sender.sendMessage("given registration key:%s\ninvalid\nmight be out of date".formatted(hash));
                                    return true;
                                }
                            }

                        } catch(Exception e) {
                            e.getStackTrace();
                            return false;
                        }
                    }

                    sender.sendMessage("usage: /<command> [register] [unique_hash]");
                    return true;
                }

                return true;
            }
        }

        return false;
    }
}

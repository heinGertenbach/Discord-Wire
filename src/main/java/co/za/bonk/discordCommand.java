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

                switch(args[0]) {
                    case "register":
                        if (args.length > 1) {
                            //update sql with username and arg[1]
                            String minecraftName = sender.getName();
                            String minecraftUUID =  Bukkit.getPlayerUniqueId(minecraftName).toString();
                            String hash = args[1];


                            Database database = DiscordPaperPlugin.getDatabase();

                            Bukkit.getScheduler().runTaskAsynchronously(DiscordPaperPlugin.getInstance(), new Runnable(){
                                @Override
                                public void run() {
                                    try{
                                        int result = database.Register(hash, minecraftName, minecraftUUID);

                                        switch (result) {
                                            case 0:
                                                sender.sendMessage("well done you registered as %s!".formatted(database.FromMinecraft(hash)));
                                            case 1: 
                                                sender.sendMessage("given registration key:\n%s\nis invalid\nmight be out of date".formatted(hash));
                                        }    
                                    } catch (Exception e) {
                                        DiscordPaperPlugin.getInstance().getLogger().warning(e.getMessage());
                                    }                                
                                }
                            });

                            return true;
                        }
                        sender.sendMessage("usage: /<command> [register] [unique_hash]");
                        return true;
                    
                    case "reload":
                        //to-do
                }
            }
        }

        return false;
    }
}

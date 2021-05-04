package co.za.bonk;

import java.sql.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
// import org.bukkit.event.Listener;
// import org.bukkit.event.player.AsyncPlayerChatEvent;
// import org.bukkit.event.EventHandler;

public class DiscordCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (sender instanceof Player){

            if (args.length > 0) {

                if (args[0].equals("register")) {
                    if (args.length > 1) {
                        //update sql with username and arg[1]
                        String minecraftName = sender.getName();
                        String hash = args[1];

                        try {
                            throw new SQLException();
                            // DiscordPaperPlugin.getDatabase().executeStatement("INSERT INTO discord (Minecraft_names) VALUES ("+minecraftName+") WHERE Reference_num = "+hash+";");
                        } catch(SQLException e) {
                            e.getStackTrace();
                        }

                        sender.sendMessage("well done you registered!");
                        return true;
                    }

                    sender.sendMessage("usage: /<command> [register] [unique_hash]");
                    return true;
                }
        


                //     class CodeListen implements Listener{
                //         @SuppressWarnings("deprecation")
                //         @EventHandler
                //         public void onPlayerChat(AsyncPlayerChatEvent event) {
                //             Player player = event.getPlayer();
                //             String message = event.getMessage();
                //             try{
                //                 DiscordPaperPlugin.getStatement().executeUpdate("INSERT INTO discord (Minecraft_names) VALUES ("+player+") WHERE Reference_num = "+message+";");
                //             } catch(SQLException exception){
                //                 exception.getStackTrace();
                //             }
                            
                //         }    
                        
                //     }  
        
                // }

                return true;
            }
        }

        return false;
    }
}

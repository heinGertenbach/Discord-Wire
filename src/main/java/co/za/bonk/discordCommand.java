package co.za.bonk;

import java.sql.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.EventHandler;

public class DiscordCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (sender instanceof Player){
            class CodeListen implements Listener{
                @SuppressWarnings("deprecation")
                @EventHandler
                public void onPlayerChat(AsyncPlayerChatEvent event) {
                    Player player = event.getPlayer();
                    String message = event.getMessage();
                    try{
                        DiscordPaperPlugin.getStatement().executeUpdate("INSERT INTO discord (Minecraft_names) VALUES ("+player+") WHERE Reference_num = "+message+";");
                    } catch(SQLException exception){
                        exception.getStackTrace();
                    }
                    
                }    
                
            }  
   
        }

        return true;
    }

}

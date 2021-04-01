package co.za.bonk;

import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import discord4j.core.object.entity.channel.MessageChannel;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class EventListener implements Listener {
    
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        
        MessageChannel channel = DiscordCLientUser.getDeafaultChannel();

        channel.createMessage(player.getName() + ": " + message);
    }
}

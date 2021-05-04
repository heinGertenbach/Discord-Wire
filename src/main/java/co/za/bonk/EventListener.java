package co.za.bonk;

import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import discord4j.rest.entity.RestChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class EventListener implements Listener {
    
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String playerHash = Hashing.hashString(player.getDisplayName());
        Database database = DiscordPaperPlugin.getDatabase();

        String message = event.getMessage();
        RestChannel channel = DiscordCLientUser.getDeafaultChannel();

        try {
            String discordName = database.fromMinecraft(playerHash);
            if (discordName != null) {
                channel.createMessage(discordName.substring(0, discordName.length()-5) +": "+ message).block();
                return;
            }

        } catch (Exception e) {
            DiscordPaperPlugin.getInstance().getLogger().warning(e.toString());
        }

        //Send message without discord name
        channel.createMessage(player.getName() +": "+ message).block();
    }
}

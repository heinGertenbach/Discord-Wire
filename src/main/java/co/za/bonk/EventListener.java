package co.za.bonk;

import org.bukkit.event.Listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class EventListener implements Listener {
    
    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        // Component messageComponent = event.message();
        //Player player = event.getPlayer();

        //player.chat("wow message sent");
    }
}

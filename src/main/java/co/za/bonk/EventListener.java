package co.za.bonk;

import discord4j.rest.entity.RestChannel;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EventListener implements Listener {

    @EventHandler
    public void onChatEVent(AsyncChatEvent event) {
        DiscordPaperPlugin plugin = DiscordPaperPlugin.getInstance();

        Player player = event.getPlayer();
        String playerHash = Hashing.hashString(player.getName());
        Database database = DiscordPaperPlugin.getDatabase();

        String message = ((TextComponent) event.message()).content();
        RestChannel channel = DiscordCLientUser.getDeafaultChannel();

        try {
            String discordName = database.FromMinecraft(playerHash);
            if (discordName != null) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable(){
                    @Override
                    public void run() {
                        channel.createMessage(discordName.substring(0, discordName.length()-5) +": "+ message).block();
                    };
                });
                return;
            }

        } catch (Exception e) {
            DiscordPaperPlugin.getInstance().getLogger().warning(e.toString());
        }

        //Send message without discord name
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable(){
            @Override
            public void run() {
                channel.createMessage(player.getName() +": "+ message).block();
            };
        });

    }
}

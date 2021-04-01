package co.za.bonk;

import org.bukkit.configuration.file.FileConfiguration;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public class DiscordCLientUser implements Runnable {

    private static DiscordCLientUser instance;
    private static MessageChannel deafaultChannel;
    
    @Override
    public void run() {
        instance = this;
        DiscordPaperPlugin plugin = DiscordPaperPlugin.getInstance();

        final FileConfiguration secrets = plugin.getSecrets();
        final String token = secrets.getString("discordToken");
        final String prefix = secrets.getString("prefix");
        final DiscordClient client = DiscordClient.create(token);
        final GatewayDiscordClient gateway = client.login().block();

        gateway.on(MessageCreateEvent.class).subscribe(event -> {
            final Message message = event.getMessage();
            final String contents = message.getContent();

            if(contents.startsWith(prefix)) {
                final MessageChannel channel = message.getChannel().block();
                deafaultChannel = channel;
                channel.createMessage(contents.substring(prefix.length()));
            }
        });
    }

    public static DiscordCLientUser getInstance() {
        return instance;
    }

    public static MessageChannel getDeafaultChannel() {
        return deafaultChannel;
    }

}

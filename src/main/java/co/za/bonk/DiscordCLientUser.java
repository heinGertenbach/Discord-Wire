package co.za.bonk;

import org.bukkit.configuration.file.FileConfiguration;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.entity.RestChannel;
import discord4j.common.util.Snowflake;

public class DiscordCLientUser implements Runnable {

    private static DiscordCLientUser instance;
    private static RestChannel deafaultChannel;
    private static GatewayDiscordClient gateway;
    
    @Override
    public void run() {
        instance = this;
        DiscordPaperPlugin plugin = DiscordPaperPlugin.getInstance();

        final FileConfiguration secrets = plugin.getSecrets();
        final String token = secrets.getString("discordToken");
        final String prefix = plugin.getConfig().getString("discordConfig.prefix");

        final DiscordClient client = DiscordClient.create(token);
        gateway = client.login().block();

        deafaultChannel = client.getChannelById(Snowflake.of(plugin.getConfig().getString("discordConfig.deafaultChannel")));

        gateway.on(MessageCreateEvent.class).subscribe(event -> {
            final Message message = event.getMessage();
            final String contents = message.getContent();

            if(contents.startsWith("$") && message.getAuthor().map(user -> !user.isBot()).orElse(false)) {
                final MessageChannel channel = message.getChannel().block();
                channel.createMessage(contents.substring(prefix.length())).subscribe();
            }
        });

        gateway.onDisconnect().block();
    }

    public static DiscordCLientUser getInstance() {
        return instance;
    }

    public static RestChannel getDeafaultChannel() {
        return deafaultChannel;
    }

    public static GatewayDiscordClient getGatway() {
        return gateway;
    }

}

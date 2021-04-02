package co.za.bonk;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.entity.RestChannel;
import discord4j.common.util.Snowflake;


//Interface for command handling
interface Command {
    void execute(MessageCreateEvent event);
}


public class DiscordCLientUser implements Runnable {

    private static DiscordCLientUser instance;
    private static RestChannel deafaultChannel;
    private static GatewayDiscordClient gateway;
    private static DiscordClient client;

    private static final Map<String, Command> commands = new HashMap<>();

    //All discord commadns:

    //test command
    static {
        commands.put("test", event -> {
            event.getMessage().getChannel().block()
            .createMessage("working!").block();
        });
    }

    //register command for linking minecraft and discord:
    static {
        commands.put("register", event -> {
            User author = event.getMessage().getAuthor().get();

            //do SQL insertion for hash:
            int hash = (author.getUsername() + author.getDiscriminator()).hashCode();


            //return the command to enter to dm:
            author.getPrivateChannel().block().createMessage("Enter command: /discord register " + hash).block();

        });
    }

    //command to set deafault channel:
    static {
        commands.put("default", event -> {
            MessageChannel channel =  event.getMessage().getChannel().block();

            deafaultChannel = client.getChannelById(channel.getId());
            DiscordPaperPlugin.getInstance().getConfig().set("discordConfig.deafaultChannel", channel.getId());

            channel.createMessage("default channel set to: "); //get channel name to insert at end
        });
    }
    
    @Override
    public void run() {
        instance = this;
        DiscordPaperPlugin plugin = DiscordPaperPlugin.getInstance();

        final FileConfiguration secrets = plugin.getSecrets();
        final String token = secrets.getString("discordToken");
        final String prefix = plugin.getConfig().getString("discordConfig.prefix");

        client = DiscordClient.create(token);
        gateway = client.login().block();

        Snowflake defaultChannelID = Snowflake.of(plugin.getConfig().getString("discordConfig.deafaultChannel"));
        deafaultChannel = client.getChannelById(defaultChannelID);

        gateway.on(ReadyEvent.class).subscribe(event -> {
            final User self = event.getSelf();
            DiscordPaperPlugin.getInstance().getLogger().info("Discord Bot logged in as: " + self.getUsername() + "$" + self.getDiscriminator());
        });

        //register message listener:
        gateway.on(MessageCreateEvent.class)
            .subscribe(event -> {
                final String contents = event.getMessage().getContent();

                for (final Map.Entry<String, Command> entry : commands.entrySet()) {
                    if (contents.startsWith(prefix + entry.getKey())) {
                        entry.getValue().execute(event);
                        break;
                    }
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
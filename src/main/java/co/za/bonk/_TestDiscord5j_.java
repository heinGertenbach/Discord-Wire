package co.za.bonk;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;
import discord4j.rest.entity.RestChannel;

public class _TestDiscord5j_ {

    public static void main(String[] args) {
        final String token = "ODI3MTAyODIzODk1MjY5NDE3.YGWJiA.cKF3WOkUPn_OD9eNDfile5mQOQY";
        final DiscordClient client = DiscordClient.create(token);
        final GatewayDiscordClient gateway = client.login().block();

        gateway.getEventDispatcher().on(ReadyEvent.class)
        .subscribe(event -> {
            final User self = event.getSelf();
            System.out.println(String.format(
                "Logged in as %s#%s", self.getUsername(), self.getDiscriminator()
            ));
        });

        System.out.println("Sending message");

        RestChannel Testchannel = client.getChannelById(Snowflake.of("827103671233937408"));
        Testchannel.createMessage("wow").subscribe();

        System.out.println("Sent message");

        gateway.onDisconnect().block();
    }
    
}

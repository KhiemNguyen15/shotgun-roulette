import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

void main() {
    Properties botProp = new Properties();

    try {
        botProp.load(new FileInputStream("conf/bot.properties"));
    } catch (IOException e) {
        e.printStackTrace();
    }

    DiscordClient client = DiscordClient.create(botProp.getProperty("api_token"));

    Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) ->
            gateway.on(ReadyEvent.class, event ->
                    Mono.fromRunnable(() -> {
                        final User self = event.getSelf();
                        System.out.printf("Logged in as %s%n", self.getUsername());
                    })));
    login.block();
}
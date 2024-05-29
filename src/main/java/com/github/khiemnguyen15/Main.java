import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

void main() throws Exception {
    Properties databaseProps = new Properties();
    try {
        databaseProps.load(new FileInputStream("conf/database.properties"));
    } catch (IOException e) {
        String errMsg = String.format("Error loading database properties: %s", e.getMessage());
        throw new Exception(errMsg);
    }

    Connection conn = DriverManager.getConnection(databaseProps.getProperty("url"), databaseProps);

    // Test code
    System.out.printf("Database client info: %s%n", conn.getClientInfo());

    Properties botProps = new Properties();
    try {
        botProps.load(new FileInputStream("conf/bot.properties"));
    } catch (IOException e) {
        String errMsg = String.format("Error loading bot properties: %s", e.getMessage());
        throw new Exception(errMsg);
    }

    DiscordClient client = DiscordClient.create(botProps.getProperty("api_token"));

    Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) ->
            gateway.on(ReadyEvent.class, event ->
                    Mono.fromRunnable(() -> {
                        final User self = event.getSelf();
                        System.out.printf("Logged in as %s%n", self.getUsername());
                    })));
    login.block();
}
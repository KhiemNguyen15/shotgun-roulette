import com.github.khiemnguyen15.DatabaseHelper;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

final String configPath = "src/main/resources";

void main() throws Exception {
    Properties databaseProps = new Properties();
    try {
        databaseProps.load(new FileInputStream(String.format("%s/database.properties", configPath)));
    } catch (IOException e) {
        String errMsg = String.format("Error loading database properties: %s", e.getMessage());
        throw new Exception(errMsg);
    }

    DatabaseHelper dbHelper = new DatabaseHelper();
    try {
        dbHelper.loadDatabase(databaseProps);
    } catch (SQLException e) {
        String errMsg = String.format("Error connecting to database: %s", e.getMessage());
        throw new Exception(errMsg);
    }

    Thread shutdownListener = new Thread(dbHelper::closeConnection);
    Runtime.getRuntime().addShutdownHook(shutdownListener);

    Properties botProps = new Properties();
    try {
        botProps.load(new FileInputStream(String.format("%s/bot.properties", configPath)));
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
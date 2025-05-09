package com.deos.telegram_chain.handlers;

import com.deos.telegram_chain.Config;
import com.deos.telegram_chain.TelegramChain;
import net.minecraft.network.chat.Component;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.ArrayList;
import java.util.HashMap;

public class TelegramMessageHandler {
    public static void handle(Message message) {
        if (!Config.broadcastFromTelegram) {
            return;
        }

        if (message.getText() == null || message.getText().isEmpty()) {
            return;
        }

        if (isCommand(message)) {
            return;
        }

        var intMessage = Config.messageTelegramBoilerplate
                .replace("${nickname}", prepareUserName(message.getFrom()))
                .replace("${text}", message.getText());

        var server = ServerLifecycleHooks.getCurrentServer();

        if (server == null) {
            return;
        }

        for (var player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(Component.literal(intMessage));
        }

        server.sendSystemMessage(Component.literal(intMessage));

        TelegramChain.LOGGER.info(intMessage);
    }

    private static void sendPlayersList() {
        var players = ServerMessageHandler.currentUsers;

        if (players.isEmpty()) {
            ServerMessageHandler.sendToTelegram(new HashMap<>(), Config.messageTelegramPlayersListEmpty);
            return;
        }

        var playersList = new StringBuilder();

        var number = 1;
        for (var player : players) {
            playersList.append(Config.messageTelegramPlayersListEntryBoilerplate
                    .replace("${number}", String.valueOf(number))
                    .replace("${nickname}", player));
            number++;
        }

        var values = new HashMap<String, String>();
        values.put("${currentPlayers}", String.valueOf(players.size()));
        values.put("${totalPlayers}", String.valueOf(ServerLifecycleHooks.getCurrentServer().getMaxPlayers()));
        values.put("${playersList}", playersList.toString());

        ServerMessageHandler.sendToTelegram(values, Config.messageTelegramPlayersListBoilerplate);
    }

    private static String prepareUserName(User user) {
        var name = new ArrayList<String>();
        if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
            name.add(user.getFirstName());
        }

        if (user.getLastName() != null && !user.getLastName().isEmpty()) {
            name.add(user.getLastName());
        }

        return String.join(" ", name).replaceAll("[^\\p{L}\\p{N} ]+", "").trim();
    }

    private static boolean isCommand(Message message) {
        var text = message.getText();

        if (!Config.commandTelegramPlayersList.isEmpty() && text.equals(Config.commandTelegramPlayersList)) {
            sendPlayersList();
            return true;
        }

        return false;
    }
}

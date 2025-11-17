package com.deos.telegram_chain.handlers;

import com.deos.telegram_chain.Config;
import com.deos.telegram_chain.TelegramChain;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerMessageHandler {
    public static ArrayList<String> currentUsers = new ArrayList<>();

    public static void handle(ServerPlayer player, Component message) {
        if (Config.messageServerBoilerplate.isEmpty()) {
            return;
        }

        var values = new HashMap<String, String>();
        values.put("${nickname}", player.getName().getString());
        values.put("${text}", message.getString());

        sendToTelegram(values, Config.messageServerBoilerplate);
    }

    public static void handle(Player player, Advancement advancement) {
        if (Config.messageServerAdvancementBoilerplate.isEmpty()) {
            return;
        }

        if (advancement.getDisplay() == null) {
            return;
        }

        var values = new HashMap<String, String>();
        values.put("${nickname}", player.getName().getString());
        values.put("${advancement}", advancement.getDisplay().getTitle().getString());

        sendToTelegram(values, Config.messageServerAdvancementBoilerplate);
    }

    public static void handleUserConnection(ServerPlayer player, boolean connected) {
        var msg = connected ? Config.messageServerPlayerJoinedBoilerplate : Config.messageServerPlayerQuitBoilerplate;

        if (connected) {
            if (!currentUsers.contains(player.getName().getString())) {
                currentUsers.add(player.getName().getString());
            } else {
                return;
            }
        } else {
            if (currentUsers.contains(player.getName().getString())) {
                currentUsers.remove(player.getName().getString());
            } else {
                return;
            }
        }

        if (msg.isEmpty()) {
            return;
        }

        var values = new HashMap<String, String>();
        values.put("${nickname}", player.getName().getString());
        values.put("${currentPlayers}", String.valueOf(currentUsers.size()));
        values.put("${totalPlayers}", String.valueOf(ServerLifecycleHooks.getCurrentServer().getMaxPlayers()));

        sendToTelegram(values, msg);
    }

    public static void handleUserDeath(ServerPlayer player, Entity killer) {
        if (Config.messageServerPlayerDiedBoilerplate.isEmpty()) {
            return;
        }

        var killerName = killer == null ? "???" : killer.getName().getString();

        var values = new HashMap<String, String>();
        values.put("${nickname}", player.getName().getString());
        values.put("${killer}", killerName);

        sendToTelegram(values, Config.messageServerPlayerDiedBoilerplate);
    }

    public static void onQuestCompleted(String questName, List<ServerPlayer> players, String teamName, boolean isQuestRepeatable) {
        if (Config.messageServerQuestSkipRepeatedQuests && isQuestRepeatable) {
            return;
        }

        String boilerplate;
        var values = new HashMap<String, String>();

        values.put("${questName}", questName);

        if (players.size() == 1) {
            if (Config.messageServerQuestCompletedSingleBoilerplate.isEmpty()) {
                return;
            }

            boilerplate = Config.messageServerQuestCompletedSingleBoilerplate;

            values.put("${nickname}", players.get(0).getName().getString());
        } else {
            if (Config.messageServerQuestCompletedTeamBoilerplate.isEmpty()) {
                return;
            }

            boilerplate = Config.messageServerQuestCompletedTeamBoilerplate;

            var playersNicknames = new ArrayList<String>();
            for (ServerPlayer player : players) {
                playersNicknames.add(player.getName().getString());
            }

            values.put("${players}", String.join(", ", playersNicknames));
            values.put("${teamName}", teamName);
        }

        sendToTelegram(values, boilerplate);
    }

    public static void sendToTelegram(HashMap<String, String> replacements, String text) {
        if (TelegramChain.TELEGRAM_BOT == null || !Config.broadcastToTelegram) {
            return;
        }

        var intMessage = text;

        for (String key : replacements.keySet()) {
            intMessage = intMessage.replace(key, replacements.get(key));
        }

        try {
            TelegramChain.TELEGRAM_BOT.CLIENT.execute(
                    TelegramChain.TELEGRAM_BOT.prepareMessageForChat()
                            .text(intMessage)
                            .parseMode(Config.messageServerParseMode)
                            .build()
            );
        } catch (TelegramApiException e) {
            TelegramChain.LOGGER.error("Unable to resend message to Telegram", e);
        }
    }
}

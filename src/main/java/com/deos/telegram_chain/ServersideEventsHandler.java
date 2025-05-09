package com.deos.telegram_chain;

import com.deos.telegram_chain.handlers.ServerMessageHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = TelegramChain.MODID, value = Dist.DEDICATED_SERVER)
public class ServersideEventsHandler {
    @SubscribeEvent
    public static void onServerLoaded(final ServerStartedEvent event) {
        if (Config.telegramBotToken.isEmpty()) {
            TelegramChain.LOGGER.info("Skipping telegram chain setup, telegramBotToken is empty");
            return;
        }

        if (Config.telegramChatId == 0) {
            TelegramChain.LOGGER.info("Skipping telegram chain setup, telegramChatId is empty");
            return;
        }

        try {
            TelegramChain.LOGGER.info("Starting telegram bot polling");

            var bot = new TelegramBot();
            var botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(Config.telegramBotToken, bot);

            TelegramChain.TELEGRAM_BOT = bot;
            TelegramChain.TELEGRAM_BOT.CLIENT = new OkHttpTelegramClient(Config.telegramBotToken);

            TelegramChain.LOGGER.info("Telegram chain started!");

            if (!Config.messageServerStarted.isEmpty()) {
                ServerMessageHandler.sendToTelegram(new HashMap<>(), Config.messageServerStarted);
            }
        } catch (TelegramApiException e) {
            TelegramChain.LOGGER.error("Unable to start bot polling", e);
        }
    }

    @SubscribeEvent
    public static void onServerMessage(final ServerChatEvent event) {
        if (event.getPlayer() == null || event.getMessage() == null) {
            return;
        }

        ServerMessageHandler.handle(event.getPlayer(), event.getMessage());
    }

    @SubscribeEvent
    public static void onAdvancement(final AdvancementEvent.AdvancementEarnEvent event) {
        ServerMessageHandler.handle(event.getEntity(), event.getAdvancement());
    }

    @SubscribeEvent
    public static void onPlayerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer)) {
            return;
        }

        ServerMessageHandler.handleUserConnection((ServerPlayer) event.getEntity(), true);
    }

    @SubscribeEvent
    public static void onPlayerDisconnect(final PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer)) {
            return;
        }

        ServerMessageHandler.handleUserConnection((ServerPlayer) event.getEntity(), false);
    }

    @SubscribeEvent
    public static void onPlayerDeath(final LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer)) {
            return;
        }

        ServerMessageHandler.handleUserDeath((ServerPlayer) event.getEntity(), event.getSource().getEntity());
    }
}
package com.deos.telegram_chain;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = TelegramChain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<String> TELEGRAM_BOT_TOKEN = BUILDER
            .comment("Telegram bot token (make sure to add bot to chat)")
            .define("telegramBotToken", "");

    private static final ForgeConfigSpec.LongValue TELEGRAM_CHAT_ID = BUILDER
            .comment("Telegram chat id to interact")
            .defineInRange("telegramChatId", 0, Long.MIN_VALUE, Long.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue TELEGRAM_THREAD_ID = BUILDER
            .comment("Telegram thread id to interact")
            .defineInRange("telegramThreadId", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.BooleanValue BROADCAST_TO_TELEGRAM = BUILDER
            .comment("Broadcast messages from server to Telegram")
            .define("broadcastToTelegram", true);

    private static final ForgeConfigSpec.BooleanValue BROADCAST_FROM_TELEGRAM = BUILDER
            .comment("Broadcast messages to server from Telegram")
            .define("broadcastFromTelegram", true);

    public static final ForgeConfigSpec.ConfigValue<String> MESSAGE_SERVER_PARSE_MODE = BUILDER
            .comment("parse_mode for any message from server to Telegram")
            .define("messageServerParseMode", "HTML");


    private static final ForgeConfigSpec.ConfigValue<String> MESSAGE_TELEGRAM_BOILERPLATE = BUILDER
            .comment("Define message format for messages from Telegram")
            .comment("Use broadcastFromTelegram to disable")
            .comment("Supported vars: ${nickname}, ${text}")
            .define("messageTelegramBoilerplate", ">TG [${nickname}]: ${text}");

    private static final ForgeConfigSpec.ConfigValue<String> MESSAGE_SERVER_STARTED = BUILDER
            .comment("Define message for server started event")
            .comment("Leave empty or use broadcastToTelegram to disable")
            .define("messageServerStarted", "<b><i>Server started!</i></b>");

    private static final ForgeConfigSpec.ConfigValue<String> MESSAGE_SERVER_PLAYER_JOINED_BOILERPLATE = BUILDER
            .comment("Define message for player joined event")
            .comment("Leave empty or use broadcastToTelegram to disable")
            .comment("Supported vars: ${nickname}, ${currentPlayers}, ${totalPlayers}")
            .define("messageServerPlayerJoinedBoilerplate", "<b>${nickname}</b> joined the server! There are ${currentPlayers} of ${totalPlayers} players");

    private static final ForgeConfigSpec.ConfigValue<String> MESSAGE_SERVER_PLAYER_QUIT_BOILERPLATE = BUILDER
            .comment("Define message for player disconnected event")
            .comment("Leave empty or use broadcastToTelegram to disable")
            .comment("Supported vars: ${nickname}, ${currentPlayers}, ${totalPlayers}")
            .define("messageServerPlayerQuitBoilerplate", "<b>${nickname}</b> disconnected. There are ${currentPlayers} of ${totalPlayers} players");

    private static final ForgeConfigSpec.ConfigValue<String> MESSAGE_SERVER_BOILERPLATE = BUILDER
            .comment("Define message format for messages to Telegram")
            .comment("Leave empty or use broadcastToTelegram to disable")
            .comment("Supported vars: ${nickname}, ${text}")
            .define("messageServerBoilerplate", "<b>[${nickname}]</b>: ${text}");

    public static final ForgeConfigSpec.ConfigValue<String> MESSAGE_SERVER_ADVANCEMENT_BOILERPLATE = BUILDER
            .comment("Define message format for advancement messages to Telegram")
            .comment("Leave empty or use broadcastToTelegram to disable")
            .comment("Supported vars: ${nickname}, ${name}")
            .define("messageServerAdvancementBoilerplate", "<b>${nickname}</b> got advancement <i>${advancement}</i>");

    public static final ForgeConfigSpec.ConfigValue<String> MESSAGE_SERVER_PLAYER_DIED_BOILERPLATE = BUILDER
            .comment("Define message format for player death")
            .comment("Leave empty or use broadcastToTelegram to disable")
            .comment("Supported vars: ${nickname}, ${killer}")
            .define("messageServerPlayerDiedBoilerplate", "<b>${nickname}</b> was killed by ${killer}");

    public static final ForgeConfigSpec.ConfigValue<String> MESSAGE_TELEGRAM_PLAYERS_LIST_BOILERPLATE = BUILDER
            .comment("Define message format for players list in Telegram")
            .comment("Supported vars: ${currentPlayers}, ${totalPlayers}, ${playersList}")
            .comment("Note that messageServerParseMode also works here")
            .define("messageTelegramPlayersListBoilerplate", "There are ${currentPlayers} of ${totalPlayers} players total:\n\n${playersList}");

    public static final ForgeConfigSpec.ConfigValue<String> MESSAGE_TELEGRAM_PLAYERS_LIST_EMPTY = BUILDER
            .comment("Define message format for empty players list in Telegram")
            .define("messageTelegramPlayersListEmpty", "There are no players connected");

    public static final ForgeConfigSpec.ConfigValue<String> MESSAGE_TELEGRAM_PLAYERS_LIST_ENTRY_BOILERPLATE = BUILDER
            .comment("Define player list entry format")
            .comment("Supported vars: ${number}, ${nickname}")
            .comment("This will be used as ${playersList} in messageTelegramPlayersListBoilerplate")
            .define("messageTelegramPlayersListEntryBoilerplate", "${number}. ${nickname}\n");

    public static final ForgeConfigSpec.ConfigValue<String> COMMAND_TELEGRAM_PLAYERS_LIST = BUILDER
            .comment("Define command text for players list")
            .comment("Leave empty to disable")
            .comment("You can use slash at start or not")
            .define("commandTelegramPlayersList", "/players");

    public static final ForgeConfigSpec.ConfigValue<String> MESSAGE_SERVER_QUEST_COMPLETED_SINGLE_BOILERPLATE = BUILDER
            .comment("Define message format for quest completed event (FTBQuests). Used when only one player from team connected")
            .comment("Leave empty or use broadcastToTelegram to disable")
            .comment("Supported vars: ${nickname}, ${questName}")
            .comment("Note that messageServerParseMode also works here")
            .define("messageServerQuestCompletedSingleBoilerplate", "<b>${nickname}</b> completed the quest <i>${questName}</i>");

    public static final ForgeConfigSpec.ConfigValue<String> MESSAGE_SERVER_QUEST_COMPLETED_TEAM_BOILERPLATE = BUILDER
            .comment("Define message format for quest completed event (FTBQuests). Used when multiple players from team connected")
            .comment("Leave empty or use broadcastToTelegram to disable")
            .comment("Supported vars: ${players}, ${questName}, ${teamName}")
            .comment("Note that messageServerParseMode also works here")
            .define("messageServerQuestCompletedTeamBoilerplate", "Players <b>${players}</b> from the <b>${teamName}</b> team completed the <i>${questName}</i> quest");

    public static final ForgeConfigSpec.ConfigValue<Boolean> MESSAGE_SERVER_QUEST_SKIP_REPEATABLE_QUESTS = BUILDER
            .comment("Skip or not repeatable quests notifications")
            .define("messageServerQuestSkipRepeatedQuests", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static String telegramBotToken;
    public static Long telegramChatId;
    public static int telegramThreadId;
    public static boolean broadcastToTelegram;
    public static boolean broadcastFromTelegram;

    // Messages section
    public static String messageServerParseMode;

    public static String messageServerStarted;
    public static String messageTelegramBoilerplate;
    public static String messageServerPlayerJoinedBoilerplate;
    public static String messageServerPlayerQuitBoilerplate;
    public static String messageServerBoilerplate;
    public static String messageServerAdvancementBoilerplate;
    public static String messageServerPlayerDiedBoilerplate;

    public static String messageTelegramPlayersListBoilerplate;
    public static String messageTelegramPlayersListEmpty;
    public static String messageTelegramPlayersListEntryBoilerplate;

    public static String commandTelegramPlayersList;

    public static String messageServerQuestCompletedSingleBoilerplate;
    public static String messageServerQuestCompletedTeamBoilerplate;
    public static Boolean messageServerQuestSkipRepeatedQuests;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        telegramBotToken = TELEGRAM_BOT_TOKEN.get();
        telegramChatId = TELEGRAM_CHAT_ID.get();
        telegramThreadId = TELEGRAM_THREAD_ID.get();

        broadcastToTelegram = BROADCAST_TO_TELEGRAM.get();
        broadcastFromTelegram = BROADCAST_FROM_TELEGRAM.get();

        messageTelegramBoilerplate = MESSAGE_TELEGRAM_BOILERPLATE.get();

        messageServerParseMode = MESSAGE_SERVER_PARSE_MODE.get();

        messageServerStarted = MESSAGE_SERVER_STARTED.get();
        messageServerPlayerJoinedBoilerplate = MESSAGE_SERVER_PLAYER_JOINED_BOILERPLATE.get();
        messageServerPlayerQuitBoilerplate = MESSAGE_SERVER_PLAYER_QUIT_BOILERPLATE.get();
        messageServerBoilerplate = MESSAGE_SERVER_BOILERPLATE.get();
        messageServerAdvancementBoilerplate = MESSAGE_SERVER_ADVANCEMENT_BOILERPLATE.get();
        messageServerPlayerDiedBoilerplate = MESSAGE_SERVER_PLAYER_DIED_BOILERPLATE.get();

        messageTelegramPlayersListBoilerplate = MESSAGE_TELEGRAM_PLAYERS_LIST_BOILERPLATE.get();
        messageTelegramPlayersListEmpty = MESSAGE_TELEGRAM_PLAYERS_LIST_EMPTY.get();
        messageTelegramPlayersListEntryBoilerplate = MESSAGE_TELEGRAM_PLAYERS_LIST_ENTRY_BOILERPLATE.get();
        commandTelegramPlayersList = COMMAND_TELEGRAM_PLAYERS_LIST.get();

        messageServerQuestCompletedSingleBoilerplate = MESSAGE_SERVER_QUEST_COMPLETED_SINGLE_BOILERPLATE.get();
        messageServerQuestCompletedTeamBoilerplate = MESSAGE_SERVER_QUEST_COMPLETED_TEAM_BOILERPLATE.get();
        messageServerQuestSkipRepeatedQuests = MESSAGE_SERVER_QUEST_SKIP_REPEATABLE_QUESTS.get();
    }
}

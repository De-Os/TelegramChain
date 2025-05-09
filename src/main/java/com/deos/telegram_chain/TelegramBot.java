package com.deos.telegram_chain;

import com.deos.telegram_chain.handlers.TelegramMessageHandler;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {
    public TelegramClient CLIENT;

    @Override
    public void consume(Update update) {
        if (!update.hasMessage()) {
            return;
        }

        var message = update.getMessage();

        var chat = message.getChat();

        if (!chat.getId().equals(Config.telegramChatId)) {
            return;
        }

        if (Config.telegramThreadId != 0) {
            var thread = message.getMessageThreadId();
            if (thread == null || thread == 0 || thread != Config.telegramThreadId) {
                return;
            }
        }

        TelegramMessageHandler.handle(message);
    }

    public SendMessage.SendMessageBuilder prepareMessageForChat(){
        var message = SendMessage.builder().chatId(Config.telegramChatId);

        if(Config.telegramThreadId != 0){
            message.messageThreadId(Config.telegramThreadId);
        }

        return message;
    };
}

package com.deos.telegram_chain.helpers;

import com.deos.telegram_chain.handlers.ServerMessageHandler;
import dev.architectury.event.EventResult;
import dev.ftb.mods.ftbquests.events.ObjectCompletedEvent;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.task.ItemTask;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.regex.Pattern;

public class FTBQuestsHelper {
    private static boolean ftbQuestsLoaded = false;

    public static void init() {
        try {
            Class.forName("dev.ftb.mods.ftbquests.quest.QuestObject");
            ftbQuestsLoaded = true;
        } catch (ClassNotFoundException e) {
            ftbQuestsLoaded = false;
        }
    }

    public static boolean isFTBQuestsLoaded() {
        return ftbQuestsLoaded;
    }

    public static EventResult onQuestCompleted(final ObjectCompletedEvent<Quest> event) {
        var quest = event.getObject();
        var onlinePlayers = event.getOnlineMembers();

        var serverQuestFile = ServerQuestFile.INSTANCE.get(quest.id);

        String title = "???";
        if(serverQuestFile instanceof Quest q){
            title = q.getRawTitle();
            if(title == null || title.isEmpty()){
                var tasks = q.getTasks();
                if(!tasks.isEmpty()){
                    var firstTask = tasks.iterator().next();
                    title = firstTask.getRawTitle();
                    if(title == null || title.isEmpty()){
                        if(firstTask instanceof ItemTask it){
                            title = it.getRawTitle();
                            if(title == null || title.isEmpty()){
                                title = it.getItemStack().getHoverName().getString();
                            }
                        }
                    }
                }
            }
        }
        if(title == null || title.isEmpty()){
            title = "???";
        }

        var formattingReplacer = Pattern.compile("([\\xA7&][0-9A-FK-OR])", Pattern.CASE_INSENSITIVE);
        title = formattingReplacer.matcher(title).replaceAll("");


        ServerMessageHandler.onQuestCompleted(
                title,
                onlinePlayers,
                event.getData().getName(),
                quest.canBeRepeated()
        );

        return EventResult.pass();
    }
}

package com.feed_the_beast.ftbcurseappbot.globalCommands;

import com.feed_the_beast.ftbcurseappbot.Main;
import com.feed_the_beast.javacurselib.websocket.WebSocket;
import com.feed_the_beast.javacurselib.websocket.messages.notifications.ConversationMessageNotification;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * Created by progwml6 on 5/20/16.
 */
@Slf4j
public class Help extends CommandBase {

    @Override
    public void onMessage (WebSocket webSocket, ConversationMessageNotification msg) {
        log.info("help ");
        webSocket.sendMessage(msg.conversationID, "commands are: " + Main.getBotTrigger() + "ban, " + Main.getBotTrigger() + "help, " + Main.getBotTrigger() + "haspaidmc, " + Main.getBotTrigger() +
                "repeat, " + Main.getBotTrigger() + "mcstatus, " + Main.getBotTrigger() +  "mcuuid, " + Main.getBotTrigger() +  "api, will try to delete things containing \"autodeletetest\" ");
    }

    @Override
    public Pattern getTriggerRegex () {
        return getSimpleCommand("help");
    }

}
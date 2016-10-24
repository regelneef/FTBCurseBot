package com.feed_the_beast.ftbcurseappbot.globalCommands;

import com.feed_the_beast.ftbcurseappbot.Main;
import com.feed_the_beast.javacurselib.websocket.WebSocket;
import com.feed_the_beast.javacurselib.websocket.messages.notifications.ConversationMessageNotification;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by progwml6 on 10/24/16.
 */
public class Curseuuid extends CommandBase {
    @Override
    public void onMessage (WebSocket webSocket, ConversationMessageNotification msg) {
        Optional<String> channelName = Main.getCacheService().getContacts().get().getChannelNamebyId(msg.conversationID);
        Optional<String> serverName = Main.getCacheService().getContacts().get().getGroupNamebyId(msg.rootConversationID);

        if (serverName.isPresent() && channelName.isPresent()) {
            webSocket.sendMessage(msg.conversationID, "Server: " + serverName.get() + " is " + msg.rootConversationID + " ; Channel: " + channelName.get() + " is " + msg.conversationID);
        } else {
            if (serverName.isPresent()) {
                webSocket.sendMessage(msg.conversationID, "Server: " + serverName + " is " + msg.rootConversationID + " ; Channel: is " + msg.conversationID);
            }
            if (channelName.isPresent()) {
                webSocket.sendMessage(msg.conversationID, "Server is " + msg.rootConversationID + " ; Channel: " + channelName.get() + " is " + msg.conversationID);
            }
        }
    }

    @Override
    public Pattern getTriggerRegex () {
        return getSimpleCommand("curseuuid");
    }

    @Override
    public String getHelp () {
        return "gets server/channel uuid for debugging purposes";
    }
}

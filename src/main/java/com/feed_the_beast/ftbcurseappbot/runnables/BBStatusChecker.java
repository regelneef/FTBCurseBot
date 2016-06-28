package com.feed_the_beast.ftbcurseappbot.runnables;

import com.feed_the_beast.ftbcurseappbot.Main;
import com.feed_the_beast.ftbcurseappbot.globalCommands.BBStatus;
import com.feed_the_beast.ftbcurseappbot.globalCommands.CFStatus;
import com.feed_the_beast.javacurselib.service.contacts.contacts.ContactsResponse;
import com.feed_the_beast.javacurselib.websocket.WebSocket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BBStatusChecker extends ServiceStatusBase {

    public BBStatusChecker (WebSocket webSocket) {
        super(webSocket, BBStatus.getInstance(), Main.getBBStatusChangeNotificationsEnabled());
    }

}
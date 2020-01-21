package ru.evillcat.client;

import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.evillcat.client.connector.ClientConnector;
import ru.evillcat.client.event.*;
import ru.evillcat.client.event.chat.ChatEventBus;
import ru.evillcat.client.handler.MessageHandler;
import ru.evillcat.client.messenger.ClientMessenger;
import ru.evillcat.client.updater.MessageParser;
import ru.evillcat.client.view.chat.ChatWindow;
import ru.evillcat.client.view.window.NameAndConnectionWindow;
import ru.evillcat.common.event.bus.EventBus;
import ru.evillcat.common.event.bus.EventListener;


import javax.swing.*;

public class Chat {

    private static final Logger log = LoggerFactory.getLogger(Chat.class);
    private final EventBus eventBus;
    private String address;
    private String nickname;
    private int port;
    private NameAndConnectionWindow nameAndConnectionWindow;
    private ClientConnector clientConnector;
    private MessageParser messageParser;
    private MessageHandler messageHandler;
    private ClientMessenger messenger;

    public Chat() {
        try {
            UIManager.setLookAndFeel(new HiFiLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            log.error("Ошибка изменения look and feel", e.getCause());
        }

        eventBus = new ChatEventBus();
        eventBus.subscribe(AddressChosenEvent.class, new AddressChosenEventListener());
        eventBus.subscribe(NicknameChosenEvent.class, new NicknameChosenEventListener());
        eventBus.subscribe(PortChosenEvent.class, new PortChosenEventListener());
        eventBus.subscribe(UserNotAuthorisedEvent.class, new UserNotAuthorizedEventListener());
        eventBus.subscribe(UserAuthorizedEvent.class, new UserAuthorizedEventListener());
    }

    public void openSettingsWindow(String viewMessage) {
        nameAndConnectionWindow = new NameAndConnectionWindow(eventBus);
        nameAndConnectionWindow.startView(viewMessage);
    }

    private void openChatWindow() {
        if (address != null && nickname != null && port != 0) {
            nameAndConnectionWindow.dropView();
            messageParser = new MessageParser(eventBus);
            messageHandler = new MessageHandler(messageParser, eventBus);
            messenger = new ClientMessenger(nickname, eventBus);
            clientConnector = new ClientConnector(address, port, messageHandler, messenger, eventBus);
            clientConnector.startConnectionLoop();
            messageHandler.startHandling();
        }
    }

    private class AddressChosenEventListener implements EventListener<AddressChosenEvent> {

        @Override
        public void update(AddressChosenEvent event) {
            address = event.getAddress();
            openChatWindow();
        }
    }

    private class NicknameChosenEventListener implements EventListener<NicknameChosenEvent> {

        @Override
        public void update(NicknameChosenEvent event) {
            nickname = event.getNickname();
            openChatWindow();
        }
    }

    private class PortChosenEventListener implements EventListener<PortChosenEvent> {

        @Override
        public void update(PortChosenEvent event) {
            port = Integer.parseInt(event.getPort());
            openChatWindow();
        }
    }

    private class UserNotAuthorizedEventListener implements EventListener<UserNotAuthorisedEvent> {

        @Override
        public void update(UserNotAuthorisedEvent event) {
            messageParser = null;
            messageHandler.stop();
            messageHandler = null;
            messenger = null;
            clientConnector = null;
            address = null;
            nickname = null;
            port = 0;
            openSettingsWindow(event.getMessage());
        }
    }

    private class UserAuthorizedEventListener implements EventListener<UserAuthorizedEvent> {

        @Override
        public void update(UserAuthorizedEvent event) {
            ChatWindow chatWindow = new ChatWindow(eventBus);
            chatWindow.startView();
        }
    }
}

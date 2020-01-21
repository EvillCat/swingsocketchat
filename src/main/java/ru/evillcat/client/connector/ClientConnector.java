package ru.evillcat.client.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.evillcat.client.event.ServerNotRespondingEvent;
import ru.evillcat.client.event.UserDisconnectEvent;
import ru.evillcat.client.handler.MessageHandler;
import ru.evillcat.client.messenger.ClientMessenger;
import ru.evillcat.common.event.bus.EventBus;
import ru.evillcat.common.event.bus.EventListener;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientConnector {

    private static final Logger log = LoggerFactory.getLogger(ClientConnector.class);

    private final MessageHandler messageHandler;
    private final String address;
    private final int port;
    private final AtomicBoolean isHandling;
    private final ClientMessenger messenger;

    private Socket socket;

    public ClientConnector(String address, int port, MessageHandler messageHandler, ClientMessenger messenger,
                           EventBus eventBus) {
        this.address = address;
        this.port = port;
        this.messageHandler = messageHandler;
        this.messenger = messenger;
        isHandling = new AtomicBoolean(true);
        eventBus.subscribe(ServerNotRespondingEvent.class, new ServerNotRespondingEventListener());
        eventBus.subscribe(UserDisconnectEvent.class, new UserDisconnectEventListener());
    }

    public void startConnectionLoop() {
        while (isHandling.get()) {
            try {
                socket = new Socket(address, port);
                log.info("Подключен к " + address + ":" + port);
                messageHandler.setIn(socket.getInputStream());
                messenger.setOut(socket.getOutputStream());
                break;
            } catch (IOException ex) {
                log.info("Переподключение по адресу " + address + ":" + port);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ServerNotRespondingEventListener implements EventListener<ServerNotRespondingEvent> {

        @Override
        public void update(ServerNotRespondingEvent event) {
            log.info("Сервер не отвечает.");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
            startConnectionLoop();
        }
    }

    private class UserDisconnectEventListener implements EventListener<UserDisconnectEvent> {

        @Override
        public void update(UserDisconnectEvent event) {
            isHandling.set(false);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

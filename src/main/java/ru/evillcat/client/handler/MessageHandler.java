package ru.evillcat.client.handler;

import ru.evillcat.client.event.ServerNotRespondingEvent;
import ru.evillcat.client.updater.MessageParser;
import ru.evillcat.common.event.bus.EventBus;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageHandler {

    private final MessageParser messageParser;
    private final EventBus eventBus;
    private final AtomicBoolean isHandling;
    private Scanner in;

    public MessageHandler(MessageParser messageParser, EventBus eventBus) {
        this.messageParser = messageParser;
        this.eventBus = eventBus;
        isHandling = new AtomicBoolean(true);
    }

    public void startHandling() {
        new Thread(() -> {
            try {
                while (isHandling.get()) {
                    String message = in.nextLine();
                    messageParser.parseIncomingMessage(message);
                }
            } catch (NoSuchElementException ex) {
                messageParser.parseIncomingMessage("Потеряно соединение с сервером. Восстанавливаем соединиение...");
                eventBus.publish(new ServerNotRespondingEvent());
            }
        }).start();
    }

    public void stop() {
        isHandling.set(false);
    }

    public void setIn(InputStream inputStream) {
        this.in = new Scanner(inputStream);
    }
}

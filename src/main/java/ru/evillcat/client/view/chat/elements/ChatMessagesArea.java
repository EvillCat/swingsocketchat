package ru.evillcat.client.view.chat.elements;

import ru.evillcat.client.event.UpdateMessageArea;
import ru.evillcat.common.event.bus.EventBus;
import ru.evillcat.common.event.bus.EventListener;

import javax.swing.*;

public class ChatMessagesArea {

    private static final String LINE_SEPARATOR = System.lineSeparator();
    private final JTextArea messageArea;

    public ChatMessagesArea(EventBus eventBus) {
        messageArea = new JTextArea();
        messageArea.setEnabled(false);
        messageArea.setLineWrap(true);
        eventBus.subscribe(UpdateMessageArea.class, new MessageEventListener());
    }

    public JTextArea getMessageArea() {
        return messageArea;
    }

    private class MessageEventListener implements EventListener<UpdateMessageArea> {

        @Override
        public void update(UpdateMessageArea event) {

            messageArea.append(event.getMessage() + LINE_SEPARATOR);
        }
    }
}

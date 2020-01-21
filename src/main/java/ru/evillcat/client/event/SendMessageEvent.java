package ru.evillcat.client.event;

import ru.evillcat.common.event.bus.Event;
import ru.evillcat.common.message.ChatMessage;
import ru.evillcat.common.message.MessageType;

public class SendMessageEvent extends Event {

    private final String text;

    public SendMessageEvent(String text) {
        this.text = text;
    }

    public ChatMessage getMessage(String name) {
        return new ChatMessage(name, text, MessageType.COMMON);
    }
}

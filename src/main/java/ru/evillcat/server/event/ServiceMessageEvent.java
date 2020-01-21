package ru.evillcat.server.event;

import ru.evillcat.common.event.bus.Event;
import ru.evillcat.common.message.ChatMessage;
import ru.evillcat.common.message.MessageType;

import java.util.List;

public class ServiceMessageEvent extends Event {

    private final ChatMessage chatMessage;

    public ServiceMessageEvent(String name, String text, List<String> names) {
        this.chatMessage = new ChatMessage(name, text, MessageType.SERVICE, names);
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }
}

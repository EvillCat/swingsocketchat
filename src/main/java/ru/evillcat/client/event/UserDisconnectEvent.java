package ru.evillcat.client.event;

import ru.evillcat.common.event.bus.Event;
import ru.evillcat.common.message.ChatMessage;
import ru.evillcat.common.message.MessageType;

public class UserDisconnectEvent extends Event {

    private final ChatMessage chatMessage;

    public UserDisconnectEvent() {
        chatMessage = new ChatMessage("", "", MessageType.USER_DISCONNECT);
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }
}

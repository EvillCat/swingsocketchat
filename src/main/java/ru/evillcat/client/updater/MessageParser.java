package ru.evillcat.client.updater;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.evillcat.client.event.UpdateMessageArea;
import ru.evillcat.client.event.UpdateUsersListEvent;
import ru.evillcat.client.event.UserAuthorizedEvent;
import ru.evillcat.client.event.UserNotAuthorisedEvent;
import ru.evillcat.common.event.bus.EventBus;
import ru.evillcat.common.message.ChatMessage;
import ru.evillcat.common.message.MessageType;


public class MessageParser {

    private final EventBus eventBus;
    private final Gson gson;

    public MessageParser(EventBus eventBus) {
        this.eventBus = eventBus;
        gson = new Gson();
    }

    public void parseIncomingMessage(String message) {
        try {
            ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
            sendMessageToView(chatMessage);
        } catch (JsonSyntaxException ex) {
            sendMessageToView(new ChatMessage("", message, MessageType.COMMON));
        }
    }

    private void sendMessageToView(ChatMessage message) {
        switch (message.getMessageType()) {
            case COMMON:
                eventBus.publish(new UpdateMessageArea(message.getMessageAsString()));
                break;
            case AUTHORIZED:
                eventBus.publish(new UserAuthorizedEvent());
                break;
            case NOT_AUTHORIZED:
                eventBus.publish(new UserNotAuthorisedEvent(message.getTextMessage()));
                break;
            case SERVICE:
                System.out.println(message.getUserNames().toString());
                eventBus.publish(new UpdateMessageArea(message.getMessageAsString()));
                eventBus.publish(new UpdateUsersListEvent(message.getUserNames()));
                break;
        }
    }
}

package ru.evillcat.server.handler;

import ru.evillcat.common.event.bus.EventBus;
import ru.evillcat.common.message.ChatMessage;
import ru.evillcat.common.message.MessageType;
import ru.evillcat.server.event.UserDisconnectedEvent;
import ru.evillcat.server.messenger.Messenger;
import ru.evillcat.server.user.User;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserHandlerPool {

    private final EventBus eventBus;
    private final Messenger messenger;
    private final Comparator<ChatMessage> chatMessageComparator;
    private final List<MessageHandler> handlers;

    public UserHandlerPool(Messenger messenger, EventBus eventBus) {
        this.messenger = messenger;
        this.eventBus = eventBus;
        handlers = new CopyOnWriteArrayList<>();
        chatMessageComparator = Comparator.comparing(ChatMessage::getZonedDateTime);
    }

    public void startHandlingInputStreams() {
        new Thread(() -> {
            while (true) {
                List<ChatMessage> chatMessages = new ArrayList<>();
                for (MessageHandler messageHandler : handlers) {
                    if (messageHandler.hasLineOnStream()) {
                        ChatMessage chatMessage = messageHandler.getChatMessage();
                        if (chatMessage.getMessageType().equals(MessageType.USER_DISCONNECT)) {
                            publishDisconnectionEvent(messageHandler.getOwner());
                            removeHandler(messageHandler);
                        } else {
                            chatMessages.add(chatMessage);
                        }
                    }
                }
                chatMessages.sort(chatMessageComparator);
                for (ChatMessage chatMessage : chatMessages) {
                    messenger.putAtMessagesQueue(chatMessage);
                }
            }
        }).start();
    }

    public void addHandler(InputStream inputStream, User owner) {
        MessageHandler messageHandler = new MessageHandler(inputStream, owner);
        handlers.add(messageHandler);
    }

    private void publishDisconnectionEvent(User user) {
        new Thread(() -> eventBus.publish(new UserDisconnectedEvent(user))).start();
    }

    private void removeHandler(MessageHandler messageHandler) {
        handlers.remove(messageHandler);
    }
}

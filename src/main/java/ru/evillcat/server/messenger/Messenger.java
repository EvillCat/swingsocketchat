package ru.evillcat.server.messenger;

import com.google.gson.Gson;
import ru.evillcat.common.event.bus.EventBus;
import ru.evillcat.common.event.bus.EventListener;
import ru.evillcat.common.message.ChatMessage;
import ru.evillcat.server.event.UserDisconnectedEvent;
import ru.evillcat.server.user.User;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Messenger {

    private static final int MESSAGES_QUEUE_CAPACITY = 500;
    private final Map<User, PrintWriter> writers;
    private final BlockingQueue<ChatMessage> messagesQueue;
    private final Gson gson;

    public Messenger(EventBus eventBus) {
        eventBus.subscribe(UserDisconnectedEvent.class, new RemoveOutputOnUserDisconnect());
        writers = new HashMap<>();
        messagesQueue = new ArrayBlockingQueue<>(MESSAGES_QUEUE_CAPACITY, true);
        gson = new Gson();
    }

    public void putAtMessagesQueue(ChatMessage message) {
        messagesQueue.add(message);
    }

    public void startConsumingQueue() {
        new Thread(() -> {
            try {
                while (true) {
                    ChatMessage chatMessage = messagesQueue.take();
                    String serializedChatMessage = gson.toJson(chatMessage);
                    sendToAll(serializedChatMessage);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void sendToAll(String message) {
        Collection<PrintWriter> printWriters = writers.values();
        for (PrintWriter out : printWriters) {
            out.println(message);
        }
    }

    public void addOutputStream(OutputStream outputStream, User user) {
        writers.put(user, new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)), true));
    }

    private void remove(User user) {
        writers.remove(user);
    }

    private class RemoveOutputOnUserDisconnect implements EventListener<UserDisconnectedEvent> {

        @Override
        public void update(UserDisconnectedEvent event) {
            remove(event.getUser());
        }
    }
}

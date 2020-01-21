package ru.evillcat.client.messenger;

import com.google.gson.Gson;
import ru.evillcat.client.event.SendMessageEvent;
import ru.evillcat.client.event.UserDisconnectEvent;
import ru.evillcat.common.event.bus.EventBus;
import ru.evillcat.common.event.bus.EventListener;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ClientMessenger {

    private final String name;
    private final Gson gson;
    private PrintWriter out;

    public ClientMessenger(String name, EventBus eventBus) {
        this.name = name;
        this.gson = new Gson();
        eventBus.subscribe(SendMessageEvent.class, new ViewMessageListener());
        eventBus.subscribe(UserDisconnectEvent.class, new UserDisconnectEventListener());
    }

    public void setOut(OutputStream outputStream) {
        this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)), true);
        out.println(name);
    }

    private class ViewMessageListener implements EventListener<SendMessageEvent> {

        @Override
        public void update(SendMessageEvent event) {
            new Thread(() -> {
                String message = gson.toJson(event.getMessage(name));
                out.println(message);
            }).start();
        }
    }

    private class UserDisconnectEventListener implements EventListener<UserDisconnectEvent> {

        @Override
        public void update(UserDisconnectEvent event) {
            String message = gson.toJson(event.getChatMessage());
            out.println(message);
        }
    }
}

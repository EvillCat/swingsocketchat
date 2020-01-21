package ru.evillcat.server.event.chat;

import ru.evillcat.common.event.bus.Event;
import ru.evillcat.common.event.bus.EventBus;
import ru.evillcat.common.event.bus.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatEventBus implements EventBus {

    private final Map<Class<? extends Event>, List<EventListener>> serverChatListeners;

    public ChatEventBus() {
        serverChatListeners = new ConcurrentHashMap<>();
    }

    //Дублируется с таким же методом в ru.cft.focusstart.client.event.chat.ChatClientEventBus
    //т.к. формально это два разных приложения.
    @SuppressWarnings("DuplicatedCode")
    @Override
    public <T extends Event> void subscribe(Class<T> eventType, EventListener<T> listener) {
        List<EventListener> listeners = serverChatListeners.get(eventType);
        if (listeners == null) {
            serverChatListeners.computeIfAbsent(eventType, aClass -> {
                List<EventListener> newListenersList = new ArrayList<>();
                newListenersList.add(listener);
                return newListenersList;
            });
        } else {
            listeners.add(listener);
        }
    }

    @Override
    public void publish(Event event) {
        List<EventListener> listeners = serverChatListeners.get(event.getClass());
        for (EventListener listener : listeners) {
            listener.update(event);
        }
    }
}

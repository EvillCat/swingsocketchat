package ru.evillcat.common.event.bus;

public interface EventBus  {

    <T extends Event> void subscribe(Class<T> eventType, EventListener<T> listener);

    void publish(Event event);
}

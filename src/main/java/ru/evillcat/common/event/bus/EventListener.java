package ru.evillcat.common.event.bus;

public interface EventListener<T extends Event> {

    void update(T event);
}

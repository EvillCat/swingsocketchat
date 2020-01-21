package ru.evillcat.client.event;

import ru.evillcat.common.event.bus.Event;

public class UserNotAuthorisedEvent extends Event {

    private final String message;

    public UserNotAuthorisedEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

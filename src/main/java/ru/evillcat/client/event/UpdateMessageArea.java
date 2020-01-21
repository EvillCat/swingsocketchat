package ru.evillcat.client.event;

import ru.evillcat.common.event.bus.Event;

public class UpdateMessageArea extends Event {

    private final String message;

    public UpdateMessageArea(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

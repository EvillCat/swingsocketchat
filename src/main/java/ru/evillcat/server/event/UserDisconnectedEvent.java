package ru.evillcat.server.event;

import ru.evillcat.common.event.bus.Event;
import ru.evillcat.server.user.User;

public class UserDisconnectedEvent extends Event {

    private final User user;

    public UserDisconnectedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

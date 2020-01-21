package ru.evillcat.client.event;

import ru.evillcat.common.event.bus.Event;

import java.util.List;

public class UpdateUsersListEvent extends Event {

    private final List<String> users;

    public UpdateUsersListEvent(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }
}

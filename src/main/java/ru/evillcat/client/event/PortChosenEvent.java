package ru.evillcat.client.event;

import ru.evillcat.common.event.bus.Event;

public class PortChosenEvent extends Event {

    private final String port;

    public PortChosenEvent(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }
}

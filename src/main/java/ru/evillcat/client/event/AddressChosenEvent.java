package ru.evillcat.client.event;

import ru.evillcat.common.event.bus.Event;

public class AddressChosenEvent extends Event {

    private final String address;

    public AddressChosenEvent(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}

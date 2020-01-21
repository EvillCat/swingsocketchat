package ru.evillcat.client.event;

import ru.evillcat.common.event.bus.Event;

public class NicknameChosenEvent extends Event {

    private final String nickname;

    public NicknameChosenEvent(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}

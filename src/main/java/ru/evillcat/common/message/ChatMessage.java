package ru.evillcat.common.message;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class ChatMessage {

    private final String name;
    private final String text;
    private final long epochSec;
    private final String timeZoneId;
    private final MessageType messageType;
    private List<String> userNames;

    public ChatMessage(String name, String text, MessageType messageType) {
        this.name = name;
        this.text = text;
        this.messageType = messageType;
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        epochSec = zonedDateTime.toEpochSecond();
        timeZoneId = zonedDateTime.getZone().getId();
    }

    public ChatMessage(String name, String text, MessageType messageType, List<String> userNames) {
        this(name, text, messageType);
        this.userNames = userNames;
    }

    public String getMessageAsString() {
        Instant instant = Instant.ofEpochSecond(epochSec);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of(timeZoneId));
        return DateUtil.format.format(zonedDateTime) + " " + name + ": " + text;
    }

    public String getTextMessage() {
        return text;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public ZonedDateTime getZonedDateTime() {
        Instant instant = Instant.ofEpochSecond(epochSec);
        return ZonedDateTime.ofInstant(instant, ZoneId.of(timeZoneId));
    }

    public List<String> getUserNames() {
        return userNames;
    }
}

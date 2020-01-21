package ru.evillcat.server.handler;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.evillcat.common.message.ChatMessage;
import ru.evillcat.server.properties.PropertiesReader;
import ru.evillcat.server.user.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MessageHandler {

    private static final Logger log = LoggerFactory.getLogger(PropertiesReader.class);
    private final Gson gson;
    private final BufferedReader bufferedReader;
    private final User owner;

    public MessageHandler(InputStream inputStream, User owner) {
        this.owner = owner;
        gson = new Gson();
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public boolean hasLineOnStream() {
        try {
            return bufferedReader.ready();
        } catch (IOException e) {
            log.error("Ошибка проверки готовности потока для чтения для: " + owner.getName(), e.getCause());
        }
        return false;
    }

    public ChatMessage getChatMessage() {
        try {
            return gson.fromJson(bufferedReader.readLine(), ChatMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getOwner() {
        return owner;
    }
}

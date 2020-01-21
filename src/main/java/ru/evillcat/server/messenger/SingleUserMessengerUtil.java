package ru.evillcat.server.messenger;

import com.google.gson.Gson;
import ru.evillcat.common.message.ChatMessage;
import ru.evillcat.common.message.MessageType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SingleUserMessengerUtil {

    private static final String REPLACE_REGEX = "<>";
    private static final Gson gson = new Gson();

    private SingleUserMessengerUtil() {
    }

    public static void send(Socket socket, String nickname, String text, MessageType messageType) throws IOException {
        PrintWriter writer = new PrintWriter(
                new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        String message = text.replaceAll(REPLACE_REGEX, nickname);
        String parsedChatMessage = gson.toJson(new ChatMessage(" ", message, messageType));
        writer.println(parsedChatMessage);
    }

    public static void send(Socket socket, MessageType messageType) throws IOException {
        send(socket, "", "", messageType);
    }
}

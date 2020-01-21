package ru.evillcat.server.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.evillcat.server.UserConnectionManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connector {

    private static final Logger log = LoggerFactory.getLogger(Connector.class);
    private final UserConnectionManager userConnectionManager;
    private final int port;

    public Connector(UserConnectionManager userConnectionManager, int port) {
        this.userConnectionManager = userConnectionManager;
        this.port = port;
    }

    public void startHandlingConnection() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    new Thread(() -> userConnectionManager.onNewUserConnected(socket)).start();
                } catch (IOException e) {
                    log.error("Ошибка подключения по сокету", e.getCause());
                }
            }
        }).start();
    }
}

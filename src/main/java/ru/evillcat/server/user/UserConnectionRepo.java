package ru.evillcat.server.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserConnectionRepo {

    private static final Logger log = LoggerFactory.getLogger(UserConnectionRepo.class);
    private final Map<User, Socket> usersConnected;

    public UserConnectionRepo() {
        usersConnected = new HashMap<>();
    }

    public void addConnection(User user, Socket socket) {
        usersConnected.put(user, socket);
    }

    public void removeConnection(User user) {
        try {
            usersConnected.get(user).close();
        } catch (IOException e) {
            log.error("Исключение при попытке закрыть сокет во время удаления клиента из userConnectionRepo",
                    e.getCause());
        }
        usersConnected.remove(user);
    }

    public Set<User> getUsers() {
        return usersConnected.keySet();
    }
}

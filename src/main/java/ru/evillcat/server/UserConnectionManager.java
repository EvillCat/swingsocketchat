package ru.evillcat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.evillcat.common.event.bus.EventBus;
import ru.evillcat.common.event.bus.EventListener;
import ru.evillcat.common.message.MessageType;
import ru.evillcat.server.authorization.Authorization;
import ru.evillcat.server.event.ServiceMessageEvent;
import ru.evillcat.server.event.UserDisconnectedEvent;
import ru.evillcat.server.handler.UserHandlerPool;
import ru.evillcat.server.messenger.Messenger;
import ru.evillcat.server.messenger.SingleUserMessengerUtil;
import ru.evillcat.server.user.User;
import ru.evillcat.server.user.UserConnectionRepo;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class UserConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(UserConnectionManager.class);

    private static final String NICKNAME_ALREADY_IN_USE_MESSAGE = "Никнейм <> уже используется.";
    private final static String ENTRANCE_MESSAGE = " присоединился.";
    private final static String EXIT_MESSAGE = " вышел.";

    private final Authorization authorization;
    private final UserConnectionRepo userConnectionRepo;
    private final UserHandlerPool userHandlerPool;
    private final Messenger messenger;
    private final EventBus eventBus;

    public UserConnectionManager(Authorization authorization,
                                 UserConnectionRepo userConnectionRepo,
                                 UserHandlerPool userHandlerPool,
                                 Messenger messenger,
                                 EventBus eventBus) {
        this.authorization = authorization;
        this.userConnectionRepo = userConnectionRepo;
        this.userHandlerPool = userHandlerPool;
        this.messenger = messenger;
        this.eventBus = eventBus;
        eventBus.subscribe(ServiceMessageEvent.class, new ServiceMessageSendListener());
        eventBus.subscribe(UserDisconnectedEvent.class, new UserDisconnectEventListener());
    }

    public void onNewUserConnected(Socket socket) {
        try {
            String nickname = new Scanner(socket.getInputStream()).nextLine();
            if (authorization.authorize(nickname)) {
                createNewUser(socket, nickname);
            } else {
                disconnectForDuplicateNickname(socket, nickname);
            }
        } catch (IOException e) {
            log.error("Socket getInputStream IOException during authorization.", e.getCause());
            try {
                socket.close();
            } catch (IOException ex) {
                log.error("Socket closing exception", ex.getCause());
            }
        }
    }

    private void createNewUser(Socket socket, String nickname) {
        try {
            User user = new User(nickname);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            userHandlerPool.addHandler(inputStream, user);
            messenger.addOutputStream(outputStream, user);
            SingleUserMessengerUtil.send(socket, MessageType.AUTHORIZED);
            userConnectionRepo.addConnection(user, socket);
            sendEntranceMessage(nickname);
        } catch (IOException e) {
            log.error("Socket getting stream IOException.", e.getCause());
        }
    }

    private void disconnectForDuplicateNickname(Socket socket, String nickname) {
        try {
            SingleUserMessengerUtil.send(socket, nickname, NICKNAME_ALREADY_IN_USE_MESSAGE, MessageType.NOT_AUTHORIZED);
            socket.close();
        } catch (IOException e) {
            log.error("Socket closing exception while disconnecting user.", e.getCause());
        }
    }

    private void sendEntranceMessage(String nickname) {
        List<String> names = getConnectedUsersNicknames(userConnectionRepo.getUsers());
        eventBus.publish(new ServiceMessageEvent(nickname, ENTRANCE_MESSAGE, names));
    }

    private List<String> getConnectedUsersNicknamesExcluding(String nickname, Set<User> users) {
        List<String> names = getConnectedUsersNicknames(users);
        names.remove(nickname);
        return names;
    }

    private List<String> getConnectedUsersNicknames(Set<User> users) {
        List<String> names = new ArrayList<>();
        for (User user : users) {
            names.add(user.getName());
        }
        return names;
    }

    private class ServiceMessageSendListener implements EventListener<ServiceMessageEvent> {

        @Override
        public void update(ServiceMessageEvent event) {
            messenger.putAtMessagesQueue(event.getChatMessage());
        }
    }

    private class UserDisconnectEventListener implements EventListener<UserDisconnectedEvent> {

        @Override
        public void update(UserDisconnectedEvent event) {
            User user = event.getUser();
            List<String> names = getConnectedUsersNicknamesExcluding(user.getName(), userConnectionRepo.getUsers());
            eventBus.publish(new ServiceMessageEvent(user.getName(), EXIT_MESSAGE, names));
            userConnectionRepo.removeConnection(user);
        }
    }
}

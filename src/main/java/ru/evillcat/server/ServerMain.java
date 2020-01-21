package ru.evillcat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.evillcat.common.event.bus.EventBus;
import ru.evillcat.server.authorization.Authorization;
import ru.evillcat.server.connector.Connector;
import ru.evillcat.server.event.chat.ChatEventBus;
import ru.evillcat.server.handler.UserHandlerPool;
import ru.evillcat.server.messenger.Messenger;
import ru.evillcat.server.properties.PropertiesReader;
import ru.evillcat.server.user.UserConnectionRepo;

import java.io.IOException;

public class ServerMain {

    private static final Logger log = LoggerFactory.getLogger(ServerMain.class);
    private static final String PORT_PROPERTY_NAME = "port";

    public static void main(String[] args) throws IOException {

        PropertiesReader propertiesReader = new PropertiesReader();
        EventBus eventBus = new ChatEventBus();

        Messenger messenger = new Messenger(eventBus);
        messenger.startConsumingQueue();

        UserHandlerPool userHandlerPool = new UserHandlerPool(messenger, eventBus);
        userHandlerPool.startHandlingInputStreams();

        UserConnectionRepo userConnectionRepo = new UserConnectionRepo();

        Authorization authorization = new Authorization(userConnectionRepo);

        UserConnectionManager userConnectionManager
                = new UserConnectionManager(authorization, userConnectionRepo, userHandlerPool, messenger, eventBus);
        int port = 0;
        try {
            port = Integer.parseInt(propertiesReader.getProperty(PORT_PROPERTY_NAME));
        } catch (NumberFormatException ex) {
            log.error("Ошибка парсинга порта. Значение проперти: " + port);

            System.exit(1);
        }

        Connector connector = new Connector(userConnectionManager, port);
        connector.startHandlingConnection();
    }
}

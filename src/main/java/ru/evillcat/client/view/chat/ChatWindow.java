package ru.evillcat.client.view.chat;

import ru.evillcat.client.event.UserDisconnectEvent;
import ru.evillcat.client.view.JFrameUtils;
import ru.evillcat.client.view.chat.elements.ChatMessagesArea;
import ru.evillcat.client.view.chat.elements.MessageField;
import ru.evillcat.client.view.chat.elements.UsersBar;
import ru.evillcat.common.event.bus.EventBus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatWindow {

    private final EventBus eventBus;
    private JFrame mainFrame;
    private JScrollPane usersScrollPane;
    private JScrollPane messagesBlock;

    public ChatWindow(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void startView() {

        MessageField messageField = new MessageField(eventBus);
        ChatMessagesArea chatMessagesArea = new ChatMessagesArea(eventBus);
        UsersBar usersBar = new UsersBar(eventBus);

        createUsersPane(usersBar.getUsersPanel());
        createMessagesBlock(chatMessagesArea.getMessageArea());

        mainFrame = new JFrame("Chat");
        mainFrame.setPreferredSize(new Dimension(800, 600));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(usersScrollPane, BorderLayout.EAST);
        mainFrame.add(messageField.getTextField(), BorderLayout.SOUTH);
        mainFrame.add(messagesBlock, BorderLayout.CENTER);
        addCloseFrameListener();
        mainFrame.pack();
        mainFrame.setResizable(true);
        JFrameUtils.setCenterLocation(mainFrame);
        mainFrame.setVisible(true);
    }

    private void createUsersPane(JPanel usersPanel) {
        usersScrollPane = new JScrollPane(usersPanel);
    }

    private void createMessagesBlock(JTextArea messageArea) {
        messagesBlock = new JScrollPane(messageArea);
    }

    private void addCloseFrameListener() {
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Thread(() -> eventBus.publish(new UserDisconnectEvent())).start();
            }
        });
    }
}

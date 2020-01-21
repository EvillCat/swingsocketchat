package ru.evillcat.client.view.chat.elements;


import ru.evillcat.client.event.UpdateUsersListEvent;
import ru.evillcat.client.view.JFrameUtils;
import ru.evillcat.common.event.bus.EventBus;
import ru.evillcat.common.event.bus.EventListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UsersBar {

    private final JPanel usersPanel;

    public UsersBar(EventBus eventBus) {
        usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
        usersPanel.setMinimumSize(new Dimension(100, 100));
        usersPanel.setPreferredSize(new Dimension(100, 350));
        usersPanel.setMaximumSize(new Dimension(100, JFrameUtils.SCREEN_DIMENSION_HEIGHT));
        eventBus.subscribe(UpdateUsersListEvent.class, new UserBarUpdateEventListener());
    }

    public JPanel getUsersPanel() {
        return usersPanel;
    }

    private void createUserPanel(List<String> users) {
        usersPanel.removeAll();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
        for (String user : users) {
            usersPanel.add(new JLabel(user));
        }
        usersPanel.revalidate();
        usersPanel.repaint();
    }

    private class UserBarUpdateEventListener implements EventListener<UpdateUsersListEvent> {

        @Override
        public void update(UpdateUsersListEvent event) {
            createUserPanel(event.getUsers());
        }
    }
}

package ru.evillcat.client.view.window;

import ru.evillcat.client.event.AddressChosenEvent;
import ru.evillcat.client.event.NicknameChosenEvent;
import ru.evillcat.client.event.PortChosenEvent;
import ru.evillcat.client.view.JFrameUtils;
import ru.evillcat.common.event.bus.EventBus;

import javax.swing.*;
import java.awt.*;

public class NameAndConnectionWindow {

    private final EventBus eventBus;

    private JFrame frame;
    private JTextField nicknameField;
    private JTextField addressField;
    private JTextField portField;
    private JButton chooseButton;

    public NameAndConnectionWindow(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void startView(String message) {

        nicknameField = new JTextField(10);
        addressField = new JTextField(10);
        portField = new JTextField(5);
        chooseButton = new JButton("Выбрать");
        setSendButtonListener();

        JPanel choosePanel = new JPanel();
        choosePanel.setLayout(new GridLayout(2, 3));
        choosePanel.add(new JLabel("Псевдоним:"));
        choosePanel.add(new JLabel("Адрес"));
        choosePanel.add(new JLabel("Порт"));
        choosePanel.add(nicknameField);
        choosePanel.add(addressField);
        choosePanel.add(portField);

        frame = new JFrame("Чат: выбор ника");
        frame.setLayout(new BorderLayout());
        frame.add(new JLabel(message), BorderLayout.NORTH);
        frame.add(choosePanel, BorderLayout.CENTER);
        frame.add(chooseButton, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(true);
        JFrameUtils.setCenterLocation(frame);
        frame.setVisible(true);
    }

    private void setSendButtonListener() {
        chooseButton.addActionListener(actionEvent -> sendValuesFromForms());
    }

    private void sendValuesFromForms() {
        String nickname = nicknameField.getText();
        String addressText = addressField.getText();
        String portText = portField.getText();
        if (!nickname.isEmpty() && !addressText.isEmpty() && !portText.isEmpty()) {
            eventBus.publish(new NicknameChosenEvent(nickname));
            eventBus.publish(new AddressChosenEvent(addressText));
            eventBus.publish(new PortChosenEvent(portText));
        }
    }

    public void dropView() {
        frame.setVisible(false);
        frame = null;
    }
}

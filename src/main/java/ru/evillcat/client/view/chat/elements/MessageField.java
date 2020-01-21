package ru.evillcat.client.view.chat.elements;

import ru.evillcat.client.event.SendMessageEvent;
import ru.evillcat.common.event.bus.EventBus;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MessageField {

    private final EventBus eventBus;

    private final JPanel textEntryPanel;
    private final JTextField textField;
    private final JButton sendButton;

    public MessageField(EventBus eventBus) {
        this.eventBus = eventBus;
        textField = new JTextField(2);
        sendButton = new JButton("Отправить");
        textEntryPanel = new JPanel();
        textEntryPanel.setLayout(new BoxLayout(textEntryPanel, BoxLayout.X_AXIS));
        textEntryPanel.add(textField);
        textEntryPanel.add(sendButton);
        setSendButtonListener();
        setTextFieldKeyListener();
    }

    private void setSendButtonListener() {
        sendButton.addActionListener(actionEvent -> sendMessage());
    }

    private void setTextFieldKeyListener() {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
    }

    private void sendMessage() {
        String message = textField.getText();
        if (!message.isEmpty()) {
            eventBus.publish(new SendMessageEvent(message));
        }
        textField.grabFocus();
        textField.setText("");
    }

    public JPanel getTextField() {
        return textEntryPanel;
    }
}

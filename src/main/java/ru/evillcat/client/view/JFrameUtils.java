package ru.evillcat.client.view;

import javax.swing.*;
import java.awt.*;

public class JFrameUtils {

    private static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int SCREEN_DIMENSION_HEIGHT = SCREEN_DIMENSION.height;

    private JFrameUtils() {
    }

    public static void setCenterLocation(JFrame frame) {
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (SCREEN_DIMENSION.width - w) / 2;
        int y = (SCREEN_DIMENSION.height - h) / 2;
        frame.setLocation(x, y);
    }
}

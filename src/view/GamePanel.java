package view;

import util.*;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public GamePanel() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BackgroundRenderer.renderGameBackground(g, this);
    }
}

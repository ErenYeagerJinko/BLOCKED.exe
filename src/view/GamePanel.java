package view;

import util.BackgroundRenderer;

import javax.swing.JPanel;
import java.awt.Graphics;

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

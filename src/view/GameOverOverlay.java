package view;

import util.*;
import javax.swing.*;
import java.awt.*;

public class GameOverOverlay extends JPanel {
    private GamePanel gamePanel;
    private JButton restartButton;
    private JButton menuButton;
    private JLabel scoreLabel;
    private JLabel bestScoreLabel;

    public GameOverOverlay() {
        this(null);
    }

    public GameOverOverlay(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setOpaque(false);
    }

    public void updateDisplay(int finalScore, int bestScore, boolean victory) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BackgroundRenderer.renderBackground(g, this);
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}

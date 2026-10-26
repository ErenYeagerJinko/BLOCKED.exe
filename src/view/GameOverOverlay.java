package view;

import util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOverOverlay extends JPanel {
    private GamePanel gamePanel;
    private JButton restartButton;
    private JButton menuButton;
    private JLabel titleLabel;
    private JLabel scoreLabel;
    private JLabel bestScoreLabel;

    public GameOverOverlay() {
        this(null);
    }

    public GameOverOverlay(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        initUI();
    }

    private void initUI() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel modalCard = new JPanel();
        modalCard.setLayout(new BoxLayout(modalCard, BoxLayout.Y_AXIS));
        modalCard.setBackground(new Color(24, 24, 38, 240));
        modalCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 90, 140), 2, true),
                BorderFactory.createEmptyBorder(25, 35, 25, 35)
        ));
        modalCard.setPreferredSize(new Dimension(360, 340));
        modalCard.setMaximumSize(new Dimension(360, 340));

        titleLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
        titleLabel.setFont(UIButtonRenderer.getFont(Font.BOLD, 26f));
        titleLabel.setForeground(new Color(255, 80, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel = new JLabel("Final Score:  0", SwingConstants.CENTER);
        scoreLabel.setFont(UIButtonRenderer.getFont(Font.BOLD, 18f));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bestScoreLabel = new JLabel("Best Score:  0", SwingConstants.CENTER);
        bestScoreLabel.setFont(UIButtonRenderer.getFont(Font.PLAIN, 15f));
        bestScoreLabel.setForeground(new Color(255, 215, 0));
        bestScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        restartButton = UIButtonRenderer.createButton("Play Again", UIButtonRenderer.RESTART_BUTTON, 200, 42);
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuButton = UIButtonRenderer.createButton("Main Menu", UIButtonRenderer.MENU_BUTTON, 200, 42);
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        modalCard.add(titleLabel);
        modalCard.add(Box.createRigidArea(new Dimension(0, 16)));
        modalCard.add(scoreLabel);
        modalCard.add(Box.createRigidArea(new Dimension(0, 6)));
        modalCard.add(bestScoreLabel);
        modalCard.add(Box.createRigidArea(new Dimension(0, 24)));
        modalCard.add(restartButton);
        modalCard.add(Box.createRigidArea(new Dimension(0, 10)));
        modalCard.add(menuButton);

        add(modalCard);
    }

    public void updateDisplay(int finalScore, int bestScore, boolean victory) {
        if (victory) {
            titleLabel.setText("VICTORY!");
            titleLabel.setForeground(new Color(80, 225, 120));
        } else {
            titleLabel.setText("GAME OVER");
            titleLabel.setForeground(new Color(255, 80, 80));
        }
        scoreLabel.setText("Final Score:  " + finalScore);
        bestScoreLabel.setText("Best Score:  " + bestScore);
    }

    public JButton getRestartButton() {
        return restartButton;
    }

    public JButton getMenuButton() {
        return menuButton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BackgroundRenderer.renderBackground(g, this);
        g.setColor(new Color(0, 0, 0, 175));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}

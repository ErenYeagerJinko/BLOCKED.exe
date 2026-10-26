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
        modalCard.setPreferredSize(new Dimension(360, 320));
        modalCard.setMaximumSize(new Dimension(360, 320));

        titleLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(255, 80, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel = new JLabel("Final Score:  0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bestScoreLabel = new JLabel("Best Score:  0", SwingConstants.CENTER);
        bestScoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        bestScoreLabel.setForeground(new Color(255, 215, 0));
        bestScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        restartButton = new JButton("Play Again");
        restartButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        restartButton.setBackground(new Color(60, 140, 240));
        restartButton.setForeground(Color.WHITE);
        restartButton.setFocusPainted(false);
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.setPreferredSize(new Dimension(200, 38));
        restartButton.setMaximumSize(new Dimension(200, 38));
        restartButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        menuButton = new JButton("Main Menu");
        menuButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        menuButton.setBackground(new Color(50, 50, 70));
        menuButton.setForeground(Color.WHITE);
        menuButton.setFocusPainted(false);
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButton.setPreferredSize(new Dimension(200, 34));
        menuButton.setMaximumSize(new Dimension(200, 34));
        menuButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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

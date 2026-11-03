package view;

import util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PauseOverlay extends JPanel {
    private final GamePanel gamePanel;
    private JButton resumeButton;
    private JButton restartButton;
    private JButton menuButton;

    public PauseOverlay(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        initUI();
        // Swallow clicks so nothing reaches the board while paused
        addMouseListener(new MouseAdapter() { });
        addMouseMotionListener(new MouseMotionAdapter() { });
    }

    private void initUI() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(24, 24, 38, 240));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 90, 140), 2, true),
                BorderFactory.createEmptyBorder(25, 35, 25, 35)
        ));

        JLabel title = new JLabel("PAUSED", SwingConstants.CENTER);
        title.setFont(UIButtonRenderer.getFont(Font.BOLD, 28f));
        title.setForeground(new Color(255, 215, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        resumeButton = UIButtonRenderer.createButton("Resume", UIButtonRenderer.GREEN_BUTTON, 220, 44);
        restartButton = UIButtonRenderer.createButton("Restart", UIButtonRenderer.RESTART_BUTTON, 220, 44);
        menuButton = UIButtonRenderer.createButton("Main Menu", UIButtonRenderer.MENU_BUTTON, 220, 44);

        resumeButton.addActionListener(e -> gamePanel.resumeGame());
        restartButton.addActionListener(e -> gamePanel.restartGame());
        menuButton.addActionListener(e -> gamePanel.goToMenu());

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 22)));
        for (JButton button : new JButton[] {resumeButton, restartButton, menuButton}) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(button);
            card.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        add(card);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    public JButton getResumeButton() {
        return resumeButton;
    }

    public JButton getRestartButton() {
        return restartButton;
    }

    public JButton getMenuButton() {
        return menuButton;
    }
}

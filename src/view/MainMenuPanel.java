package view;

import audio.*;
import enums.*;
import persistence.*;
import util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main menu navigation panel providing options to start game modes,
 * view how-to-play instructions, open top score rankings, check profile, or exit.
 */
public class MainMenuPanel extends JPanel {

    private static final int BUTTON_WIDTH = 280;
    private static final int BUTTON_HEIGHT = 44;

    private GameFrame frame;
    private DataPersistenceManager persistenceManager;

    private JButton classicButton;
    private JButton timeAttackButton;
    private JButton instructionsButton;
    private JButton leaderboardButton;
    private JButton profileButton;
    private JButton exitButton;

    /**
     * Default no-arg constructor.
     */
    public MainMenuPanel() {
        this(null, null);
    }

    /**
     * Constructor with GameFrame reference.
     *
     * @param frame parent GameFrame
     */
    public MainMenuPanel(GameFrame frame) {
        this(frame, null);
    }

    /**
     * Full constructor with GameFrame and DataPersistenceManager.
     *
     * @param frame parent GameFrame
     * @param persistenceManager persistence manager
     */
    public MainMenuPanel(GameFrame frame, DataPersistenceManager persistenceManager) {
        this.frame = frame;
        this.persistenceManager = persistenceManager;
        initUI();
        wireActions();
        Logger.getLogger().log("MainMenuPanel initialized");
    }

    /**
     * Constructs and arranges the visual menu layout.
     */
    private void initUI() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel menuCard = new JPanel();
        menuCard.setLayout(new BoxLayout(menuCard, BoxLayout.Y_AXIS));
        menuCard.setBackground(new Color(20, 22, 34, 230));
        menuCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 100, 140), 2, true),
                BorderFactory.createEmptyBorder(24, 32, 24, 32)
        ));
        menuCard.setPreferredSize(new Dimension(360, 520));
        menuCard.setMaximumSize(new Dimension(360, 520));

        JLabel titleLabel = new JLabel("BLOCKED.exe");
        titleLabel.setFont(UIButtonRenderer.getFont(Font.BOLD, 26f));
        titleLabel.setForeground(new Color(255, 214, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Polyomino Puzzle Challenge");
        subtitleLabel.setFont(UIButtonRenderer.getFont(Font.PLAIN, 12f));
        subtitleLabel.setForeground(new Color(190, 200, 220));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        classicButton = UIButtonRenderer.createButton("CLASSIC MODE", UIButtonRenderer.GREEN_BUTTON, BUTTON_WIDTH, BUTTON_HEIGHT);
        timeAttackButton = UIButtonRenderer.createButton("TIME ATTACK (180s)", UIButtonRenderer.YELLOW_BUTTON, BUTTON_WIDTH, BUTTON_HEIGHT);
        instructionsButton = UIButtonRenderer.createButton("INSTRUCTIONS", UIButtonRenderer.BLUE_BUTTON, BUTTON_WIDTH, BUTTON_HEIGHT);
        leaderboardButton = UIButtonRenderer.createButton("LEADERBOARD", UIButtonRenderer.NEUTRAL_BUTTON, BUTTON_WIDTH, BUTTON_HEIGHT);
        profileButton = UIButtonRenderer.createButton("MY PROFILE", UIButtonRenderer.MENU_BUTTON, BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton = UIButtonRenderer.createButton("EXIT GAME", UIButtonRenderer.RED_BUTTON, BUTTON_WIDTH, BUTTON_HEIGHT);

        centerButton(classicButton);
        centerButton(timeAttackButton);
        centerButton(instructionsButton);
        centerButton(leaderboardButton);
        centerButton(profileButton);
        centerButton(exitButton);

        menuCard.add(Box.createVerticalStrut(6));
        menuCard.add(titleLabel);
        menuCard.add(Box.createVerticalStrut(4));
        menuCard.add(subtitleLabel);
        menuCard.add(Box.createVerticalStrut(22));

        menuCard.add(classicButton);
        menuCard.add(Box.createVerticalStrut(10));
        menuCard.add(timeAttackButton);
        menuCard.add(Box.createVerticalStrut(10));
        menuCard.add(instructionsButton);
        menuCard.add(Box.createVerticalStrut(10));
        menuCard.add(leaderboardButton);
        menuCard.add(Box.createVerticalStrut(10));
        menuCard.add(profileButton);
        menuCard.add(Box.createVerticalStrut(10));
        menuCard.add(exitButton);

        add(menuCard);
    }

    /**
     * Aligns a button along the X center axis.
     *
     * @param button target button
     */
    private void centerButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Attaches interactive event listeners to all menu buttons.
     */
    private void wireActions() {
        classicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.getInstance().playSelect();
                if (frame != null) {
                    frame.startNewGame(GameMode.CLASSIC);
                }
            }
        });

        timeAttackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.getInstance().playSelect();
                if (frame != null) {
                    frame.startNewGame(GameMode.TIME_ATTACK);
                }
            }
        });

        instructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.getInstance().playSelect();
                if (frame != null) {
                    frame.showScreen("INSTRUCTIONS");
                }
            }
        });

        leaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.getInstance().playSelect();
                if (frame != null) {
                    frame.showScreen("LEADERBOARD");
                }
            }
        });

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.getInstance().playSelect();
                if (frame != null) {
                    frame.showScreen("PROFILE");
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.getInstance().playSelect();
                Logger.getLogger().log("User initiated exit from MainMenuPanel.");
                System.exit(0);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BackgroundRenderer.renderBackground(g, this);
    }

    public JButton getClassicButton() {
        return classicButton;
    }

    public JButton getTimeAttackButton() {
        return timeAttackButton;
    }

    public JButton getInstructionsButton() {
        return instructionsButton;
    }

    public JButton getLeaderboardButton() {
        return leaderboardButton;
    }

    public JButton getProfileButton() {
        return profileButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}

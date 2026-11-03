package view;

import model.*;
import util.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class UserProfileDialog extends JDialog {
    private JLabel avatarLabel;
    private JLabel usernameLabel;
    private JTextArea aboutMeArea;
    private JLabel highestScoreLabel;
    private JLabel cumulativeScoreLabel;
    private JLabel timePlayedLabel;
    private JLabel statsSummaryLabel;
    private JButton closeButton;

    public UserProfileDialog(Frame owner, UserProfile profile) {
        super(owner, "Player Profile", true);
        initUI(profile);
        Logger.getLogger().log("UserProfileDialog opened for: " + (profile != null ? profile.getUsername() : "null"));
    }

    private void initUI(UserProfile profile) {
        setLayout(new BorderLayout(10, 10));
        setSize(460, 540);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                BackgroundRenderer.renderBackground(g, this);
                g.setColor(new Color(20, 20, 30, 210));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
        mainPanel.setOpaque(false);

        avatarLabel = new JLabel();
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarLabel.setPreferredSize(new Dimension(96, 96));
        avatarLabel.setMaximumSize(new Dimension(96, 96));
        avatarLabel.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 110), 2, true));
        loadAvatar(profile != null ? profile.getProfilePicturePath() : null);

        usernameLabel = new JLabel(profile != null ? profile.getUsername() : "Unknown");
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setFont(UIButtonRenderer.getFont(Font.BOLD, 22f));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 12, 0));

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(new Color(42, 42, 58));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(65, 65, 90), 1),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));
        statsPanel.setPreferredSize(new Dimension(390, 130));
        statsPanel.setMaximumSize(new Dimension(390, 130));
        statsPanel.setMinimumSize(new Dimension(390, 130));
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        highestScoreLabel = createStatLabel("Highest Score:", profile != null ? String.valueOf(profile.getHighestScore()) : "0");
        cumulativeScoreLabel = createStatLabel("Cumulative Score:", profile != null ? String.valueOf(profile.getTotalCumulativeScore()) : "0");
        timePlayedLabel = createStatLabel("Time Played:", profile != null ? profile.getFormattedPlayTime() : "0h 0m");
        int games = profile != null ? profile.getGamesPlayed() : 0;
        int lines = profile != null ? profile.getLinesCleared() : 0;
        statsSummaryLabel = createStatLabel("Games / Lines Cleared:", games + " / " + lines);

        statsPanel.add(highestScoreLabel);
        statsPanel.add(cumulativeScoreLabel);
        statsPanel.add(timePlayedLabel);
        statsPanel.add(statsSummaryLabel);

        JLabel bioHeading = new JLabel("About Me");
        bioHeading.setAlignmentX(Component.CENTER_ALIGNMENT);
        bioHeading.setFont(UIButtonRenderer.getFont(Font.BOLD, 13f));
        bioHeading.setForeground(new Color(180, 180, 210));
        bioHeading.setBorder(BorderFactory.createEmptyBorder(10, 0, 4, 0));

        aboutMeArea = new JTextArea(profile != null && !profile.getAboutMe().isEmpty() ? profile.getAboutMe() : "No bio provided.");
        aboutMeArea.setEditable(false);
        aboutMeArea.setLineWrap(true);
        aboutMeArea.setWrapStyleWord(true);
        aboutMeArea.setFont(UIButtonRenderer.getFont(Font.PLAIN, 12f));
        aboutMeArea.setForeground(new Color(220, 220, 235));
        aboutMeArea.setBackground(new Color(24, 24, 34));
        aboutMeArea.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JScrollPane scrollPane = new JScrollPane(aboutMeArea);
        scrollPane.setMaximumSize(new Dimension(390, 75));
        scrollPane.setPreferredSize(new Dimension(390, 75));
        scrollPane.setMinimumSize(new Dimension(390, 75));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 1));

        mainPanel.add(avatarLabel);
        mainPanel.add(usernameLabel);
        mainPanel.add(statsPanel);
        mainPanel.add(bioHeading);
        mainPanel.add(scrollPane);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        closeButton = UIButtonRenderer.createButton("Close", UIButtonRenderer.BACK_BUTTON, 180, 40);
        closeButton.addActionListener(e -> {
            Logger.getLogger().log("UserProfileDialog closed");
            dispose();
        });
        buttonPanel.add(closeButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JLabel createStatLabel(String title, String value) {
        JLabel label = new JLabel(title + "  " + value);
        label.setFont(UIButtonRenderer.getFont(Font.PLAIN, 12f));
        label.setForeground(new Color(230, 230, 245));
        label.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void loadAvatar(String path) {
        if (path != null && !path.trim().isEmpty()) {
            File file = new File(path);
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(96, 96, Image.SCALE_SMOOTH);
                avatarLabel.setIcon(new ImageIcon(img));
                Logger.getLogger().log("UserProfileDialog avatar image loaded: " + path);
                return;
            }
        }
        avatarLabel.setText("👤");
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setForeground(new Color(150, 150, 180));
        Logger.getLogger().log("UserProfileDialog avatar defaulted to placeholder emoji");
    }
}

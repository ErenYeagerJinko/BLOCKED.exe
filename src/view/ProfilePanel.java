package view;

import manager.*;
import model.*;
import util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ProfilePanel extends JPanel {
    private JLabel avatarLabel;
    private JLabel usernameLabel;
    private JTextArea aboutMeDisplay;
    private JLabel highestScoreLabel;
    private JLabel cumulativeScoreLabel;
    private JLabel timePlayedLabel;
    private JLabel statsLabel;
    private JButton playButton;
    private JButton editProfileButton;
    private JButton leaderboardButton;
    private JButton backButton;
    private ProfileManager profileManager;
    private GameFrame frame;

    public ProfilePanel() {
        this(null, null);
    }

    public ProfilePanel(GameFrame frame, ProfileManager profileManager) {
        this.frame = frame;
        this.profileManager = profileManager;
        initUI();
        Logger.getLogger().log("ProfilePanel initialized");
    }

    private void initUI() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(24, 24, 36, 235));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 120), 2, true),
                BorderFactory.createEmptyBorder(22, 28, 22, 28)
        ));
        card.setPreferredSize(new Dimension(500, 580));
        card.setMaximumSize(new Dimension(500, 580));

        JLabel titleLabel = new JLabel("PLAYER PROFILE");
        titleLabel.setFont(UIButtonRenderer.getFont(Font.BOLD, 22f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        avatarLabel = new JLabel("👤", SwingConstants.CENTER);
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        avatarLabel.setForeground(new Color(150, 150, 190));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarLabel.setPreferredSize(new Dimension(96, 96));
        avatarLabel.setMaximumSize(new Dimension(96, 96));
        avatarLabel.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 110), 2, true));

        usernameLabel = new JLabel("Guest");
        usernameLabel.setFont(UIButtonRenderer.getFont(Font.BOLD, 20f));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel statsBox = new JPanel();
        statsBox.setLayout(new BoxLayout(statsBox, BoxLayout.Y_AXIS));
        statsBox.setBackground(new Color(36, 36, 52));
        statsBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(65, 65, 95), 1),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));
        statsBox.setPreferredSize(new Dimension(430, 130));
        statsBox.setMaximumSize(new Dimension(430, 130));
        statsBox.setMinimumSize(new Dimension(430, 130));
        statsBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        highestScoreLabel = new JLabel("Highest Score:  0");
        styleStatLabel(highestScoreLabel);
        cumulativeScoreLabel = new JLabel("Cumulative Score:  0");
        styleStatLabel(cumulativeScoreLabel);
        timePlayedLabel = new JLabel("Time Played:  0h 0m");
        styleStatLabel(timePlayedLabel);
        statsLabel = new JLabel("Games / Lines Cleared:  0 / 0");
        styleStatLabel(statsLabel);

        statsBox.add(highestScoreLabel);
        statsBox.add(cumulativeScoreLabel);
        statsBox.add(timePlayedLabel);
        statsBox.add(statsLabel);

        JLabel bioTitle = new JLabel("About Me");
        bioTitle.setFont(UIButtonRenderer.getFont(Font.BOLD, 13f));
        bioTitle.setForeground(new Color(180, 180, 210));
        bioTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        aboutMeDisplay = new JTextArea("No bio provided.");
        aboutMeDisplay.setFont(UIButtonRenderer.getFont(Font.PLAIN, 12f));
        aboutMeDisplay.setForeground(new Color(220, 220, 235));
        aboutMeDisplay.setBackground(new Color(28, 28, 40));
        aboutMeDisplay.setEditable(false);
        aboutMeDisplay.setLineWrap(true);
        aboutMeDisplay.setWrapStyleWord(true);
        aboutMeDisplay.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JScrollPane bioScroll = new JScrollPane(aboutMeDisplay);
        bioScroll.setPreferredSize(new Dimension(430, 65));
        bioScroll.setMaximumSize(new Dimension(430, 65));
        bioScroll.setMinimumSize(new Dimension(430, 65));
        bioScroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        bioScroll.setBorder(BorderFactory.createLineBorder(new Color(65, 65, 95), 1));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        topRow.setOpaque(false);
        topRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton = UIButtonRenderer.createButton("Play", UIButtonRenderer.GREEN_BUTTON, 208, 40);
        leaderboardButton = UIButtonRenderer.createButton("Leaderboard", UIButtonRenderer.NEUTRAL_BUTTON, 208, 40);
        topRow.add(playButton);
        topRow.add(leaderboardButton);

        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        bottomRow.setOpaque(false);
        bottomRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        editProfileButton = UIButtonRenderer.createButton("Edit Bio", UIButtonRenderer.BLUE_BUTTON, 208, 40);
        backButton = UIButtonRenderer.createButton("Back", UIButtonRenderer.BACK_BUTTON, 208, 40);
        bottomRow.add(editProfileButton);
        bottomRow.add(backButton);

        buttonsPanel.add(topRow);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        buttonsPanel.add(bottomRow);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(avatarLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(usernameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(statsBox);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(bioTitle);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(bioScroll);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(buttonsPanel);

        add(card);
    }

    private void styleStatLabel(JLabel label) {
        label.setFont(UIButtonRenderer.getFont(Font.PLAIN, 12f));
        label.setForeground(new Color(230, 230, 245));
        label.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public void refreshProfile(UserProfile profile) {
        if (profile == null) {
            Logger.getLogger().log("ProfilePanel refresh skipped: null profile");
            return;
        }
        usernameLabel.setText(profile.getUsername());
        highestScoreLabel.setText("Highest Score:  " + profile.getHighestScore());
        cumulativeScoreLabel.setText("Cumulative Score:  " + profile.getTotalCumulativeScore());
        timePlayedLabel.setText("Time Played:  " + profile.getFormattedPlayTime());
        statsLabel.setText("Games / Lines Cleared:  " + profile.getGamesPlayed() + " / " + profile.getLinesCleared());
        aboutMeDisplay.setText(profile.getAboutMe() != null && !profile.getAboutMe().isEmpty() ? profile.getAboutMe() : "No bio provided.");
        loadAvatar(profile.getProfilePicturePath());
        Logger.getLogger().log("ProfilePanel refreshed for user: " + profile.getUsername());
    }

    private void loadAvatar(String path) {
        if (path != null && !path.trim().isEmpty()) {
            File f = new File(path);
            if (f.exists()) {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(92, 92, Image.SCALE_SMOOTH);
                avatarLabel.setIcon(new ImageIcon(img));
                avatarLabel.setText("");
                Logger.getLogger().log("ProfilePanel loaded avatar: " + path);
                return;
            }
        }
        avatarLabel.setIcon(null);
        avatarLabel.setText("👤");
        Logger.getLogger().log("ProfilePanel avatar defaulted to placeholder emoji");
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public JButton getEditProfileButton() {
        return editProfileButton;
    }

    public JButton getLeaderboardButton() {
        return leaderboardButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BackgroundRenderer.renderBackground(g, this);
    }
}

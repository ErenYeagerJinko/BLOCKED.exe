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
    }

    private void initUI() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(24, 24, 36, 235));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 120), 2, true),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        card.setPreferredSize(new Dimension(420, 560));
        card.setMaximumSize(new Dimension(420, 560));

        JLabel titleLabel = new JLabel("PLAYER PROFILE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
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
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel statsBox = new JPanel();
        statsBox.setLayout(new BoxLayout(statsBox, BoxLayout.Y_AXIS));
        statsBox.setBackground(new Color(36, 36, 52));
        statsBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(65, 65, 95), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        statsBox.setMaximumSize(new Dimension(360, 130));

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
        bioTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        bioTitle.setForeground(new Color(180, 180, 210));
        bioTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        aboutMeDisplay = new JTextArea("No bio provided.");
        aboutMeDisplay.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        aboutMeDisplay.setForeground(new Color(220, 220, 235));
        aboutMeDisplay.setBackground(new Color(28, 28, 40));
        aboutMeDisplay.setEditable(false);
        aboutMeDisplay.setLineWrap(true);
        aboutMeDisplay.setWrapStyleWord(true);
        aboutMeDisplay.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JScrollPane bioScroll = new JScrollPane(aboutMeDisplay);
        bioScroll.setPreferredSize(new Dimension(360, 70));
        bioScroll.setMaximumSize(new Dimension(360, 70));
        bioScroll.setBorder(BorderFactory.createLineBorder(new Color(65, 65, 95), 1));

        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        buttonsRow.setOpaque(false);

        editProfileButton = new JButton("Edit Profile");
        editProfileButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        editProfileButton.setPreferredSize(new Dimension(110, 32));

        leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        leaderboardButton.setPreferredSize(new Dimension(110, 32));

        backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.setPreferredSize(new Dimension(80, 32));

        buttonsRow.add(editProfileButton);
        buttonsRow.add(leaderboardButton);
        buttonsRow.add(backButton);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(avatarLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(usernameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(statsBox);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(bioTitle);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(bioScroll);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(buttonsRow);

        add(card);
    }

    private void styleStatLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(230, 230, 245));
        label.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
    }

    public void refreshProfile(UserProfile profile) {
        if (profile == null) {
            return;
        }
        usernameLabel.setText(profile.getUsername());
        highestScoreLabel.setText("Highest Score:  " + profile.getHighestScore());
        cumulativeScoreLabel.setText("Cumulative Score:  " + profile.getTotalCumulativeScore());
        timePlayedLabel.setText("Time Played:  " + profile.getFormattedPlayTime());
        statsLabel.setText("Games / Lines Cleared:  " + profile.getGamesPlayed() + " / " + profile.getLinesCleared());
        aboutMeDisplay.setText(profile.getAboutMe() != null && !profile.getAboutMe().isEmpty() ? profile.getAboutMe() : "No bio provided.");
        loadAvatar(profile.getProfilePicturePath());
    }

    private void loadAvatar(String path) {
        if (path != null && !path.trim().isEmpty()) {
            File f = new File(path);
            if (f.exists()) {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(92, 92, Image.SCALE_SMOOTH);
                avatarLabel.setIcon(new ImageIcon(img));
                avatarLabel.setText("");
                return;
            }
        }
        avatarLabel.setIcon(null);
        avatarLabel.setText("👤");
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

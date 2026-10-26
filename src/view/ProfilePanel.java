package view;

import manager.ProfileManager;
import model.UserProfile;
import util.BackgroundRenderer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Graphics;

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
    }

    public void refreshProfile(UserProfile profile) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BackgroundRenderer.renderBackground(g, this);
    }
}

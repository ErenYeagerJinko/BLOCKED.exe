package view;

import controller.*;
import enums.*;
import manager.*;
import model.*;
import util.*;
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private AuthManager authManager;
    private ProfileManager profileManager;
    private LeaderboardManager leaderboardManager;
    private ProfileController profileController;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private ProfilePanel profilePanel;
    private LeaderboardPanel leaderboardPanel;
    private GameOverOverlay gameOverOverlay;

    public GameFrame() {
        initManagers();
        initUI();
    }

    private void initManagers() {
        authManager = new AuthManager();
        profileManager = new ProfileManager();
        leaderboardManager = new LeaderboardManager();
        profileController = new ProfileController(this, profileManager, leaderboardManager);
    }

    private void initUI() {
        setTitle("BLOCKED.exe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setMinimumSize(new Dimension(650, 650));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this, authManager);
        registerPanel = new RegisterPanel(this, authManager);
        profilePanel = new ProfilePanel(this, profileManager);
        leaderboardPanel = new LeaderboardPanel(this, leaderboardManager, profileController);
        gameOverOverlay = new GameOverOverlay(null);

        wireNavigation();

        cardPanel.add(loginPanel, "LOGIN");
        cardPanel.add(registerPanel, "REGISTER");
        cardPanel.add(profilePanel, "PROFILE");
        cardPanel.add(leaderboardPanel, "LEADERBOARD");
        cardPanel.add(gameOverOverlay, "GAMEOVER");

        add(cardPanel);
        showScreen("LOGIN");
    }

    private void wireNavigation() {
        loginPanel.getSwitchToRegisterButton().addActionListener(e -> showScreen("REGISTER"));
        loginPanel.getBackButton().addActionListener(e -> showScreen("LEADERBOARD"));

        registerPanel.getSwitchToLoginButton().addActionListener(e -> showScreen("LOGIN"));
        registerPanel.getBackButton().addActionListener(e -> showScreen("LOGIN"));

        profilePanel.getLeaderboardButton().addActionListener(e -> showScreen("LEADERBOARD"));
        profilePanel.getBackButton().addActionListener(e -> showScreen("LOGIN"));
        profilePanel.getEditProfileButton().addActionListener(e -> {
            UserProfile user = authManager != null ? authManager.getCurrentUser() : null;
            if (user != null) {
                String currentBio = user.getAboutMe() != null ? user.getAboutMe() : "";
                String newBio = JOptionPane.showInputDialog(this, "Enter new bio:", currentBio);
                if (newBio != null && profileManager != null) {
                    try {
                        profileManager.updateAboutMe(newBio.trim());
                        profilePanel.refreshProfile(user);
                    } catch (Exception ignored) {
                    }
                }
            }
        });

        leaderboardPanel.getBackButton().addActionListener(e -> showScreen("LOGIN"));

        gameOverOverlay.getMenuButton().addActionListener(e -> showScreen("LOGIN"));
        gameOverOverlay.getRestartButton().addActionListener(e -> showScreen("LEADERBOARD"));
    }

    public void showScreen(String screenName) {
        if ("PROFILE".equalsIgnoreCase(screenName)) {
            UserProfile user = authManager != null ? authManager.getCurrentUser() : null;
            if (user != null) {
                profilePanel.refreshProfile(user);
            }
        } else if ("LEADERBOARD".equalsIgnoreCase(screenName)) {
            leaderboardPanel.loadLeaderboardData();
        }
        cardLayout.show(cardPanel, screenName);
    }

    public void startNewGame(GameMode mode) {
        showScreen("LEADERBOARD");
    }

    public void resumeSavedGame() {
        showScreen("LEADERBOARD");
    }

    public void showGameOver(int finalScore, int bestScore, boolean victory) {
        gameOverOverlay.updateDisplay(finalScore, bestScore, victory);
        showScreen("GAMEOVER");
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }

    public ProfileController getProfileController() {
        return profileController;
    }
}

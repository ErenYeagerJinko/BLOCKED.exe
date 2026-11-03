package view;

import audio.*;
import controller.*;
import enums.*;
import manager.*;
import model.*;
import persistence.*;
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
    private DataPersistenceManager persistenceManager;
    private ScoreManager scoreManager;
    private MainMenuPanel mainMenuPanel;
    private InstructionsPanel instructionsPanel;
    private GamePanel gamePanel;

    public GameFrame() {
        initManagers();
        initUI();
        Logger.getLogger().log("GameFrame initialized and ready");
    }

    private void initManagers() {
        authManager = new AuthManager();
        // Share the login session so profile edits and game stats go to the signed-in player
        profileManager = new ProfileManager(authManager.getPersistence(), authManager);
        leaderboardManager = new LeaderboardManager(authManager.getPersistence());
        profileController = new ProfileController(this, profileManager, leaderboardManager);
        persistenceManager = new DataPersistenceManager();
        scoreManager = new ScoreManager(persistenceManager);
    }

    private void initUI() {
        setTitle("BLOCKED.exe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                saveCurrentSession();
            }
        });
        setSize(800, 720);
        setMinimumSize(new Dimension(650, 650));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this, authManager);
        registerPanel = new RegisterPanel(this, authManager);
        profilePanel = new ProfilePanel(this, profileManager);
        leaderboardPanel = new LeaderboardPanel(this, leaderboardManager, profileController);
        mainMenuPanel = new MainMenuPanel(this, persistenceManager);
        instructionsPanel = new InstructionsPanel(this);
        gamePanel = new GamePanel(this);
        gameOverOverlay = new GameOverOverlay(gamePanel);

        wireNavigation();

        cardPanel.add(mainMenuPanel, "MENU");
        cardPanel.add(instructionsPanel, "INSTRUCTIONS");
        cardPanel.add(gamePanel, "GAME");
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

        profilePanel.getPlayButton().addActionListener(e -> {
            SoundManager.getInstance().playSelect();
            showScreen("MENU");
        });
        profilePanel.getLeaderboardButton().addActionListener(e -> {
            SoundManager.getInstance().playSelect();
            showScreen("LEADERBOARD");
        });
        profilePanel.getBackButton().addActionListener(e -> {
            SoundManager.getInstance().playSelect();
            if (isGameActive()) {
                showScreen("MENU");
            } else {
                if (authManager != null) {
                    authManager.logout();
                }
                if (loginPanel != null) {
                    loginPanel.clearFields();
                }
                showScreen("LOGIN");
            }
        });
        profilePanel.getEditProfileButton().addActionListener(e -> {
            UserProfile user = authManager != null ? authManager.getCurrentUser() : null;
            if (user != null) {
                String[] options = {"Edit Bio", "Change Profile Picture", "Remove Profile Picture", "Cancel"};
                int choice = JOptionPane.showOptionDialog(this,
                        "What would you like to update?",
                        "Edit Profile",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (choice == 0) {
                    String currentBio = user.getAboutMe() != null ? user.getAboutMe() : "";
                    String newBio = JOptionPane.showInputDialog(this, "Enter new bio:", currentBio);
                    if (newBio != null && profileManager != null) {
                        try {
                            profileManager.updateAboutMe(newBio.trim());
                            profilePanel.refreshProfile(user);
                        } catch (Exception ignored) {
                        }
                    }
                } else if (choice == 1) {
                    profilePanel.handleChooseAvatar();
                } else if (choice == 2) {
                    profilePanel.handleRemoveAvatar();
                }
            }
        });

        leaderboardPanel.getBackButton().addActionListener(e -> showScreen("MENU"));

        gameOverOverlay.getMenuButton().addActionListener(e -> returnToMenu());
        gameOverOverlay.getRestartButton().addActionListener(e -> restartGame());
    }

    public void showScreen(String screenName) {
        Logger.getLogger().log("GameFrame switching view to screen: " + screenName);
        if ("PROFILE".equalsIgnoreCase(screenName)) {
            UserProfile user = authManager != null ? authManager.getCurrentUser() : null;
            if (user != null) {
                profilePanel.refreshProfile(user);
            }
        } else if ("LEADERBOARD".equalsIgnoreCase(screenName)) {
            leaderboardPanel.loadLeaderboardData();
        } else if ("MENU".equalsIgnoreCase(screenName)) {
            mainMenuPanel.refreshResumeButton();
        }
        cardLayout.show(cardPanel, screenName);
    }

    public boolean isGameActive() {
        Game current = gamePanel != null ? gamePanel.getGame() : null;
        return current != null && !current.isOver();
    }

    public void startNewGame(GameMode mode) {
        Logger.getLogger().log("New game starting with mode: " + mode);
        persistenceManager.clearSavedSession();
        scoreManager.resetCurrentScore();
        Game game = new Game(mode, scoreManager);
        game.start();
        launchGame(game);
    }

    private void launchGame(Game game) {
        gamePanel.setGame(game);
        showScreen("GAME");
        gamePanel.requestFocusInWindow();
    }

    // Only unfinished games are written; the persistence layer skips finished ones
    private void saveCurrentSession() {
        Game current = gamePanel.getGame();
        if (current != null && !current.isOver()) {
            persistenceManager.saveGameSession(current, scoreManager);
        }
    }

    // Stats are only kept for a signed-in player; ProfileManager ignores guests
    public void recordFinishedGame(Game game) {
        if (game == null || profileManager == null) {
            return;
        }
        profileManager.recordGameSession(game.getCurrentScore(), game.getElapsedTimeMs() / 1000, game.getTotalLinesCleared());
    }

    public void restartGame() {
        Game current = gamePanel.getGame();
        startNewGame(current != null ? current.getGameMode() : GameMode.CLASSIC);
    }

    public void returnToMenu() {
        gamePanel.stopRendering();
        saveCurrentSession();
        showScreen("MENU");
    }

    public void resumeSavedGame() {
        Logger.getLogger().log("Resuming saved game session");
        GameMode mode = persistenceManager.getSavedGameMode();
        if (mode == null) {
            showScreen("MENU");
            return;
        }
        Game game = new Game(mode, scoreManager);
        if (!persistenceManager.resumeGameSession(game, scoreManager)) {
            showScreen("MENU");
            return;
        }
        game.start();
        launchGame(game);
    }

    public void showGameOver(int finalScore, int bestScore, boolean victory) {
        persistenceManager.clearSavedSession();
        Logger.getLogger().log("Game over presented: score=" + finalScore + ", best=" + bestScore + ", victory=" + victory);
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

package view;

import exceptions.*;
import manager.*;
import util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton switchToRegisterButton;
    private JButton backButton;
    private JLabel statusLabel;
    private AuthManager authManager;
    private GameFrame frame;

    public LoginPanel() {
        this(null, null);
    }

    public LoginPanel(GameFrame frame, AuthManager authManager) {
        this.frame = frame;
        this.authManager = authManager;
        initUI();
        Logger.getLogger().log("LoginPanel initialized");
    }

    private void initUI() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(24, 24, 36, 230));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 120), 2, true),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        card.setPreferredSize(new Dimension(400, 460));
        card.setMaximumSize(new Dimension(400, 460));

        JLabel titleLabel = new JLabel("PLAYER LOGIN");
        titleLabel.setFont(UIButtonRenderer.getFont(Font.BOLD, 22f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(UIButtonRenderer.getFont(Font.BOLD, 13f));
        userLabel.setForeground(new Color(200, 200, 220));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField(16);
        usernameField.setMaximumSize(new Dimension(300, 34));
        usernameField.setPreferredSize(new Dimension(300, 34));
        usernameField.setFont(UIButtonRenderer.getFont(Font.PLAIN, 14f));
        usernameField.setBackground(new Color(36, 36, 52));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 90, 130), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(UIButtonRenderer.getFont(Font.BOLD, 13f));
        passLabel.setForeground(new Color(200, 200, 220));
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField(16);
        passwordField.setMaximumSize(new Dimension(300, 34));
        passwordField.setPreferredSize(new Dimension(300, 34));
        passwordField.setFont(UIButtonRenderer.getFont(Font.PLAIN, 14f));
        passwordField.setBackground(new Color(36, 36, 52));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 90, 130), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        loginButton = UIButtonRenderer.createButton("Login", UIButtonRenderer.BLUE_BUTTON, 290, 44);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> handleLogin());

        switchToRegisterButton = UIButtonRenderer.createButton("Create New Account", UIButtonRenderer.NEUTRAL_BUTTON, 290, 44);
        switchToRegisterButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        backButton = UIButtonRenderer.createButton("Leaderboard", UIButtonRenderer.NEUTRAL_BUTTON, 290, 44);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(UIButtonRenderer.getFont(Font.PLAIN, 12f));
        statusLabel.setForeground(new Color(255, 100, 100));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(userLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(usernameField);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(passLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(passwordField);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(loginButton);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(switchToRegisterButton);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(backButton);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(statusLabel);

        add(card);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            Logger.getLogger().error("Login attempt with empty username or password");
            statusLabel.setForeground(new Color(255, 100, 100));
            statusLabel.setText("Please enter both username and password.");
            return;
        }

        Logger.getLogger().log("Login submitted for user: " + username);
        if (authManager != null) {
            try {
                authManager.login(username, password);
                Logger.getLogger().log("Login succeeded in LoginPanel for: " + username);
                statusLabel.setForeground(new Color(100, 230, 120));
                statusLabel.setText("Login successful!");
                if (frame != null) {
                    frame.showScreen("PROFILE");
                }
            } catch (AuthException ex) {
                Logger.getLogger().error("Login failed in LoginPanel: " + ex.getMessage());
                statusLabel.setForeground(new Color(255, 100, 100));
                statusLabel.setText(ex.getMessage() != null ? ex.getMessage() : "Invalid credentials.");
            }
        } else {
            Logger.getLogger().log("AuthManager unavailable, bypass login for: " + username);
            statusLabel.setForeground(new Color(100, 230, 120));
            statusLabel.setText("Authenticated: " + username);
            if (frame != null) {
                frame.showScreen("PROFILE");
            }
        }
    }

    public void clearFields() {
        if (usernameField != null) usernameField.setText("");
        if (passwordField != null) passwordField.setText("");
        if (statusLabel != null) statusLabel.setText(" ");
        Logger.getLogger().log("LoginPanel fields cleared");
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getSwitchToRegisterButton() {
        return switchToRegisterButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public String getEnteredUsername() {
        return usernameField != null ? usernameField.getText().trim() : "";
    }

    public String getEnteredPassword() {
        return passwordField != null ? new String(passwordField.getPassword()) : "";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BackgroundRenderer.renderBackground(g, this);
    }
}

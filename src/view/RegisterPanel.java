package view;

import exceptions.*;
import manager.*;
import util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class RegisterPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField avatarPathField;
    private JButton browseAvatarButton;
    private JTextArea aboutMeArea;
    private JButton registerButton;
    private JButton switchToLoginButton;
    private JButton backButton;
    private JLabel statusLabel;
    private AuthManager authManager;
    private GameFrame frame;

    public RegisterPanel() {
        this(null, null);
    }

    public RegisterPanel(GameFrame frame, AuthManager authManager) {
        this.frame = frame;
        this.authManager = authManager;
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
                BorderFactory.createEmptyBorder(16, 25, 16, 25)
        ));
        card.setPreferredSize(new Dimension(420, 500));
        card.setMaximumSize(new Dimension(420, 500));

        JLabel titleLabel = new JLabel("CREATE ACCOUNT");
        titleLabel.setFont(UIButtonRenderer.getFont(Font.BOLD, 22f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = createStyledTextField();
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = createStyledPasswordField();
        confirmPasswordField = createStyledPasswordField();

        JPanel passRow = new JPanel(new GridLayout(1, 2, 10, 0));
        passRow.setOpaque(false);
        passRow.setPreferredSize(new Dimension(310, 48));
        passRow.setMaximumSize(new Dimension(310, 48));
        passRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        p1.setOpaque(false);
        p1.add(createFieldLabel("Password"));
        p1.add(Box.createRigidArea(new Dimension(0, 3)));
        p1.add(passwordField);

        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        p2.setOpaque(false);
        p2.add(createFieldLabel("Confirm Pass"));
        p2.add(Box.createRigidArea(new Dimension(0, 3)));
        p2.add(confirmPasswordField);

        passRow.add(p1);
        passRow.add(p2);

        avatarPathField = createStyledTextField();

        browseAvatarButton = UIButtonRenderer.createButton("Browse", UIButtonRenderer.NEUTRAL_BUTTON, 85, 28);
        browseAvatarButton.addActionListener(e -> handleBrowseAvatar());

        JPanel avatarRow = new JPanel(new BorderLayout(8, 0));
        avatarRow.setOpaque(false);
        avatarRow.setPreferredSize(new Dimension(310, 28));
        avatarRow.setMaximumSize(new Dimension(310, 28));
        avatarRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarRow.add(avatarPathField, BorderLayout.CENTER);
        avatarRow.add(browseAvatarButton, BorderLayout.EAST);

        aboutMeArea = new JTextArea(2, 20);
        aboutMeArea.setFont(UIButtonRenderer.getFont(Font.PLAIN, 12f));
        aboutMeArea.setBackground(new Color(36, 36, 52));
        aboutMeArea.setForeground(Color.WHITE);
        aboutMeArea.setCaretColor(Color.WHITE);
        aboutMeArea.setLineWrap(true);
        aboutMeArea.setWrapStyleWord(true);
        aboutMeArea.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));

        JScrollPane bioScroll = new JScrollPane(aboutMeArea);
        bioScroll.setPreferredSize(new Dimension(310, 44));
        bioScroll.setMaximumSize(new Dimension(310, 44));
        bioScroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        bioScroll.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 130), 1));

        registerButton = UIButtonRenderer.createButton("Register", UIButtonRenderer.GREEN_BUTTON, 310, 38);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(e -> handleRegister());

        switchToLoginButton = UIButtonRenderer.createButton("Sign In", UIButtonRenderer.NEUTRAL_BUTTON, 150, 36);
        backButton = UIButtonRenderer.createButton("Back", UIButtonRenderer.BACK_BUTTON, 150, 36);

        JPanel bottomButtons = new JPanel(new GridLayout(1, 2, 10, 0));
        bottomButtons.setOpaque(false);
        bottomButtons.setPreferredSize(new Dimension(310, 36));
        bottomButtons.setMaximumSize(new Dimension(310, 36));
        bottomButtons.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomButtons.add(switchToLoginButton);
        bottomButtons.add(backButton);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(UIButtonRenderer.getFont(Font.PLAIN, 12f));
        statusLabel.setForeground(new Color(255, 100, 100));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(createFieldLabel("Username"));
        card.add(Box.createRigidArea(new Dimension(0, 3)));
        card.add(usernameField);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(passRow);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(createFieldLabel("Profile Picture (Optional)"));
        card.add(Box.createRigidArea(new Dimension(0, 3)));
        card.add(avatarRow);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(createFieldLabel("About Me"));
        card.add(Box.createRigidArea(new Dimension(0, 3)));
        card.add(bioScroll);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(registerButton);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(bottomButtons);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(statusLabel);

        add(card);
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIButtonRenderer.getFont(Font.BOLD, 12f));
        label.setForeground(new Color(200, 200, 220));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setPreferredSize(new Dimension(310, 16));
        label.setMaximumSize(new Dimension(310, 16));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField tf = new JTextField(16);
        tf.setMaximumSize(new Dimension(310, 28));
        tf.setPreferredSize(new Dimension(310, 28));
        tf.setFont(UIButtonRenderer.getFont(Font.PLAIN, 13f));
        tf.setBackground(new Color(36, 36, 52));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 90, 130), 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));
        return tf;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField pf = new JPasswordField(16);
        pf.setMaximumSize(new Dimension(150, 28));
        pf.setPreferredSize(new Dimension(150, 28));
        pf.setFont(UIButtonRenderer.getFont(Font.PLAIN, 13f));
        pf.setBackground(new Color(36, 36, 52));
        pf.setForeground(Color.WHITE);
        pf.setCaretColor(Color.WHITE);
        pf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 90, 130), 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));
        return pf;
    }

    private void handleBrowseAvatar() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            avatarPathField.setText(selected.getAbsolutePath());
        }
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());
        String avatar = avatarPathField.getText().trim();
        String bio = aboutMeArea.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setForeground(new Color(255, 100, 100));
            statusLabel.setText("Username and password are required.");
            return;
        }

        if (!password.equals(confirm)) {
            statusLabel.setForeground(new Color(255, 100, 100));
            statusLabel.setText("Passwords do not match.");
            return;
        }

        if (authManager != null) {
            try {
                authManager.register(username, password, avatar.isEmpty() ? null : avatar, bio);
                statusLabel.setForeground(new Color(100, 230, 120));
                statusLabel.setText("Account created successfully!");
                if (frame != null) {
                    frame.showScreen("PROFILE");
                }
            } catch (AuthException ex) {
                statusLabel.setForeground(new Color(255, 100, 100));
                statusLabel.setText(ex.getMessage() != null ? ex.getMessage() : "Registration failed.");
            }
        } else {
            statusLabel.setForeground(new Color(100, 230, 120));
            statusLabel.setText("Account created for " + username);
            if (frame != null) {
                frame.showScreen("PROFILE");
            }
        }
    }

    public void clearFields() {
        if (usernameField != null) usernameField.setText("");
        if (passwordField != null) passwordField.setText("");
        if (confirmPasswordField != null) confirmPasswordField.setText("");
        if (avatarPathField != null) avatarPathField.setText("");
        if (aboutMeArea != null) aboutMeArea.setText("");
        if (statusLabel != null) statusLabel.setText(" ");
    }

    public JButton getRegisterButton() {
        return registerButton;
    }

    public JButton getSwitchToLoginButton() {
        return switchToLoginButton;
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

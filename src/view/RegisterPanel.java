package view;

import manager.AuthManager;
import util.BackgroundRenderer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Graphics;

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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BackgroundRenderer.renderBackground(g, this);
    }

    public void clearFields() {
        if (usernameField != null) usernameField.setText("");
        if (passwordField != null) passwordField.setText("");
        if (confirmPasswordField != null) confirmPasswordField.setText("");
        if (avatarPathField != null) avatarPathField.setText("");
        if (aboutMeArea != null) aboutMeArea.setText("");
        if (statusLabel != null) statusLabel.setText("");
    }
}

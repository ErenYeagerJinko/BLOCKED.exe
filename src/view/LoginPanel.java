package view;

import manager.AuthManager;
import util.BackgroundRenderer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Graphics;

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
        if (statusLabel != null) statusLabel.setText("");
    }
}

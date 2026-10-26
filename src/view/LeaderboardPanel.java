package view;

import controller.ProfileController;
import manager.LeaderboardManager;
import util.BackgroundRenderer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Graphics;

public class LeaderboardPanel extends JPanel {
    private JTable leaderboardTable;
    private DefaultTableModel tableModel;
    private JButton filterHighestButton;
    private JButton filterCumulativeButton;
    private JButton backButton;
    private LeaderboardManager leaderboardManager;
    private ProfileController profileController;
    private GameFrame frame;

    public LeaderboardPanel() {
        this(null, null, null);
    }

    public LeaderboardPanel(GameFrame frame, LeaderboardManager leaderboardManager, ProfileController profileController) {
        this.frame = frame;
        this.leaderboardManager = leaderboardManager;
        this.profileController = profileController;
        initUI();
    }

    private void initUI() {
        setOpaque(false);
    }

    public void loadLeaderboardData() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BackgroundRenderer.renderBackground(g, this);
    }
}

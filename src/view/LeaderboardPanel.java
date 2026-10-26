package view;

import controller.*;
import manager.*;
import model.*;
import util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class LeaderboardPanel extends JPanel {
    private JTable leaderboardTable;
    private DefaultTableModel tableModel;
    private JButton filterHighestButton;
    private JButton filterCumulativeButton;
    private JButton backButton;
    private LeaderboardManager leaderboardManager;
    private ProfileController profileController;
    private GameFrame frame;
    private boolean showingHighest = true;

    public LeaderboardPanel() {
        this(null, null, null);
    }

    public LeaderboardPanel(GameFrame frame, LeaderboardManager leaderboardManager, ProfileController profileController) {
        this.frame = frame;
        this.leaderboardManager = leaderboardManager != null ? leaderboardManager : new LeaderboardManager();
        this.profileController = profileController;
        initUI();
    }

    private void initUI() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new JPanel(new BorderLayout(10, 12));
        card.setBackground(new Color(24, 24, 36, 235));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 120), 2, true),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        card.setPreferredSize(new Dimension(520, 560));
        card.setMaximumSize(new Dimension(520, 560));

        JLabel titleLabel = new JLabel("TOP PLAYERS LEADERBOARD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topSection.add(titleLabel);
        topSection.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        filterRow.setOpaque(false);

        filterHighestButton = new JButton("Highest Score");
        filterHighestButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filterHighestButton.setBackground(new Color(60, 110, 230));
        filterHighestButton.setForeground(Color.WHITE);
        filterHighestButton.addActionListener(e -> {
            showingHighest = true;
            filterHighestButton.setBackground(new Color(60, 110, 230));
            filterCumulativeButton.setBackground(new Color(45, 45, 65));
            loadLeaderboardData();
        });

        filterCumulativeButton = new JButton("Cumulative Score");
        filterCumulativeButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filterCumulativeButton.setBackground(new Color(45, 45, 65));
        filterCumulativeButton.setForeground(Color.WHITE);
        filterCumulativeButton.addActionListener(e -> {
            showingHighest = false;
            filterCumulativeButton.setBackground(new Color(60, 110, 230));
            filterHighestButton.setBackground(new Color(45, 45, 65));
            loadLeaderboardData();
        });

        filterRow.add(filterHighestButton);
        filterRow.add(filterCumulativeButton);
        topSection.add(filterRow);

        String[] columns = {"Rank", "Player", "Highest", "Total", "Playtime"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        leaderboardTable = new JTable(tableModel);
        leaderboardTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        leaderboardTable.setRowHeight(28);
        leaderboardTable.setBackground(new Color(30, 30, 44));
        leaderboardTable.setForeground(new Color(230, 230, 245));
        leaderboardTable.setSelectionBackground(new Color(60, 80, 150));
        leaderboardTable.setSelectionForeground(Color.WHITE);
        leaderboardTable.setShowGrid(false);
        leaderboardTable.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = leaderboardTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(45, 45, 68));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 30));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        leaderboardTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        leaderboardTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        leaderboardTable.getColumnModel().getColumn(1).setPreferredWidth(140);
        leaderboardTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        leaderboardTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        leaderboardTable.getColumnModel().getColumn(3).setPreferredWidth(90);
        leaderboardTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        leaderboardTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        leaderboardTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        if (profileController != null) {
            leaderboardTable.addMouseListener(new LeaderboardMouseHandler(leaderboardTable, profileController));
            leaderboardTable.addMouseMotionListener(new LeaderboardMouseHandler(leaderboardTable, profileController));
        }

        JScrollPane tableScroll = new JScrollPane(leaderboardTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(65, 65, 95), 1));
        tableScroll.getViewport().setBackground(new Color(30, 30, 44));

        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomRow.setOpaque(false);
        backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setPreferredSize(new Dimension(130, 32));
        bottomRow.add(backButton);

        card.add(topSection, BorderLayout.NORTH);
        card.add(tableScroll, BorderLayout.CENTER);
        card.add(bottomRow, BorderLayout.SOUTH);

        add(card);
        loadLeaderboardData();
    }

    public void loadLeaderboardData() {
        if (tableModel == null) {
            return;
        }
        tableModel.setRowCount(0);
        if (leaderboardManager == null) {
            return;
        }

        Leaderboard lb = showingHighest
                ? leaderboardManager.getTopScores(50)
                : leaderboardManager.getTopCumulativeScores(50);

        if (lb != null) {
            List<LeaderboardEntry> list = lb.getEntries();
            for (LeaderboardEntry e : list) {
                tableModel.addRow(new Object[]{
                        "#" + e.getRank(),
                        e.getUsername(),
                        e.getHighestScore(),
                        e.getTotalScore(),
                        e.getFormattedPlayTime()
                });
            }
        }
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JTable getLeaderboardTable() {
        return leaderboardTable;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BackgroundRenderer.renderBackground(g, this);
    }
}

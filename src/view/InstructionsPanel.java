package view;

import audio.*;
import util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Instructions and rules panel displaying game objectives,
 * scoring mechanics, and keyboard / mouse controls.
 */
public class InstructionsPanel extends JPanel {

    private GameFrame frame;
    private JButton backButton;

    /**
     * Default no-arg constructor.
     */
    public InstructionsPanel() {
        this(null);
    }

    /**
     * Constructor with GameFrame reference.
     *
     * @param frame parent GameFrame for navigation
     */
    public InstructionsPanel(GameFrame frame) {
        this.frame = frame;
        initUI();
        wireActions();
        Logger.getLogger().log("InstructionsPanel initialized");
    }

    /**
     * Builds and arranges the instructions layout with proper alignment and spacing.
     */
    private void initUI() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(20, 22, 34, 235));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 100, 140), 2, true),
                BorderFactory.createEmptyBorder(18, 24, 18, 24)
        ));
        card.setPreferredSize(new Dimension(620, 600));
        card.setMaximumSize(new Dimension(620, 600));

        // Header Title
        JLabel titleLabel = new JLabel("HOW TO PLAY");
        titleLabel.setFont(UIButtonRenderer.getFont(Font.BOLD, 24f));
        titleLabel.setForeground(new Color(255, 214, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Header Subtitle
        JLabel subtitleLabel = new JLabel("Master the Grid, Clear Rows & Columns, Set High Scores!");
        subtitleLabel.setFont(UIButtonRenderer.getFont(Font.PLAIN, 12f));
        subtitleLabel.setForeground(new Color(190, 200, 220));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(2));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(14));

        // Objective Section
        JPanel rulesSection = createSectionPanel("GAME RULES & OBJECTIVES");
        addBulletPoint(rulesSection, "Drag and drop polyomino pieces from the 3-piece tray onto the 8x8 grid.");
        addBulletPoint(rulesSection, "Fill complete horizontal rows or vertical columns to clear them for points.");
        addBulletPoint(rulesSection, "Simultaneous row and column clears trigger high-scoring combo multipliers.");
        addBulletPoint(rulesSection, "The tray automatically refills with 3 fresh pieces once all 3 are placed.");
        addBulletPoint(rulesSection, "Classic Mode: Play until no remaining tray pieces fit on the board.");
        addBulletPoint(rulesSection, "Time Attack: 180-second countdown with a target of 500 points to win.");
        card.add(rulesSection);

        card.add(Box.createVerticalStrut(12));

        // Controls Section
        JPanel controlsSection = createSectionPanel("CONTROLS REFERENCE");
        JPanel tablePanel = new JPanel(new GridLayout(0, 3, 14, 5));
        tablePanel.setOpaque(false);
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        addTableHeader(tablePanel, "Action");
        addTableHeader(tablePanel, "Mouse Control");
        addTableHeader(tablePanel, "Keyboard Shortcut");

        addTableRow(tablePanel, "Select Piece", "Drag from Tray", "Keys 1, 2, or 3");
        addTableRow(tablePanel, "Move Preview", "Drag across Board", "Arrow Keys");
        addTableRow(tablePanel, "Place Piece", "Release Mouse", "Enter / Space");
        addTableRow(tablePanel, "Pause / Resume", "Click Pause", "Key P");
        addTableRow(tablePanel, "Restart Game", "Click Restart", "Key R");
        addTableRow(tablePanel, "Mute / Unmute", "Click Sound", "Key M");

        controlsBox.add(tablePanel);
        card.add(controlsBox);

        card.add(Box.createVerticalStrut(18));

        // Back Button
        backButton = UIButtonRenderer.createButton("BACK TO MENU", UIButtonRenderer.BACK_BUTTON, 230, 40);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(backButton);

        add(card);
    }

    /**
     * Appends a bullet-point text line to a section panel.
     *
     * @param panel target section container
     * @param text explanation text
     */
    private void addBulletPoint(JPanel panel, String text) {
        JLabel label = new JLabel("<html><table width='556' cellpadding='0' cellspacing='0'><tr>"
                + "<td valign='top' width='14' style='color:#e1e6f0; font-family:\"Kenney Future Narrow\"; font-size:11pt;'>-</td>"
                + "<td style='color:#e1e6f0; font-family:\"Kenney Future Narrow\"; font-size:11pt;'>" + text + "</td>"
                + "</tr></table></html>");
        label.setFont(UIButtonRenderer.getFont(Font.PLAIN, 11f));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(3));
    }

    /**
     * Adds a header cell to the controls table.
     */
    private void addTableHeader(JPanel table, String title) {
        JLabel label = new JLabel(title);
        label.setFont(UIButtonRenderer.getFont(Font.BOLD, 12f));
        label.setForeground(new Color(255, 214, 0));
        table.add(label);
    }

    /**
     * Adds a row of data to the controls table.
     */
    private void addTableRow(JPanel table, String action, String mouse, String keyboard) {
        JLabel actionLabel = new JLabel(action);
        actionLabel.setFont(UIButtonRenderer.getFont(Font.BOLD, 11f));
        actionLabel.setForeground(new Color(210, 220, 235));

        JLabel mouseLabel = new JLabel(mouse);
        mouseLabel.setFont(UIButtonRenderer.getFont(Font.PLAIN, 11f));
        mouseLabel.setForeground(new Color(170, 185, 210));

        JLabel keyLabel = new JLabel(keyboard);
        keyLabel.setFont(UIButtonRenderer.getFont(Font.PLAIN, 11f));
        keyLabel.setForeground(new Color(170, 185, 210));

        table.add(actionLabel);
        table.add(mouseLabel);
        table.add(keyLabel);
    }

    /**
     * Wires action listeners to interactive components.
     */
    private void wireActions() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.getInstance().playSelect();
                if (frame != null) {
                    frame.showScreen("MENU");
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BackgroundRenderer.renderBackground(g, this);
    }

    public JButton getBackButton() {
        return backButton;
    }
}

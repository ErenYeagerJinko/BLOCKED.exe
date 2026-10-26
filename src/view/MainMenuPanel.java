package view;

import persistence.*;
import util.*;
import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private GameFrame frame;
    private DataPersistenceManager persistenceManager;

    public MainMenuPanel() {
        this(null, null);
    }

    public MainMenuPanel(GameFrame frame, DataPersistenceManager persistenceManager) {
        this.frame = frame;
        this.persistenceManager = persistenceManager;
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
}

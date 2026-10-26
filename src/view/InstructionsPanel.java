package view;

import util.BackgroundRenderer;

import javax.swing.JPanel;
import java.awt.Graphics;

public class InstructionsPanel extends JPanel {
    private GameFrame frame;

    public InstructionsPanel() {
        this(null);
    }

    public InstructionsPanel(GameFrame frame) {
        this.frame = frame;
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

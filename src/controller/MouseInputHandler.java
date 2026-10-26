package controller;

import model.*;
import view.*;
import java.awt.*;
import java.awt.event.*;

public class MouseInputHandler extends MouseAdapter {
    private GamePanel panel;
    private Game game;
    private int draggedSlot = -1;
    private Point currentDragPoint;

    public MouseInputHandler() {
        this(null, null);
    }

    public MouseInputHandler(GamePanel panel, Game game) {
        this.panel = panel;
        this.game = game;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        Point p = e.getPoint();
        int slot = determineSlotFromPoint(p);
        if (slot >= 0) {
            draggedSlot = slot;
            currentDragPoint = p;
            if (panel != null) {
                panel.repaint();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggedSlot >= 0) {
            currentDragPoint = e.getPoint();
            if (panel != null) {
                panel.repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        if (draggedSlot >= 0) {
            resetDrag();
            if (panel != null) {
                panel.repaint();
            }
        }
    }

    private int determineSlotFromPoint(Point p) {
        if (panel == null || p == null) {
            return -1;
        }
        int panelW = panel.getWidth();
        int panelH = panel.getHeight();
        int trayY = panelH - 140;
        if (p.y >= trayY && p.y <= panelH - 20) {
            int slotW = 110;
            int gap = 20;
            int totalW = 3 * slotW + 2 * gap;
            int startX = (panelW - totalW) / 2;
            for (int i = 0; i < 3; i++) {
                int sx = startX + i * (slotW + gap);
                if (p.x >= sx && p.x <= sx + slotW) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void resetDrag() {
        draggedSlot = -1;
        currentDragPoint = null;
    }

    public boolean isDragging() {
        return draggedSlot >= 0;
    }

    public int getDraggedSlot() {
        return draggedSlot;
    }

    public Point getCurrentDragPoint() {
        return currentDragPoint;
    }

    public GamePanel getPanel() {
        return panel;
    }

    public void setPanel(GamePanel panel) {
        this.panel = panel;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}

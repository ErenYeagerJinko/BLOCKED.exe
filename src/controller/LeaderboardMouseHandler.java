package controller;

import util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LeaderboardMouseHandler extends MouseAdapter {
    private JTable table;
    private ProfileController profileController;

    public LeaderboardMouseHandler(JTable table, ProfileController profileController) {
        this.table = table;
        this.profileController = profileController;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (table == null || profileController == null) {
            return;
        }
        Point p = e.getPoint();
        int row = table.rowAtPoint(p);
        int col = table.columnAtPoint(p);

        if (row >= 0 && col == 1) {
            Object val = table.getValueAt(row, 1);
            if (val != null) {
                Logger.getLogger().log("Leaderboard username clicked: " + val.toString());
                profileController.showUserProfileDialog(val.toString());
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (table == null) {
            return;
        }
        Point p = e.getPoint();
        int row = table.rowAtPoint(p);
        int col = table.columnAtPoint(p);

        if (row >= 0 && col == 1) {
            table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            table.setCursor(Cursor.getDefaultCursor());
        }
    }
}

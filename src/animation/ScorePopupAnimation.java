package animation;

import util.*;
import java.awt.*;

public class ScorePopupAnimation extends Animation {
    private static final long DEFAULT_DURATION_MS = 900;
    private static final int RISE_DISTANCE = 60;

    private final int points;
    private final int startX;
    private final int startY;

    public ScorePopupAnimation(int points, int startX, int startY, long durationMs) {
        super(durationMs);
        this.points = points;
        this.startX = startX;
        this.startY = startY;
    }

    public ScorePopupAnimation(int points, int startX, int startY) {
        this(points, startX, startY, DEFAULT_DURATION_MS);
    }

    // Drifts upward while fading out over its lifetime
    @Override
    public void draw(Graphics2D g2d) {
        if (finished || g2d == null) {
            return;
        }

        float p = getProgress();
        int alpha = Math.max(0, Math.min(255, (int) ((1.0f - p) * 255)));
        int y = startY - (int) (p * RISE_DISTANCE);
        String text = "+" + points;

        Object oldAntialias = g2d.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(UIButtonRenderer.getFont(Font.BOLD, 30f));

        FontMetrics fm = g2d.getFontMetrics();
        int x = startX - fm.stringWidth(text) / 2;

        g2d.setColor(new Color(0, 0, 0, alpha / 2));
        g2d.drawString(text, x + 2, y + 2);
        g2d.setColor(new Color(255, 215, 0, alpha));
        g2d.drawString(text, x, y);

        if (oldAntialias != null) {
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, oldAntialias);
        }
    }

    public int getPoints() {
        return points;
    }
}

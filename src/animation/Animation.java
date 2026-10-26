package animation;

import java.awt.*;

public abstract class Animation implements Drawable {
    protected long durationMs;
    protected long elapsedMs;
    protected boolean finished;

    public Animation() {
        this(0);
    }

    public Animation(long durationMs) {
        this.durationMs = durationMs;
        this.elapsedMs = 0;
        this.finished = false;
    }

    public void update(long deltaMs) {
        if (finished) {
            return;
        }
        elapsedMs += deltaMs;
        if (durationMs > 0 && elapsedMs >= durationMs) {
            finished = true;
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public float getProgress() {
        if (durationMs <= 0) {
            return 1.0f;
        }
        return Math.min(1.0f, (float) elapsedMs / durationMs);
    }

    public long getDurationMs() {
        return durationMs;
    }

    public long getElapsedMs() {
        return elapsedMs;
    }

    @Override
    public void draw(Graphics2D g2d) {
    }
}

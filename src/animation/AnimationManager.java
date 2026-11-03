package animation;

import java.awt.*;
import java.util.*;

public class AnimationManager {
    private final java.util.List<Animation> activeAnimations;

    public AnimationManager() {
        this.activeAnimations = new ArrayList<>();
    }

    public void addAnimation(Animation anim) {
        if (anim != null) {
            activeAnimations.add(anim);
        }
    }

    // Advances every animation and drops the ones that have finished
    public void update(long deltaMs) {
        Iterator<Animation> it = activeAnimations.iterator();
        while (it.hasNext()) {
            Animation anim = it.next();
            anim.update(deltaMs);
            if (anim.isFinished()) {
                it.remove();
            }
        }
    }

    public void renderAll(Graphics2D g2d) {
        for (Animation anim : activeAnimations) {
            anim.draw(g2d);
        }
    }

    public void clear() {
        activeAnimations.clear();
    }

    public boolean hasActiveAnimations() {
        return !activeAnimations.isEmpty();
    }
}

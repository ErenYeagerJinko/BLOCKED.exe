package util;

import javax.imageio.ImageIO;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class BackgroundRenderer {
    private static final String DEFAULT_IMAGE_PATH = "resources/Images/mainMenu.png";
    private static Image backgroundImage;

    static {
        loadImage();
    }

    private static void loadImage() {
        File file = new File(DEFAULT_IMAGE_PATH);
        if (file.exists()) {
            try {
                backgroundImage = ImageIO.read(file);
            } catch (IOException ignored) {
            }
        }
    }

    public static void renderBackground(Graphics g, Component c) {
        if (backgroundImage == null) {
            loadImage();
        }
        if (backgroundImage != null) {
            int panelW = c.getWidth();
            int panelH = c.getHeight();
            int imgW = backgroundImage.getWidth(null);
            int imgH = backgroundImage.getHeight(null);

            if (panelW > 0 && panelH > 0 && imgW > 0 && imgH > 0) {
                double scale = Math.max((double) panelW / imgW, (double) panelH / imgH);
                int drawW = (int) Math.round(imgW * scale);
                int drawH = (int) Math.round(imgH * scale);
                int drawX = (panelW - drawW) / 2;
                int drawY = (panelH - drawH) / 2;

                g.drawImage(backgroundImage, drawX, drawY, drawW, drawH, c);
            }
        }
    }

    public static Image getBackgroundImage() {
        if (backgroundImage == null) {
            loadImage();
        }
        return backgroundImage;
    }
}

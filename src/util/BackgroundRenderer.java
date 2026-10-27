package util;

import javax.imageio.*;
import java.awt.*;
import java.io.*;

public class BackgroundRenderer {
    private static final String DEFAULT_MENU_IMAGE = "resources/Images/mainMenu.png";
    private static final String DEFAULT_GAME_IMAGE = "resources/Images/gameScreen.png";

    private static Image menuBackgroundImage;
    private static Image gameBackgroundImage;

    static {
        loadMenuImage();
        loadGameImage();
    }

    private static void loadMenuImage() {
        File file = new File(DEFAULT_MENU_IMAGE);
        if (file.exists()) {
            try {
                menuBackgroundImage = ImageIO.read(file);
                Logger.getLogger().log("Menu background image loaded: " + DEFAULT_MENU_IMAGE);
            } catch (IOException e) {
                Logger.getLogger().error("Failed to load menu background image: " + e.getMessage());
            }
        } else {
            Logger.getLogger().error("Menu background image file not found: " + DEFAULT_MENU_IMAGE);
        }
    }

    private static void loadGameImage() {
        File file = new File(DEFAULT_GAME_IMAGE);
        if (file.exists()) {
            try {
                gameBackgroundImage = ImageIO.read(file);
                Logger.getLogger().log("Game background image loaded: " + DEFAULT_GAME_IMAGE);
            } catch (IOException e) {
                Logger.getLogger().error("Failed to load game background image: " + e.getMessage());
            }
        } else {
            Logger.getLogger().error("Game background image file not found: " + DEFAULT_GAME_IMAGE);
        }
    }

    public static void renderBackground(Graphics g, Component c) {
        if (menuBackgroundImage == null) {
            loadMenuImage();
        }
        renderImage(g, c, menuBackgroundImage);
    }

    public static void renderGameBackground(Graphics g, Component c) {
        if (gameBackgroundImage == null) {
            loadGameImage();
        }
        renderImage(g, c, gameBackgroundImage);
    }

    private static void renderImage(Graphics g, Component c, Image image) {
        if (image != null && c != null) {
            int panelW = c.getWidth();
            int panelH = c.getHeight();
            int imgW = image.getWidth(null);
            int imgH = image.getHeight(null);

            if (panelW > 0 && panelH > 0 && imgW > 0 && imgH > 0) {
                double scale = Math.max((double) panelW / imgW, (double) panelH / imgH);
                int drawW = (int) Math.round(imgW * scale);
                int drawH = (int) Math.round(imgH * scale);
                int drawX = (panelW - drawW) / 2;
                int drawY = (panelH - drawH) / 2;

                g.drawImage(image, drawX, drawY, drawW, drawH, c);
            }
        }
    }

    public static Image getMenuBackgroundImage() {
        if (menuBackgroundImage == null) {
            loadMenuImage();
        }
        return menuBackgroundImage;
    }

    public static Image getGameBackgroundImage() {
        if (gameBackgroundImage == null) {
            loadGameImage();
        }
        return gameBackgroundImage;
    }
}

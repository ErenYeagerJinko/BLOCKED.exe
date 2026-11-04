package util;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

public class UIButtonRenderer {
    public static final String BACK_BUTTON = "resources/UI/back/button.png";
    public static final String MENU_BUTTON = "resources/UI/menu/button.png";
    public static final String RESTART_BUTTON = "resources/UI/restart/button.png";
    public static final String BLUE_BUTTON = "resources/UI/instructions/button.png";
    public static final String GREEN_BUTTON = "resources/UI/classic/button.png";
    public static final String RED_BUTTON = "resources/UI/exit/button.png";
    public static final String YELLOW_BUTTON = "resources/UI/time-attack/button.png";
    public static final String NEUTRAL_BUTTON = "resources/UI/menu/button.png";

    private static final Map<String, BufferedImage> imageCache = new HashMap<>();
    private static Font customFont;

    static {
        loadCustomFont();
    }

    private static void loadCustomFont() {
        File fontFile = new File("resources/UI/Font/Kenney Future Narrow.ttf");
        if (fontFile.exists()) {
            try {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
                Logger.getLogger().log("Custom font loaded successfully: Kenney Future Narrow");
            } catch (Exception e) {
                Logger.getLogger().error("Failed to load custom font: " + e.getMessage());
                customFont = null;
            }
        } else {
            Logger.getLogger().error("Custom font file not found: resources/UI/Font/Kenney Future Narrow.ttf");
        }
    }

    public static BufferedImage getImage(String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) {
            return null;
        }
        if (imageCache.containsKey(relativePath)) {
            return imageCache.get(relativePath);
        }
        File file = new File(relativePath);
        if (file.exists()) {
            try {
                BufferedImage img = ImageIO.read(file);
                imageCache.put(relativePath, img);
                Logger.getLogger().log("UI button texture loaded and cached: " + relativePath);
                return img;
            } catch (IOException e) {
                Logger.getLogger().error("Failed to read UI button texture " + relativePath + ": " + e.getMessage());
            }
        } else {
            Logger.getLogger().error("UI button texture file not found: " + relativePath);
        }
        return null;
    }

    public static Font getFont(float size) {
        if (customFont != null) {
            return customFont.deriveFont(Font.PLAIN, size);
        }
        return new Font("SansSerif", Font.PLAIN, (int) size);
    }

    public static Font getFont(int style, float size) {
        if (customFont != null) {
            return customFont.deriveFont(style, size);
        }
        return new Font("SansSerif", style, (int) size);
    }

    public static Font getButtonFont(float size) {
        return getFont(Font.BOLD, size);
    }

    public static JButton createButton(String text, String imagePath, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                BufferedImage img = getImage(imagePath);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                if (img != null) {
                    g2d.drawImage(img, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g2d.setColor(new Color(60, 60, 70));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(0, 0, 0, 70));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(255, 255, 255, 40));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        configureButton(button, width, height);
        return button;
    }

    public static void styleButton(JButton button, String imagePath, int width, int height) {
        BufferedImage img = getImage(imagePath);
        if (img != null && width > 0 && height > 0) {
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaled));
        }
        configureButton(button, width, height);
    }

    private static void configureButton(JButton button, int width, int height) {
        button.setFont(getButtonFont(14f));
        int effectiveWidth = width;
        String text = button.getText();
        if (text != null && !text.isEmpty()) {
            FontMetrics fm = button.getFontMetrics(button.getFont());
            int textWidth = fm.stringWidth(text);
            if (textWidth + 36 > effectiveWidth) {
                effectiveWidth = textWidth + 36;
            }
        }

        Dimension dim = new Dimension(effectiveWidth, height);
        button.setPreferredSize(dim);
        button.setMinimumSize(dim);
        button.setMaximumSize(dim);
        button.setSize(dim);

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setMargin(new Insets(0, 4, 0, 4));
        button.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));

        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setForeground(new Color(40, 40, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}

import util.*;
import view.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Logger.getLogger().log("Application launching: BLOCKED.exe");
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
            Logger.getLogger().log("Main GameFrame displayed");
        });
    }
}

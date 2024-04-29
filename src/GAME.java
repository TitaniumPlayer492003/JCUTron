import javax.swing.JFrame;

public class GAME {
    public static void launch() {
        JFrame window = new JFrame("JCUTron");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // Add GamePanelto the window
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack(); // by doing this the window adjusts it size to the GamePanel

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame(); // once the window is created we call the launchGame method
    }

    public static void main(String... avi) {
        launch();
    }
}
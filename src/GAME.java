import javax.swing.JFrame;

public class GAME {
    public static void NEW() {

        JFrame gameWindow = new JFrame("JCUTron");
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation

        // Add GamePanel to the window
        GamePanel gp = new GamePanel();
        gp.launchGame(); // Once the window is created, call the launchGame method

        // Add the GamePanel to the CENTER of the JFrame's content pane
        gameWindow.add(gp);
        gameWindow.pack();
        gameWindow.setLocationRelativeTo(null); // Center the window on the screen
        gameWindow.setResizable(false);

        // Make the window visible
        gameWindow.setVisible(true);
    }

    public static void main(String... avi) {
        NEW();
    }
}

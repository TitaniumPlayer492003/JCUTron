
/*
 * To run the Game Loop: -> Use Thread Class -> Implement Runnable -> Create run method
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 30;
    Thread gameThread;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
    }

    public void launchGame() {
        gameThread = new Thread(this); // instantiating thread
        gameThread.start(); // When we 'start' this thread; the run() method is called
    }

    /**
     * This method contains the Game Loop
     */
    @Override
    public void run() {
        /*
         * Game Loop: is a sequence of processes that
         * run continuously as long as the game is running.
         * 
         * Here we use System.nanoTime() to measure the
         * elapsed time and call update() and repaint() methods
         * once every drawInterval second.
         */
        double drawInterval = 1000000000.0 / FPS; // 1000000000.0 nanoseconds in a second
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1){
                update();
                repaint();
                delta --;
            }
        }
    }

    private void update() {

    }

    /**
     * paintComponent is a method in JComponent that JPanel inherits
     * and is used to draw objects on the panel.
     * We call this method at a certain time interval(FPS).
     */
    private void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

}
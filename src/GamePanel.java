/*
 * To run the Game Loop: -> Use Thread Class -> Implement Runnable -> Create run method
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 30;
    Thread gameThread;
    Board board = new Board();

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
        double delta = 0; // Represents the time elapsed since the last frame was drawn.
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            /*
             * (currentTime - lastTime): This calculates the difference in time (in
             * nanoseconds) between the current time (currentTime)
             * and the time of the last frame (lastTime). This tells us how much time has
             * passed since the last frame was drawn.
             * 
             * /drawInterval: This divides the time difference by the drawInterval, which
             * represents the target time between frames (in nanoseconds).
             * /drawInterval: helps us measure how many times the target frame
             * time fits into the actual time difference between frames. This allows us to
             * keep track of elapsed time in a way that's independent of the actual frame
             * rate.
             * 
             * delta += ...: This adds the result of the division to delta. delta is a
             * variable that accumulates the time between frames.
             * By adding the result of the division, we're effectively keeping track of how
             * many "target intervals" have passed since the last frame.
             * 
             * lastTime = currentTime: This updates the lastTime variable to the current
             * time. This ensures that we're measuring the time difference
             * from the last frame to the current frame in subsequent iterations of the game
             * loop.
             */

            if (delta >= 1) {
                update();
                repaint();
                delta--;
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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        board.draw(g2);
    }

}
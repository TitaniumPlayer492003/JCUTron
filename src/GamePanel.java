
/*
 * To run the Game Loop: -> Use Thread Class -> Implement Runnable -> Create run method
 */
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 30;
    Thread gameThread;
    Board board = new Board();
    Mouse mouse = new Mouse();

    // PIECES
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    Piece activeP;

    // COLOR
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;

    // Booleans
    boolean canMove;
    boolean validSquare;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);

        setPieces();
        copyPieces(pieces, simPieces);
    }

    public void launchGame() {
        gameThread = new Thread(this); // instantiating thread
        gameThread.start(); // When we 'start' this thread; the run() method is called
    }

    public void setPieces() {

        // White
        pieces.add(new Pieces.Pawn(WHITE, 0, 6));
        pieces.add(new Pieces.Pawn(WHITE, 1, 6));
        pieces.add(new Pieces.Pawn(WHITE, 2, 6));
        pieces.add(new Pieces.Pawn(WHITE, 3, 6));
        pieces.add(new Pieces.Pawn(WHITE, 4, 6));
        pieces.add(new Pieces.Pawn(WHITE, 5, 6));
        pieces.add(new Pieces.Pawn(WHITE, 6, 6));
        pieces.add(new Pieces.Pawn(WHITE, 7, 6));
        pieces.add(new Pieces.Rook(WHITE, 4, 4));
        pieces.add(new Pieces.Knight(WHITE, 1, 7));
        pieces.add(new Pieces.Bishop(WHITE, 2, 7));
        pieces.add(new Pieces.Queen(WHITE, 3, 7));
        pieces.add(new Pieces.King(WHITE, 4, 7));
        pieces.add(new Pieces.Bishop(WHITE, 5, 7));
        pieces.add(new Pieces.Knight(WHITE, 6, 7));
        pieces.add(new Pieces.Rook(WHITE, 7, 7));

        // BlackPieces.
        pieces.add(new Pieces.Rook(BLACK, 0, 0));
        pieces.add(new Pieces.Knight(BLACK, 1, 0));
        pieces.add(new Pieces.Bishop(BLACK, 2, 0));
        pieces.add(new Pieces.Queen(BLACK, 3, 0));
        pieces.add(new Pieces.King(BLACK, 4, 0));
        pieces.add(new Pieces.Bishop(BLACK, 5, 0));
        pieces.add(new Pieces.Knight(BLACK, 6, 0));
        pieces.add(new Pieces.Rook(BLACK, 7, 0));
        pieces.add(new Pieces.Pawn(BLACK, 0, 1));
        pieces.add(new Pieces.Pawn(BLACK, 1, 1));
        pieces.add(new Pieces.Pawn(BLACK, 2, 1));
        pieces.add(new Pieces.Pawn(BLACK, 3, 1));
        pieces.add(new Pieces.Pawn(BLACK, 4, 1));
        pieces.add(new Pieces.Pawn(BLACK, 5, 1));
        pieces.add(new Pieces.Pawn(BLACK, 6, 1));
        pieces.add(new Pieces.Pawn(BLACK, 7, 1));
    }

    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {

        target.clear();
        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
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

        /// MOUSE PRESSED ///
        if (mouse.pressed) {
            if (activeP == null) {
                // If the activeP is null, check if you can pick up a piece
                for (Piece piece : simPieces) {
                    // If the mouse is on an ally piece, pick it up as the activeP
                    if (piece.color == currentColor && piece.col == mouse.x / Board.SQUARE_SIZE
                            && piece.row == mouse.y / Board.SQUARE_SIZE) {

                        activeP = piece;
                    }
                }
            } else {
                // If the player is holding, a piece, simulate the move
                simulate();
            }
        }

        /// MOUSE BUTTON RELEASED ///
        if(mouse.pressed == false){
            if(activeP != null){
                if(validSquare){

                    // MOVE CONFIRMED
                    
                    // Update the piece list in case a piece has been  captured and removed during the simulation 
                    copyPieces(simPieces, pieces);
                    activeP.updatePosition();
                }
                else{
                    // The move is not valid so reset everything
                    copyPieces(pieces, simPieces);
                    activeP.resetPosition();
                    activeP = null;
                }
            }
        }
    }

    /**
     * This method simulates a thinking phase. This phase is important in turn-based
     * strategy games. Here a player has not made a move (yet), but picked up a
     * piece and
     * is 'thinking' where to put it.
     */
    private void simulate() {

        canMove = false;
        validSquare = false;

        //Reset the piece list in every loop
        //This is basically for restoring the removed place during the simulation
        copyPieces(pieces, simPieces);

        // If a piece is being held, update its position
        activeP.x = mouse.x - Board.HALF_SQUARE_SIZE;
        activeP.y = mouse.y - Board.HALF_SQUARE_SIZE;
        activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getRow(activeP.y);

        // Check if the piece is hovering over a reachable square
        if (activeP.canMove(activeP.col, activeP.row)) {
            canMove = true;
            // validSquare = true;

            if (activeP.hittingP != null){
                simPieces.remove(activeP.hittingP.getIndex());
            }
            validSquare = true;
        }

    }

    /**
     * paintComponent is a method in JComponent that JPanel inherits
     * and is used to draw objects on the panel.
     * We call this method at a certain time interval(FPS).
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // BOARD
        board.draw(g2);

        // PIECES
        for (Piece p : simPieces) {
            p.draw(g2);
        }

        if (activeP != null) {
            if(canMove){
                g2.setColor(Color.green);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
                g2.fillRect(activeP.col * Board.SQUARE_SIZE, activeP.row * Board.SQUARE_SIZE, Board.SQUARE_SIZE,
                    Board.SQUARE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }

            activeP.draw(g2);
        }
    }

}
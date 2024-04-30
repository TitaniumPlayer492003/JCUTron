
/*
 * To run the Game Loop: -> Use Thread Class -> Implement Runnable -> Create run method
 */
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
    ArrayList<Piece> promoPieces = new ArrayList<>();
    Piece activeP, checkingP;
    public static Piece castlingP;

    // COLOR
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;

    // Booleans
    boolean canMove;
    boolean validSquare;
    boolean promotion;
    boolean gameover;
    boolean stalemate;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);

        // testPromotion();
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
        pieces.add(new Pieces.Rook(WHITE, 0, 7));
        pieces.add(new Pieces.Knight(WHITE, 1, 7));
        pieces.add(new Pieces.Bishop(WHITE, 2, 7));
        pieces.add(new Pieces.Queen(WHITE, 3, 7));
        pieces.add(new Pieces.King(WHITE, 4, 7));
        pieces.add(new Pieces.Bishop(WHITE, 5, 7));
        pieces.add(new Pieces.Knight(WHITE, 6, 7));
        pieces.add(new Pieces.Rook(WHITE, 7, 7));

        // BlackPieces
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

    public void testPromotion() {
        pieces.add(new Pieces.Pawn(WHITE, 0, 6));
        pieces.add(new Pieces.Pawn(BLACK, 1, 1));
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

        if (promotion) {
            promoting();
        } else if (gameover == false && stalemate == false) {

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
            if (mouse.pressed == false) {
                if (activeP != null) {
                    if (validSquare) {

                        // MOVE CONFIRMED

                        // Update the piece list in case a piece has been captured and removed during
                        // the simulation
                        copyPieces(simPieces, pieces);
                        activeP.updatePosition();

                        if (castlingP != null) {
                            castlingP.updatePosition();
                        }

                        // isKingInCheck();
                        if (isKingInCheck() && isCheckmate()) {
                            gameover = true;
                        } else if (isStalemate() && isKingInCheck() == false) {
                            stalemate = true;
                        } else {
                            // The game continues
                            if (canPromote()) {
                                promotion = true;
                            } else {
                                changePlayer();
                            }
                        }

                    } else {
                        // The move is not valid so reset everything
                        copyPieces(pieces, simPieces);
                        activeP.resetPosition();
                        activeP = null;
                    }
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

        // Reset the piece list in every loop
        // This is basically for restoring the removed place during the simulation
        copyPieces(pieces, simPieces);

        // Reset the castlingP's position
        if (castlingP != null) {
            castlingP.col = castlingP.preCol;
            castlingP.x = castlingP.getX(castlingP.col);
            castlingP = null;
        }

        // If a piece is being held, update its position
        activeP.x = mouse.x - Board.HALF_SQUARE_SIZE;
        activeP.y = mouse.y - Board.HALF_SQUARE_SIZE;
        activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getRow(activeP.y);

        // Check if the piece is hovering over a reachable square
        if (activeP.canMove(activeP.col, activeP.row)) {
            canMove = true;
            // validSquare = true;

            if (activeP.hittingP != null) {
                simPieces.remove(activeP.hittingP.getIndex());
            }

            checkCastling();

            if (isIllegal(activeP) == false && opponentCanCaptureKing() == false) {
                validSquare = true;
            }
        }
    }

    private boolean opponentCanCaptureKing() {

        Piece king = getKing(false);

        for (Piece piece : simPieces) {
            if (piece.color != king.color && piece.canMove(king.col, king.row)) {
                return true;
            }
        }
        return false;
    }

    private boolean isIllegal(Piece king) {

        if (king.type == Type.KING) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece != king && piece.color != king.color && piece.canMove(king.col, king.row)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isKingInCheck() {

        Piece king = getKing(true);

        if (activeP.canMove(king.col, king.row)) {
            checkingP = activeP;
            return true;
        } else {
            checkingP = null;
            return false;
        }
    }

    private Piece getKing(boolean opponent) {
        Piece king = null;

        for (Piece piece : simPieces) {
            if (opponent) {
                if (piece.type == Type.KING && piece.color != currentColor) {
                    king = piece;
                }
            } else {
                if (piece.type == Type.KING && piece.color == currentColor) {
                    king = piece;
                }
            }
        }
        return king;
    }

    private boolean isCheckmate() {
        Piece king = getKing(true);

        if (kingCanMove(king)) {
            return false;
        } else {
            // Check if the player can block

            // Check the position of the checkingP and the king
            int colDiff = Math.abs(checkingP.col - king.col);
            int rowDiff = Math.abs(checkingP.row - king.row);

            if (colDiff == 0) {
                // checkingP is attacking vertically

                if (checkingP.row < king.row) {
                    // The checkingP is above the king
                    for (int row = checkingP.row; row < king.row; row++) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor && piece.canMove(checkingP.col, row)) {
                                return false;
                            }
                        }
                    }
                }

                if (checkingP.row > king.row) {
                    // The checkingP is below the king
                    for (int row = checkingP.row; row > king.row; row--) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor && piece.canMove(checkingP.col, row)) {
                                return false;
                            }
                        }
                    }

                }
            } else if (rowDiff == 0) {
                // checkingP is attacking horizontally

                if (checkingP.col < king.col) {
                    // The checkingP is to the left
                    for (int col = checkingP.col; col < king.col; col++) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor && piece.canMove(col, checkingP.row)) {
                                return false;
                            }
                        }
                    }

                }
                if (checkingP.col > king.col) {
                    // The checkinP is to the right
                    for (int col = checkingP.col; col > king.col; col--) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor && piece.canMove(col, checkingP.row)) {
                                return false;
                            }
                        }
                    }
                }
            } else if (colDiff == rowDiff) {
                // checkingP is attacking diagonally

                if (checkingP.row < king.row) {
                    // The checking piece is above the king

                    if (checkingP.col < king.col) {
                        // The checkingP is in the upper-left
                        for (int col = checkingP.col, row = checkingP.row; col < king.col; col++, row++) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }
                    if (checkingP.col > king.col) {
                        // The checkingP is in the upper right
                        for (int col = checkingP.col, row = checkingP.row; col > king.col; col--, row++) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }
                }

                if (checkingP.row > king.row) {
                    // The checkingP is below the king

                    if (checkingP.col > king.row) {
                        // The checkingP is in the lower left
                        for (int col = checkingP.col, row = checkingP.row; col < king.col; col++, row--) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }

                    if (checkingP.col > king.row) {
                        // The checkingP is in th lower right
                        for (int col = checkingP.col, row = checkingP.row; col > king.col; col--, row--) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            } else {
                // checkingP is a knight
                // This 'else' is unnecessary as a knight's attack cannot be blocked
            }
        }
        return true;
    }

    private boolean kingCanMove(Piece king) {

        // Simlulate if there is any square where the king can move to
        if (isValidMove(king, -1, -1))
            return true;
        if (isValidMove(king, 0, -1))
            return true;
        if (isValidMove(king, 1, -1))
            return true;
        if (isValidMove(king, -1, 0))
            return true;
        if (isValidMove(king, 1, 0))
            return true;
        if (isValidMove(king, -1, 1))
            return true;
        if (isValidMove(king, 0, 1))
            return true;
        if (isValidMove(king, 1, 1))
            return true;

        return false;
    }

    private boolean isValidMove(Piece king, int colPlus, int rowPlus) {
        boolean isValidMove = false;

        // Update the king's position for a second
        king.col += colPlus;
        king.row += rowPlus;

        if (king.canMove(king.col, king.row)) {

            if (king.hittingP != null) {
                simPieces.remove(king.hittingP.getIndex());
            }
            if (isIllegal(king) == false) {
                isValidMove = true;
            }
        }
        // Reset the king's position and restore the removed piece
        king.resetPosition();
        copyPieces(pieces, simPieces);

        return isValidMove;
    }

    private boolean isStalemate() {
        // TODO: Stalemate is possible even if there is more than just one(king) piece on the board.  
        int count = 0;
        for (Piece piece : simPieces) {
            if (piece.color != currentColor) {
                count++;
            }
            if (count == 1) {
                if (kingCanMove(getKing(true)) == false) {
                    return true;
                }
            }
        }
        return false;
    }

    private void checkCastling() {
        if (castlingP != null) {
            if (castlingP.col == 0) {
                castlingP.col += 3;
            } else if (castlingP.col == 7) {
                castlingP.col -= 2;
            }
            castlingP.x = castlingP.getX(castlingP.col);
        }
    }

    private void changePlayer() {
        if (currentColor == WHITE) {
            currentColor = BLACK;
            // Reset Black's twoStepped status
            for (Piece piece : GamePanel.simPieces) {
                if (piece.color == BLACK) {
                    piece.twoStepped = false;
                }
            }
        } else {
            currentColor = WHITE;
            // Reset White's twoStepped status
            for (Piece piece : GamePanel.simPieces) {
                if (piece.color == WHITE) {
                    piece.twoStepped = false;
                }
            }
        }
        activeP = null;
    }

    private boolean canPromote() {

        if (activeP.type == Type.PAWN) {
            if ((currentColor == WHITE && activeP.row == 0) || (currentColor == BLACK && activeP.row == 7)) {
                promoPieces.clear();
                promoPieces.add(new Pieces.Queen(currentColor, 9, 2));
                promoPieces.add(new Pieces.Knight(currentColor, 9, 3));
                promoPieces.add(new Pieces.Rook(currentColor, 9, 4));
                promoPieces.add(new Pieces.Bishop(currentColor, 9, 5));
                return true;
            }
        }

        return false;
    }

    private void promoting() {
        if (mouse.pressed) {
            for (Piece piece : promoPieces) {
                if (piece.col == mouse.x / Board.SQUARE_SIZE && piece.row == mouse.y / Board.SQUARE_SIZE) {
                    switch (piece.type) {
                        case QUEEN:
                            simPieces.add(new Pieces.Queen(currentColor, activeP.col, activeP.row));
                            break;
                        case KNIGHT:
                            simPieces.add(new Pieces.Knight(currentColor, activeP.col, activeP.row));
                            break;
                        case ROOK:
                            simPieces.add(new Pieces.Rook(currentColor, activeP.col, activeP.row));
                            break;
                        case BISHOP:
                            simPieces.add(new Pieces.Bishop(currentColor, activeP.col, activeP.row));
                            break;
                        default:
                            break;
                    }
                    simPieces.remove(activeP.getIndex());
                    copyPieces(simPieces, pieces);
                    activeP = null;
                    promotion = false;
                    changePlayer();
                }
            }
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
            if (canMove) {
                if (gameover == false && checkingP != null && activeP.type != Type.KING) {
                    Piece k = getKing(false);
                    g2.setColor(Color.BLUE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                    g2.fillRect(k.col * Board.SQUARE_SIZE, k.row * Board.SQUARE_SIZE, Board.SQUARE_SIZE,
                            Board.SQUARE_SIZE);
                    k.draw(g2);
                }
                if (isIllegal(activeP)) {
                    g2.setColor(Color.gray);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                } else {
                    g2.setColor(Color.green);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
                }
                g2.fillRect(activeP.col * Board.SQUARE_SIZE, activeP.row * Board.SQUARE_SIZE, Board.SQUARE_SIZE,
                        Board.SQUARE_SIZE);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
            activeP.draw(g2);
        }
        if (promotion) {
            g2.drawString("Promote to:", 840, 150);
            for (Piece piece : promoPieces) {
                g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.row), Board.SQUARE_SIZE,
                        Board.SQUARE_SIZE, null);
            }
        }
        if (gameover) {
            Piece k = getKing(true);
            g2.setColor(Color.BLUE);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g2.fillRect(k.col * Board.SQUARE_SIZE, k.row * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
            k.draw(g2);

            String s = "";
            if (currentColor == WHITE) {
                s = "White WINS!";
            } else {
                s = "Black Wins!";
            }
            g2.setFont(new Font("Arial", Font.BOLD, 90));
            g2.setColor(Color.YELLOW);
            g2.drawString(s, 200, 400);
        }

        if (stalemate) {
            g2.setFont(new Font("Arial", Font.BOLD, 90));
            g2.setColor(Color.ORANGE);
            g2.drawString("Draw by Stalemate.", 200, 400);
        }
    }

}

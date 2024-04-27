
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Piece {

    public BufferedImage image;
    public int x, y;
    public int col, row, preCol, preRow;
    public int color;
    public Piece hittingP;

    public Piece(int color, int col, int row) {

        this.color = color;
        this.col = col;
        this.row = row;
        x = getX(col);
        y = getY(row);
        preCol = col;
        preRow = row;
    }

    public BufferedImage getImage(String imagePath) {

        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public int getX(int col) {
        return col * Board.SQUARE_SIZE;
    }

    public int getY(int row) {
        return row * Board.SQUARE_SIZE;
    }

    /*
     * In the next 2 methods we add HALF_SQUARE_SIZE get to the center point of the
     * image. Else, the top left part of the image will be considered, and wherever
     * that is the activeP will be placed there.
     */
    public int getCol(int x) {
        return (x + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }

    public int getRow(int y) {
        return (y + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }

    public int getIndex() {
        for (int index = 0; index < GamePanel.simPieces.size(); index++) {
            if (GamePanel.simPieces.get(index) == this) {
                return index;
            }
        }
        return 0;
    }

    public void updatePosition() {

        x = getX(col);
        y = getY(row);
        // Updating the pre as the piece has moved; confirming the move
        preCol = getCol(x);
        preRow = getRow(y);
    }

    public void resetPosition() {
        col = preCol;
        row = preRow;
        x = getX(col);
        y = getY(row);
    }

    public boolean canMove(int targetCol, int targetRow) {
        return false;
    }

    /**
     * Checks whether a piece is on the board or not.
     * 
     * @param targetCol
     * @param targetRow
     * @return true/false
     */
    public boolean isWithinBoard(int targetCol, int targetRow) {
        if (0 <= targetCol && targetCol <= 7 && 0 <= targetRow && targetRow <= 7) {
            return true;
        }
        return false;
    }

    public boolean isSameSquare(int targetCol, int targetRow) {
        if (targetCol == preCol && targetRow == preRow) {
            return true;
        }
        return false;
    }

    public Piece getHittingP(int targetCol, int targetRow) {
        for (Piece piece : GamePanel.simPieces) {
            if (piece.col == targetCol && piece.row == targetRow && piece != this) {
                return piece;
            }
        }
        return null;
    }

    public boolean isValidSquare(int targetCol, int targetRow) {

        hittingP = getHittingP(targetCol, targetRow);

        if (hittingP == null) {
            return true; // Square is vacant
        } else {

            if (hittingP.color != this.color) {
                return true; // If the colour is different, it can be captured
            } else {
                hittingP = null;
            }
        }
        return false;
    }

    public boolean pieceIsOnStraightLine(int targetCol, int targetRow) {

        // When piece is moving left
        for (int c = preCol - 1; c > targetCol; c--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == c && piece.row == targetRow) {
                    hittingP = piece;
                    return true;
                }
            }
        }
        // When piece is moving right
        for (int c = preCol + 1; c < targetCol; c++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == c && piece.row == targetRow) {
                    hittingP = piece;
                    return true;
                }
            }
        }
        // When piece is moving up
        for (int r = preRow - 1; r > targetRow; r--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == r && piece.col == targetCol) {
                    hittingP = piece;
                    return true;
                }
            }
        }
        // When piece is moving down
        for (int r = preRow + 1; r < targetRow; r++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == r && piece.col == targetCol) {
                    hittingP = piece;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean pieceIsOnDiagonalLine(int targetCol, int targetRow){
        
        if (targetRow < preRow){
            // When piece is moving up-left
            for (int c = preCol - 1; c > targetCol; c--){
                int diff = Math.abs(c - preCol);
                for(Piece piece : GamePanel.simPieces){
                    if(piece.col == c && piece.row == preRow - diff){
                        hittingP = piece;
                        return true;
                    }
                }
            }
            // When piece is moving up-right
            for (int c = preCol + 1; c < targetCol; c++){
                int diff = Math.abs(c - preCol);
                for(Piece piece : GamePanel.simPieces){
                    if(piece.col == c && piece.row == preRow - diff){
                        hittingP = piece;
                        return true;
                    }
                }
            }
        }
        if (targetRow > preRow){
            // When piece is moving down-left
            for (int c = preCol - 1; c > targetCol; c--){
                int diff = Math.abs(c - preCol);
                for(Piece piece : GamePanel.simPieces){
                    if(piece.col == c && piece.row == preRow + diff){
                        hittingP = piece;
                        return true;
                    }
                }
            }
            // When piece is moving down-right
            for (int c = preCol + 1; c < targetCol; c++){
                int diff = Math.abs(c - preCol);
                for(Piece piece : GamePanel.simPieces){
                    if(piece.col == c && piece.row == preRow + diff){
                        hittingP = piece;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
    }
}
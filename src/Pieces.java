public class Pieces {

    public static class King extends Piece {

        public King(int color, int col, int row) {
            super(color, col, row);

            if (color == GamePanel.WHITE) {
                image = getImage("/piece/w-king");
            } else {
                image = getImage("/piece/b-king");
            }
        }

        public boolean canMove(int targetCol, int targetRow) {
            if (isWithinBoard(targetCol, targetRow)) {

                if (Math.abs(targetCol - preCol) + (Math.abs(targetRow - preRow)) == 1 ||
                        (Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow)) == 1) {
                            
                            if(isValidSquare(targetCol, targetRow)){
                                return true;
                            }
                }
            }
            return false;
        }
    }

    public static class Queen extends Piece {

        public Queen(int color, int col, int row) {
            super(color, col, row);

            if (color == GamePanel.WHITE) {
                image = getImage("/piece/w-queen");
            } else {
                image = getImage("/piece/b-queen");
            }
        }
    }

    public static class Rook extends Piece {

        public Rook(int color, int col, int row) {
            super(color, col, row);

            if (color == GamePanel.WHITE) {
                image = getImage("/piece/w-rook");
            } else {
                image = getImage("/piece/b-rook");
            }
        }
    }

    public static class Bishop extends Piece {

        public Bishop(int color, int col, int row) {
            super(color, col, row);

            if (color == GamePanel.WHITE) {
                image = getImage("/piece/w-bishop");
            } else {
                image = getImage("/piece/b-bishop");
            }
        }
    }

    public static class Knight extends Piece {

        public Knight(int color, int col, int row) {
            super(color, col, row);

            if (color == GamePanel.WHITE) {
                image = getImage("/piece/w-knight");
            } else {
                image = getImage("/piece/b-knight");
            }
        }
    }

    public static class Pawn extends Piece {

        public Pawn(int color, int col, int row) {
            super(color, col, row);

            if (color == GamePanel.WHITE) {
                image = getImage("piece/w-pawn");
            } else {
                image = getImage("piece/b-pawn");
            }
        }
    }

}

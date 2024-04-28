public class Pieces {

    public static class King extends Piece {

        public King(int color, int col, int row) {
            super(color, col, row);

            type = Type.KING;

            if (color == GamePanel.WHITE) {
                image = getImage("/piece/w-king");
            } else {
                image = getImage("/piece/b-king");
            }
        }

        public boolean canMove(int targetCol, int targetRow) {
            if (isWithinBoard(targetCol, targetRow)) {

                // General Movement
                if (Math.abs(targetCol - preCol) + (Math.abs(targetRow - preRow)) == 1 ||
                        (Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow)) == 1) {

                    if (isValidSquare(targetCol, targetRow)) {
                        return true;
                    }
                }

                // Castling
                if (moved == false) {
                    // King-side castling
                    if (targetCol == preCol + 2 && targetRow == preRow
                            && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                        for (Piece piece : GamePanel.simPieces) {
                            if (piece.col == preCol + 3 && piece.row == preRow && piece.moved == false) {
                                GamePanel.castlingP = piece;
                                return true;
                            }
                        }
                    }

                    // Queen-side castling
                    if (targetCol == preCol - 2 && targetRow == preRow
                            && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                        Piece p[] = new Piece[2];
                        for (Piece piece : GamePanel.simPieces) {

                            if (piece.col == preCol - 3 && piece.row == targetRow) {
                                p[0] = piece;
                            }
                            if (piece.col == preCol - 4 && piece.row == targetRow) {
                                p[1] = piece;
                            }

                            if (p[0] == null && p[1] != null && p[1].moved == false) {
                                GamePanel.castlingP = p[1];
                                return true;
                            }
                        }
                    }
                }
            }

            return false;
        }
    }

    public static class Queen extends Piece {

        public Queen(int color, int col, int row) {
            super(color, col, row);

            type = Type.QUEEN;

            if (color == GamePanel.WHITE) {
                image = getImage("/piece/w-queen");
            } else {
                image = getImage("/piece/b-queen");
            }
        }

        public boolean canMove(int targetCol, int targetRow) {

            if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {

                // Vertical & Horizontal movement
                if (targetCol == preCol || targetRow == preRow) {
                    if (isValidSquare(targetCol, targetRow) && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                        return true;
                    }
                }
                // Diagonal Movement
                if (Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow)) {
                    if (isValidSquare(targetCol, targetRow) && pieceIsOnDiagonalLine(targetCol, targetRow) == false) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public static class Rook extends Piece {

        public Rook(int color, int col, int row) {
            super(color, col, row);

            type = Type.ROOK;

            if (color == GamePanel.WHITE) {
                image = getImage("/piece/w-rook");
            } else {
                image = getImage("/piece/b-rook");
            }
        }

        public boolean canMove(int targetCol, int targetRow) {
            if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {
                if (targetCol == preCol || targetRow == preRow) {
                    if (isValidSquare(targetCol, targetRow) && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static class Bishop extends Piece {

        public Bishop(int color, int col, int row) {
            super(color, col, row);

            type = Type.BISHOP;

            if (color == GamePanel.WHITE) {
                image = getImage("/piece/w-bishop");
            } else {
                image = getImage("/piece/b-bishop");
            }
        }

        public boolean canMove(int targetCol, int targetRow) {
            if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {
                if (Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow)) {
                    if (isValidSquare(targetCol, targetRow) && pieceIsOnDiagonalLine(targetCol, targetRow) == false) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static class Knight extends Piece {

        public Knight(int color, int col, int row) {
            super(color, col, row);

            type = Type.KNIGHT;

            if (color == GamePanel.WHITE) {
                image = getImage("/piece/w-knight");
            } else {
                image = getImage("/piece/b-knight");
            }
        }

        public boolean canMove(int targetCol, int targetRow) {
            if (isWithinBoard(targetCol, targetRow)) {
                if ((Math.abs(targetCol - preCol)) * (Math.abs(targetRow - preRow)) == 2) {
                    if (isValidSquare(targetCol, targetRow)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static class Pawn extends Piece {

        public Pawn(int color, int col, int row) {
            super(color, col, row);

            type = Type.PAWN;

            if (color == GamePanel.WHITE) {
                image = getImage("piece/w-pawn");
            } else {
                image = getImage("piece/b-pawn");
            }
        }

        public boolean canMove(int targetCol, int targetRow) {
            if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {

                // Define the moveValue based on the pawn's color
                int moveValue;
                if (color == GamePanel.WHITE)
                    moveValue = -1;
                else
                    moveValue = 1;

                // Check the hittingP
                hittingP = getHittingP(targetCol, targetRow);

                // 1 square movement
                if (targetCol == preCol && targetRow == preRow + moveValue && hittingP == null)
                    return true;

                // 2 square movement
                if (targetCol == preCol && targetRow == preRow + moveValue * 2 && hittingP == null && moved == false
                        && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                    return true;
                }

                // Capturing Pieces
                if (Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue && hittingP != null
                        && hittingP.color != color) {
                    return true;
                }

                // En passant
                if (Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue) {
                    for (Piece piece : GamePanel.simPieces) {
                        if (piece.col == targetCol && piece.row == preRow && piece.twoStepped == true) {
                            hittingP = piece;
                            return true;
                        }
                    }
                }

            }
            return false;
        }
    }
}

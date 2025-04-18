package model.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board {
    private Square[][] squareChessBoard;

    public Board() {
        this.squareChessBoard = new Square[8][8];
        initializeBoard();
    }

    /**
     * Initializes the chessboard with valid `Square` objects.
     */
    private void initializeBoard() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                // Determine square color based on coordinates (0 = black, 1 = white)
                int color = (x % 2 == y % 2) ? 1 : 0;
                squareChessBoard[x][y] = new Square(this, color, x, y);
            }
        }
    }
}
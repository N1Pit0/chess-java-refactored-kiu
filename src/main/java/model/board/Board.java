package model.board;

import lombok.Getter;
import lombok.Setter;
import model.pieces.common.Piece;
import services.CheckmateDetector;
import view.gui.GameWindow;



@Getter
@Setter
public class Board {
    private final Square[][] squareChessBoard;

    private Piece currPiece;

    private int currX;
    private int currY;


    public Board() {
        squareChessBoard = new Square[8][8];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int xMod = x % 2;
                int yMod = y % 2;

                if ((xMod == 0 && yMod == 0) || (xMod == 1 && yMod == 1)) {
                    squareChessBoard[x][y] = new Square(this, 1, y, x);
                } else {
                    squareChessBoard[x][y] = new Square(this, 0, y, x);
                }
            }
        }


    }

}
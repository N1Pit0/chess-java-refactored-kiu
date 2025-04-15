package model.board;

import lombok.Getter;
import lombok.Setter;
import model.pieces.common.Piece;
import services.CheckmateDetector;
import view.gui.GameWindow;

import java.util.LinkedList;
import java.util.List;


@Getter
@Setter
public class Board {
    // Logical and graphical representations of board
    private final Square[][] squareChessBoard;
    private final GameWindow gameWindow;

    private Piece currPiece;

    private int currX;
    private int currY;

    private CheckmateDetector ckeckmateDetector;

    public Board(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
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
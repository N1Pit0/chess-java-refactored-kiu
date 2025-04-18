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

    private final Square[][] squareChessBoard;
    private final GameWindow gameWindow;

    private Piece currPiece;

    private int currX;
    private int currY;

    private CheckmateDetector ckeckmateDetector;

    public Board(GameWindow gameWindow) {
        this.gameWindow = gameWindow;

        this.squareChessBoard = new Square[8][8];
    }

}
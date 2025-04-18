package model.pieces;

import model.board.Square;
import model.enums.PieceColor;
import model.pieces.common.Piece;

public class Knight extends Piece {

    public Knight(PieceColor color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }

}

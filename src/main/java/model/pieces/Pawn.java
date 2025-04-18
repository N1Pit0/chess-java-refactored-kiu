package model.pieces;

import model.board.Square;
import model.enums.PieceColor;
import model.pieces.common.Piece;

public class Pawn extends Piece {
    private boolean wasMoved;

    public Pawn(PieceColor color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }


}

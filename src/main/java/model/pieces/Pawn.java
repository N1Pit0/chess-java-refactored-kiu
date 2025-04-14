package model.pieces;

import model.board.Square;
import model.pieces.common.Piece;

public class Pawn extends Piece {
    private boolean wasMoved;

    public Pawn(int color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }


}

package model.pieces;

import model.board.Board;
import model.board.Square;
import model.pieces.common.Piece;

import java.util.LinkedList;
import java.util.List;

public class Queen extends Piece {

    public Queen(int color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }

    @Override
    public List<Square> getLegalMoves(Board b) {
        LinkedList<Square> legalMoves = new LinkedList<Square>();
        Square[][] board = b.getSquareChessBoard();

        int x = this.getCurrentSquare().getXNum();
        int y = this.getCurrentSquare().getYNum();

        int[] occups = getLinearOccupations(board, x, y);

        for (int i = occups[0]; i <= occups[1]; i++) {
            if (i != y) legalMoves.add(board[i][x]);
        }

        for (int i = occups[2]; i <= occups[3]; i++) {
            if (i != x) legalMoves.add(board[y][i]);
        }

        List<Square> bMoves = getDiagonalOccupations(board, x, y);

        legalMoves.addAll(bMoves);

        return legalMoves;
    }

}

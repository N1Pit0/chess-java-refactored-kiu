package services.strategy;

import model.board.Board;
import model.board.Square;
import model.pieces.common.Piece;
import services.strategy.common.PieceStrategy;
import services.strategy.movement.utils.PieceMovementUtil;

import java.util.LinkedList;
import java.util.List;

public class QueenStrategy extends PieceStrategy {

    public QueenStrategy(Piece piece) {
        super(piece);
    }

    @Override
    public List<Square> getLegalMoves(Board board) {
        LinkedList<Square> legalMoves = new LinkedList<Square>();
        Square[][] squareArrayBoard = board.getSquareChessBoard();

        int x = super.getPiece().getCurrentSquare().getXNum();
        int y = super.getPiece().getCurrentSquare().getYNum();

        int[] occups = PieceMovementUtil.getLinearOccupations(squareArrayBoard, x, y, super.getPiece());

        for (int i = occups[0]; i <= occups[1]; i++) {
            if (i != y) legalMoves.add(squareArrayBoard[i][x]);
        }

        for (int i = occups[2]; i <= occups[3]; i++) {
            if (i != x) legalMoves.add(squareArrayBoard[y][i]);
        }

        List<Square> bMoves = PieceMovementUtil.getDiagonalOccupations(squareArrayBoard, x, y, super.getPiece());

        legalMoves.addAll(bMoves);

        return legalMoves;
    }

}

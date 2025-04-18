package services.strategy;

import model.pieces.common.Piece;
import services.BoardService;
import services.SquareService;
import services.strategy.common.PieceStrategy;
import services.movement.utils.PieceMovementUtil;

import java.util.LinkedList;
import java.util.List;

public class QueenStrategy extends PieceStrategy {

    public QueenStrategy(Piece piece) {
        super(piece);
    }

    @Override
    public List<SquareService> getLegalMoves(BoardService board) {
        LinkedList<SquareService> legalMoves = new LinkedList<>();
        SquareService[][] squareArrayBoard = board.getSquareBoard();

        int x = super.getPiece().getCurrentSquare().getXNum();
        int y = super.getPiece().getCurrentSquare().getYNum();

        List<SquareService> occups = PieceMovementUtil.getLinearMoves(squareArrayBoard, this);

        for (int i = occups.get(0); i <= occups[1]; i++) {
            if (i != y) legalMoves.add(squareArrayBoard[i][x]);
        }

        for (int i = occups[2]; i <= occups[3]; i++) {
            if (i != x) legalMoves.add(squareArrayBoard[y][i]);
        }

        List<SquareService> bMoves = PieceMovementUtil.getDiagonalMoves(squareArrayBoard, this);

        legalMoves.addAll(bMoves);

        return legalMoves;
    }

}

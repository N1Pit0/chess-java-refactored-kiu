package services.strategy;

import model.board.Board;
import model.board.Square;
import model.pieces.common.Piece;
import services.BoardService;
import services.SquareService;
import services.strategy.common.PieceStrategy;

import java.util.LinkedList;
import java.util.List;

public class KnightStrategy extends PieceStrategy {

    public KnightStrategy(Piece piece) {
        super(piece);
    }

    @Override
    public List<SquareService> getLegalMoves(BoardService board) {
        LinkedList<SquareService> legalMoves = new LinkedList<>();
        SquareService[][] squareArrayBoard = board.getSquareBoard();

        int x = super.getPiece().getCurrentSquare().getXNum();
        int y = super.getPiece().getCurrentSquare().getYNum();

        for (int i = 2; i > -3; i--) {
            for (int k = 2; k > -3; k--) {
                if (Math.abs(i) == 2 ^ Math.abs(k) == 2) {
                    if (k != 0 && i != 0) {
                        try {
                            legalMoves.add(squareArrayBoard[y + k][x + i]);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            continue;
                        }
                    }
                }
            }
        }

        return legalMoves;
    }
}

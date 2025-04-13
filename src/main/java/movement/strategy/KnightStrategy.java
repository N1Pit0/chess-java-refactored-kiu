package movement.strategy;

import chess.board.Board;
import chess.board.Square;
import model.common.Piece;
import movement.strategy.common.PieceStrategy;

import java.util.LinkedList;
import java.util.List;

public class KnightStrategy extends PieceStrategy {

    public KnightStrategy(Piece piece) {
        super(piece);
    }

    @Override
    public List<Square> getLegalMoves(Board board) {
        LinkedList<Square> legalMoves = new LinkedList<Square>();
        Square[][] squareArrayBoard = board.getSquareArray();

        int x = super.getPiece().getCurrentSquare().getXNum();
        int y = super.getPiece().getCurrentSquare().getYNum();

        for (int i = 2; i > -3; i--) {
            for (int k = 2; k > -3; k--) {
                if(Math.abs(i) == 2 ^ Math.abs(k) == 2) {
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

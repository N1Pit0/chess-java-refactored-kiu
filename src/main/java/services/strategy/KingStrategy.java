package services.strategy;

import model.board.Board;
import model.board.Square;
import model.pieces.common.Piece;
import services.strategy.common.PieceStrategy;

import java.util.LinkedList;
import java.util.List;

public class KingStrategy extends PieceStrategy {

    public KingStrategy(Piece piece) {
        super(piece);
    }

    @Override
    public List<Square> getLegalMoves(Board board) {
        LinkedList<Square> legalMoves = new LinkedList<Square>();

        Square[][] squareArrayBoard = board.getSquareChessBoard();

        int x = super.getPiece().getCurrentSquare().getXNum();
        int y = super.getPiece().getCurrentSquare().getYNum();

        for (int i = 1; i > -2; i--) {
            for (int k = 1; k > -2; k--) {
                if (!(i == 0 && k == 0)) {
                    try {
                        if (!squareArrayBoard[y + k][x + i].isOccupied() ||
                                squareArrayBoard[y + k][x + i].getOccupyingPiece().getColor()
                                        != super.getPiece().getColor()) {
                            legalMoves.add(squareArrayBoard[y + k][x + i]);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                }
            }
        }

        return legalMoves;
    }
}


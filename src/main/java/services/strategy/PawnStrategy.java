package services.strategy;

import model.board.Board;
import model.board.Square;
import model.pieces.common.Piece;
import services.strategy.common.PieceStrategy;

import java.util.LinkedList;
import java.util.List;

public class PawnStrategy extends PieceStrategy {
    private boolean wasMoved;

    public PawnStrategy(Piece piece) {
        super(piece);
    }

    @Override
    public boolean move(Square fin) {
        boolean b = super.move(fin);
        wasMoved = true;
        return b;
    }

    @Override
    public List<Square> getLegalMoves(Board board) {
        LinkedList<Square> legalMoves = new LinkedList<Square>();

        Square[][] squareArrayBoard = board.getSquareChessBoard();

        int x = super.getPiece().getCurrentSquare().getXNum();
        int y = super.getPiece().getCurrentSquare().getYNum();
        int c = super.getPiece().getColor();

        if (c == 0) {
            if (!wasMoved) {
                if (!squareArrayBoard[y + 2][x].isOccupied()) {
                    legalMoves.add(squareArrayBoard[y + 2][x]);
                }
            }

            if (y + 1 < 8) {
                if (!squareArrayBoard[y + 1][x].isOccupied()) {
                    legalMoves.add(squareArrayBoard[y + 1][x]);
                }
            }

            if (x + 1 < 8 && y + 1 < 8) {
                if (squareArrayBoard[y + 1][x + 1].isOccupied()) {
                    legalMoves.add(squareArrayBoard[y + 1][x + 1]);
                }
            }

            if (x - 1 >= 0 && y + 1 < 8) {
                if (squareArrayBoard[y + 1][x - 1].isOccupied()) {
                    legalMoves.add(squareArrayBoard[y + 1][x - 1]);
                }
            }
        }

        if (c == 1) {
            if (!wasMoved) {
                if (!squareArrayBoard[y - 2][x].isOccupied()) {
                    legalMoves.add(squareArrayBoard[y - 2][x]);
                }
            }

            if (y - 1 >= 0) {
                if (!squareArrayBoard[y - 1][x].isOccupied()) {
                    legalMoves.add(squareArrayBoard[y - 1][x]);
                }
            }

            if (x + 1 < 8 && y - 1 >= 0) {
                if (squareArrayBoard[y - 1][x + 1].isOccupied()) {
                    legalMoves.add(squareArrayBoard[y - 1][x + 1]);
                }
            }

            if (x - 1 >= 0 && y - 1 >= 0) {
                if (squareArrayBoard[y - 1][x - 1].isOccupied()) {
                    legalMoves.add(squareArrayBoard[y - 1][x - 1]);
                }
            }
        }

        return legalMoves;
    }
}

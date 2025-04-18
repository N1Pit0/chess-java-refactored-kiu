package services.strategy;

import model.enums.PieceColor;
import model.pieces.common.Piece;
import services.BoardService;
import services.SquareService;
import services.strategy.common.PieceStrategy;

import java.util.LinkedList;
import java.util.List;

import static model.enums.PieceColor.BLACK;
import static model.enums.PieceColor.WHITE;

public class PawnStrategy extends PieceStrategy {
    private boolean wasMoved;

    public PawnStrategy(Piece piece) {
        super(piece);
    }

    @Override
    public boolean move(SquareService fin, BoardService board) {
        boolean b = super.move(fin, board);
        wasMoved = true;
        return b;
    }

    @Override
    public List<SquareService> getLegalMoves(BoardService boardService) {
        LinkedList<SquareService> legalMoves = new LinkedList<>();

        SquareService[][] squareArrayBoard = boardService.getSquareBoard();

        int x = super.getPiece().getCurrentSquare().getXNum();
        int y = super.getPiece().getCurrentSquare().getYNum();
        PieceColor c = super.getPiece().getColor();

        if (c == BLACK) {
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

        if (c == WHITE) {
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

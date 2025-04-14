package services.strategy.common;

import model.board.Board;
import model.board.Square;
import lombok.Getter;
import model.pieces.common.Piece;
import services.SquareService;

import java.util.List;

@Getter
public abstract class PieceStrategy {
    private final Piece piece;

    public PieceStrategy(Piece piece) {
        this.piece = piece;
    }

    public boolean move(Square square, Board board) {
        Piece occup = square.getOccupyingPiece();

        if (occup != null) {
            if (occup.getColor() == piece.getColor()) return false;
            else board.capture(piece, piece.getCurrentSquare());
        }

        piece.getCurrentSquare().removePiece();
        piece.setCurrentSquare(square);
        piece.getCurrentSquare().put(piece);
        return true;
    }

    public abstract List<Square> getLegalMoves(Board board);
}

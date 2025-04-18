package services;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import model.board.Square;
import model.pieces.common.Piece;
import services.strategy.common.PieceStrategy;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"occupyingPiece"})
public class SquareService {

    private Square square;
    private PieceStrategy occupyingPiece; // might be some bug here

    public SquareService(Square square) {
        this.square = square;
    }

    public boolean isOccupied() {
        return (square.getOccupyingPiece() != null);
    }

    public void put(PieceStrategy pieceStrategy) {
        if (square == null) {
            throw new IllegalStateException("Cannot place a piece on a null square!");
        }

        square.setOccupyingPiece(pieceStrategy.getPiece());
        occupyingPiece = pieceStrategy;
        pieceStrategy.setSquareService(this);
    }

    public PieceStrategy removePiece() {
        PieceStrategy piece = occupyingPiece;
        square.setOccupyingPiece(null);
        occupyingPiece = null; //here problem??
        return piece;
    }

}

package services;

import lombok.Getter;
import lombok.Setter;
import model.board.Square;
import model.pieces.common.Piece;
import services.strategy.common.PieceStrategy;

@Getter
@Setter
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
        square.setOccupyingPiece(pieceStrategy.getPiece());
        pieceStrategy.setSquareService(this);
    }

    public Piece removePiece() {
        Piece piece = square.getOccupyingPiece();
        square.setOccupyingPiece(null);
        return piece;
    }

}

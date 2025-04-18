package services.movement.utils;


import lombok.Getter;
import lombok.Setter;
import services.BoardService;
import services.SquareService;
import services.strategy.common.PieceStrategy;

import static model.enums.PieceColor.WHITE;

@Getter
@Setter
public class MoveManager {

    private PieceStrategy movedPiece;
    private SquareService from;
    private SquareService to;
    private PieceStrategy capturePiece;

    public MoveManager(PieceStrategy movedPiece, SquareService from, SquareService to) {
        this.movedPiece = movedPiece;
        this.from = from;
        this.to = to;
    }

    public void undo(BoardService board) {
        from.setOccupyingPiece(movedPiece);
        to.setOccupyingPiece(capturePiece);
        movedPiece.setSquareService(from);
        if (capturePiece != null) {
            capturePiece.setSquareService(to);
            if (capturePiece.getPiece().getColor() == WHITE) {
                board.getWhitePieces().add(capturePiece);
            } else {
                board.getBlackPieces().add(capturePiece);
            }
        }
    }

    @Override
    public String toString() {
        return "Move{" +
                "movedPiece=" + movedPiece.getClass().toString() +
                ", from=" + from.getSquare().getXNum() + from.getSquare().getYNum() +
                ", to=" + to.getSquare().getXNum() + to.getSquare().getYNum() +
                '}';
    }
}
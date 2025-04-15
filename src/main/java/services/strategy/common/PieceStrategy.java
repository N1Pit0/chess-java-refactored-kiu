package services.strategy.common;

import lombok.Setter;
import model.board.Board;
import model.board.Square;
import lombok.Getter;
import model.pieces.common.Piece;
import services.BoardService;
import services.SquareService;

import java.awt.*;
import java.util.List;

@Getter
@Setter
public abstract class PieceStrategy {
    private final Piece piece;
    private SquareService squareService;

    public PieceStrategy(Piece piece) {
        this.piece = piece;
    }

    public boolean move(SquareService squareService1, BoardService boardService) {
        PieceStrategy occupyingPiece = squareService1.getOccupyingPiece();

        if (occupyingPiece != null) {
            if (occupyingPiece.getPiece().getColor() == piece.getColor()) return false;
            else boardService.capture(this, squareService1); //something is off either here or in capture()
        }

        squareService.removePiece();
        squareService = squareService1;
        squareService.put(this); // possible error here
        return true;
    }

    public void draw(Graphics g) {

        g.drawImage(this.getPiece().getImg(), 0, 0, null);
    }

    public abstract List<SquareService> getLegalMoves(BoardService board);
}

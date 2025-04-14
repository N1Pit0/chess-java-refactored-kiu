package services.strategy;

import model.board.Board;
import model.board.Square;
import model.pieces.common.Piece;
import services.BoardService;
import services.SquareService;
import services.strategy.common.PieceStrategy;
import services.strategy.movement.utils.PieceMovementUtil;

import java.util.List;

public class BishopStrategy extends PieceStrategy {


    public BishopStrategy(Piece piece) {
        super(piece);
    }

    @Override
    public List<SquareService> getLegalMoves(BoardService boardService) {

        SquareService[][] squareArrayBoard = boardService.getSquareBoard();
        int x = super.getPiece().getCurrentSquare().getXNum();
        int y = super.getPiece().getCurrentSquare().getYNum();

        return PieceMovementUtil.getDiagonalOccupations(squareArrayBoard, x, y, super.getPiece());
    }
}

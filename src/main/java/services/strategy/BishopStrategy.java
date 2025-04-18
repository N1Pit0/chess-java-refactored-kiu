package services.strategy;

import model.pieces.common.Piece;
import services.BoardService;
import services.SquareService;
import services.strategy.common.PieceStrategy;
import services.movement.utils.PieceMovementUtil;

import java.util.List;

public class BishopStrategy extends PieceStrategy {


    public BishopStrategy(Piece piece) {
        super(piece);
    }

    @Override
    public List<SquareService> getLegalMoves(BoardService boardService) {

        SquareService[][] squareArrayBoard = boardService.getSquareBoard();


        return PieceMovementUtil.getDiagonalMoves(squareArrayBoard,this);
    }
}

package services;

import model.enums.PieceColor;
import services.movement.utils.MoveManager;
import services.strategy.common.PieceStrategy;

import java.util.List;


/**
 * Component of the Chess game that detects check mates in the game.
 *
 * @author Jussi Lundstedt
 */
public class CheckmateDetector {
    private final BoardService boardService;

    public CheckmateDetector(BoardService boardService) {
        this.boardService = boardService;
    }

    public boolean isInCheck(PieceColor color) {
        PieceStrategy king = switch (color) {
            case BLACK -> boardService.getBlackKing();
            case WHITE -> boardService.getWhiteKing();
        };
        SquareService kingPosition = king.getSquareService();
        return boardService.isSquareUnderThreat(kingPosition, color);
    }

    public boolean isCheckMate(PieceColor color) {


        List<MoveManager> moves = getAllPossibleMoves(color);

        boolean v = moves.stream().allMatch(move -> {
            move.getMovedPiece().move(move.getTo(), boardService);
            boolean b = isInCheck(color);
            System.out.println(move);
            System.out.println(b);

            move.undo(boardService);
            return b;
        });
        return v && isInCheck(color);
    }


    public boolean isStalemate(PieceColor color) {
        List<MoveManager> moves = getAllPossibleMoves(color);

        boolean v = moves.stream().allMatch(move -> {
            move.getMovedPiece().move(move.getTo(), boardService);
            boolean b = isInCheck(color);
            System.out.println(move);
            System.out.println(b);

            move.undo(boardService);
            return b;
        });
        var k = isInCheck(color);
        return v && !isInCheck(color);
    }


    private List<MoveManager> getAllPossibleMoves(PieceColor color) {
        List<PieceStrategy> pieces = switch (color) {
            case BLACK -> boardService.getBlackPieces();
            case WHITE -> boardService.getWhitePieces();
        };

        return pieces.stream()
                .flatMap(piece -> piece.getLegalMoves(boardService)
                        .stream()
                        .filter(x -> !x.isOccupied()
                                || x.getOccupyingPiece().getPiece().getColor() != color).map(square -> {
                                    SquareService from = piece.getSquareService();

                                    var move = new MoveManager(piece, from, square);
                                    if (square.getOccupyingPiece() != null
                                            && square.getOccupyingPiece().getPiece().getColor() != color) {
                                        move.setCapturePiece(square.getOccupyingPiece());
                                    }
                                    return move;
                                }
                        )
                )
                .toList();
    }

}

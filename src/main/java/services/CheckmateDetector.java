package services;

import model.board.Board;
import model.board.Square;
import model.enums.PieceColor;
import model.pieces.Bishop;
import model.pieces.King;
import model.pieces.Queen;
import model.pieces.common.Piece;
import services.strategy.KingStrategy;
import services.strategy.common.PieceStrategy;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;


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
        PieceStrategy king = switch (color){
            case BLACK -> boardService.getBlackKing();
            case WHITE -> boardService.getWhiteKing();
        };
        SquareService kingPosition = king.getSquareService();
        return boardService.isSquareUnderThreat(kingPosition, color);
    }

    public boolean isCheckMate(PieceColor color) {


        List<Move> moves = getAllPossibleMoves(color);

        boolean v = moves.stream().allMatch( move -> {
            move.getMovedPiece().move(move.getTo(),board);
            boolean b = isInCheck(color);
            System.out.println(move);
            System.out.println(b);

            move.undo(board);
            return b;
        });
        return v && isInCheck(color);
    }


    public boolean isStalemate(PieceColor color) {
        List<Move> moves = getAllPossibleMoves(color);

        boolean v = moves.stream().allMatch( move -> {
            move.getMovedPiece().move(move.getTo(),board);
            boolean b = isInCheck(color);
            System.out.println(move);
            System.out.println(b);

            move.undo(board);
            return b;
        });
        var k = isInCheck(color);
        return v && !isInCheck(color);
    }




    private List<Move> getAllPossibleMoves(PieceColor color){
        List<Piece> pieces = switch (color){
            case BLACK -> board.getBlackPieces();
            case WHITE -> board.getWhitePieces();
        };

        return pieces.stream()
                .flatMap(piece -> piece.getLegalMoves(board).stream().filter(x->!x.isOccupied() || x.getOccupyingPiece().getColor() != color).map(square -> {
                    Square from = piece.getPosition();

                    var move = new Move(piece,from, square);
                    if(square.getOccupyingPiece() != null && square.getOccupyingPiece().getColor() != color){
                        move.setCapturePiece(square.getOccupyingPiece());
                    }
                    return move;
                }))
                .toList();
    }

}

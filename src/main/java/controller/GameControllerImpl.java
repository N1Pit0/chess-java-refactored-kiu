package controller;

import services.board.BoardService;
import services.board.SquareInterface;
import services.checkmatedetection.CheckmateDetector;
import services.enums.PieceColor;
import services.strategy.common.PieceInterface;

import java.util.List;

import static services.enums.PieceColor.BLACK;
import static services.enums.PieceColor.WHITE;

public class GameControllerImpl implements GameController {
    private final BoardService boardService;
    private final CheckmateDetector checkmateDetector;
    private final GameWindowInterface gameWindowInterface;

    public GameControllerImpl(BoardService boardService, CheckmateDetector checkmateDetector, GameWindowInterface gameWindowInterface) {
        this.boardService = boardService;
        this.checkmateDetector = checkmateDetector;
        this.gameWindowInterface = gameWindowInterface;
    }


    @Override
    public boolean handlePress(SquareInterface square) {
        if (square.isOccupied()) {
            boardService.setCurrPiece(square.getOccupyingPiece());
            PieceColor currentPieceColor = boardService.getCurrPiece().getPieceColor();
            if (currentPieceColor.equals(BLACK) && boardService.isWhiteTurn())
                return false;

            return !currentPieceColor.equals(WHITE) || boardService.isWhiteTurn();
        }
        return true;
    }

    @Override
    public boolean handleRelease(SquareInterface targetSquare) {
        PieceInterface currentPiece = boardService.getCurrPiece();

        if (boardService.getCurrPiece() == null) return false;

        PieceColor currentPieceColor = boardService.getCurrPiece().getPieceColor();
        if (currentPieceColor.equals(BLACK) && boardService.isWhiteTurn())
            return false;

        if (currentPieceColor.equals(WHITE) && !boardService.isWhiteTurn())
            return false;

        List<SquareInterface> legalMoves = currentPiece.getLegalMoves(boardService.getBoardSquareArray());

        if (!legalMoves.contains(targetSquare)) return false;

        // Store the original square of the current piece
        SquareInterface originalSquare = currentPiece.getCurrentSquare();

        // Store the captured piece (if any)
        PieceInterface capturedPiece = targetSquare.getOccupyingPiece();

        makeMoveAndCheckSpecialRules(originalSquare, currentPiece, currentPieceColor, capturedPiece, targetSquare);

        return true;
    }

    private void makeMoveAndCheckSpecialRules(
            SquareInterface originalSquare, PieceInterface originalPiece, PieceColor originalPieceColor,
            PieceInterface targetPiece, SquareInterface targetSquare) {

        // Make the move
        originalPiece.move(targetSquare, boardService);

        // Check if the current player's king is in check after the move
        if (checkmateDetector.isInCheck(boardService, originalPieceColor)) {
            // Undo the move
            originalPiece.move(originalSquare, boardService);

            // Restore the captured piece (if any)
            if (targetPiece != null) {
                targetSquare.setOccupyingPiece(targetPiece);
                if (targetPiece.getPieceColor() == WHITE) {
                    boardService.getWhitePieces().add(targetPiece);
                } else {
                    boardService.getBlackPieces().add(targetPiece);
                }
            }

            System.out.println("Invalid move. Your king is in check!");
        } else {

            PieceColor opponentColor = originalPieceColor.equals(WHITE) ? BLACK : WHITE;
            // Check if the opponent is in checkmate
            if (checkmateDetector.isInCheckmate(boardService, opponentColor)) {

                gameWindowInterface.checkmateOccurred(opponentColor);
            }
            // Check if the opponent is in stalemate
            else if (checkmateDetector.isInStalemate(boardService, opponentColor)) {

                gameWindowInterface.stalemateOccurred();
            }
            // Change the turn to the other player
            boardService.setWhiteTurn(!boardService.isWhiteTurn());
        }
    }
}

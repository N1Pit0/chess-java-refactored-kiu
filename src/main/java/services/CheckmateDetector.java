package services;

import model.board.Square;
import model.pieces.common.Piece;
import services.strategy.KingStrategy;
import services.strategy.common.PieceStrategy;

import java.util.*;

/**
 * Detects and handles check and checkmate logic in the chess game.
 */
public class CheckmateDetector {
    private final BoardService boardService;

    private final List<PieceStrategy> whitePieces;
    private final List<PieceStrategy> blackPieces;

    private final KingStrategy whiteKing;
    private final KingStrategy blackKing;

    private final List<SquareService> allSquares; // All squares on the board
    private final Map<SquareService, List<PieceStrategy>> whiteMoves; // Moves white pieces can make
    private final Map<SquareService, List<PieceStrategy>> blackMoves; // Moves black pieces can make

    public CheckmateDetector(BoardService boardService, List<PieceStrategy> whitePieces,
                             List<PieceStrategy> blackPieces, KingStrategy whiteKing, KingStrategy blackKing) {
        this.boardService = boardService;
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;

        this.allSquares = new ArrayList<>();
        SquareService[][] squareBoard = boardService.getSquareBoard();

        this.whiteMoves = new HashMap<>();
        this.blackMoves = new HashMap<>();

        // Initialize all squares and their move maps
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                SquareService square = squareBoard[y][x];
                allSquares.add(square);
                whiteMoves.put(square, new LinkedList<>());
                blackMoves.put(square, new LinkedList<>());
            }
        }

        // Update the initial board state
        update();
    }

    /**
     * Updates the checkmate detector’s knowledge of the current game state.
     * This recalculates all possible moves for white and black pieces.
     */
    public void update() {
        // Clear previous moves
        whiteMoves.values().forEach(List::clear);
        blackMoves.values().forEach(List::clear);

        // Update moves for white pieces
        for (Iterator<PieceStrategy> iterator = whitePieces.iterator(); iterator.hasNext(); ) {
            PieceStrategy piece = iterator.next();
            if (piece.getSquareService().getSquare() == null) {
                iterator.remove(); // Remove dead/invalid pieces
                continue;
            }

            List<SquareService> moves = piece.getLegalMoves(boardService);
            for (SquareService move : moves) {
                whiteMoves.get(move).add(piece);
            }
        }

        // Update moves for black pieces
        for (Iterator<PieceStrategy> iterator = blackPieces.iterator(); iterator.hasNext(); ) {
            PieceStrategy piece = iterator.next();
            if (piece.getSquareService().getSquare() == null) {
                iterator.remove(); // Remove dead/invalid pieces
                continue;
            }

            List<SquareService> moves = piece.getLegalMoves(boardService);
            for (SquareService move : moves) {
                blackMoves.get(move).add(piece);
            }
        }
    }

    /**
     * Determines if the black king is in check.
     *
     * @return `true` if the black king is under threat, `false` otherwise.
     */
    public boolean blackInCheck() {
        return !whiteMoves.get(blackKing.getSquareService()).isEmpty();
    }

    /**
     * Determines if the white king is in check.
     *
     * @return `true` if the white king is under threat, `false` otherwise.
     */
    public boolean whiteInCheck() {
        return !blackMoves.get(whiteKing.getSquareService()).isEmpty();
    }

    /**
     * Tests if a player's move is valid. This ensures the move complies with game rules
     * and doesn’t leave their king in check.
     *
     * @param movingPiece The piece that is being moved.
     * @param targetSquare The square to which the piece is moved.
     * @return `true` if the move is valid, `false` otherwise.
     */
    public boolean testMove(PieceStrategy movingPiece, SquareService targetSquare) {
        // Save the current board state
        SquareService startSquare = movingPiece.getSquareService();
        PieceStrategy capturedPiece = targetSquare.getOccupyingPiece();

        boolean isValidMove;

        // Simulate the move
        simulateMove(movingPiece, targetSquare);

        // Determine if the move is valid
        if (movingPiece.getPiece().getColor() == 0) { // Black piece
            isValidMove = !blackInCheck();
        } else { // White piece
            isValidMove = !whiteInCheck();
        }

        // Rollback to the original state
        rollbackMove(movingPiece, startSquare, targetSquare, capturedPiece);

        return isValidMove;
    }

    // Helper method to simulate a temporary move
    private void simulateMove(PieceStrategy movingPiece, SquareService targetSquare) {
        SquareService startSquare = movingPiece.getSquareService();
        targetSquare.put(movingPiece); // Place piece on target square
        startSquare.removePiece(); // Remove piece from its original square
    }

    // Helper method to rollback a simulated move
    private void rollbackMove(PieceStrategy movingPiece, SquareService startSquare,
                              SquareService targetSquare, PieceStrategy capturedPiece) {
        startSquare.put(movingPiece); // Restore the moving piece
        targetSquare.removePiece();
        if (capturedPiece != null) {
            targetSquare.put(capturedPiece); // Restore the captured piece, if any
        }
    }

    /**
     * Determines if black is in checkmate.
     *
     * @return `true` if black is in checkmate, `false` otherwise.
     */
    public boolean blackCheckMated() {
        // If not in check, black is not in checkmate
        if (!blackInCheck()) return false;

        return !hasLegalMoves(blackPieces, blackKing);
    }

    /**
     * Determines if white is in checkmate.
     *
     * @return `true` if white is in checkmate, `false` otherwise.
     */
    public boolean whiteCheckMated() {
        // If not in check, white is not in checkmate
        if (!whiteInCheck()) return false;

        return !hasLegalMoves(whitePieces, whiteKing);
    }

    // Helper method to determine if a player has legal moves remaining
    private boolean hasLegalMoves(List<PieceStrategy> pieces, KingStrategy king) {
        for (PieceStrategy piece : pieces) {
            List<SquareService> legalMoves = piece.getLegalMoves(boardService);
            for (SquareService move : legalMoves) {
                if (testMove(piece, move)) {
                    return true; // Found at least one valid move
                }
            }
        }
        return false; // No legal moves left
    }

    /**
     * Returns a list of squares the current player can legally move to.
     *
     * @param isWhiteTurn `true` if it’s white’s turn, `false` otherwise.
     * @return List of allowable squares for the current player.
     */
    public List<SquareService> getAllowableSquares(boolean isWhiteTurn) {
        List<SquareService> allowableSquares = new ArrayList<>();

        List<PieceStrategy> pieces = isWhiteTurn ? whitePieces : blackPieces;
        for (PieceStrategy piece : pieces) {
            List<SquareService> legalMoves = piece.getLegalMoves(boardService);
            for (SquareService square : legalMoves) {
                if (testMove(piece, square)) {
                    allowableSquares.add(square);
                }
            }
        }

        return allowableSquares;
    }
}

package services.movement.utils;

import model.pieces.common.Piece;
import services.BoardService;
import services.SquareService;
import services.strategy.common.PieceStrategy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PieceMovementUtil {

    /**
     * Direction vectors for cardinal directions (up, down, left, right)
     */
    private static final int[][] LINEAR_DIRECTIONS = {
            {-1, 0}, // up
            {1, 0},  // down
            {0, -1}, // left
            {0, 1}   // right
    };

    /**
     * Direction vectors for diagonal directions
     */
    private static final int[][] DIAGONAL_DIRECTIONS = {
            {-1, -1}, // northwest
            {1, -1},  // southwest
            {1, 1},   // southeast
            {-1, 1}   // northeast
    };

    /**
     * Gets linear moves (horizontal and vertical) for a piece
     * @param board The current SquareService[][] state
     * @param piece The piece to calculate moves for
     * @return List of squares the piece can move to linearly
     */
    public static List<SquareService> getLinearMoves(SquareService[][] board, PieceStrategy piece) {
        List<SquareService> legalSquares = new ArrayList<>();
        SquareService position = piece.getSquareService();
        int x = position.getSquare().getXNum();
        int y = position.getSquare().getYNum();

        // Check all four directions
        for (int[] direction : LINEAR_DIRECTIONS) {
            int dy = direction[0];
            int dx = direction[1];

            int currentY = y + dy;
            int currentX = x + dx;

            while (isValidPosition(currentY, currentX)) {
                SquareService targetSquare = board[currentY][currentX];

                if (targetSquare.isOccupied()) {
                    // If enemy piece, add and stop in this direction
                    if (targetSquare.getOccupyingPiece().getPiece().getColor() != piece.getPiece().getColor()) {
                        legalSquares.add(targetSquare);
                    }
                    break; // Stop at any piece (friendly or enemy)
                }

                // Empty square, add it
                legalSquares.add(targetSquare);

                // Continue in the same direction
                currentY += dy;
                currentX += dx;
            }
        }

        return legalSquares;
    }

    /**
     * Gets diagonal moves for a piece
     * @param board The current SquareService[][] state
     * @param piece The piece to calculate moves for
     * @return List of squares the piece can move to diagonally
     */
    public static List<SquareService> getDiagonalMoves(SquareService[][] board, PieceStrategy piece) {
        List<SquareService> legalSquares = new ArrayList<>();
        SquareService position = piece.getSquareService();
        int x = position.getSquare().getXNum();
        int y = position.getSquare().getYNum();

        // Check all four diagonal directions
        for (int[] direction : DIAGONAL_DIRECTIONS) {
            int dy = direction[0];
            int dx = direction[1];

            int currentY = y + dy;
            int currentX = x + dx;

            while (isValidPosition(currentY, currentX)) {
                SquareService targetSquare = board[currentY][currentX];

                if (targetSquare.isOccupied()) {
                    // If enemy piece, add and stop in this direction
                    if (targetSquare.getOccupyingPiece().getPiece().getColor() != piece.getPiece().getColor()) {
                        legalSquares.add(targetSquare);
                    }
                    break; // Stop at any piece (friendly or enemy)
                }

                // Empty square, add it
                legalSquares.add(targetSquare);

                // Continue in the same direction
                currentY += dy;
                currentX += dx;
            }
        }

        return legalSquares;
    }

    /**
     * Checks if a position is within the chess board bounds
     * @param y Row coordinate
     * @param x Column coordinate
     * @return true if position is valid, false otherwise
     */
    private static boolean isValidPosition(int y, int x) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

//    /**
//     * Gets the combined moves (linear and diagonal) for a piece like a queen
//     * @param chessBoard The current board state
//     * @param piece The piece to calculate moves for
//     * @return List of squares the piece can move to
//     */
//    public static List<SquareService> getCombinedMoves(BoardService chessBoard, PieceStrategy piece) {
//        List<SquareService> moves = new ArrayList<>();
//        moves.addAll(getLinearMoves(chessBoard, piece));
//        moves.addAll(getDiagonalMoves(chessBoard, piece));
//        return moves;
//    }

}

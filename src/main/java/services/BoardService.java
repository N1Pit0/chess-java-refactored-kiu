package services;

import lombok.Getter;
import lombok.Setter;
import model.board.Board;
import model.board.Square;
import model.enums.ImagePath;
import services.strategy.*;
import services.strategy.common.PieceStrategy;
import view.gui.GameWindow;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class BoardService {

    private Board board; // Model-level representation of the chessboard
    private SquareService[][] squareBoard; // Service-based representation of the board
    private final GameWindow gameWindow; // GUI reference

    private PieceStrategy piece; // The currently selected piece (e.g., with a mouse click)
    private List<PieceStrategy> blackPieces; // List of all black pieces
    private List<PieceStrategy> whitePieces; // List of all white pieces

    private CheckmateDetector checkmateDetector; // Handles check/checkmate logic
    private List<SquareService> movableSquares; // The squares the current player can legally move to
    private boolean whiteTurn; // Tracks turn (true = white to move, false = black to move)

    /**
     * Initializes the `BoardService`, setting up the board, pieces, and auxiliary services.
     *
     * @param board      The model representation of the chessboard.
     * @param gameWindow The GUI reference for the game window.
     */
    public BoardService(Board board, GameWindow gameWindow) {
        this.board = board;
        this.gameWindow = gameWindow;

        this.squareBoard = new SquareService[8][8];
        this.movableSquares = new LinkedList<>();
        this.blackPieces = new LinkedList<>();
        this.whitePieces = new LinkedList<>();

        this.whiteTurn = true; // White always starts
        this.piece = null; // No piece selected initially

        initializeSquares();
        initializePieces();
        initializeServices();
    }

    /**
     * Initializes the `SquareService` grid and syncs it with the `Square` grid from the model.
     */
    private void initializeSquares() {
        Square[][] modelSquares = board.getSquareChessBoard();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                // Ensure each square is properly initialized
                Square modelSquare = modelSquares[x][y];
                if (modelSquare == null) {
                    throw new IllegalStateException("Square is not initialized at (" + x + ", " + y + ")");
                }

                SquareService squareService = new SquareService(modelSquare); // Map to service layer
                squareBoard[x][y] = squareService;
            }
        }
    }

    /**
     * Initializes all chess pieces and places them in their starting positions.
     */
    private void initializePieces() {
        Square[][] squares = board.getSquareChessBoard();

        // Initialize pawns
        for (int x = 0; x < 8; x++) {
            squareBoard[1][x].put(new PawnStrategy(new model.pieces.Pawn(0, squares[1][x], ImagePath.RESOURCES_BPAWN_PNG.label)));
            squareBoard[6][x].put(new PawnStrategy(new model.pieces.Pawn(1, squares[6][x], ImagePath.RESOURCES_WPAWN_PNG.label)));
        }

        // Initialize other pieces
        setupPrimaryPieces(0, squares[0], blackPieces); // Black pieces
        setupPrimaryPieces(7, squares[7], whitePieces); // White pieces
    }

    /**
     * Helper method to set up non-pawn pieces for a given row.
     *
     * @param row           The row where pieces are placed (0 = black, 7 = white).
     * @param squaresRow    The array of `Square` objects in the row.
     * @param targetPieces  The list to store the generated pieces (black or white).
     */
    private void setupPrimaryPieces(int row, Square[] squaresRow, List<PieceStrategy> targetPieces) {
        int color = (row == 7) ? 1 : 0; // Determine color (1 = white, 0 = black)

        // Place rooks
        squareBoard[row][0].put(new RookStrategy(new model.pieces.Rook(color, squaresRow[0], color == 1 ? ImagePath.RESOURCES_WROOK_PNG.label : ImagePath.RESOURCES_BROOK_PNG.label)));
        squareBoard[row][7].put(new RookStrategy(new model.pieces.Rook(color, squaresRow[7], color == 1 ? ImagePath.RESOURCES_WROOK_PNG.label : ImagePath.RESOURCES_BROOK_PNG.label)));

        // Place knights
        squareBoard[row][1].put(new KnightStrategy(new model.pieces.Knight(color, squaresRow[1], color == 1 ? ImagePath.RESOURCES_WKNIGHT_PNG.label : ImagePath.RESOURCES_BKNIGHT_PNG.label)));
        squareBoard[row][6].put(new KnightStrategy(new model.pieces.Knight(color, squaresRow[6], color == 1 ? ImagePath.RESOURCES_WKNIGHT_PNG.label : ImagePath.RESOURCES_BKNIGHT_PNG.label)));

        // Place bishops
        squareBoard[row][2].put(new BishopStrategy(new model.pieces.Bishop(color, squaresRow[2], color == 1 ? ImagePath.RESOURCES_WBISHOP_PNG.label : ImagePath.RESOURCES_BBISHOP_PNG.label)));
        squareBoard[row][5].put(new BishopStrategy(new model.pieces.Bishop(color, squaresRow[5], color == 1 ? ImagePath.RESOURCES_WBISHOP_PNG.label : ImagePath.RESOURCES_BBISHOP_PNG.label)));

        // Place queen and king
        squareBoard[row][3].put(new QueenStrategy(new model.pieces.Queen(color, squaresRow[3], color == 1 ? ImagePath.RESOURCES_WQUEEN_PNG.label : ImagePath.RESOURCES_BQUEEN_PNG.label)));
        KingStrategy king = new KingStrategy(new model.pieces.King(color, squaresRow[4], color == 1 ? ImagePath.RESOURCES_WKING_PNG.label : ImagePath.RESOURCES_BKING_PNG.label));
        squareBoard[row][4].put(king);

        // Populate the piece list
        for (int i = 0; i < 8; i++) {
            PieceStrategy occupyingPiece = squareBoard[row][i].getOccupyingPiece();
            if (occupyingPiece != null) {
                targetPieces.add(occupyingPiece);
            }
        }
    }

    /**
     * Initializes auxiliary services like `CheckmateDetector`.
     */
    private void initializeServices() {
        KingStrategy whiteKing = (KingStrategy) squareBoard[7][4].getOccupyingPiece();
        KingStrategy blackKing = (KingStrategy) squareBoard[0][4].getOccupyingPiece();

        checkmateDetector = new CheckmateDetector(this, whitePieces, blackPieces, whiteKing, blackKing);
    }

    /**
     * Updates the game state after a move.
     * Recalculates legal moves and checks for checks/checkmate.
     */
    public void updateGameState() {
        checkmateDetector.update(); // Recalculate all board states
        movableSquares = checkmateDetector.getAllowableSquares(whiteTurn);
    }

    /**
     * Executes a piece capture while updating the board state.
     *
     * @param attackingPiece The attacking `PieceStrategy`.
     * @param targetSquare   The square containing the captured piece.
     */
    public void capture(PieceStrategy attackingPiece, SquareService targetSquare) {
        PieceStrategy capturedPiece = targetSquare.getOccupyingPiece();

        // Remove captured piece from the respective list
        if (capturedPiece != null) {
            if (capturedPiece.getPiece().getColor() == 0) {
                blackPieces.remove(capturedPiece);
            } else {
                whitePieces.remove(capturedPiece);
            }
        }

        // Place the attacking piece in the target square
        targetSquare.removePiece();
        targetSquare.put(attackingPiece);
    }

    /**
     * Toggles the player turn between white and black.
     */
    public void toggleTurn() {
        whiteTurn = !whiteTurn;
    }

    /**
     * Executes a player's move and updates the game state.
     *
     * @param piece        The piece being moved.
     * @param targetSquare The square where the piece will be moved.
     */
    public void playMove(PieceStrategy piece, SquareService targetSquare) {
        if (targetSquare.isOccupied()) {
            // Handle capture scenario
            capture(piece, targetSquare);
        } else {
            // Handle normal move
            piece.getSquareService().removePiece();
            targetSquare.put(piece);
        }

        updateGameState(); // Refresh game state
        toggleTurn(); // Switch to the other player's turn
    }
}
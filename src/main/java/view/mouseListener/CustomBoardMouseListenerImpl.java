package view.mouseListener;

import services.BoardService;
import services.SquareService;
import services.strategy.common.PieceStrategy;
import view.BoardView;
import view.SquareView;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

public class CustomBoardMouseListenerImpl implements CustomBoardMouseListener {

    private final BoardView boardView;
    private final BoardMouseListener boardMouseListener;
    private final BoardMouseMotionListener boardMouseMotionListener;

    public CustomBoardMouseListenerImpl(BoardView boardView) {

        this.boardView = boardView;
        this.boardMouseListener = new BoardMouseListener(this);
        this.boardMouseMotionListener = new BoardMouseMotionListener(this);
        this.boardView.addMouseListener(boardMouseListener);
        this.boardView.addMouseMotionListener(boardMouseMotionListener);
    }

    @Override
    public void handleMousePressed(MouseEvent e) {
        BoardService boardService = boardView.getBoardService();

        boardService.getBoard().setCurrX(e.getX());
        boardService.getBoard().setCurrY(e.getY());

        SquareView squareView = (SquareView) boardView.getComponentAt(new Point(e.getX(), e.getY()));
        SquareService squareService = squareView.getSquareService();

        if (squareService.isOccupied()) {
            boardService.setPiece(squareService.getOccupyingPiece());
            if (boardService.getPiece().getPiece().getColor() == 0 && boardService.isWhiteTurn())
                return;
            if (boardService.getPiece().getPiece().getColor() == 1 && !boardService.isWhiteTurn())
                return;
            squareView.setDisplayPiece(true);
        }
        boardView.repaint();
    }

    @Override
    public void handleMouseReleased(MouseEvent e) {
        SquareView targetSquareView = (SquareView) boardView.getComponentAt(new Point(e.getX(), e.getY()));
        SquareService targetSquare = targetSquareView.getSquareService();
        BoardService boardService = boardView.getBoardService();
        PieceStrategy selectedPiece = boardService.getPiece();

        // Abort if no piece is selected
        if (selectedPiece == null) return;

        // Ensure turn validation
        if ((selectedPiece.getPiece().getColor() == 0 && boardService.isWhiteTurn()) ||
                (selectedPiece.getPiece().getColor() == 1 && !boardService.isWhiteTurn())) {
            return;
        }

        // Get the piece's available legal moves
        List<SquareService> legalMoves = selectedPiece.getLegalMoves(boardService);

        // Ensure the move is valid and won't leave the king in check
        if (legalMoves.contains(targetSquare) &&
                boardService.getCheckmateDetector().testMove(selectedPiece, targetSquare)) {

            // Execute the move, update game state
            selectedPiece.move(targetSquare, boardService);
            boardService.updateGameState();

            // Check for checkmate
            if (boardService.getCheckmateDetector().blackCheckMated()) {
                setupBoardForCheckmate(boardService, 0);
            } else if (boardService.getCheckmateDetector().whiteCheckMated()) {
                setupBoardForCheckmate(boardService, 1);
            } else {
                boardService.setPiece(null);
                boardService.toggleTurn(); // Toggle player turn
            }
        } else {
            // Illegal move, deselect the piece
            boardService.setPiece(null);
        }

        // Redraw the board
        boardView.repaint();
    }

    @Override
    public void handleMouseDragged(MouseEvent e) {
        boardView.getBoardService().getBoard().setCurrX(e.getX());
        boardView.getBoardService().getBoard().setCurrY(e.getY());
        boardView.repaint();
    }

    private void setupBoardForCheckmate(BoardService board, int colorCheckMated) {
        board.setPiece(null);
        boardView.repaint();
        boardView.removeMouseListener(boardMouseListener);
        boardView.removeMouseMotionListener(boardMouseMotionListener);
        board.getGameWindow().checkmateOccurred(colorCheckMated);
    }
}

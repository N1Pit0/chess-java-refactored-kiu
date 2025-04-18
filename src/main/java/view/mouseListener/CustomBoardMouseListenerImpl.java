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

        // Calculate the clicked square based on the mouse position
        Point clickPoint = e.getPoint();
        SquareView clickedSquareView = (SquareView) boardView.getComponentAt(clickPoint);
        if (clickedSquareView == null) return; // Ignore invalid clicks

        SquareService clickedSquare = clickedSquareView.getSquareService();

        // Select the piece on the clicked square if it's valid and matches the current turn
        if (clickedSquare.isOccupied()) {
            PieceStrategy clickedPiece = clickedSquare.getOccupyingPiece();
            if ((clickedPiece.getPiece().getColor() == 1 && boardService.isWhiteTurn()) ||
                    (clickedPiece.getPiece().getColor() == 0 && !boardService.isWhiteTurn())) {
                boardService.setPiece(clickedPiece); // Select the piece
            }
        }
    }

    @Override
    public void handleMouseReleased(MouseEvent e) {
        BoardService boardService = boardView.getBoardService();
        PieceStrategy selectedPiece = boardService.getPiece();

        if (selectedPiece == null) return; // Ignore if no piece selected

        // Calculate the target square
        Point releasePoint = e.getPoint();
        SquareView targetSquareView = (SquareView) boardView.getComponentAt(releasePoint);
        if (targetSquareView == null) {
            boardView.clearMousePosition(); // Reset drag
            boardView.repaint();
            return;
        }

        SquareService targetSquare = targetSquareView.getSquareService();

        // Validate and execute the move
        List<SquareService> legalMoves = selectedPiece.getLegalMoves(boardService);
        if (legalMoves.contains(targetSquare) && boardService.getCheckmateDetector().testMove(selectedPiece, targetSquare)) {
            selectedPiece.move(targetSquare, boardService);
            boardService.updateGameState();
            boardService.toggleTurn();
        }

        // Clear selection and reset mouse position
        boardService.setPiece(null);
        boardView.clearMousePosition();
        boardView.repaint();
    }

    @Override
    public void handleMouseDragged(MouseEvent e) {
        // Update mouse position for dragging
        boardView.updateMousePosition(e.getX(), e.getY());
        boardView.repaint();
    }
}
package view.mouseListener;

import services.BoardService;
import services.SquareService;
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
        SquareView squareView = (SquareView) boardView.getComponentAt(new Point(e.getX(), e.getY()));
        SquareService squareService = squareView.getSquareService();
        BoardService boardService = boardView.getBoardService();

        if (boardService.getPiece() == null) return;

        if (boardService.getPiece().getPiece().getColor() == 0 && boardService.isWhiteTurn())
            return;

        if (boardService.getPiece().getPiece().getColor() == 1 && !boardService.isWhiteTurn())
            return;

        List<SquareService> legalMoves = boardService.getPiece().getLegalMoves(boardService);

        List<SquareService> movableSquares = boardService.getCkeckmateDetector().getAllowableSquares(boardService.isWhiteTurn());

        boardService.setMovableSquares(movableSquares);

        if (legalMoves.contains(squareService) && boardService.getMovableSquares().contains(squareService)
                && boardService.getCkeckmateDetector().testMove(boardService.getPiece(), squareService)) {
            squareView.setDisplayPiece(true);
            boardService.getPiece().move(squareService, boardService);
            boardService.getCkeckmateDetector().update();

            if (boardService.getCkeckmateDetector().blackCheckMated()) {

                setupBoardForCheckmate(boardService, 0);

            } else if (boardService.getCkeckmateDetector().blackCheckMated()) {

                setupBoardForCheckmate(boardService, 1);

            } else {
                boardService.setPiece(null);

                boardService.setWhiteTurn(!boardService.isWhiteTurn());

                boardService.setMovableSquares(boardService.getCkeckmateDetector().getAllowableSquares(boardService.isWhiteTurn()));
            }

        } else {
            squareView.setDisplayPiece(true);
            boardService.setPiece(null);
        }

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

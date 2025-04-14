package view.mouseListener;

import model.board.Board;
import model.board.Square;
import services.BoardService;
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
        Board board = boardView.getBoardService().getBoard();

        board.setCurrX(e.getX());
        board.setCurrY(e.getY());

        SquareView squareView = (SquareView) boardView.getComponentAt(new Point(e.getX(), e.getY()));
        Square square = squareView.getSquare();

        if (square.isOccupied()) {
            board.setCurrPiece(square.getOccupyingPiece());
            if (board.getCurrPiece().getColor() == 0 && board.isWhiteTurn())
                return;
            if (board.getCurrPiece().getColor() == 1 && !board.isWhiteTurn())
                return;
            squareView.setDisplayPiece(true);
        }
        boardView.repaint();
    }

    @Override
    public void handleMouseReleased(MouseEvent e) {
        SquareView squareView = (SquareView) boardView.getComponentAt(new Point(e.getX(), e.getY()));
        Square square = squareView.getSquare();
        Board board = boardView.getBoardService().getBoard();

        if (board.getCurrPiece() == null) return;

        if (board.getCurrPiece().getColor() == 0 && board.isWhiteTurn())
            return;

        if (board.getCurrPiece().getColor() == 1 && !board.isWhiteTurn())
            return;

        List<Square> legalMoves = boardView.getBoardService().getPiece().getLegalMoves(board);

        List<Square> movableSquares = board.getCkeckmateDetector().getAllowableSquares(board.isWhiteTurn());
        board.setMovable(movableSquares);

        if (legalMoves.contains(square) && board.getMovable().contains(square)
                && board.getCkeckmateDetector().testMove(board.getCurrPiece(), square)) {
            squareView.setDisplayPiece(true);
            boardView.getBoardService().getPiece().move(square, board);
            board.getCkeckmateDetector().update();

            if (board.getCkeckmateDetector().blackCheckMated()) {

                setupBoardForCheckmate(board, 0);

            } else if (board.getCkeckmateDetector().blackCheckMated()) {

                setupBoardForCheckmate(board, 1);

            } else {
                board.setCurrPiece(null);

                board.setWhiteTurn(!board.isWhiteTurn());

                board.setMovable(board.getCkeckmateDetector().getAllowableSquares(board.isWhiteTurn()));
            }

        } else {
            squareView.setDisplayPiece(true);
            board.setCurrPiece(null);
        }

        boardView.repaint();
    }

    @Override
    public void handleMouseDragged(MouseEvent e) {
        boardView.getBoardService().getBoard().setCurrX(e.getX());
        boardView.getBoardService().getBoard().setCurrY(e.getY());
        boardView.repaint();
    }

    private void setupBoardForCheckmate(Board board, int colorCheckMated) {
        board.setCurrPiece(null);
        boardView.repaint();
        boardView.removeMouseListener(boardMouseListener);
        boardView.removeMouseMotionListener(boardMouseMotionListener);
        board.getGameWindow().checkmateOccurred(colorCheckMated);
    }
}

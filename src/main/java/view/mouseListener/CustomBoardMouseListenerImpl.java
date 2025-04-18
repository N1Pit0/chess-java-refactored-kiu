package view.mouseListener;

import model.enums.PieceColor;
import services.BoardService;
import services.CheckmateDetector;
import services.SquareService;
import services.strategy.common.PieceStrategy;
import view.BoardView;
import view.SquareView;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

import static model.enums.PieceColor.BLACK;
import static model.enums.PieceColor.WHITE;

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
            PieceStrategy pieceStrategy = squareService.getOccupyingPiece();
            if (pieceStrategy.getPiece().getColor() == BLACK && boardService.isWhiteTurn())
                return;
            if (pieceStrategy.getPiece().getColor() == WHITE && !boardService.isWhiteTurn())
                return;
            squareView.setDisplayPiece(true);
            boardService.setPiece(pieceStrategy);
        }
        boardView.repaint();
    }

    @Override
    public void handleMouseReleased(MouseEvent e) {
        BoardService boardService = boardView.getBoardService();
        PieceStrategy currPiece = boardService.getPiece();
        if (currPiece == null) return;

        List<SquareService> legalMoves = currPiece.getLegalMoves(boardService);

        CheckmateDetector chd = new CheckmateDetector(boardService);

        PieceColor color = boardService.getTurn()? PieceColor.BLACK : PieceColor.WHITE;

        PieceColor checkColor = boardService.getTurn()? PieceColor.WHITE : PieceColor.BLACK;
        if(legalMoves.contains(targetSquare)){

            Move move = new Move(currPiece,currPiece.getPosition(),targetSquare);

            if(targetSquare.isOccupied() && targetSquare.getColor() == color){
                move.setCapturePiece(targetSquare.getOccupyingPiece());
            }

            currPiece.move(targetSquare,board);
            if(chd.isInCheck(checkColor)){
                move.undo(board);
                System.out.println("check play another move");
                return;
            }

            if (chd.isCheckMate(color)){
                System.out.println("mate");
                return;
            }
            board.toggleTurn();
        }else{
            currPiece.getPosition().setDisplay(true);
        }

        board.setCurrPiece(null);
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

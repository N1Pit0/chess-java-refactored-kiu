package view;

import model.board.Board;
import model.board.Square;
import lombok.Getter;
import model.pieces.common.Piece;
import services.BoardService;
import services.SquareService;

import javax.swing.*;
import java.awt.*;

@Getter
public class BoardView extends JPanel {
    private final BoardService boardService;

    public BoardView(BoardService boardService) {
        this.boardService = boardService;

        setLayout(new GridLayout(8, 8, 0, 0));

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                this.add(new SquareView(new SquareService(this.boardService.getBoard().getSquareChessBoard()[x][y]))); // ??
            }
        }

        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Square[][] board = this.boardService.getBoard().getSquareChessBoard();
        Piece currPiece = this.boardService.getBoard().getCurrPiece();
        boolean whiteTurn = this.boardService.getBoard().isWhiteTurn();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square sq = board[y][x];
                SquareView sqView = new SquareView(sq);
                sqView.setDisplayPiece(true);
                sqView.paintComponent(g);
            }
        }

        if (currPiece != null) {
            if ((currPiece.getColor() == 1 && whiteTurn)
                    || (currPiece.getColor() == 0 && !whiteTurn)) {
                final Image i = currPiece.getImg();
                g.drawImage(i, this.boardService.getBoard().getCurrX(), this.boardService.getBoard().getCurrY(), null);
            }
        }
    }
}

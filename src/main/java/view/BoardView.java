package view;

import lombok.Getter;
import services.BoardService;
import services.strategy.common.PieceStrategy;

import javax.swing.*;
import java.awt.*;

@Getter
public class BoardView extends JPanel {
    private final BoardService boardService;
    private final SquareView[][] squareViewsTwoDArray;

    public BoardView(BoardService boardService) {
        this.boardService = boardService;

        setLayout(new GridLayout(8, 8, 0, 0));
        squareViewsTwoDArray = new SquareView[8][8];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                SquareView squareView = new SquareView(getBoardService().getSquareBoard()[x][y]);
                this.add(squareView); // ??
                squareViewsTwoDArray[x][y] = squareView;
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
        PieceStrategy currPiece = this.boardService.getPiece();
        boolean whiteTurn = this.boardService.isWhiteTurn();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                SquareView sqView = squareViewsTwoDArray[y][x];
                sqView.setDisplayPiece(true);
                sqView.paintComponent(g);
            }
        }

        if (currPiece != null) {
            if ((currPiece.getPiece().getColor() == 1 && whiteTurn)
                    || (currPiece.getPiece().getColor() == 0 && !whiteTurn)) {
                final Image i = currPiece.getPiece().getImg();
                g.drawImage(i, this.boardService.getBoard().getCurrX(), this.boardService.getBoard().getCurrY(), null);
            }
        }
    }
}

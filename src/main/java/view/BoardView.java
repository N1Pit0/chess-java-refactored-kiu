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

    // Mouse position for rendering dragged piece
    private int mouseX = -1;
    private int mouseY = -1;

    public BoardView(BoardService boardService) {
        this.boardService = boardService;

        setLayout(new GridLayout(8, 8, 0, 0)); // 8x8 layout for chessboard
        squareViewsTwoDArray = new SquareView[8][8];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                SquareView squareView = new SquareView(getBoardService().getSquareBoard()[x][y]);
                this.add(squareView); // Add square to board
                squareViewsTwoDArray[x][y] = squareView;
            }
        }

        // Dimensions of the board
        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Render the full board
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                SquareView squareView = squareViewsTwoDArray[y][x];
                squareView.setDisplayPiece(true); // Enable piece display
                squareView.paintComponent(g); // Paint the individual square
            }
        }

        // If a piece is currently selected by the user, paint it being dragged
        PieceStrategy selectedPiece = boardService.getPiece();
        if (selectedPiece != null && mouseX != -1 && mouseY != -1) {
            final Image pieceImage = selectedPiece.getPiece().getImg();
            g.drawImage(pieceImage, mouseX - 20, mouseY - 20, null); // Adjust position to center the piece
        }
    }

    /**
     * Updates the drag position of the selected piece.
     * @param x Mouse X position
     * @param y Mouse Y position
     */
    public void updateMousePosition(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    /**
     * Resets the dragged piece position.
     */
    public void clearMousePosition() {
        this.mouseX = -1;
        this.mouseY = -1;
    }
}
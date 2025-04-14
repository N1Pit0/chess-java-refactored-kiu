package view;

import model.board.Square;
import lombok.Getter;
import lombok.Setter;
import services.SquareService;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
public class SquareView extends JComponent {
    private SquareService squareService;
    private boolean displayPiece;

    public SquareView(SquareService squareService) {
        this.squareService = squareService;
        this.displayPiece = true;

        this.setBorder(BorderFactory.createEmptyBorder());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.squareService.getSquare().getColor() == 1) {
            g.setColor(new Color(221, 192, 127));
        } else {
            g.setColor(new Color(101, 67, 33));
        }

        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        if (squareService.getOccupyingPiece() != null && displayPiece) {
            squareService.getOccupyingPiece().draw(g);
        }
    }
}

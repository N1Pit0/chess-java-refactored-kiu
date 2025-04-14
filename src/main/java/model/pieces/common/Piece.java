package model.pieces.common;

import lombok.Getter;
import lombok.Setter;
import model.board.Square;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.awt.Graphics;

@Getter
@Setter
public abstract class Piece {
    private final int color;
    private Square currentSquare;
    private BufferedImage img;

    public Piece(int color, Square initSq, String img_file) {
        this.color = color;
        this.currentSquare = initSq;

        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResource(img_file)));
        } catch (IOException | NullPointerException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

}
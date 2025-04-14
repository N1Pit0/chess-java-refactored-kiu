package model.board;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import model.pieces.common.Piece;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"board", "color", "occupyingPiece"}, callSuper = false)
public class Square {
    private final int color;
    private Board board;
    private Piece occupyingPiece;

    private int xNum;
    private int yNum;

    public Square(Board board, int c, int xNum, int yNum) {

        this.board = board;
        this.color = c;
        this.xNum = xNum;
        this.yNum = yNum;

    }

}

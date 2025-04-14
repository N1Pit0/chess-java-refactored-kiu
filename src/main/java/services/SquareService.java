package services;

import lombok.Getter;
import lombok.Setter;
import model.board.Square;

@Getter
@Setter
public class SquareService {

    private Square square;

    public SquareService(Square square) {
        this.square = square;
    }

}

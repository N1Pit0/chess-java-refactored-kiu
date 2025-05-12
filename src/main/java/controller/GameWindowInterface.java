package controller;

import services.enums.PieceColor;

public interface GameWindowInterface {
    void checkmateOccurred(PieceColor pieceColor);

    void stalemateOccurred();
}

package model.board;

import services.CheckmateDetector;
import view.gui.GameWindow;
import lombok.Getter;
import lombok.Setter;
import model.pieces.*;
import model.pieces.common.Piece;

import java.util.LinkedList;
import java.util.List;

import static model.enums.ImagePath.*;


@Getter
@Setter
public class Board {
    // Logical and graphical representations of board
    private final Square[][] squareChessBoard;
    private final GameWindow gameWindow;

    // List of pieces and whether they are movable
    private final LinkedList<Piece> blackPieces;
    private final LinkedList<Piece> whitePieces;

    private List<Square> movable;
    private boolean whiteTurn;
    private Piece currPiece;

    private int currX;
    private int currY;

    private CheckmateDetector ckeckmateDetector;

    public Board(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        squareChessBoard = new Square[8][8];
        blackPieces = new LinkedList<>();
        whitePieces = new LinkedList<>();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int xMod = x % 2;
                int yMod = y % 2;

                if ((xMod == 0 && yMod == 0) || (xMod == 1 && yMod == 1)) {
                    squareChessBoard[x][y] = new Square(this, 1, y, x);
                } else {
                    squareChessBoard[x][y] = new Square(this, 0, y, x);
                }
            }
        }

        initializePieces();

        whiteTurn = true;

    }

    private void initializePieces() {

        for (int x = 0; x < 8; x++) {
            squareChessBoard[1][x].put(new Pawn(0, squareChessBoard[1][x], RESOURCES_BPAWN_PNG.label));
            squareChessBoard[6][x].put(new Pawn(1, squareChessBoard[6][x], RESOURCES_WPAWN_PNG.label));
        }

        squareChessBoard[7][3].put(new Queen(1, squareChessBoard[7][3], RESOURCES_WQUEEN_PNG.label));
        squareChessBoard[0][3].put(new Queen(0, squareChessBoard[0][3], RESOURCES_BQUEEN_PNG.label));

        King bk = new King(0, squareChessBoard[0][4], RESOURCES_BKING_PNG.label);
        King wk = new King(1, squareChessBoard[7][4], RESOURCES_WKING_PNG.label);
        squareChessBoard[0][4].put(bk);
        squareChessBoard[7][4].put(wk);

        squareChessBoard[0][0].put(new Rook(0, squareChessBoard[0][0], RESOURCES_BROOK_PNG.label));
        squareChessBoard[0][7].put(new Rook(0, squareChessBoard[0][7], RESOURCES_BROOK_PNG.label));
        squareChessBoard[7][0].put(new Rook(1, squareChessBoard[7][0], RESOURCES_WROOK_PNG.label));
        squareChessBoard[7][7].put(new Rook(1, squareChessBoard[7][7], RESOURCES_WROOK_PNG.label));

        squareChessBoard[0][1].put(new Knight(0, squareChessBoard[0][1], RESOURCES_BKNIGHT_PNG.label));
        squareChessBoard[0][6].put(new Knight(0, squareChessBoard[0][6], RESOURCES_BKNIGHT_PNG.label));
        squareChessBoard[7][1].put(new Knight(1, squareChessBoard[7][1], RESOURCES_WKNIGHT_PNG.label));
        squareChessBoard[7][6].put(new Knight(1, squareChessBoard[7][6], RESOURCES_WKNIGHT_PNG.label));

        squareChessBoard[0][2].put(new Bishop(0, squareChessBoard[0][2], RESOURCES_BBISHOP_PNG.label));
        squareChessBoard[0][5].put(new Bishop(0, squareChessBoard[0][5], RESOURCES_BBISHOP_PNG.label));
        squareChessBoard[7][2].put(new Bishop(1, squareChessBoard[7][2], RESOURCES_WBISHOP_PNG.label));
        squareChessBoard[7][5].put(new Bishop(1, squareChessBoard[7][5], RESOURCES_WBISHOP_PNG.label));


        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 8; x++) {
                blackPieces.add(squareChessBoard[y][x].getOccupyingPiece());
                whitePieces.add(squareChessBoard[7 - y][x].getOccupyingPiece());
            }
        }

        ckeckmateDetector = new CheckmateDetector(this, whitePieces, blackPieces, wk, bk);
    }

    public void capture(Piece p, Square square) {
        Piece k = square.getOccupyingPiece();
        if (k.getColor() == 0) getBlackPieces().remove(k);
        if (k.getColor() == 1) getWhitePieces().remove(k);
        square.setOccupyingPiece(k);
    }

}
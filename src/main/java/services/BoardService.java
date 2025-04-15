package services;

import lombok.Getter;
import lombok.Setter;
import model.board.Board;
import model.board.Square;
import model.pieces.*;
import model.pieces.common.Piece;
import services.strategy.*;
import services.strategy.common.PieceStrategy;
import view.gui.GameWindow;

import java.util.LinkedList;
import java.util.List;

import static model.enums.ImagePath.*;

@Getter
@Setter
public class BoardService {

    private Board board;
    private SquareService[][] squareBoard;
    private final GameWindow gameWindow;

    private PieceStrategy piece;

    private final List<PieceStrategy> blackPieces;
    private final List<PieceStrategy> whitePieces;

    private CheckmateDetector ckeckmateDetector;

    private List<SquareService> movableSquares;

    private boolean whiteTurn;

    public BoardService(GameWindow gameWindow, Board board) {

        this.gameWindow = gameWindow;
        this.board = board;

        blackPieces = new LinkedList<>();
        whitePieces = new LinkedList<>();

        squareBoard = new SquareService[8][8];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int xMod = x % 2;
                int yMod = y % 2;

                if ((xMod == 0 && yMod == 0) || (xMod == 1 && yMod == 1)) {
                    squareBoard[x][y] = new SquareService(new Square(this.board, 1, y, x));
                } else {
                    squareBoard[x][y] = new SquareService(new Square(this.board, 0, y, x));
                }
            }
        }

        whiteTurn = true;

        initializePieces();
    }

    private void initializePieces() {
        Square[][] squares = board.getSquareChessBoard();

        for (int x = 0; x < 8; x++) {
            squareBoard[1][x].put(new PawnStrategy(new Pawn(0, squares[1][x], RESOURCES_BPAWN_PNG.label)));
            squareBoard[6][x].put(new PawnStrategy(new Pawn(1, squares[6][x], RESOURCES_WPAWN_PNG.label)));
        }

        squareBoard[7][3].put(new QueenStrategy(new Queen(1, squares[7][3], RESOURCES_WQUEEN_PNG.label)));
        squareBoard[0][3].put(new QueenStrategy(new Queen(0, squares[0][3], RESOURCES_BQUEEN_PNG.label)));

        KingStrategy bk = new KingStrategy(new King(0, squares[0][4], RESOURCES_BKING_PNG.label));
        KingStrategy wk = new KingStrategy(new King(1, squares[7][4], RESOURCES_WKING_PNG.label));
        squareBoard[0][4].put(bk);
        squareBoard[7][4].put(wk);

        squareBoard[0][0].put(new RookStrategy(new Rook(0, squares[0][0], RESOURCES_BROOK_PNG.label)));
        squareBoard[0][7].put(new RookStrategy(new Rook(0, squares[0][7], RESOURCES_BROOK_PNG.label)));
        squareBoard[7][0].put(new RookStrategy(new Rook(1, squares[7][0], RESOURCES_WROOK_PNG.label)));
        squareBoard[7][7].put(new RookStrategy(new Rook(1, squares[7][0], RESOURCES_WROOK_PNG.label)));

        squareBoard[0][1].put(new KnightStrategy(new Knight(0, squares[0][1], RESOURCES_BKNIGHT_PNG.label)));
        squareBoard[0][6].put(new KnightStrategy(new Knight(0, squares[0][6], RESOURCES_BKNIGHT_PNG.label)));
        squareBoard[7][1].put(new KnightStrategy(new Knight(0, squares[7][1], RESOURCES_WKNIGHT_PNG.label)));
        squareBoard[7][6].put(new KnightStrategy(new Knight(1, squares[7][6], RESOURCES_WKNIGHT_PNG.label)));

        squareBoard[0][2].put(new BishopStrategy(new Bishop(0, squares[0][2], RESOURCES_BBISHOP_PNG.label)));
        squareBoard[0][5].put(new BishopStrategy(new Bishop(0, squares[0][5], RESOURCES_BBISHOP_PNG.label)));
        squareBoard[7][2].put(new BishopStrategy(new Bishop(0, squares[7][2], RESOURCES_WBISHOP_PNG.label)));
        squareBoard[7][5].put(new BishopStrategy(new Bishop(1, squares[7][5], RESOURCES_WBISHOP_PNG.label)));


        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 8; x++) {
                blackPieces.add(squareBoard[y][x].getOccupyingPiece());
                whitePieces.add(squareBoard[7 - y][x].getOccupyingPiece());
            }
        }

        ckeckmateDetector = new CheckmateDetector(this, whitePieces, blackPieces, wk, bk);
    }

    public void capture(PieceStrategy p, SquareService squareService) {
        PieceStrategy piece = squareService.getOccupyingPiece();
        if (piece.getPiece().getColor() == 0) getBlackPieces().remove(piece);
        if (piece.getPiece().getColor() == 1) getWhitePieces().remove(piece);
        squareService.removePiece();
        squareService.put(p);
    }

}

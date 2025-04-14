//package services;
//
//import model.board.Board;
//import model.board.Square;
//import model.pieces.*;
//import services.strategy.common.PieceStrategy;
//import view.gui.GameWindow;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import static model.enums.ImagePath.*;
//import static model.enums.ImagePath.RESOURCES_WBISHOP_PNG;
//
//public class BoardService {
//
//    private Board board;
//    private final GameWindow gameWindow;
//
//    private final List<PieceStrategy> blackPieces;
//    private final List<PieceStrategy> whitePieces;
//
//    private CheckmateDetector ckeckmateDetector;
//
//    public BoardService(GameWindow gameWindow, Board board) {
//
//        this.gameWindow = gameWindow;
//        this.board = board;
//
//        blackPieces = new LinkedList<>();
//        whitePieces = new LinkedList<>();
//    }
//
//    private void initializePieces() {
//        Square[][] squareChessBoard = board.getSquareChessBoard();
//
//        for (int x = 0; x < 8; x++) {
//            squareChessBoard[1][x].put(new Pawn(0, squareChessBoard[1][x], RESOURCES_BPAWN_PNG.label));
//            squareChessBoard[6][x].put(new Pawn(1, squareChessBoard[6][x], RESOURCES_WPAWN_PNG.label));
//        }
//
//        squareChessBoard[7][3].put(new Queen(1, squareChessBoard[7][3], RESOURCES_WQUEEN_PNG.label));
//        squareChessBoard[0][3].put(new Queen(0, squareChessBoard[0][3], RESOURCES_BQUEEN_PNG.label));
//
//        King bk = new King(0, squareChessBoard[0][4], RESOURCES_BKING_PNG.label);
//        King wk = new King(1, squareChessBoard[7][4], RESOURCES_WKING_PNG.label);
//        squareChessBoard[0][4].put(bk);
//        squareChessBoard[7][4].put(wk);
//
//        squareChessBoard[0][0].put(new Rook(0, squareChessBoard[0][0], RESOURCES_BROOK_PNG.label));
//        squareChessBoard[0][7].put(new Rook(0, squareChessBoard[0][7], RESOURCES_BROOK_PNG.label));
//        squareChessBoard[7][0].put(new Rook(1, squareChessBoard[7][0], RESOURCES_WROOK_PNG.label));
//        squareChessBoard[7][7].put(new Rook(1, squareChessBoard[7][7], RESOURCES_WROOK_PNG.label));
//
//        squareChessBoard[0][1].put(new Knight(0, squareChessBoard[0][1], RESOURCES_BKNIGHT_PNG.label));
//        squareChessBoard[0][6].put(new Knight(0, squareChessBoard[0][6], RESOURCES_BKNIGHT_PNG.label));
//        squareChessBoard[7][1].put(new Knight(1, squareChessBoard[7][1], RESOURCES_WKNIGHT_PNG.label));
//        squareChessBoard[7][6].put(new Knight(1, squareChessBoard[7][6], RESOURCES_WKNIGHT_PNG.label));
//
//        squareChessBoard[0][2].put(new Bishop(0, squareChessBoard[0][2], RESOURCES_BBISHOP_PNG.label));
//        squareChessBoard[0][5].put(new Bishop(0, squareChessBoard[0][5], RESOURCES_BBISHOP_PNG.label));
//        squareChessBoard[7][2].put(new Bishop(1, squareChessBoard[7][2], RESOURCES_WBISHOP_PNG.label));
//        squareChessBoard[7][5].put(new Bishop(1, squareChessBoard[7][5], RESOURCES_WBISHOP_PNG.label));
//
//
//        for (int y = 0; y < 2; y++) {
//            for (int x = 0; x < 8; x++) {
//                blackPieces.add(squareChessBoard[y][x].getOccupyingPiece());
//                whitePieces.add(squareChessBoard[7 - y][x].getOccupyingPiece());
//            }
//        }
//
//        ckeckmateDetector = new CheckmateDetector(this, whitePieces, blackPieces, wk, bk);
//    }
//
//
//}

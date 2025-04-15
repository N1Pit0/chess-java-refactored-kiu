package services;

import model.board.Board;
import model.board.Square;
import model.pieces.Bishop;
import model.pieces.King;
import model.pieces.Queen;
import model.pieces.common.Piece;
import services.strategy.KingStrategy;
import services.strategy.common.PieceStrategy;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * Component of the Chess game that detects check mates in the game.
 *
 * @author Jussi Lundstedt
 */
public class CheckmateDetector {
    private final List<SquareService> squares;
    private BoardService board;
    private List<PieceStrategy> wPieces;
    private List<PieceStrategy> bPieces;
    private List<SquareService> movableSquares;
    private KingStrategy bk;
    private KingStrategy wk;
    private HashMap<SquareService, List<PieceStrategy>> wMoves;
    private HashMap<SquareService, List<PieceStrategy>> bMoves;

    /**
     * Constructs a new instance of services.CheckmateDetector on a given board. By
     * convention should be called when the board is in its initial state.
     *
     * @param board       The board which the detector monitors
     * @param wPieces White pieces on the board.
     * @param bPieces Black pieces on the board.
     * @param wk      chesspieces.common.Piece object representing the white king
     * @param bk      chesspieces.common.Piece object representing the black king
     */
    public CheckmateDetector(BoardService board, List<PieceStrategy> wPieces,
                             List<PieceStrategy> bPieces, KingStrategy wk, KingStrategy bk) {
        this.board = board;
        this.wPieces = wPieces;
        this.bPieces = bPieces;
        this.bk = bk;
        this.wk = wk;

        // Initialize other fields
        squares = new LinkedList<>();
        movableSquares = new LinkedList<>();
        wMoves = new HashMap<>();
        bMoves = new HashMap<>();

        SquareService[][] brd = board.getSquareBoard();

        // add all squares to squares list and as hashmap keys
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                squares.add(brd[y][x]);
                wMoves.put(brd[y][x], new LinkedList<>());
                bMoves.put(brd[y][x], new LinkedList<>());
            }
        }

        // update situation
        update();
    }

    /**
     * Updates the object with the current situation of the game.
     */
    public void update() {
        // Iterators through pieces
        Iterator<PieceStrategy> wIter = wPieces.iterator();
        Iterator<PieceStrategy> bIter = bPieces.iterator();

        // empty moves and movable squares at each update
        for (List<PieceStrategy> pieces : wMoves.values()) {
            pieces.removeAll(pieces);
        }

        for (List<PieceStrategy> pieces : bMoves.values()) {
            pieces.removeAll(pieces);
        }

        movableSquares.removeAll(movableSquares);

        // Add each move white and black can make to map
        while (wIter.hasNext()) {
            PieceStrategy p = wIter.next();

            if (p.getSquareService().getSquare() == null) {
                wIter.remove();
                continue;
            }

            List<SquareService> mvs = p.getLegalMoves(board);
            for (SquareService mv : mvs) {
                List<PieceStrategy> pieces = wMoves.get(mv);
                pieces.add(p);
            }
        }

        while (bIter.hasNext()) {
            PieceStrategy p = bIter.next();

            if (p.getSquareService().getSquare() == null) {
                wIter.remove();
                continue;
            }

            List<SquareService> mvs = p.getLegalMoves(board);
            for (SquareService mv : mvs) {
                List<PieceStrategy> pieces = bMoves.get(mv);
                pieces.add(p);
            }
        }
    }

    /**
     * Checks if the black king is threatened
     *
     * @return boolean representing whether the black king is in check.
     */
    public boolean blackInCheck() {
        update();
        SquareService sq = bk.getSquareService();
        if (wMoves.get(sq).isEmpty()) {
            movableSquares.addAll(squares);
            return false;
        } else return true;
    }

    /**
     * Checks if the white king is threatened
     *
     * @return boolean representing whether the white king is in check.
     */
    public boolean whiteInCheck() {
        update();
        SquareService sq = wk.getSquareService();
        if (bMoves.get(sq).isEmpty()) {
            movableSquares.addAll(squares);
            return false;
        } else return true;
    }

    /**
     * Checks whether black is in checkmate.
     *
     * @return boolean representing if black player is checkmated.
     */
    public boolean blackCheckMated() {
        boolean checkmate = true;
        // Check if black is in check
        if (!this.blackInCheck()) return false;

        // If yes, check if king can evade
        if (canEvade(wMoves, bk)) checkmate = false;

        // If no, check if threat can be captured
        List<PieceStrategy> threats = wMoves.get(bk.getSquareService());
        if (canCapture(bMoves, threats, bk)) checkmate = false;

        // If no, check if threat can be blocked
        if (canBlock(threats, bMoves, bk)) checkmate = false;

        // If no possible ways of removing check, checkmate occurred
        return checkmate;
    }

    /**
     * Checks whether white is in checkmate.
     *
     * @return boolean representing if white player is checkmated.
     */
    public boolean whiteCheckMated() {
        boolean checkmate = true;
        // Check if white is in check
        if (!this.whiteInCheck()) return false;

        // If yes, check if king can evade
        if (canEvade(bMoves, wk)) checkmate = false;

        // If no, check if threat can be captured
        List<PieceStrategy> threats = bMoves.get(wk.getSquareService());
        if (canCapture(wMoves, threats, wk)) checkmate = false;

        // If no, check if threat can be blocked
        if (canBlock(threats, wMoves, wk)) checkmate = false;

        // If no possible ways of removing check, checkmate occurred
        return checkmate;
    }

    /*
     * Helper method to determine if the king can evade the check.
     * Gives a false positive if the king can capture the checking piece.
     */
    private boolean canEvade(Map<SquareService, List<PieceStrategy>> tMoves, KingStrategy tKing) {
        boolean evade = false;
        List<SquareService> kingsMoves = tKing.getLegalMoves(board);

        // If king is not threatened at some square, it can evade
        for (SquareService sq : kingsMoves) {
            if (!testMove(tKing, sq)) continue;
            if (tMoves.get(sq).isEmpty()) {
                movableSquares.add(sq);
                evade = true;
            }
        }

        return evade;
    }

    /*
     * Helper method to determine if the threatening piece can be captured.
     */
    private boolean canCapture(Map<SquareService, List<PieceStrategy>> poss,
                               List<PieceStrategy> threats, KingStrategy k) {

        boolean capture = false;
        if (threats.size() == 1) {
            SquareService sq = threats.get(0).getSquareService();

            if (k.getLegalMoves(board).contains(sq)) {
                movableSquares.add(sq);
                if (testMove(k, sq)) {
                    capture = true;
                }
            }

            List<PieceStrategy> caps = poss.get(sq);
            ConcurrentLinkedDeque<PieceStrategy> capturers = new ConcurrentLinkedDeque<>(caps);

            if (!capturers.isEmpty()) {
                movableSquares.add(sq);
                for (PieceStrategy p : capturers) {
                    if (testMove(p, sq)) {
                        capture = true;
                    }
                }
            }
        }

        return capture;
    }

    /*
     * Helper method to determine if check can be blocked by a piece.
     */
    private boolean canBlock(List<PieceStrategy> threats,
                             Map<SquareService, List<PieceStrategy>> blockMoves, KingStrategy k) {
        boolean blockable = false;

        if (threats.size() == 1) {
            SquareService squareService = threats.get(0).getSquareService();
            Square ts = squareService.getSquare();
            SquareService squareService1 = k.getSquareService();
            Square ks = squareService1.getSquare();
            SquareService[][] brdArray = board.getSquareBoard();

            if (ks.getXNum() == ts.getXNum()) {
                int max = Math.max(ks.getYNum(), ts.getYNum());
                int min = Math.min(ks.getYNum(), ts.getYNum());

                for (int i = min + 1; i < max; i++) {
                    List<PieceStrategy> blks =
                            blockMoves.get(brdArray[i][ks.getXNum()]);
                    ConcurrentLinkedDeque<PieceStrategy> blockers =
                            new ConcurrentLinkedDeque<>(blks);

                    if (!blockers.isEmpty()) {
                        movableSquares.add(brdArray[i][ks.getXNum()]);

                        for (PieceStrategy p : blockers) {
                            if (testMove(p, brdArray[i][ks.getXNum()])) {
                                blockable = true;
                            }
                        }

                    }
                }
            }

            if (ks.getYNum() == ts.getYNum()) {
                int max = Math.max(ks.getXNum(), ts.getXNum());
                int min = Math.min(ks.getXNum(), ts.getXNum());

                for (int i = min + 1; i < max; i++) {
                    List<PieceStrategy> blks =
                            blockMoves.get(brdArray[ks.getYNum()][i]);
                    ConcurrentLinkedDeque<PieceStrategy> blockers =
                            new ConcurrentLinkedDeque<>(blks);

                    if (!blockers.isEmpty()) {

                        movableSquares.add(brdArray[ks.getYNum()][i]);

                        for (PieceStrategy p : blockers) {
                            if (testMove(p, brdArray[ks.getYNum()][i])) {
                                blockable = true;
                            }
                        }

                    }
                }
            }

            Class<? extends PieceStrategy> tC = threats.get(0).getClass();

            if (tC.equals(Queen.class) || tC.equals(Bishop.class)) {
                int kX = ks.getXNum();
                int kY = ks.getYNum();
                int tX = ts.getXNum();
                int tY = ts.getYNum();

                if (kX > tX && kY > tY) {
                    for (int i = tX + 1; i < kX; i++) {
                        tY++;
                        List<PieceStrategy> blks =
                                blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<PieceStrategy> blockers =
                                new ConcurrentLinkedDeque<>();
                        blockers.addAll(blks);

                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[tY][i]);

                            for (PieceStrategy p : blockers) {
                                if (testMove(p, brdArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }

                if (kX > tX && tY > kY) {
                    for (int i = tX + 1; i < kX; i++) {
                        tY--;
                        List<PieceStrategy> blks =
                                blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<PieceStrategy> blockers =
                                new ConcurrentLinkedDeque<>();
                        blockers.addAll(blks);

                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[tY][i]);

                            for (PieceStrategy p : blockers) {
                                if (testMove(p, brdArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }

                if (tX > kX && kY > tY) {
                    for (int i = tX - 1; i > kX; i--) {
                        tY++;
                        List<PieceStrategy> blks =
                                blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<PieceStrategy> blockers =
                                new ConcurrentLinkedDeque<>();
                        blockers.addAll(blks);

                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[tY][i]);

                            for (PieceStrategy p : blockers) {
                                if (testMove(p, brdArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }

                if (tX > kX && tY > kY) {
                    for (int i = tX - 1; i > kX; i--) {
                        tY--;
                        List<PieceStrategy> blks =
                                blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<PieceStrategy> blockers =
                                new ConcurrentLinkedDeque<>();
                        blockers.addAll(blks);

                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[tY][i]);

                            for (PieceStrategy p : blockers) {
                                if (testMove(p, brdArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return blockable;
    }

    /**
     * Method to get a list of allowable squares that the player can move.
     * Defaults to all squares, but limits available squares if player is in
     * check.
     *
     * @param b boolean representing whether it's white player's turn (if yes,
     *          true)
     * @return List of squares that the player can move into.
     */
    public List<SquareService> getAllowableSquares(boolean b) {
        movableSquares.removeAll(movableSquares);
        if (whiteInCheck()) {
            whiteCheckMated();
        } else if (blackInCheck()) {
            blackCheckMated();
        }
        return movableSquares;
    }

    /**
     * Tests a move a player is about to make to prevent making an illegal move
     * that puts the player in check.
     *
     * @param p  chesspieces.common.Piece moved
     * @param sq chesspieces.Square to which p is about to move
     * @return false if move would cause a check
     */
    public boolean testMove(PieceStrategy p, SquareService sq) {
        PieceStrategy pieceStrategy = sq.getOccupyingPiece();

        boolean movetest = true;
        SquareService squareService = p.getSquareService();

        p.move(sq, board);
        update();

        if (p.getPiece().getColor() == 0 && blackInCheck()) movetest = false;
        else if (p.getPiece().getColor() == 1 && whiteInCheck()) movetest = false;

        p.move(squareService, board);
        if (pieceStrategy != null) sq.put(pieceStrategy);

        update();

        movableSquares.addAll(squares);
        return movetest;
    }

}

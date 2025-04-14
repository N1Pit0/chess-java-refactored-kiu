package model.pieces;

import model.board.Board;
import model.board.Square;
import model.pieces.common.Piece;

import java.util.LinkedList;
import java.util.List;

public class Pawn extends Piece {
    private boolean wasMoved;

    public Pawn(int color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }

    @Override
    public boolean move(Square fin) {
        boolean b = super.move(fin, super.getCurrentSquare().getBoard());
        wasMoved = true;
        return b;
    }

    @Override
    public List<Square> getLegalMoves(Board b) {
        LinkedList<Square> legalMoves = new LinkedList<Square>();

        Square[][] board = b.getSquareChessBoard();

        int x = this.getCurrentSquare().getXNum();
        int y = this.getCurrentSquare().getYNum();
        int c = this.getColor();

        if (c == 0) {
            if (!wasMoved) {
                if (!board[y + 2][x].isOccupied()) {
                    legalMoves.add(board[y + 2][x]);
                }
            }

            if (y + 1 < 8) {
                if (!board[y + 1][x].isOccupied()) {
                    legalMoves.add(board[y + 1][x]);
                }
            }

            if (x + 1 < 8 && y + 1 < 8) {
                if (board[y + 1][x + 1].isOccupied()) {
                    legalMoves.add(board[y + 1][x + 1]);
                }
            }

            if (x - 1 >= 0 && y + 1 < 8) {
                if (board[y + 1][x - 1].isOccupied()) {
                    legalMoves.add(board[y + 1][x - 1]);
                }
            }
        }

        if (c == 1) {
            if (!wasMoved) {
                if (!board[y - 2][x].isOccupied()) {
                    legalMoves.add(board[y - 2][x]);
                }
            }

            if (y - 1 >= 0) {
                if (!board[y - 1][x].isOccupied()) {
                    legalMoves.add(board[y - 1][x]);
                }
            }

            if (x + 1 < 8 && y - 1 >= 0) {
                if (board[y - 1][x + 1].isOccupied()) {
                    legalMoves.add(board[y - 1][x + 1]);
                }
            }

            if (x - 1 >= 0 && y - 1 >= 0) {
                if (board[y - 1][x - 1].isOccupied()) {
                    legalMoves.add(board[y - 1][x - 1]);
                }
            }
        }

        return legalMoves;
    }
}

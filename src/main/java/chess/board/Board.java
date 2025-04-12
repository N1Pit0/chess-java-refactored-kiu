package chess.board;

import chess.pieces.*;
import chess.pieces.common.Piece;
import game.control.CheckmateDetector;
import game.gui.GameWindow;
import lombok.Getter;
import lombok.Setter;

import static game.enums.ImagePath.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

@SuppressWarnings("serial")
@Getter
@Setter
public class Board extends JPanel{
	// Logical and graphical representations of board
	private final Square[][] board;
    private final GameWindow gameWindow;
    
    // List of pieces and whether they are movable
    public final LinkedList<Piece> Bpieces;
    public final LinkedList<Piece> Wpieces;
    public List<Square> movable;

    private boolean whiteTurn;

    @Getter
    private Piece currPiece;
    @Getter
    private int currX;
    private int currY;
    
    private CheckmateDetector cmd;

    public Board(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        board = new Square[8][8];
        Bpieces = new LinkedList<Piece>();
        Wpieces = new LinkedList<Piece>();
        setLayout(new GridLayout(8, 8, 0, 0));

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int xMod = x % 2;
                int yMod = y % 2;

                if ((xMod == 0 && yMod == 0) || (xMod == 1 && yMod == 1)) {
                    board[x][y] = new Square(this, 1, y, x);
                    this.add(board[x][y]);
                } else {
                    board[x][y] = new Square(this, 0, y, x);
                    this.add(board[x][y]);
                }
            }
        }

        initializePieces();

        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));

        whiteTurn = true;

    }

    private void initializePieces() {
    	
        for (int x = 0; x < 8; x++) {
            board[1][x].put(new Pawn(0, board[1][x], RESOURCES_BPAWN_PNG.label));
            board[6][x].put(new Pawn(1, board[6][x], RESOURCES_WPAWN_PNG.label));
        }
        
        board[7][3].put(new Queen(1, board[7][3], RESOURCES_WQUEEN_PNG.label));
        board[0][3].put(new Queen(0, board[0][3], RESOURCES_BQUEEN_PNG.label));
        
        King bk = new King(0, board[0][4], RESOURCES_BKING_PNG.label);
        King wk = new King(1, board[7][4], RESOURCES_WKING_PNG.label);
        board[0][4].put(bk);
        board[7][4].put(wk);

        board[0][0].put(new Rook(0, board[0][0], RESOURCES_BROOK_PNG.label));
        board[0][7].put(new Rook(0, board[0][7], RESOURCES_BROOK_PNG.label));
        board[7][0].put(new Rook(1, board[7][0], RESOURCES_WROOK_PNG.label));
        board[7][7].put(new Rook(1, board[7][7], RESOURCES_WROOK_PNG.label));

        board[0][1].put(new Knight(0, board[0][1], RESOURCES_BKNIGHT_PNG.label));
        board[0][6].put(new Knight(0, board[0][6], RESOURCES_BKNIGHT_PNG.label));
        board[7][1].put(new Knight(1, board[7][1], RESOURCES_WKNIGHT_PNG.label));
        board[7][6].put(new Knight(1, board[7][6], RESOURCES_WKNIGHT_PNG.label));

        board[0][2].put(new Bishop(0, board[0][2], RESOURCES_BBISHOP_PNG.label));
        board[0][5].put(new Bishop(0, board[0][5], RESOURCES_BBISHOP_PNG.label));
        board[7][2].put(new Bishop(1, board[7][2], RESOURCES_WBISHOP_PNG.label));
        board[7][5].put(new Bishop(1, board[7][5], RESOURCES_WBISHOP_PNG.label));
        
        
        for(int y = 0; y < 2; y++) {
            for (int x = 0; x < 8; x++) {
                Bpieces.add(board[y][x].getOccupyingPiece());
                Wpieces.add(board[7-y][x].getOccupyingPiece());
            }
        }
        
        cmd = new CheckmateDetector(this, Wpieces, Bpieces, wk, bk);
    }

    public Square[][] getSquareArray() {
        return this.board;
    }

    public boolean getTurn() {
        return whiteTurn;
    }

    @Override
    public void paintComponent(Graphics g) {
        // super.paintComponent(g);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square sq = board[y][x];
                sq.paintComponent(g);
            }
        }

        if (currPiece != null) {
            if ((currPiece.getColor() == 1 && whiteTurn)
                    || (currPiece.getColor() == 0 && !whiteTurn)) {
                final Image i = currPiece.getImage();
                g.drawImage(i, currX, currY, null);
            }
        }
    }

}
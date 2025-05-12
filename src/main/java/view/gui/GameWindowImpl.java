package view.gui;

import controller.GameController;
import controller.GameControllerImpl;
import controller.GameWindowInterface;
import model.board.Board;
import services.board.BoardInterface;
import services.board.BoardService;
import services.board.BoardServiceImpl;
import services.checkmatedetection.CheckmateDetector;
import services.checkmatedetection.CheckmateDetectorImpl;
import services.enums.PieceColor;
import services.utils.Clock;
import view.BoardView;
import view.mouseListener.CustomBoardMouseListenerImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static services.enums.ImagePath.RESOURCES_WPAWN_PNG;
import static services.enums.PieceColor.BLACK;


public class GameWindowImpl implements GameWindow, GameWindowInterface {
    private final JFrame gameWindow;
    private final BoardService boardService;
    public Clock blackClock;
    public Clock whiteClock;
    private Timer timer;


    public GameWindowImpl(String blackName, String whiteName, int hh,
                          int mm, int ss) {

        blackClock = new Clock(hh, ss, mm);
        whiteClock = new Clock(hh, ss, mm);

        gameWindow = new JFrame("Chess");

        try {
            Image whiteImg = ImageIO.read(getClass().getResource(RESOURCES_WPAWN_PNG.label));
            gameWindow.setIconImage(whiteImg);
        } catch (Exception e) {
            System.out.println("Game file wp.png not found");
        }

        gameWindow.setLocation(100, 100);

        gameWindow.setLayout(new BorderLayout(20, 20));

        // Game Data window
        JPanel gameData = gameDataPanel(blackName, whiteName, hh, mm, ss);
        gameData.setSize(gameData.getPreferredSize());
        gameWindow.add(gameData, BorderLayout.NORTH);

        BoardInterface boardInterface = new Board();

        this.boardService = new BoardServiceImpl(boardInterface);

        BoardView boardView = new BoardView(boardService);

        CheckmateDetector checkmateDetector = new CheckmateDetectorImpl();

        GameController gameController = new GameControllerImpl(boardService, checkmateDetector, this);

        new CustomBoardMouseListenerImpl(boardView, this, gameController);

        gameWindow.add(boardView, BorderLayout.CENTER);

        gameWindow.add(buttons(), BorderLayout.SOUTH);

        gameWindow.setMinimumSize(gameWindow.getPreferredSize());
        gameWindow.setSize(gameWindow.getPreferredSize());
        gameWindow.setResizable(false);

        gameWindow.pack();
        gameWindow.setVisible(true);
        gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

// Helper function to create data panel

    private JPanel gameDataPanel(final String blackName, final String whiteName,
                                 final int hours, final int minutes, final int seconds) {

        JPanel gameData = new JPanel();
        gameData.setLayout(new GridLayout(3, 2, 0, 0));

        // PLAYER NAMES
        JLabel whitePlayerLabel = new JLabel(whiteName);
        JLabel blackPlayerLabel = new JLabel(blackName);

        whitePlayerLabel.setHorizontalAlignment(JLabel.CENTER);
        whitePlayerLabel.setVerticalAlignment(JLabel.CENTER);
        blackPlayerLabel.setHorizontalAlignment(JLabel.CENTER);
        blackPlayerLabel.setVerticalAlignment(JLabel.CENTER);

        whitePlayerLabel.setSize(whitePlayerLabel.getMinimumSize());
        blackPlayerLabel.setSize(blackPlayerLabel.getMinimumSize());

        gameData.add(whitePlayerLabel);
        gameData.add(blackPlayerLabel);

        // CLOCKS
        final JLabel blackTimeLabel = new JLabel(blackClock.getFormattedTime());
        final JLabel whiteTimeLabel = new JLabel(whiteClock.getFormattedTime());

        blackTimeLabel.setHorizontalAlignment(JLabel.CENTER);
        blackTimeLabel.setVerticalAlignment(JLabel.CENTER);
        whiteTimeLabel.setHorizontalAlignment(JLabel.CENTER);
        whiteTimeLabel.setVerticalAlignment(JLabel.CENTER);

        if (!(hours == 0 && minutes == 0 && seconds == 0)) {
            timer = new Timer(1000, null);
            timer.addActionListener(e -> {
                boolean turn = boardService.isWhiteTurn();

                if (turn) {
                    handleClockTimer(true, whiteTimeLabel, whiteName, blackName, hours, minutes, seconds);
                } else {
                    handleClockTimer(false, blackTimeLabel, blackName, whiteName, hours, minutes, seconds);
                }
            });
            timer.start();
        } else {
            whiteTimeLabel.setText("Untimed game");
            blackTimeLabel.setText("Untimed game");
        }

        gameData.add(whiteTimeLabel);
        gameData.add(blackTimeLabel);

        gameData.setPreferredSize(gameData.getMinimumSize());

        return gameData;
    }

    private void handleClockTimer(boolean whiteTurn, JLabel time, String currentPlayerName, String opponentPlayerName,
                                  int hours, int minutes, int seconds) {
        Clock playerClock = whiteTurn ? whiteClock : blackClock;

        playerClock.decrementTime();

        time.setText(whiteClock.getFormattedTime());

        if (playerClock.isTimeUp()) {
            timer.stop();
            int n = JOptionPane.showConfirmDialog(
                    gameWindow,
                    opponentPlayerName + " wins by time! Play a new game? \n" +
                            "Choosing \"No\" quits the game.",
                    opponentPlayerName + " wins!",
                    JOptionPane.YES_NO_OPTION);

            if (n == JOptionPane.YES_OPTION) {
                new GameWindowImpl(opponentPlayerName, currentPlayerName, hours, minutes, seconds);
                gameWindow.dispose();
            } else gameWindow.dispose();
        }
    }

    private JPanel buttons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 10, 0));

        final var quit = getQuitButton();

        final var newGame = getNewGameButton();

        final JButton instr = new JButton("How to play");

        instr.addActionListener(e -> JOptionPane.showMessageDialog(gameWindow,
                "MoveImpl the chess pieces on the boardService by clicking\n"
                        + "and dragging. The game will watch out for illegal\n"
                        + "moves. You can win either by your opponent running\n"
                        + "out of time or by checkmating your opponent.\n"
                        + "\nGood luck, hope you enjoy the game!",
                "How to play",
                JOptionPane.PLAIN_MESSAGE));

        buttons.add(instr);
        buttons.add(newGame);
        buttons.add(quit);

        buttons.setPreferredSize(buttons.getMinimumSize());

        return buttons;
    }

    private JButton getNewGameButton() {
        final JButton nGame = new JButton("New game");

        nGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        gameWindow,
                        "Are you sure you want to begin a new game?",
                        "Confirm new game", JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    SwingUtilities.invokeLater(new StartMenu());
                    gameWindow.dispose();
                }
            }
        });
        return nGame;
    }

    private JButton getQuitButton() {
        final JButton quit = new JButton("Quit");

        quit.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(
                    gameWindow,
                    "Are you sure you want to quit?",
                    "Confirm quit", JOptionPane.YES_NO_OPTION);

            if (n == JOptionPane.YES_OPTION) {
                if (timer != null) timer.stop();
                gameWindow.dispose();
            }
        });
        return quit;
    }

    public void checkmateOccurred(PieceColor pieceColor) {
        String outputMessage = "";
        String title = "";
        if (pieceColor.equals(BLACK)) {

            outputMessage = "White wins by checkmate! Set up a new game? \n" +
                    "Choosing \"No\" lets you look at the final situation.";

            title = "White wins by checkmate!";
        } else {

            outputMessage = "Black wins by checkmate! Set up a new game? \n" +
                    "Choosing \"No\" lets you look at the final situation.";

            title = "Black wins by checkmate!";
        }

        endLogicHelper(outputMessage, title);
    }

    public void stalemateOccurred(){
        String outputMessage = "Stalemate! Set up a new game? \n" +
                "Choosing \"No\" lets you look at the final situation.";

        String title = "Draw!";

        endLogicHelper(outputMessage, title);
    }

    private void endLogicHelper(String outputMessage, String title){
        if (timer != null) timer.stop();
        int n = JOptionPane.showConfirmDialog(
                gameWindow,
                outputMessage,
                title,
                JOptionPane.YES_NO_OPTION);

        if (n == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(new StartMenu());
            gameWindow.dispose();
        }
    }

}

package checkers.ui;

import checkers.Checkers;
import checkers.Move;
import checkers.MoveCollection;
import checkers.ui.board.Board;
import checkers.ui.board.Cell;
import checkers.ui.board.Chip;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class Game extends VBox {

    /**
     * Event handler fired when game is quit.
     */
    private EventHandler<ActionEvent> onGameExit;

    /**
     * Game configuration object.
     */
    private final GameConfig gameConfig;

    /**
     * Internal checkers game instance.
     */
    private Checkers checkers;

    /**
     * Board UI instance.
     */
    private final Board boardUI;

    /**
     * Game UI.
     *
     * This class takes input from the user(s) and updates the internal game
     * state.
     *
     * @param gameConfig    Game configuration object.
     */
    public Game(GameConfig gameConfig) {
        super();

        this.gameConfig = gameConfig;

        Menu m1 = new Menu("Game");
        MenuItem mi1 = new MenuItem("Reset");
        MenuItem mi2 = new MenuItem("Quit");
        Menu m2 = new Menu("Help");
        MenuItem mi4 = new MenuItem("Get A Hint");
        MenuItem mi5 = new MenuItem("How To Play");
        MenuBar mb = new MenuBar();

        boardUI = new Board(gameConfig.getResolution(),
                gameConfig.getBoardSize());

        m1.getItems().addAll(mi1, new SeparatorMenuItem(), mi2);
        m2.getItems().addAll(mi4, mi5);
        mb.getMenus().addAll(m1, m2);
        this.getChildren().addAll(mb, boardUI);

        checkers = new Checkers(gameConfig.getBoardSize());

        mi1.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Reset");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to reset this game?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.orElse(null) == ButtonType.OK) {
                reset();
            }
        });

        mi2.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Quit");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to quit this game?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.orElse(null) == ButtonType.OK) {
                onGameExit.handle(null);
            }
        });

        mi4.setOnAction(e -> {
            Alert hintAlert = new Alert(Alert.AlertType.INFORMATION);
            hintAlert.setTitle("Hint");
            hintAlert.setHeaderText(null);
            String player = checkers.getCurrentPlayer() == 1 ? "Black" :
                    "White";
            Move move = checkers.getNextBestMove(5);
            hintAlert.setContentText(String.format("%s, move chip at " +
                            "position %d to position %d.", player,
                    move.getStart()+1,
                    move.getDest()+1));
            hintAlert.showAndWait();
        });

        mi5.setOnAction(e -> {
            Alert helpAlert = new Alert(Alert.AlertType.INFORMATION);
            helpAlert.setTitle("How To Play");
            helpAlert.setHeaderText("How To Play Checkers");
            helpAlert.setContentText(
                    "The object is to capture all opposing players checkers " +
                            "or to create a situation where your opponent has" +
                            " no moves left to make." +
                            "\n\nCheckers may only move forward.\n\nThere are" +
                            " two types of move, capturing and non-capturing." +
                            " Non-capturing moves are a move forward " +
                            "diagonally to an adjacent square. Capturing " +
                            "moves are when a chip jumps over an opponent " +
                            "chip.\n\nOn a capturing move, a player can make " +
                            "multiple jumps if they land in a position in " +
                            "which they can make another.\n\nWhen a player is" +
                            " in a position to make a capturing move, that " +
                            "move must be taken. If they can make multiple " +
                            "capturing moves, any of these moves can be taken" +
                            ".\n\nOnce a chip reaches the opponent far-side " +
                            "of the board, the chip becomes a king, and can " +
                            "then move that chip in any direction until the " +
                            "end of the game.\n\nIf a chip captures an " +
                            "opposing chip which is already a king, then that" +
                            " chip becomes a king and the opposing chip is " +
                            "captured as normal."
            );
            helpAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            helpAlert.showAndWait();
        });

        beginRound();
    }

    /**
     * Sets an event which will be fired once the game has been quit.
     *
     * @param onGameExit    The EventHandler to invoke.
     */
    public void setOnGameExit(EventHandler<ActionEvent> onGameExit) {
        this.onGameExit = onGameExit;
    }

    /**
     * Moves the game on to the next round.
     */
    private void beginRound() {
        updateBoard();

        if(checkers.getValidMoves().size() == 0) {
            Platform.runLater(this::gameOver);
        }

        int computerPlayer = gameConfig.isComputerStarts() ? 1 : 2;
        if(gameConfig.isComputer() &&
                checkers.getCurrentPlayer() == computerPlayer) {
            CompletableFuture
                    .supplyAsync(() -> checkers.getNextBestMove(
                            gameConfig.getComputerDifficulty()
                    ))
                    .thenAccept(this::autoMove);
        } else {
            engageMoveChips();
        }
    }

    /**
     * Resets the game back to beginning.
     */
    private void reset() {
        boardUI.reset();
        checkers = new Checkers(gameConfig.getBoardSize());
        beginRound();
    }

    /**
     * Called once the game has been won. Displays a message showing which
     * player has won the game and the option to rematch or quit.
     */
    private void gameOver() {
        String player = checkers.getCurrentPlayer() == 2 ? "Black" : "White";
        Alert gameOverAlert = new Alert(Alert.AlertType.CONFIRMATION);
        gameOverAlert.setTitle("Game Over");
        gameOverAlert.setHeaderText(null);
        gameOverAlert.setContentText(String.format("%s wins!", player));

        ButtonType rematchButton = new ButtonType("Rematch");
        ButtonType quitButton = new ButtonType("Quit",
                ButtonBar.ButtonData.CANCEL_CLOSE);

        gameOverAlert.getButtonTypes().setAll(rematchButton, quitButton);

        Optional<ButtonType> result = gameOverAlert.showAndWait();
        if(result.orElse(null) == rematchButton) {
            reset();
        } else {
            onGameExit.handle(null);
        }
    }

    /**
     * Makes a move automatically without player input. Used for simulating
     * computer moves.
     *
     * @param move  The move to make.
     */
    public void autoMove(Move move) {
        checkers.Chip c = checkers.getChip(move.getStart());
        Chip chip = c.getPlayer() == 1 ?
                boardUI.getBlackChip(move.getStart()) :
                boardUI.getWhiteChip(move.getStart());
        chip.setLifted(true);
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(1000), e ->
                placeChip(chip, new MoveCollection(), move)));
        tl.play();
    }

    /**
     * Makes all of the available move chips interactive so that the current
     * player can choose a chip to move.
     */
    public void engageMoveChips() {
        HashMap<Integer, MoveCollection> moves = checkers.getValidMovesByCell();
        IntStream.range(0, checkers.getBoard().length)
                .filter(i -> !checkers.isCellEmpty(i))
                .filter(moves::containsKey).forEach(i -> {
            checkers.Chip c = checkers.getChip(i);
            Chip chip = c.getPlayer() == 1 ?
                    boardUI.getBlackChip(i) : boardUI.getWhiteChip(i);

            if(c.getPlayer() == checkers.getCurrentPlayer()) {
                chip.setInteractive(true);
                chip.setOnMouseReleased(e -> liftChip(chip, moves.get(i)));
            }
        });
    }

    /**
     * Makes all chips non-interactive for when cells are being selected or
     * when the computer is making a move.
     */
    public void disengageMoveChips() {
        IntStream.range(0, checkers.getBoard().length)
                .filter(i -> !checkers.isCellEmpty(i)).forEach(i -> {
            checkers.Chip c = checkers.getChip(i);
            Chip chip = c.getPlayer() == 1 ?
                    boardUI.getBlackChip(i) :
                    boardUI.getWhiteChip(i);
            chip.setOnMouseReleased(null);
            chip.setInteractive(false);
        });
    }

    /**
     * Sets a chip to it's lifted state and invokes engage cells method for
     * when user has clicked on a chip.
     *
     * @param chip  The UI Chip that has been clicked.
     * @param moves The moves available to that chip.
     */
    public void liftChip(Chip chip, MoveCollection moves) {
        disengageMoveChips();
        chip.setLifted(true);
        chip.setInteractive(true);
        chip.setOnMouseReleased(e -> {
            dropChip(chip, moves);
            engageMoveChips();
        });
        engageMoveCells(chip, moves);
    }

    /**
     * Resets a chip to it's non-lifted state and invokes disengage cells
     * method for when user has chosen a cell or to move a different chip.
     *
     * @param chip  The UI Chip that has been clicked.
     * @param moves The moves available to that chip.
     */
    public void dropChip(Chip chip, MoveCollection moves) {
        chip.setLifted(false);
        chip.setInteractive(false);
        disengageMoveCells(moves);
    }

    /**
     * Drops the chip, moves the chip inside the internal game instance, and
     * moves on to the next round, updating the UI board state. Called when
     * the user has chosen a cell to move the chip on to.
     *
     * @param chip  The Chip being moved.
     * @param moves The moves available to that Chip.
     * @param move  The move chosen by the player.
     */
    public void placeChip(Chip chip, MoveCollection moves, Move move) {
        dropChip(chip, moves);
        checkers.moveChip(move);
        beginRound();
    }

    /**
     * Makes all of the cells that user can move a chip on to interactive so
     * they may click and choose a cell.
     *
     * @param chip  The Chip responsible for engaging the cells.
     * @param moves The moves available to that Chip.
     */
    public void engageMoveCells(Chip chip, MoveCollection moves) {
        for(Move move: moves) {
            Cell cell = boardUI.getCell(move.getDest());
            cell.setInteractive(true);
            cell.setOnMouseReleased(e -> placeChip(chip, moves, move));
        }
    }

    /**
     * Makes all cells non-interactive for when when user has chosen a cell
     * or to move a different chip.
     *
     * @param moves The moves to disengage cells for.
     */
    public void disengageMoveCells(MoveCollection moves) {
        for(Move move: moves) {
            Cell cell = boardUI.getCell(move.getDest());
            cell.setInteractive(false);
            cell.setOnMouseReleased(null);
        }
    }

    /**
     * Updates the UI board state to match the internal game instance board
     * state.
     */
    public void updateBoard() {
        checkers.Chip[] board = checkers.getBoard();
        for(int i=0; i<board.length;i++) {
            if(board[i] == null) {
                boardUI.getBlackChip(i).setVisible(false);
                boardUI.getWhiteChip(i).setVisible(false);
                boardUI.getCrown(i).setVisible(false);
            } else if(board[i].getPlayer() == 2) {
                boardUI.getWhiteChip(i).setVisible(true);
                boardUI.getCrown(i).setVisible(board[i].isKing());
            } else if(board[i].getPlayer() == 1) {
                boardUI.getBlackChip(i).setVisible(true);
                boardUI.getCrown(i).setVisible(board[i].isKing());
            }
        }
    }
}

package checkers.ui;

import checkers.*;
import checkers.ui.board.Board;
import checkers.ui.board.Cell;
import checkers.ui.board.Chip;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.stream.IntStream;

public class Game extends VBox {

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
     */
    public Game() {
        super();

        Menu m1 = new Menu("Game");
        MenuItem mi1 = new MenuItem("Reset");
        MenuItem mi2 = new MenuItem("Save");
        MenuItem mi3 = new MenuItem("Quit");
        Menu m2 = new Menu("Help");
        MenuItem mi4 = new MenuItem("Get A Hint");
        MenuItem mi5 = new MenuItem("How To Play");
        MenuBar mb = new MenuBar();
        boardUI = new Board(800);

        m1.getItems().addAll(mi1, mi2, new SeparatorMenuItem(), mi3);
        m2.getItems().addAll(mi4, mi5);
        mb.getMenus().addAll(m1, m2);
        this.getChildren().addAll(mb, boardUI);

        checkers = new Checkers();
        beginRound();
    }

    /**
     * Moves the game on to the next round.
     */
    public void beginRound() {
        updateBoard();
        engageMoveChips();
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

package checkers.ui.board;

import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Board extends GridPane {

    /**
     * Access to white chip instances.
     */
    private static Chip[] whiteChips;

    /**
     * Access to black chip instances.
     */
    private static Chip[] blackChips;

    /**
     * Access to habitable cell instances.
     */
    private static Cell[] cells;

    /**
     * Access to king crown instances.
     */
    private static Circle[] crowns;

    /**
     * The size of the outer pane container.
     */
    private final int v;

    /**
     * The size of the board layout.
     */
    private final int size;

    /**
     * Board UI element for checkers game.
     *
     * @param v The size of the outer pane container.
     * @param size  The size of the board layout.
     */
    public Board(int v, int size) {
        super();

        int chipCount = size * size / 2;
        blackChips = new Chip[chipCount];
        whiteChips = new Chip[chipCount];
        cells = new Cell[chipCount];
        crowns = new Circle[chipCount];

        this.v = v;
        this.size = size;
        this.setMinSize(v, v);

        this.reset();
    }

    /**
     * @return White chips array.
     */
    public static Chip[] getWhiteChips() {
        return whiteChips;
    }

    /**
     * @return Black chips array.
     */
    public static Chip[] getBlackChips() {
        return blackChips;
    }

    /**
     * @return Cells array.
     */
    public static Cell[] getCells() {
        return cells;
    }

    /**
     * @return King crowns array.
     */
    public static Circle[] getCrowns() {
        return crowns;
    }

    /**
     * @return Resolution of board.
     */
    public int getV() {
        return v;
    }

    /**
     * @return Size of board layout.
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns a white chip by board index.
     *
     * @param i Board index of chip.
     * @return  Chip instance.
     */
    public Chip getWhiteChip(int i) {
        return whiteChips[i];
    }

    /**
     * Returns a black chip by board index.
     *
     * @param i Board index of chip.
     * @return  Chip instance.
     */
    public Chip getBlackChip(int i) {
        return blackChips[i];
    }

    /**
     * Returns a cell by board index.
     *
     * @param i Board index of cell.
     * @return  Cell instance.
     */
    public Cell getCell(int i) {
        return cells[i];
    }

    /**
     * Returns a king crown by board index.
     *
     * @param i Board index of cell.
     * @return  Crown instance.
     */
    public Circle getCrown(int i) {
        return crowns[i];
    }

    /**
     * Resets the board state.
     */
    public void reset() {
        int cellIdx = 0;
        for(int col = 0; col < getSize(); col++) {
            for(int row = 0; row < getSize(); row++) {
                boolean isHabitableCell = (row + (col % 2)) % 2 == 1;
                Pane cell;
                double cellSize = (double)v/getSize();
                if(isHabitableCell) {
                    cell = new Cell();
                    cell.setBackground(new Background(new BackgroundFill(
                            Color.web("#a67d5d"),
                            CornerRadii.EMPTY,
                            Insets.EMPTY
                    )));
                    cell.setPrefSize(cellSize, cellSize);

                    double chipPos = (double)v/(getSize()*2);
                    double chipSize = (double)v/(getSize()*2.5);
                    Chip blackChip = new Chip(chipPos, chipPos, chipSize,
                            Color.web("#382723"));
                    Chip whiteChip = new Chip(chipPos, chipPos, chipSize,
                            Color.web("#e8d9b0"));

                    Circle crown = new Circle(chipPos, chipPos, chipSize);
                    crown.setStroke(Color.GOLD);
                    crown.setFill(null);
                    crown.setStrokeWidth(chipSize*0.15);
                    DropShadow ds = new DropShadow();
                    ds.setOffsetX(0);
                    ds.setOffsetY(0);
                    ds.setColor(Color.GOLD);
                    crown.setEffect(ds);

                    whiteChip.setVisible(false);
                    blackChip.setVisible(false);
                    crown.setVisible(false);

                    double fontSize = (double)v/(getSize()*8);
                    Text t = new Text(2, cellSize - 2,
                            String.valueOf(cellIdx+1));
                    t.setFont(new Font(fontSize));

                    cell.getChildren().addAll(crown, blackChip, whiteChip, t);

                    blackChips[cellIdx] = blackChip;
                    whiteChips[cellIdx] = whiteChip;
                    cells[cellIdx] = (Cell)cell;
                    crowns[cellIdx] = crown;

                    cellIdx += 1;
                } else {
                    cell = new Pane();
                    cell.setBackground(new Background(new BackgroundFill(
                            Color.web("#e8d0aa"),
                            CornerRadii.EMPTY,
                            Insets.EMPTY
                    )));
                    cell.setPrefSize(cellSize, cellSize);
                }

                this.add(cell, row, col);
            }
        }
    }
}

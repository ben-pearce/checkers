package checkers.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class Menu extends VBox {

    /**
     * Event handler fired when game is begun.
     */
    private EventHandler<MouseEvent> onGameOpen;

    /**
     * Game configuration object.
     */
    private GameConfig gameConfig;

    /**
     * Creates a simple form for configuring the Checkers game.
     */
    public Menu() {
        super();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 10, 10));

        class SizeComboBoxItem {
            private final int size;
            public SizeComboBoxItem(int resolution) {
                this.size = resolution;
            }
            public int getSize() {
                return size;
            }

            @Override
            public String toString() {
                return String.format("%dx%d", size, size);
            }
        }

        class DifficultyComboBoxItem {
            private final String label;
            private final int depth;
            public DifficultyComboBoxItem(String label, int depth) {
                this.label = label;
                this.depth = depth;
            }

            public String getLabel() {
                return label;
            }

            public int getDepth() {
                return depth;
            }

            @Override
            public String toString() {
                return label;
            }
        }

        ObservableList<String> gameModeOptions =
                FXCollections.observableArrayList(
                        "Player vs Player",
                        "Player vs Computer"
                );

        SizeComboBoxItem defaultBoardSize = new SizeComboBoxItem(10);
        ObservableList<SizeComboBoxItem> boardSizeOptions =
                FXCollections.observableArrayList(
                new SizeComboBoxItem(8),
                        defaultBoardSize,
                        new SizeComboBoxItem(16)
        );

        SizeComboBoxItem defaultResolution = new SizeComboBoxItem(800);
        ObservableList<SizeComboBoxItem> resolutionOptions =
                FXCollections.observableArrayList(
                        new SizeComboBoxItem(600),
                        defaultResolution,
                        new SizeComboBoxItem(1000)
        );

        Label modeLabel = new Label("Game Mode:");
        ComboBox<String> modes = new ComboBox<>(gameModeOptions);
        modes.setValue("Player vs Player");

        Label sizeLabel = new Label("Board Size:");
        ComboBox<SizeComboBoxItem> sizes = new ComboBox<>(boardSizeOptions);
        sizes.setValue(defaultBoardSize);

        Label resolutionLabel = new Label("Resolution: ");
        ComboBox<SizeComboBoxItem> resolutions =
                new ComboBox<>(resolutionOptions);
        resolutions.setValue(defaultResolution);

        Button startButton = new Button("Start️");
        GridPane.setHalignment(startButton, HPos.RIGHT);

        Pane computerOptions = new Pane();
        computerOptions.setBorder(new Border(new BorderStroke(
                Color.LIGHTGREY,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT
        )));
        GridPane.setColumnSpan(computerOptions, 2);

        GridPane computerGrid = new GridPane();
        computerGrid.setHgap(10);
        computerGrid.setVgap(10);
        computerGrid.setPadding(new Insets(0, 10, 0, 10));

        Label computerLabel = new Label("Computer");
        computerLabel.setLayoutX(10);
        computerLabel.setLayoutY(-8);
        computerLabel.setBackground(new Background(new BackgroundFill(
                Color.web("#f4f4f4"),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));

        DifficultyComboBoxItem defaultDifficulty =
                new DifficultyComboBoxItem("Medium", 6);
        ObservableList<DifficultyComboBoxItem> difficultyOptions =
                FXCollections.observableArrayList(
                        new DifficultyComboBoxItem("Easy", 3),
                        defaultDifficulty,
                        new DifficultyComboBoxItem("Hard", 8),
                        new DifficultyComboBoxItem("Extreme", 11)
                );
        Label difficulty = new Label("Difficulty: ");
        ComboBox<DifficultyComboBoxItem> difficulties =
                new ComboBox<>(difficultyOptions);
        difficulties.setValue(defaultDifficulty);

        Label startPlayer = new Label("Start:");
        CheckBox computerStarts = new CheckBox("Computer Starts");

        computerGrid.add(difficulty, 0, 1);
        computerGrid.add(difficulties, 1, 1);
        computerGrid.add(startPlayer, 0, 2);
        computerGrid.add(computerStarts, 1, 2);

        computerOptions.getChildren().addAll(computerLabel, computerGrid);

        grid.add(modeLabel, 0, 1);
        grid.add(modes, 1, 1);
        grid.add(sizeLabel, 0, 2);
        grid.add(sizes, 1, 2);
        grid.add(resolutionLabel, 0, 3);
        grid.add(resolutions, 1, 3);
        grid.add(computerOptions, 0, 4);
        grid.add(startButton, 1, 5);

        difficulties.setDisable(true);
        computerStarts.setDisable(true);
        modes.valueProperty().addListener((options, oldValue, newValue) -> {
            boolean isComputer = newValue.equals("Player vs Computer");
            difficulties.setDisable(!isComputer);
            computerStarts.setDisable(!isComputer);
        });

        startButton.setOnMouseReleased(e -> {
            boolean isComputer = modes.getValue().equals("Player vs Computer");
            int computerDifficulty = difficulties.getValue().getDepth();
            boolean computerStart = computerStarts.isSelected();
            int boardSize = sizes.getValue().getSize();
            int resolution = resolutions.getValue().getSize();
            this.gameConfig = new GameConfig(isComputer,
                    computerDifficulty, computerStart,
                    boardSize, resolution);
            onGameOpen.handle(e);
        });

        this.getChildren().addAll(grid);
    }

    /**
     * Sets an event which will be fired once the game has begun.
     *
     * @param onGameOpen    The EventHandler to invoke.
     */
    public void setOnGameOpen(EventHandler<MouseEvent> onGameOpen) {
        this.onGameOpen = onGameOpen;
    }

    /**
     * @return The game configuration produced by this menu.
     */
    public GameConfig getGameConfig() {
        return gameConfig;
    }
}

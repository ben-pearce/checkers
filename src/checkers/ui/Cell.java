package checkers.ui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Cell extends Pane {

    /**
     * Is cell in an interactive state.
     */
    private boolean interactive = false;

    /**
     * Event handler when mouse enters cell.
     */
    private final EventHandler<MouseEvent> entered;

    /**
     * Event handler when mouse exits cell.
     */
    private final EventHandler<MouseEvent> exited;

    /**
     * Cell UI element for checkers game.
     */
    public Cell() {
        super();

        entered = mouseEvent -> {
            this.setBackground(new Background(new BackgroundFill(
                    Color.web("#1db345"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )));
        };

        exited = mouseEvent -> {
            this.setBackground(new Background(new BackgroundFill(
                    Color.web("#1fd14f"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )));
        };
    }

    /**
     * @return Is cell in interactive state.
     */
    public boolean isInteractive() {
        return interactive;
    }

    /**
     * Set cell interactive state.
     *
     * @param interactive   Cell interactive state.
     */
    public void setInteractive(boolean interactive) {
        if(interactive) {
            this.setBackground(new Background(new BackgroundFill(
                    Color.web("#1fd14f"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )));

            this.setCursor(Cursor.HAND);
            this.addEventHandler(MouseEvent.MOUSE_ENTERED, entered);
            this.addEventHandler(MouseEvent.MOUSE_EXITED, exited);
        } else {
            this.setBackground(new Background(new BackgroundFill(
                    Color.web("#a67d5d"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )));

            this.setCursor(Cursor.DEFAULT);
            this.removeEventHandler(MouseEvent.MOUSE_ENTERED, entered);
            this.removeEventHandler(MouseEvent.MOUSE_EXITED, exited);
        }
        this.interactive = interactive;
    }
}

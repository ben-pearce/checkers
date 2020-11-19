package checkers.ui.board;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Chip extends Circle {

    /**
     * Is chip in an interactive state.
     */
    private boolean interactive = false;

    /**
     * Is chip in a lifted state.
     */
    private boolean lifted = false;

    /**
     * Event handler when mouse enters chip.
     */
    private final EventHandler<MouseEvent> entered;

    /**
     * Event handler when mouse exits chip.
     */
    private final EventHandler<MouseEvent> exited;


    /**
     * Chip UI element for checkers game.
     */
    public Chip(double v, double v1, double v2, Paint paint) {
        super(v, v1, v2, paint);

        entered = mouseEvent -> {
            this.setStroke(Color.BLACK);
            this.setStrokeWidth(5);
        };

        exited = mouseEvent -> {
            this.setStroke(null);
        };
    }

    /**
     * @return Is chip in interactive state.
     */
    public boolean isInteractive() {
        return interactive;
    }

    /**
     * @return Is chip in lifted state.
     */
    public boolean isLifted() {
        return lifted;
    }

    /**
     * Set chip lifted state.
     *
     * @param lifted    Chip lifted state.
     */
    public void setLifted(boolean lifted) {
        if(lifted) {
            DropShadow ds = new DropShadow();
            ds.setOffsetX(0);
            ds.setOffsetY(0);
            this.setEffect(ds);
        } else {
            this.setEffect(null);
        }
        this.lifted = lifted;
    }

    /**
     * Set chip interactive state.
     *
     * @param interactive    Chip interactive state.
     */
    public void setInteractive(boolean interactive) {
        if(interactive) {
            this.setCursor(Cursor.HAND);
            this.addEventHandler(MouseEvent.MOUSE_ENTERED, entered);
            this.addEventHandler(MouseEvent.MOUSE_EXITED, exited);
        } else {
            this.setCursor(Cursor.DEFAULT);
            this.removeEventHandler(MouseEvent.MOUSE_ENTERED, entered);
            this.removeEventHandler(MouseEvent.MOUSE_EXITED, exited);
        }
        this.interactive = interactive;
    }
}

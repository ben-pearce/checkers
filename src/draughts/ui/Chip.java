package draughts.ui;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Chip extends Circle {

    private final EventHandler<MouseEvent> pressed;
    private final EventHandler<MouseEvent> entered;
    private final EventHandler<MouseEvent> exited;


    public Chip(double v, double v1, double v2, Paint paint) {
        super(v, v1, v2, paint);
        pressed = mouseEvent -> {
            DropShadow ds = new DropShadow();
            ds.setOffsetX(0);
            ds.setOffsetY(0);
            this.setEffect(ds);
            this.setStroke(null);
        };

        entered = mouseEvent -> {
            this.setStroke(Color.BLACK);
            this.setStrokeWidth(5);
        };

        exited = mouseEvent -> {
            this.setStroke(null);
        };

    }

    public void setActive(boolean active) {
        if(active) {
            this.setCursor(Cursor.HAND);
            this.addEventHandler(MouseEvent.MOUSE_PRESSED, pressed);
            this.addEventHandler(MouseEvent.MOUSE_ENTERED, entered);
            this.addEventHandler(MouseEvent.MOUSE_EXITED, exited);
        } else {
            this.setCursor(Cursor.DEFAULT);
            this.setEffect(null);
            this.removeEventHandler(MouseEvent.MOUSE_PRESSED, pressed);
            this.removeEventHandler(MouseEvent.MOUSE_ENTERED, entered);
            this.removeEventHandler(MouseEvent.MOUSE_EXITED, exited);
        }
    }
}

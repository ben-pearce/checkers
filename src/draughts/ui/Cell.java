package draughts.ui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Cell extends Pane {

    public Cell() {
        super();
    }

    public void setActive(boolean active) {
        if(active) {
            EventHandler<MouseEvent> selected = mouseEvent -> {
                this.setActive(false);
            };

            this.setCursor(Cursor.HAND);
            this.setBackground(new Background(new BackgroundFill(
                    Color.web("#1fd14f"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )));
            this.addEventHandler(MouseEvent.MOUSE_PRESSED, selected);
        } else {
            this.setCursor(Cursor.DEFAULT);
            this.setBackground(new Background(new BackgroundFill(
                    Color.web("#a67d5d"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )));
        }
    }
}

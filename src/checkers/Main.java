package checkers;

import checkers.ui.Board;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        Menu m1 = new Menu("Game");
        MenuItem mi1 = new MenuItem("Reset");
        MenuItem mi2 = new MenuItem("Save");
        MenuItem mi3 = new MenuItem("Quit");
        m1.getItems().addAll(mi1, mi2, new SeparatorMenuItem(), mi3);

        Menu m2 = new Menu("Help");

        MenuBar mb = new MenuBar();
        mb.getMenus().addAll(m1, m2);

        Board gb = new Board(800);
        gb.getWhiteChip(0).setVisible(true);
        gb.getWhiteChip(0).setInteractive(true);
        gb.getCell(5).setInteractive(true);
        gb.getCell(6).setInteractive(true);

        VBox vb = new VBox(mb, gb);
        Scene scene = new Scene(vb);
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

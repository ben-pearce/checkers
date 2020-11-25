package checkers;

import checkers.ui.Game;
import checkers.ui.Menu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Menu m = new Menu();
        Scene menuScene = new Scene(m);

        primaryStage.setTitle("Checkers Setup");
        primaryStage.setScene(menuScene);
        primaryStage.setResizable(false);
        primaryStage.show();

        m.setOnGameOpen(e -> {
            primaryStage.hide();
            Game g = new Game(m.getGameConfig());
            g.setOnGameExit(e1 -> {
                primaryStage.hide();
                primaryStage.setTitle("Checkers Setup");
                primaryStage.setScene(menuScene);
                primaryStage.show();
            });

            Scene gameScene = new Scene(g);
            primaryStage.setTitle("Checkers");
            primaryStage.setScene(gameScene);
            primaryStage.show();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

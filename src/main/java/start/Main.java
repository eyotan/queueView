package start;

import controllers.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    static Controller c1 = new Controller();

    @Override
    public void start(Stage primaryStage) throws Exception {
        c1.start(primaryStage);
    }

    public static void main(String[] args) throws InterruptedException {
        new properties.Props();
        launch(args);
    }
}

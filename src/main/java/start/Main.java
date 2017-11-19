package start;

import controllers.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class Main extends Application {
    static Controller c1 = new Controller();

    @Override
    public void start(Stage primaryStage) throws Exception {
        c1.start(primaryStage);
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            http.Http.url = args[0];
        }catch (IndexOutOfBoundsException ex){
            Logger.getLogger(Main.class.getName()).warning("NO URL IN FIRST ARGUMENT "+ex);
        }
        launch(args);
    }
}

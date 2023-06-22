package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Initiates the application
 */
public class Main extends Application {

    /**
     * Initializes the loginScreen.fxml for the opening of the appication
     * @param stage Represents the primary stage of the application where the user interface will be displayed.
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../views/loginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 325, 382);
        stage.setTitle("C195 PA - Tyler Harris");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads the database connection
     * @param args Represents the command-line arguments passed to the main method.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        JDBC.startConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
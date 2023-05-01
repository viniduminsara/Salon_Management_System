import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Appinitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/loginForm.fxml"))));
        primaryStage.centerOnScreen();
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Login");
        primaryStage.getIcons().add(new Image("F:\\1st semester final project\\moods salon\\src\\main\\resources\\img\\logo.png"));
        primaryStage.show();
    }
}

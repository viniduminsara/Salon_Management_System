package lk.ijse.moods_salon.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PreLoaderFormController implements Initializable {

    @FXML
    private ProgressBar progress;

    @FXML
    private Label label;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i <= 10; i++) {
                    updateProgress(i, 100);
                    Thread.sleep(100);
                }
                Platform.runLater(() -> label.setText("Loading modules..."));
                updateProgress(10, 100);
                Thread.sleep(500);
                for (int i = 21; i <= 90; i++) {
                    updateProgress(i, 100);
                    Thread.sleep(20);
                }
                Platform.runLater(() -> label.setText("Opening Login Page..."));
                updateProgress(90, 100);
                Thread.sleep(500);
                for (int i = 91; i <= 100; i++) {
                    updateProgress(i, 100);
                    Thread.sleep(30);
                }
                return null;
            }
        };

        progress.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(event -> {
            try {
                Stage stage1= (Stage) progress.getScene().getWindow();
                stage1.close();
                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/loginForm.fxml"))));
                stage.setTitle("Login");
                stage.getIcons().add(new Image("img/logo.png"));
                stage.setMaximized(true);
                stage.centerOnScreen();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}

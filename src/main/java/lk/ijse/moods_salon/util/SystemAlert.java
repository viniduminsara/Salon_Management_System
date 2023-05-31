package lk.ijse.moods_salon.util;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.File;

public class SystemAlert extends Alert {

    public SystemAlert(AlertType alertType, String title, String message, ButtonType... buttonTypes) {
        super(alertType, message,buttonTypes);
        setTitle(title);

        String image = null;
        switch (alertType){
            case ERROR:
                image = "src/main/resources/img/alert/error.gif";
                break;
            case INFORMATION:
                image = "src/main/resources/img/alert/info.gif";
                break;
            case WARNING:
                image = "src/main/resources/img/alert/warning.gif";
                break;
            case CONFIRMATION:
                image = "src/main/resources/img/alert/confirmation.gif";
                break;
            case NONE:
                image = "src/main/resources/img/alert/connection_error.gif";
                break;
        }

        if (image != null){
            if (title.equals("Email")){
                image = "src/main/resources/img/alert/send_mail.gif";
            }else if(title.equals("Logout")){
                image = "src/main/resources/img/alert/logout.gif";
            }
            ImageView imageView = new ImageView(new Image(new File(image).toURI().toString()));
            imageView.setFitWidth(170);
            imageView.setFitHeight(130);

            StackPane graphicPane = new StackPane(imageView);
            graphicPane.setAlignment(Pos.CENTER);
            graphicPane.getStyleClass().add("stackpane");
            getDialogPane().setHeader(graphicPane);
        }

        centerButtons(getDialogPane());

        getDialogPane().getStylesheets().add(getClass().getResource("/style/alert.css").toExternalForm());
        getDialogPane().getStyleClass().add("dialog-pane");
    }

    private void centerButtons(DialogPane dialogPane) {
        Region spacer = new Region();
        ButtonBar.setButtonData(spacer, ButtonBar.ButtonData.BIG_GAP);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        dialogPane.applyCss();
        HBox hboxDialogPane = (HBox) dialogPane.lookup(".container");
        hboxDialogPane.getChildren().add(spacer);
    }

}

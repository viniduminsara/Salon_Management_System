package lk.ijse.gdse.controller;

import com.github.sarxos.webcam.Webcam;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse.qr.WebCamView;
import lk.ijse.gdse.qr.WebcamService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class QRScanFormController implements Initializable{
    @FXML
    private AnchorPane pane;

    @FXML
    private ImageView imageView;

    @FXML
    private ProgressBar progress;

    @FXML
    private Label lblEmployee;

    @FXML
    private JFXButton btnStart;

    @FXML
    private JFXButton btnStop;

    private EmployeeFormController employeeController;

    private WebcamService service;

    @FXML
    void startBtnOnAction(ActionEvent event) {
        service.restart();
        progress.setVisible(true);
        btnStart.setDisable(true);
    }

    @FXML
    void stopBtnOnAction(ActionEvent event) {
        service.cancel();
        progress.setVisible(false);
        btnStart.setDisable(false);
        setGif();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setGif();
        progress.setVisible(false);
        Webcam cam = Webcam.getWebcams().get(0);
        service = new WebcamService(cam);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebCamView view = new WebCamView(service,imageView);
        pane.getChildren().add(view.getView());
        progress.progressProperty().bind(service.progressProperty());

        service.messageProperty().addListener((a,old,c) -> {
            if(c!=null){
                if(old==null){
                    System.out.println(c);
                }else
                if(!old.equals(c)) {
                    lblEmployee.setText(employeeController.findEmployee(c));
                    employeeController.markAttendence(c);
                }
            }
        });
    }

    private void setGif() {
        Image image = new Image(new File("F:\\1st semester final project\\moods salon\\src\\main\\resources\\img\\loading-75.gif").toURI().toString());
        imageView.setImage(image);
    }

    public WebcamService getService() {
        return service;
    }

    public void setEmployeeController(EmployeeFormController employeeController) {
        this.employeeController = employeeController;
    }
}

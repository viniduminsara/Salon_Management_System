package lk.ijse.gdse.controller;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lk.ijse.gdse.dto.User;
import lk.ijse.gdse.util.SystemAlert;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CashierDashboardFormController implements Initializable {

    @FXML
    private AnchorPane mainPane;

    @FXML
    private JFXButton dashboardbtn;

    @FXML
    private JFXButton customerbtn;

    @FXML
    private JFXButton appointmentbtn;

    @FXML
    private JFXButton paymentbtn;

    @FXML
    private AnchorPane pane;

    @FXML
    private Label lblName;

    @FXML
    private ImageView imageView;

    @FXML
    private Rectangle rectngle;

    private User user;

    @FXML
    void appointmentbtnOnAction(ActionEvent event) throws IOException {
        setStyles(appointmentbtn,dashboardbtn,customerbtn,paymentbtn);
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/appointmentForm.fxml")));
    }

    @FXML
    void customerbtnOnAction(ActionEvent event) throws IOException {
        setStyles(customerbtn,dashboardbtn,appointmentbtn,paymentbtn);
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/customerForm.fxml")));
    }

    @FXML
    void dashboardbtnOnAction(ActionEvent event) {
        setStyles(dashboardbtn,customerbtn,appointmentbtn,paymentbtn);
        loadHomePage();
    }

    @FXML
    void paymentBtnOnAction(ActionEvent event) throws IOException {
        setStyles(paymentbtn,dashboardbtn,customerbtn,appointmentbtn);
        pane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/paymentForm.fxml"));
        pane.getChildren().add(loader.load());
        PaymentFormController controller = loader.getController();
        controller.setUser(user);
        controller.getFilePath();
    }

    @FXML
    void logoutBtnOnAction(ActionEvent event) throws IOException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No",ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new SystemAlert(Alert.AlertType.INFORMATION,"Logout","Do you want to logout?",yes,no).showAndWait();
        if (result.orElse(no) == yes) {
            mainPane.getChildren().clear();
            mainPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/loginForm.fxml")));
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.setTitle("Login");
        }
    }

    @FXML
    void profileOnAction(MouseEvent event) throws IOException {
        removeAllStyles();
        pane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/profileForm.fxml"));
        pane.getChildren().add(loader.load());
        ProfileFormController controller = loader.getController();
        controller.setUser(user);
        controller.setDetails();
        controller.setCashierDashboardFormController(this);
    }

    private void removeAllStyles() {
        rectngle.setOpacity(1.0);
        dashboardbtn.getStyleClass().remove("clickedbtn");
        customerbtn.getStyleClass().remove("clickedbtn");
        paymentbtn.getStyleClass().remove("clickedbtn");
        appointmentbtn.getStyleClass().remove("clickedbtn");
    }

    private void setStyles(JFXButton clickedbtn, JFXButton btn1, JFXButton btn2, JFXButton btn3) {
        btn1.getStyleClass().remove("clickedbtn");
        btn2.getStyleClass().remove("clickedbtn");
        btn3.getStyleClass().remove("clickedbtn");
        clickedbtn.getStyleClass().remove("clickedbtn");
        clickedbtn.getStyleClass().add("clickedbtn");
        rectngle.setOpacity(0.0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadHomePage();
        setImageView();
    }

    private void setImageView() {
        double cornerRadius = 20.0; // Set the desired corner radius
        Rectangle clip = new Rectangle(
                imageView.getFitWidth(), imageView.getFitHeight());
        clip.setArcWidth(cornerRadius);
        clip.setArcHeight(cornerRadius);
        imageView.setClip(clip);
    }

    private void loadHomePage() {
        pane.getChildren().clear();
        try {
            pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/homeForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDetails() {
        lblName.setText(user.getFullName());
        if (user.getInputStream() != null) {
            InputStream inputStream = user.getInputStream();
            imageView.setImage(new Image(inputStream, 58, 57, false, true));
        }else {
            imageView.setImage(new Image("img/images.png"));
        }
    }

    public void setUser(User user){
        this.user = user;
    }
}

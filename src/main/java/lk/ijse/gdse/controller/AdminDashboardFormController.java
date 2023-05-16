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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lk.ijse.gdse.dto.User;
import lk.ijse.gdse.model.UserModel;
import lk.ijse.gdse.util.SystemAlert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;


public class AdminDashboardFormController implements Initializable {

    @FXML
    private AnchorPane mainPane;

    @FXML
    private AnchorPane pane;

    @FXML
    private JFXButton dashboardbtn;

    @FXML
    private JFXButton customerbtn;

    @FXML
    private JFXButton appointmnetbtn;

    @FXML
    private JFXButton inventorybtn;

    @FXML
    private JFXButton employeebtn;

    @FXML
    private JFXButton servicebtn;

    @FXML
    private JFXButton supplierbtn;

    @FXML
    private JFXButton paymentbtn;

    @FXML
    private Label lblName;

    @FXML
    private ImageView imageView;

    @FXML
    private Rectangle rectangle;

    private User user;


    @FXML
    void appointmentbtnOnAction(ActionEvent event) throws IOException {
        setStyles(appointmnetbtn,dashboardbtn,employeebtn,inventorybtn,servicebtn,supplierbtn,customerbtn,paymentbtn);
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/appointmentForm.fxml")));
    }

    @FXML
    void customerbtnOnAction(ActionEvent event) throws IOException {
        setStyles(customerbtn,dashboardbtn,appointmnetbtn,inventorybtn,employeebtn,servicebtn,supplierbtn,paymentbtn);
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/customerForm.fxml")));
    }

    @FXML
    void dashboardbtnOnAction(ActionEvent event) throws IOException {
        setStyles(dashboardbtn,customerbtn,appointmnetbtn,inventorybtn,employeebtn,servicebtn,supplierbtn,paymentbtn);
        loadMainPage();
    }

    @FXML
    void paymentBtnOnAction(ActionEvent event) throws IOException {
        setStyles(paymentbtn,dashboardbtn,appointmnetbtn,inventorybtn,employeebtn,servicebtn,supplierbtn,customerbtn);
        pane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/paymentForm.fxml"));
        pane.getChildren().add(loader.load());
        PaymentFormController controller = loader.getController();
        controller.setUser(user);
    }

    @FXML
    void employeeOnAction(ActionEvent event) throws IOException {
        setStyles(employeebtn,dashboardbtn,appointmnetbtn,inventorybtn,customerbtn,servicebtn,supplierbtn,paymentbtn);
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/employeeForm.fxml")));
    }

    @FXML
    void inventoryOnAction(ActionEvent event) throws IOException {
        setStyles(inventorybtn,dashboardbtn,appointmnetbtn,customerbtn,employeebtn,servicebtn,supplierbtn,paymentbtn);
        pane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/inventoryForm.fxml"));
        pane.getChildren().add(loader.load());
        InventoryFormController controller = loader.getController();
        controller.setUser(user);
    }

    @FXML
    void servicebtnOnAction(ActionEvent event) throws IOException {
        setStyles(servicebtn,dashboardbtn,appointmnetbtn,inventorybtn,employeebtn,customerbtn,supplierbtn,paymentbtn);
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/serviceForm.fxml")));
    }

    @FXML
    void supplierbtnOnAction(ActionEvent event) throws IOException {
        setStyles(supplierbtn,dashboardbtn,appointmnetbtn,inventorybtn,employeebtn,servicebtn,customerbtn,paymentbtn);
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/supplierForm.fxml")));
    }

    @FXML
    void logoutBtnOnAction(ActionEvent event) throws IOException {
        ButtonType yes = new ButtonType("Yes",ButtonBar.ButtonData.OK_DONE);
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
        rectangle.setOpacity(1.0);
        pane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/profileForm.fxml"));
        pane.getChildren().add(loader.load());
        ProfileFormController controller = loader.getController();
        controller.setUser(user);
        controller.setDetails();
        controller.setAdminDashboardFormController(this);
    }

    private void removeAllStyles() {
        customerbtn.getStyleClass().remove("clickedbtn");
        appointmnetbtn.getStyleClass().remove("clickedbtn");
        paymentbtn.getStyleClass().remove("clickedbtn");
        inventorybtn.getStyleClass().remove("clickedbtn");
        employeebtn.getStyleClass().remove("clickedbtn");
        supplierbtn.getStyleClass().remove("clickedbtn");
        servicebtn.getStyleClass().remove("clickedbtn");
        dashboardbtn.getStyleClass().remove("clickedbtn");
    }

    public void setStyles(JFXButton clicked,JFXButton btn1, JFXButton btn2, JFXButton btn3, JFXButton btn4, JFXButton btn5, JFXButton btn6, JFXButton btn7) {
        clicked.getStyleClass().remove("clickedbtn");
        btn1.getStyleClass().remove("clickedbtn");
        btn2.getStyleClass().remove("clickedbtn");
        btn3.getStyleClass().remove("clickedbtn");
        btn4.getStyleClass().remove("clickedbtn");
        btn5.getStyleClass().remove("clickedbtn");
        btn6.getStyleClass().remove("clickedbtn");
        btn7.getStyleClass().remove("clickedbtn");
        clicked.getStyleClass().add("clickedbtn");
        rectangle.setOpacity(0.0);
    }

    void loadMainPage(){

        try {
            pane.getChildren().clear();
            pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/homeForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadMainPage();
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

    public void setUserDetails() {
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

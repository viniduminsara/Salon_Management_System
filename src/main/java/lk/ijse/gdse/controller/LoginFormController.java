package lk.ijse.gdse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lk.ijse.gdse.dto.User;
import lk.ijse.gdse.model.UserModel;
import lk.ijse.gdse.util.TxtColours;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {
    @FXML
    private AnchorPane pane;

    @FXML
    private JFXTextField txtusername;

    @FXML
    private JFXPasswordField txtpassword;

    @FXML
    private Label lblcreateAccount;

    @FXML
    private JFXButton loginbtn;

    @FXML
    private JFXTextField txtVisiblePassword;

    @FXML
    private JFXCheckBox checkBox;

    @FXML
    private Label lblError;

    @FXML
    void checkBoxOnAction(ActionEvent event) {
        if (checkBox.isSelected()){
            txtVisiblePassword.setVisible(true);
            txtpassword.setVisible(false);
        }else {
            txtpassword.setVisible(true);
            txtVisiblePassword.setVisible(false);
        }
    }

    @FXML
    void txtpasswordOnAction(ActionEvent event) throws IOException {
        loginbtnOnAction(event);
    }

    @FXML
    void txtusernameOnAction(ActionEvent event) {
        txtpassword.requestFocus();
    }

    public void createlblMouseClicked(MouseEvent mouseEvent) throws IOException {
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/createAccountForm.fxml")));
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setTitle("Create Account");
    }

    public void loginbtnOnAction(ActionEvent actionEvent) throws IOException {
        if (!txtusername.getText().isEmpty()) {
            txtusername.setFocusColor(Paint.valueOf("#cf5bd2"));
            txtusername.setUnFocusColor(Paint.valueOf("#cf5bd2"));
            if (!(txtpassword.getText().isEmpty() || txtVisiblePassword.getText().isEmpty())) {
                txtpassword.setFocusColor(Paint.valueOf("#cf5bd2"));
                txtpassword.setUnFocusColor(Paint.valueOf("#cf5bd2"));
                txtVisiblePassword.setFocusColor(Paint.valueOf("#cf5bd2"));
                txtVisiblePassword.setUnFocusColor(Paint.valueOf("#cf5bd2"));

                String userName = txtusername.getText();
                String password = txtpassword.getText();

                try {
                    User user = UserModel.findUser(userName, password);
                    if (user != null) {
                        if (user.getType().equals("Admin")) {
                            pane.getChildren().clear();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard/adminDashboardForm.fxml"));
                            pane.getChildren().add(loader.load());
                            AdminDashboardFormController controller = loader.getController();
                            controller.setUser(user);
                            controller.setUserDetails();
                            Stage stage = (Stage) pane.getScene().getWindow();
                            stage.setTitle("Moods Salon");
                        }
                        if (user.getType().equals("Cashier")) {
                            pane.getChildren().clear();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard/cashierDashboardForm.fxml"));
                            pane.getChildren().add(loader.load());
                            CashierDashboardFormController controller = loader.getController();
                            controller.setUser(user);
                            controller.setDetails();
                            Stage stage = (Stage) pane.getScene().getWindow();
                            stage.setTitle("Moods Salon");
                        }
                    } else {
                        TxtColours.setErrorColours(txtusername);
                        TxtColours.setErrorColours(txtpassword);
                        TxtColours.setErrorColours(txtVisiblePassword);
                        lblError.setText("Your username or password is incorrect!");
                        lblError.setVisible(true);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
            }else {
                TxtColours.setErrorColours(txtpassword);
                TxtColours.setErrorColours(txtVisiblePassword);
                lblError.setText("Please enter your password");
                lblError.setVisible(true);
            }
        }else {
            TxtColours.setErrorColours(txtusername);
            lblError.setText("Please enter your username");
            lblError.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtVisiblePassword.textProperty().bindBidirectional(txtpassword.textProperty());
    }
}

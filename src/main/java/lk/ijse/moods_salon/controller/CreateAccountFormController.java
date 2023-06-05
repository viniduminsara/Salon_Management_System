package lk.ijse.moods_salon.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.moods_salon.bo.custom.CreateAccountBO;
import lk.ijse.moods_salon.bo.custom.impl.CreateAccountBOImpl;
import lk.ijse.moods_salon.dto.UserDTO;
import lk.ijse.moods_salon.util.RegExPatterns;
import lk.ijse.moods_salon.util.SystemAlert;
import lk.ijse.moods_salon.util.TxtColours;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateAccountFormController implements Initializable {

    @FXML
    private AnchorPane pane;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private JFXTextField txtGmail;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXTextField txtPasswordConfirm;

    @FXML
    private JFXComboBox<String> cmbAccountType;

    @FXML
    private JFXButton signupbtn;

    @FXML
    private Group loginbtngrp;

    CreateAccountBO createAccountBO = new CreateAccountBOImpl();

    public void logingrpMouseClicked(MouseEvent mouseEvent) throws IOException {
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/loginForm.fxml")));
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setTitle("Login");
    }

    public void signupbtnOnAction(ActionEvent actionEvent) throws IOException {
        if (!(txtFullName.getText().isEmpty() || txtUserName.getText().isEmpty() || txtGmail.getText().isEmpty() || txtPassword.getText().isEmpty() || txtPasswordConfirm.getText().isEmpty() || cmbAccountType.getSelectionModel().getSelectedItem() == null)) {
            if (RegExPatterns.getPasswordPattern().matcher(txtPassword.getText()).matches()) {
                TxtColours.setDefaultColours(txtPassword);
                if (txtPassword.getText().equals(txtPasswordConfirm.getText())) {
                    TxtColours.setDefaultColours(txtPasswordConfirm);
                    if (RegExPatterns.getEmailPattern().matcher(txtGmail.getText()).matches()) {
                        TxtColours.setDefaultColours(txtGmail);

                        String userId = null;
                        try {
                            userId = createAccountBO.generateUserId();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        String fullName = txtFullName.getText();
                        String userName = txtUserName.getText();
                        String type = cmbAccountType.getSelectionModel().getSelectedItem();
                        String gmail = txtGmail.getText();
                        String password = txtPassword.getText();

                        try {
                            boolean isSaved = createAccountBO.addUser(new UserDTO(userId, fullName, userName, type, gmail, password,null,null));
                            if (isSaved) {
                                new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Sign up successful!",ButtonType.OK).show();
                                pane.getChildren().clear();
                                pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/loginForm.fxml")));
                                Stage stage = (Stage) pane.getScene().getWindow();
                                stage.setTitle("Login");
                            } else {
                                new SystemAlert(Alert.AlertType.WARNING,"Warning","Sign up unsuccessful!", ButtonType.OK).show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
                        }
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Invalid gmail.").show();
                        TxtColours.setErrorColours(txtGmail);
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Password is not matching").show();
                    TxtColours.setErrorColours(txtPasswordConfirm);
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"Password should have 8 characters with letters and numbers.").show();
                TxtColours.setErrorColours(txtPassword);
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Please fill all the details.").show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setComboBox();
    }

    private void setComboBox() {
        String[] types = {"Admin", "Cashier"};
        cmbAccountType.setItems(FXCollections.observableArrayList(types));
    }
}

package lk.ijse.gdse.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import lk.ijse.gdse.dto.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import lk.ijse.gdse.model.UserModel;
import lk.ijse.gdse.util.RegExPatterns;
import lk.ijse.gdse.util.SystemAlert;
import lk.ijse.gdse.util.TxtColours;

import java.io.*;
import java.sql.SQLException;

public class ProfileFormController {

    @FXML
    private JFXButton btnImageChooser;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private JFXTextField txtGmail;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnChange;

    @FXML
    private Rectangle rectangle;

    @FXML
    private Label lblAccount;

    @FXML
    private JFXTextField txtCurrentPassword;

    @FXML
    private JFXTextField txtNewPassword;

    @FXML
    private JFXTextField txtPasswordConfirm;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private ImageView imageView;

    @FXML
    private Label lblSubmitError;

    @FXML
    private Label lblUpdateError;

    @FXML
    private JFXButton btnCancel;

    public User user;

    private File file;

    private AdminDashboardFormController adminDashboardFormController;

    private CashierDashboardFormController cashierDashboardFormController;


    @FXML
    void btnChangeOnAction(ActionEvent event) {
        btnSave.setVisible(true);
        btnCancel.setVisible(true);
        txtFullName.setEditable(true);
        txtUserName.setEditable(true);
        txtGmail.setEditable(true);
        btnImageChooser.setDisable(false);
        btnChange.setVisible(false);
        txtFullName.requestFocus();
    }

    @FXML
    void btnImageChooserOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the image");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(imageFilter);
        file = fileChooser.showOpenDialog(btnImageChooser.getScene().getWindow());
        if (file != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                imageView.setImage(new Image(fileInputStream,179,171,false,true));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!(txtFullName.getText().isEmpty() || txtUserName.getText().isEmpty() || txtGmail.getText().isEmpty())){
            if (RegExPatterns.getNamePattern().matcher(txtFullName.getText()).matches()){
                TxtColours.setDefaultColours(txtFullName);
                if (RegExPatterns.getEmailPattern().matcher(txtGmail.getText()).matches()){
                    TxtColours.setDefaultColours(txtGmail);

                    String id = user.getUserId();
                    String fullName = txtFullName.getText();
                    String username = txtUserName.getText();
                    String type = user.getType();
                    String gmail = txtGmail.getText();
                    String password = user.getPassword();

                    if (file != null){
                        try {
                            InputStream inputStream = new FileInputStream(file);
                            boolean isUpdated = UserModel.updateUser(new User(id,fullName,username,type,gmail,password,inputStream));
                            if (isUpdated){
                                new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","User details updated!", ButtonType.OK).show();
                                refreshUser();
                                setDetails();
                                setTextFields();
                            }else {
                                new SystemAlert(Alert.AlertType.WARNING,"Warning","User details not updated!",ButtonType.OK).show();
                            }
                        } catch (SQLException | FileNotFoundException e) {
                            e.printStackTrace();
                            new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong",ButtonType.OK).show();
                        }
                    }else {
                        try {
                            boolean isUpdated = UserModel.updateWithoutImage(new User(id,fullName,username,type,gmail,password,null));
                            if (isUpdated){
                                new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","User details updated!", ButtonType.OK).show();
                                refreshUser();
                                setDetails();
                                setTextFields();
                            }else {
                                new SystemAlert(Alert.AlertType.WARNING,"Warning","User details not updated!",ButtonType.OK).show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong",ButtonType.OK).show();
                        }
                    }
                }else {
                    new Alert(Alert.AlertType.WARNING,"Invalid gmail!").show();
                    TxtColours.setErrorColours(txtGmail);
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"Invalid name!").show();
                TxtColours.setErrorColours(txtFullName);
            }
        }else {
            new SystemAlert(Alert.AlertType.WARNING,"Warning","Please fill all fields!",ButtonType.OK);
        }
    }

    @FXML
    void btnSubmitOnAction(ActionEvent event) {
        if (!txtCurrentPassword.getText().isEmpty()) {
            boolean isCorrect = txtCurrentPassword.getText().equals(user.getPassword());
            TxtColours.setDefaultColours(txtCurrentPassword);

            if (isCorrect) {
                txtNewPassword.setDisable(false);
                txtPasswordConfirm.setDisable(false);
                btnUpdate.setDisable(false);
                txtCurrentPassword.setEditable(false);
                lblSubmitError.setText("");
            } else {
                TxtColours.setErrorColours(txtCurrentPassword);
                lblSubmitError.setText("Password is incorrect!");
            }
        }else {
            TxtColours.setErrorColours(txtCurrentPassword);
            lblSubmitError.setText("Please enter the password");
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!txtNewPassword.getText().isEmpty()){
            TxtColours.setDefaultColours(txtNewPassword);
            if (!txtPasswordConfirm.getText().isEmpty()){
                TxtColours.setDefaultColours(txtPasswordConfirm);
                if (RegExPatterns.getPasswordPattern().matcher(txtNewPassword.getText()).matches()){
                    TxtColours.setDefaultColours(txtNewPassword);
                    if (txtNewPassword.getText().equals(txtPasswordConfirm.getText())){
                        TxtColours.setDefaultColours(txtNewPassword);
                        TxtColours.setDefaultColours(txtPasswordConfirm);

                        String id = user.getUserId();
                        String newPassword = txtNewPassword.getText();
                        try {
                            boolean isUpdated = UserModel.updatePassword(id,newPassword);
                            if (isUpdated){
                                new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Password is updated!",ButtonType.OK).show();
                                refreshUser();
                                clearUpdate();
                            }else {
                                new SystemAlert(Alert.AlertType.WARNING,"Warning","Password not updated",ButtonType.OK).show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong",ButtonType.OK).show();
                        }
                    }else {
                        TxtColours.setErrorColours(txtNewPassword);
                        TxtColours.setErrorColours(txtPasswordConfirm);
                        lblUpdateError.setText("Passwords are not matching");
                    }
                }else {
                    TxtColours.setErrorColours(txtNewPassword);
                    lblUpdateError.setText("Password should have least 8 characters");
                }
            }else {
                TxtColours.setErrorColours(txtPasswordConfirm);
                lblUpdateError.setText("Please fill password confirm.");
            }
        }else {
            TxtColours.setErrorColours(txtNewPassword);
            lblUpdateError.setText("Please enter new password.");
        }
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        setDetails();
        setTextFields();
    }

    private void clearUpdate() {
        lblUpdateError.setText("");
        txtNewPassword.setText("");
        txtPasswordConfirm.setText("");
        txtNewPassword.setDisable(true);
        txtPasswordConfirm.setDisable(true);
        btnUpdate.setDisable(true);
        txtCurrentPassword.setEditable(true);
        txtCurrentPassword.setText("");
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAdminDashboardFormController(AdminDashboardFormController adminDashboardFormController) {
        this.adminDashboardFormController = adminDashboardFormController;
    }

    public void setCashierDashboardFormController(CashierDashboardFormController cashierDashboardFormController) {
        this.cashierDashboardFormController = cashierDashboardFormController;
    }

    private void setTextFields() {
        btnSave.setVisible(false);
        btnCancel.setVisible(false);
        txtFullName.setEditable(false);
        txtUserName.setEditable(false);
        txtGmail.setEditable(false);
        btnImageChooser.setDisable(true);
        btnChange.setVisible(true);
    }

    private void refreshUser() {
        try {
            this.user = UserModel.getUser(user.getUserId());
            if (adminDashboardFormController != null) {
                adminDashboardFormController.setUser(user);
                adminDashboardFormController.setUserDetails();
            }else {
                cashierDashboardFormController.setUser(user);
                cashierDashboardFormController.setDetails();
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setDetails() {
        txtFullName.setText(user.getFullName());
        txtUserName.setText(user.getUserName());
        txtGmail.setText(user.getGmail());
        try {
            InputStream inputStream = UserModel.getImage(user.getUserId());
            if (inputStream != null){
                imageView.setImage(new Image(inputStream,179,171,false,true));
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
        if (user.getType().equals("Admin")) {
            rectangle.setFill(Paint.valueOf("#ee4266"));
            lblAccount.setText("Admin");
        }else {
            rectangle.setFill(Paint.valueOf("#540d6e"));
            lblAccount.setText("Cashier");
        }
    }
}

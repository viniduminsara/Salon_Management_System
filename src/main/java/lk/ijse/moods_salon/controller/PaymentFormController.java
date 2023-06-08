package lk.ijse.moods_salon.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import lk.ijse.moods_salon.bo.custom.PaymentBO;
import lk.ijse.moods_salon.bo.factory.BOFactory;
import lk.ijse.moods_salon.bo.factory.BOTypes;
import lk.ijse.moods_salon.db.DBConnection;
import lk.ijse.moods_salon.dto.PaymentDTO;
import lk.ijse.moods_salon.dto.UserDTO;
import lk.ijse.moods_salon.dto.tm.PaymentTM;
import lk.ijse.moods_salon.mail.Mail;
import lk.ijse.moods_salon.util.RegExPatterns;
import lk.ijse.moods_salon.util.SystemAlert;
import lk.ijse.moods_salon.util.TxtColours;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PaymentFormController implements Initializable {
    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXTextField txtAmount;

    @FXML
    private JFXComboBox<String> cmbAppointmentId;

    @FXML
    private Label lblCustomerName;

    @FXML
    private TableView<PaymentTM> tblPayment;

    @FXML
    private TableColumn<?, ?> colPaymentId;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colUser;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private Label lblPaymentId;

    @FXML
    private JFXButton searchCancelBtn;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private JFXButton btnChange;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXTextField txtFilePath;

    ObservableList<PaymentTM> data = FXCollections.observableArrayList();

    PaymentBO paymentBO = (PaymentBO) BOFactory.getBoFactory().getBO(BOTypes.PAYMENT);

    private UserDTO user;

    private String filePath;

    @FXML
    void paymentBtnOnAction(ActionEvent event) {
        if (!(cmbAppointmentId.getSelectionModel().getSelectedItem() == null || txtAmount.getText().isEmpty() || lblCustomerName.getText().isEmpty())) {
            if (RegExPatterns.getDoublePattern().matcher(txtAmount.getText()).matches()) {
                TxtColours.setDefaultColours(txtAmount);
                if (filePath != null) {

                    String paymentId = lblPaymentId.getText();
                    double amount = Double.parseDouble(txtAmount.getText());
                    LocalDate date = LocalDate.now();
                    String userId = user.getUserId();
                    String appointmentId = cmbAppointmentId.getSelectionModel().getSelectedItem();

                    try {
                        boolean isPaymentPlaced = paymentBO.placePayment(new PaymentDTO(paymentId, amount, date, userId, appointmentId));
                        if (isPaymentPlaced) {
                            generateReceipt(paymentId, appointmentId);
                            new SystemAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Payment has placed!", ButtonType.OK).show();
                            populatePaymentTable();
                            searchFilter();
                            clearComponents();
                            generatePaymentId();
                            getAppointments();
                        } else {
                            new SystemAlert(Alert.AlertType.WARNING, "Warning", "Payment has not placed!", ButtonType.OK).show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        new SystemAlert(Alert.AlertType.ERROR, "Error", "Something went wrong!", ButtonType.OK).show();
                    }
                }else {
                    new SystemAlert(Alert.AlertType.WARNING,"Warning","Please select a folder.",ButtonType.OK).show();
                    TxtColours.setErrorColours(txtFilePath);
                }
            }else {
                new SystemAlert(Alert.AlertType.WARNING,"Warning","Please enter valid amount.",ButtonType.OK).show();
                TxtColours.setErrorColours(txtAmount);
            }
        }else {
            new SystemAlert(Alert.AlertType.WARNING,"Warning","Select the appointmentId!",ButtonType.OK).show();
        }
    }

    private void clearComponents() {
        cmbAppointmentId.setValue(null);
        txtAmount.setText("");
        lblCustomerName.setText("");
    }

    @FXML
    void searchCancelBtnOnAction(ActionEvent event) {
        txtSearch.setText("");
        searchCancelBtn.setVisible(false);
    }

    @FXML
    void cmbAppointmentOnAction(ActionEvent event) {
        String id = cmbAppointmentId.getSelectionModel().getSelectedItem();
        try {
            String amount = paymentBO.getPaymentAmount(id);
            txtAmount.setText(amount);
            String customer = paymentBO.getCustomerName(id);
            lblCustomerName.setText(customer);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        getFilePath();
        btnSave.setVisible(false);
        btnCancel.setVisible(false);
        btnChange.setVisible(true);
    }

    @FXML
    void btnChangeOnAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a folder");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(btnChange.getScene().getWindow());
        if (selectedDirectory != null){
            filePath = selectedDirectory.getAbsolutePath();
            txtFilePath.setText(filePath);
        }
        btnSave.setVisible(true);
        btnCancel.setVisible(true);
        btnChange.setVisible(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        try {
            boolean isSaved = paymentBO.saveFilePath(filePath,user.getUserId());
            if (isSaved){
                new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","File path saved!",ButtonType.OK).show();
            }else {
                new SystemAlert(Alert.AlertType.WARNING,"Warning","File path not saved!",ButtonType.OK).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
        }
        TxtColours.setDefaultColours(txtFilePath);
        btnSave.setVisible(false);
        btnCancel.setVisible(false);
        btnChange.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatePaymentId();
        getAppointments();
        setCellValues();
        populatePaymentTable();
        searchFilter();
    }

    public void getFilePath() {
        try {
            String file = paymentBO.getFilePath(user.getUserId());
            if (file != null){
                filePath = file;
                txtFilePath.setText(filePath);
            }else {
                txtFilePath.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
        }
    }

    private void generateReceipt(String paymentId,String appointmentId) {
        try {
            JasperDesign design = JRXmlLoader.load("src/main/java/lk/ijse/moods_salon/report/paymentReceipt.jrxml");
            JasperReport report = JasperCompileManager.compileReport(design);
            HashMap<String, Object> map = new HashMap();
            map.put("parameterPaymentId", paymentId);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, DBConnection.getInstance().getConnection());
            //JasperViewer.viewReport(jasperPrint,false);

            JasperExportManager.exportReportToPdfFile(jasperPrint, filePath + "\\Receipt " + paymentId + ".pdf");

            String email = paymentBO.getEmail(paymentId);

            String subject = "Payment has done!";
            String msg = "Payment has done and your receipt is attached here.";

            Mail mail = new Mail(email, msg, subject, new File(filePath + "\\Receipt " + paymentId + ".pdf"));
            Thread thread = new Thread(mail);
            thread.setDaemon(true);
            mail.valueProperty().addListener((a, oldValue, newValue) -> {
                if (newValue) {
                    new SystemAlert(Alert.AlertType.INFORMATION, "Email", "Payment receipt was sent successfully", ButtonType.OK).show();
                } else {
                    new SystemAlert(Alert.AlertType.WARNING, "Warning", "Connection failed", ButtonType.OK).show();
                }
            });
            progressBar.progressProperty().bind(mail.progressProperty());
            progressBar.visibleProperty().bind(mail.runningProperty());
            thread.start();

        } catch (JRException | SQLException e) {
            e.printStackTrace();
            new SystemAlert(Alert.AlertType.ERROR, "Error", "Something went wrong!0").show();
        }
    }

    public void setUser(UserDTO user){
        this.user = user;
    }

    private void searchFilter() {
        FilteredList<PaymentTM> filteredData = new FilteredList<>(data, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(PaymentTM -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    searchCancelBtn.setVisible(false);
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                if (PaymentTM.getPaymentId().toLowerCase().contains(searchKeyword)){
                    searchCancelBtn.setVisible(true);
                    return true;
                }else if(PaymentTM.getUser().toLowerCase().contains(searchKeyword)){
                    searchCancelBtn.setVisible(true);
                    return true;
                }else
                    searchCancelBtn.setVisible(true);
                return false;

            });
        });

        SortedList<PaymentTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblPayment.comparatorProperty());
        tblPayment.setItems(sortedData);
    }

    private void setCellValues() {
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("user"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    private void populatePaymentTable() {
        try {
            data = paymentBO.getAllPayments();
            tblPayment.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void getAppointments() {
        try {
            ObservableList<String> appointmentId = paymentBO.getPendingAppointments();
            cmbAppointmentId.setItems(appointmentId);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void generatePaymentId() {
        try {
            String id = paymentBO.generatePaymentId();
            lblPaymentId.setText(id);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }
}

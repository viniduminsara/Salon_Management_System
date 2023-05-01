package lk.ijse.gdse.controller;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.gdse.db.DBConnection;
import lk.ijse.gdse.dto.User;
import lk.ijse.gdse.dto.tm.PaymentTM;
import lk.ijse.gdse.mail.Mail;
import lk.ijse.gdse.model.AppointmentModel;
import lk.ijse.gdse.model.PaymentModel;
import lk.ijse.gdse.model.PlacePaymentModel;
import lk.ijse.gdse.util.RegExPatterns;
import lk.ijse.gdse.util.TxtColours;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    ObservableList<PaymentTM> data = FXCollections.observableArrayList();

    private User user;

    @FXML
    void paymentBtnOnAction(ActionEvent event) {
        if (!(cmbAppointmentId.getSelectionModel().getSelectedItem() == null || txtAmount.getText().isEmpty() || lblCustomerName.getText().isEmpty())) {
            if (RegExPatterns.getDoublePattern().matcher(txtAmount.getText()).matches()) {
                TxtColours.setDefaultColours(txtAmount);

                String paymentId = lblPaymentId.getText();
                double amount = Double.parseDouble(txtAmount.getText());
                Date dateformat = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = formatter.format(dateformat);
                String userId = user.getUserId();
                String appointmentId = cmbAppointmentId.getSelectionModel().getSelectedItem();

                try {
                    boolean isPaymentPlaced = PlacePaymentModel.placePayment(paymentId, amount, date, userId, appointmentId);
                    if (isPaymentPlaced) {
                        generateReceipt(paymentId,appointmentId);
                        new Alert(Alert.AlertType.CONFIRMATION, "Payment has placed!").show();
                        populatePaymentTable();
                        searchFilter();
                        clearComponents();
                        generatePaymentId();
                        getAppointments();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Payment has not placed!").show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"Please enter valid amount.").show();
                TxtColours.setErrorColours(txtAmount);
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Select the appointmentId!").show();
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
            String amount = AppointmentModel.findAmount(id);
            txtAmount.setText(amount);
            String customer = AppointmentModel.getCustomer(id);
            lblCustomerName.setText(customer);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatePaymentId();
        getAppointments();
        setCellValues();
        populatePaymentTable();
        searchFilter();
    }

    private void generateReceipt(String paymentId,String appointmentId) {

        try {
            JasperDesign design = JRXmlLoader.load("F:\\1st semester final project\\moods salon\\src\\main\\java\\lk\\ijse\\gdse\\report\\paymentReceipt.jrxml");
            JasperReport report = JasperCompileManager.compileReport(design);
            HashMap<String,Object> map = new HashMap();
            map.put("parameterPaymentId",paymentId);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report,map, DBConnection.getInstance().getConnection());
            //JasperViewer.viewReport(jasperPrint,false);

            JasperExportManager.exportReportToPdfFile(jasperPrint,"F:\\1st semester final project\\receipts\\Receipt "+paymentId+".pdf");

            String email = PaymentModel.getEmail(paymentId);

            String subject = "Payment has done!";
            String msg = "Payment has done and your receipt is attached here.";

            Mail mail = new Mail(email,msg,subject,new File("F:\\1st semester final project\\receipts\\Receipt "+paymentId+".pdf"));
            Thread thread = new Thread(mail);
            thread.setDaemon(true);
            thread.start();

        } catch (JRException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }

    }

    public void setUser(User user){
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
            data = PaymentModel.getAll();
            tblPayment.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void getAppointments() {
        try {
            ObservableList<String> appointmentId = AppointmentModel.getPendingAppointments();
            cmbAppointmentId.setItems(appointmentId);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void generatePaymentId() {
        try {
            String id = PaymentModel.generateId();
            lblPaymentId.setText(id);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }
}
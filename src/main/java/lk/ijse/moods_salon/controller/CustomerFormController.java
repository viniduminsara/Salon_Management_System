package lk.ijse.moods_salon.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.scene.image.ImageView;
import lk.ijse.moods_salon.dto.CustomerDTO;
import lk.ijse.moods_salon.dto.tm.CustomerTM;
import lk.ijse.moods_salon.mail.Mail;
import lk.ijse.moods_salon.model.CustomerModel;
import lk.ijse.moods_salon.util.RegExPatterns;
import lk.ijse.moods_salon.util.SystemAlert;
import lk.ijse.moods_salon.util.TxtColours;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class CustomerFormController implements Initializable {

    @FXML
    private TableView<CustomerTM> tblCustomer;

    @FXML
    private TableColumn<?, ?> ColCustomerId;

    @FXML
    private TableColumn<?, ?> ColName;

    @FXML
    private TableColumn<?, ?> ColAddress;

    @FXML
    private TableColumn<?, ?> ColContact;

    @FXML
    private TableColumn<?, ?> ColGmail;

    @FXML
    private TableColumn<?, ?> ColAction;

    @FXML
    private JFXTextField txtsearch;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private JFXTextField txtGmail;

    @FXML
    private JFXTextField txtCustomerId;

    @FXML
    private JFXButton searchCancelBtn;

    @FXML
    private JFXTextArea txtMessage;

    @FXML
    private JFXTextField txtSubject;

    ObservableList<CustomerTM> data = FXCollections.observableArrayList();

    @FXML
    private ProgressBar progress;

    @FXML
    private Label lblerror;

    @FXML
    void addCustomerBtnOnAction(ActionEvent event) {
        boolean isExists = false;
        try {
            isExists = CustomerModel.findExists(txtCustomerId.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!isExists) {
            TxtColours.setDefaultColours(txtCustomerId);
            if (!(txtCustomerId.getText().isEmpty() || txtCustomerName.getText().isEmpty() || txtGmail.getText().isEmpty() || txtContact.getText().isEmpty() || txtAddress.getText().isEmpty())) {
                if (RegExPatterns.getCustomerId().matcher(txtCustomerId.getText()).matches()) {
                    TxtColours.setDefaultColours(txtCustomerId);
                    TxtColours.setDefaultColours(txtCustomerName);
                    TxtColours.setDefaultColours(txtAddress);
                    if (RegExPatterns.getContactPattern().matcher(txtContact.getText()).matches()) {
                        TxtColours.setDefaultColours(txtContact);
                        if (RegExPatterns.getEmailPattern().matcher(txtGmail.getText()).matches()) {
                            TxtColours.setDefaultColours(txtGmail);
                            lblerror.setText("");

                            String id = txtCustomerId.getText();
                            String name = txtCustomerName.getText();
                            String gmail = txtGmail.getText();
                            String contact = txtContact.getText();
                            String address = txtAddress.getText();

                            try {
                                boolean isSaved = CustomerModel.addCustomer(new CustomerDTO(id, name, gmail, contact, address));
                                if (isSaved) {
                                    new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Customer saved!",ButtonType.OK).show();
                                    populateCustomerTable();
                                    searchFilter();
                                    clearTextfields();
                                } else {
                                    new SystemAlert(Alert.AlertType.WARNING,"Warning","Customer not saved!",ButtonType.OK).show();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                new SystemAlert(Alert.AlertType.ERROR,"Error","Somehing went wrong!",ButtonType.OK).show();
                            }
                        }else {
                            lblerror.setText("Invalid gmail.");
                            TxtColours.setErrorColours(txtGmail);

                        }
                    }else {
                        lblerror.setText("Invalid contact number");
                        TxtColours.setErrorColours(txtContact);
                    }
                }else {
                    lblerror.setText("Invalid customerId.");
                    TxtColours.setErrorColours(txtCustomerId);
                }
            }else {
                lblerror.setText("Fill all the details");
                if (txtCustomerId.getText().isEmpty()){
                    TxtColours.setErrorColours(txtCustomerId);
                }
                if (txtCustomerName.getText().isEmpty()){
                    TxtColours.setErrorColours(txtCustomerName);
                }
                if (txtAddress.getText().isEmpty()){
                    TxtColours.setErrorColours(txtAddress);
                }
                if (txtContact.getText().isEmpty()){
                    TxtColours.setErrorColours(txtContact);
                }
                if (txtGmail.getText().isEmpty()){
                    TxtColours.setErrorColours(txtGmail);
                }
            }
        }else {
            lblerror.setText("Customer Id is already exists!");
            TxtColours.setErrorColours(txtCustomerId);
        }
    }

    private void clearTextfields() {
        txtCustomerId.setText("");
        txtCustomerName.setText("");
        txtGmail.setText("");
        txtContact.setText("");
        txtAddress.setText("");
    }

    @FXML
    void deleteCustomerBtnOnAction(ActionEvent event) {
        if (!txtCustomerId.getText().isEmpty()) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new SystemAlert(Alert.AlertType.INFORMATION,"Information","Are you sure to delete ?",yes,no).showAndWait();

        if (result.orElse(no) == yes) {
            String id = txtCustomerId.getText();
            if (RegExPatterns.getCustomerId().matcher(id).matches()) {
                TxtColours.setDefaultColours(txtCustomerId);
                lblerror.setText("");
                try {
                    boolean isDeleted = CustomerModel.deleteCustomer(id);
                    if (isDeleted) {
                        new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Customer deleted!",ButtonType.OK).show();
                        clearTextfields();
                        populateCustomerTable();
                        searchFilter();
                    } else {
                        new SystemAlert(Alert.AlertType.WARNING,"Warning","Customer not deleted!",ButtonType.OK).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new SystemAlert(Alert.AlertType.ERROR,"Error","Somehing went wrong!",ButtonType.OK).show();
                }
            }else {
                lblerror.setText("Invalid customer id");
                TxtColours.setErrorColours(txtCustomerId);
            }
        }
        }else {
            lblerror.setText("Please enter the customer id");
            TxtColours.setErrorColours(txtCustomerId);
        }
    }

    @FXML
    void updateCustomerBtnOnAction(ActionEvent event) {

        if (!(txtCustomerId.getText().isEmpty() || txtCustomerName.getText().isEmpty() || txtGmail.getText().isEmpty() || txtContact.getText().isEmpty() || txtAddress.getText().isEmpty())) {
            if (RegExPatterns.getCustomerId().matcher(txtCustomerId.getText()).matches()) {
                TxtColours.setDefaultColours(txtCustomerId);
                TxtColours.setDefaultColours(txtCustomerName);
                TxtColours.setDefaultColours(txtAddress);
                if (RegExPatterns.getContactPattern().matcher(txtContact.getText()).matches()) {
                    TxtColours.setDefaultColours(txtContact);
                    if (RegExPatterns.getEmailPattern().matcher(txtGmail.getText()).matches()) {
                        TxtColours.setDefaultColours(txtGmail);
                        lblerror.setText("");

                        String id = txtCustomerId.getText();
                        String name = txtCustomerName.getText();
                        String gmail = txtGmail.getText();
                        String contact = txtContact.getText();
                        String address = txtAddress.getText();

                        try {
                            boolean isUpdated = CustomerModel.updateCustomer(new CustomerDTO(id, name, gmail, contact, address));
                            if (isUpdated) {
                                new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Customer updated!",ButtonType.OK).show();
                                clearTextfields();
                                populateCustomerTable();
                                searchFilter();
                            } else {
                                new SystemAlert(Alert.AlertType.WARNING,"Warning","Customer not updated!",ButtonType.OK).show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            new SystemAlert(Alert.AlertType.ERROR,"Error","Somehing went wrong!",ButtonType.OK).show();
                        }
                    }else {
                        lblerror.setText("Invalid gmail.");
                        TxtColours.setErrorColours(txtGmail);

                    }
                }else {
                    lblerror.setText("Invalid contact number");
                    TxtColours.setErrorColours(txtContact);
                }
            }else {
                lblerror.setText("Invalid customerId.");
                TxtColours.setErrorColours(txtCustomerId);
            }
        }else {
            lblerror.setText("Fill all the details");
            if (txtCustomerId.getText().isEmpty()){
                TxtColours.setErrorColours(txtCustomerId);
            }
            if (txtCustomerName.getText().isEmpty()){
                TxtColours.setErrorColours(txtCustomerName);
            }
            if (txtAddress.getText().isEmpty()){
                TxtColours.setErrorColours(txtAddress);
            }
            if (txtContact.getText().isEmpty()){
                TxtColours.setErrorColours(txtContact);
            }
            if (txtGmail.getText().isEmpty()){
                TxtColours.setErrorColours(txtGmail);
            }
        }
    }

    @FXML
    void searchCancelBtnOnAction(ActionEvent event) {
        txtsearch.setText("");
        searchCancelBtn.setVisible(false);
    }

    @FXML
    void sendBtnOnAction(ActionEvent event) {
        if (!(txtSubject.getText().isEmpty() || txtMessage.getText().isEmpty())) {

            List<String> emails = new ArrayList<>();
            try {
                emails = CustomerModel.getEmails();
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }

            String subject = txtSubject.getText();
            String message = txtMessage.getText();

            Mail mail = new Mail(emails,message,subject);
            Thread thread = new Thread(mail);
            mail.valueProperty().addListener((a, oldValue, newValue) -> {
                if (newValue){
                    new SystemAlert(Alert.AlertType.INFORMATION,"Email","Announcement was sent!",ButtonType.OK).show();
                    txtSubject.setText("");
                    txtMessage.setText("");
                }else {
                    new SystemAlert(Alert.AlertType.NONE,"Connection Error","Connection Error!",ButtonType.OK).show();
                }
            });
            progress.progressProperty().bind(mail.progressProperty());
            progress.visibleProperty().bind(mail.runningProperty());
            thread.start();
        }else {
            new SystemAlert(Alert.AlertType.WARNING,"Warning","Please fill the message!",ButtonType.OK).show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setValueFactory();
        populateCustomerTable();
        searchFilter();
    }

    private void searchFilter() {
        FilteredList<CustomerTM> filteredData = new FilteredList<>(data, b -> true);
        txtsearch.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(CustomerTM -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    searchCancelBtn.setVisible(false);
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                if (CustomerTM.getCustomerId().toLowerCase().contains(searchKeyword)){
                    searchCancelBtn.setVisible(true);
                    return true;
                }else if(CustomerTM.getName().toLowerCase().contains(searchKeyword)){
                    searchCancelBtn.setVisible(true);
                    return true;
                }else
                    searchCancelBtn.setVisible(true);
                    return false;

            });
        });

        SortedList<CustomerTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblCustomer.comparatorProperty());
        tblCustomer.setItems(sortedData);
    }

    private void setValueFactory() {
        ColCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        ColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ColAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        ColContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        ColGmail.setCellValueFactory(new PropertyValueFactory<>("gmail"));
        ColAction.setCellValueFactory(new PropertyValueFactory<>("remove"));
    }

    private void populateCustomerTable() {
        try {
            ResultSet rs = CustomerModel.getAllCustomers();
                data.clear();
                while (rs.next()) {
                    JFXButton button = new JFXButton("edit",new ImageView("img/edit-97@30x.png"));
                    button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    button.getStyleClass().add("infoBtn");
                    setButtonOnAction(button);
                    data.add(new CustomerTM(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), button));
                }
                tblCustomer.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Someyhing went wrong!").show();
        }
    }

    private void setButtonOnAction(JFXButton button) {
        button.setOnAction((e) -> {
            int index = -1;

            for (int i=0 ; i < tblCustomer.getItems().size(); i++){
                if (ColAction.getCellData(i).equals(button)){
                    index = i;
                }
            }
            CustomerTM customer = tblCustomer.getItems().get(index);
            txtCustomerId.setText(customer.getCustomerId());
            txtCustomerName.setText(customer.getName());
            txtAddress.setText(customer.getAddress());
            txtContact.setText(customer.getContact());
            txtGmail.setText(customer.getGmail());
        });
    }
}

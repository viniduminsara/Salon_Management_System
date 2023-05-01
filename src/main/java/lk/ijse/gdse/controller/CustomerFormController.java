package lk.ijse.gdse.controller;

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
import javafx.scene.paint.Paint;
import lk.ijse.gdse.dto.Appointment;
import lk.ijse.gdse.dto.Customer;
import lk.ijse.gdse.dto.tm.CustomerTM;
import lk.ijse.gdse.mail.Mail;
import lk.ijse.gdse.model.CustomerModel;
import lk.ijse.gdse.util.RegExPatterns;
import lk.ijse.gdse.util.TxtColours;

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
    private ProgressIndicator progress;

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
                    if (RegExPatterns.getEmailPattern().matcher(txtGmail.getText()).matches()) {
                        TxtColours.setDefaultColours(txtGmail);
                        if (RegExPatterns.getContactPattern().matcher(txtContact.getText()).matches()) {
                            TxtColours.setDefaultColours(txtContact);

                            String id = txtCustomerId.getText();
                            String name = txtCustomerName.getText();
                            String gmail = txtGmail.getText();
                            String contact = txtContact.getText();
                            String address = txtAddress.getText();

                            try {
                                boolean isSaved = CustomerModel.addCustomer(new Customer(id, name, gmail, contact, address));
                                if (isSaved) {
                                    new Alert(Alert.AlertType.CONFIRMATION, "Customer saved :)").show();
                                    populateCustomerTable();
                                    searchFilter();
                                    clearTextfields();
                                } else {
                                    new Alert(Alert.AlertType.WARNING, "Customer not saved :(").show();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                            }
                        }else {
                            new Alert(Alert.AlertType.WARNING,"Invalid contact number").show();
                            TxtColours.setErrorColours(txtContact);
                        }
                    }else {
                        new Alert(Alert.AlertType.WARNING,"Invalid gmail.").show();
                        TxtColours.setErrorColours(txtGmail);
                    }
                }else {
                    new Alert(Alert.AlertType.WARNING,"Invalid customerId.").show();
                    TxtColours.setErrorColours(txtCustomerId);
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"Fill all the details").show();
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Customer Id is already exists!").show();
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

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION,"Are you sure to delete ?",yes,no).showAndWait();

        if (result.orElse(no) == yes){
                String id = txtCustomerId.getText();

                try {
                    boolean isDeleted = CustomerModel.deleteCustomer(id);
                    if (isDeleted) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Customer has deleted!").show();
                        clearTextfields();
                        populateCustomerTable();
                        searchFilter();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Customer not deleted :(").show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Please give customerId").show();
        }
    }

    @FXML
    void updateCustomerBtnOnAction(ActionEvent event) {

        if (!(txtCustomerId.getText().isEmpty() || txtCustomerName.getText().isEmpty() || txtGmail.getText().isEmpty() || txtContact.getText().isEmpty() || txtAddress.getText().isEmpty())) {
            if (RegExPatterns.getCustomerId().matcher(txtCustomerId.getText()).matches()) {
                TxtColours.setDefaultColours(txtCustomerId);
                if (RegExPatterns.getEmailPattern().matcher(txtGmail.getText()).matches()) {
                    TxtColours.setDefaultColours(txtGmail);
                    if (RegExPatterns.getContactPattern().matcher(txtContact.getText()).matches()) {
                        TxtColours.setDefaultColours(txtContact);

                        String id = txtCustomerId.getText();
                        String name = txtCustomerName.getText();
                        String gmail = txtGmail.getText();
                        String contact = txtContact.getText();
                        String address = txtAddress.getText();

                        try {
                            boolean isUpdated = CustomerModel.updateCustomer(new Customer(id, name, gmail, contact, address));
                            if (isUpdated) {
                                new Alert(Alert.AlertType.CONFIRMATION, "Customer details updated!").show();
                                clearTextfields();
                                populateCustomerTable();
                                searchFilter();
                            } else {
                                new Alert(Alert.AlertType.WARNING, "Customer details not updated!").show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
                        }
                    }else {
                        new Alert(Alert.AlertType.WARNING,"Invalid contact number").show();
                        TxtColours.setErrorColours(txtContact);
                    }
                }else {
                    new Alert(Alert.AlertType.WARNING,"Invalid gmail.").show();
                    TxtColours.setErrorColours(txtGmail);
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"Invalid customerId.").show();
                TxtColours.setErrorColours(txtCustomerId);
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Please fill all details").show();
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
            System.out.println("Start");

            List<String> emails = new ArrayList<>();
            try {
                emails = CustomerModel.getEmails();
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }

            String subject = txtSubject.getText();
            String message = txtMessage.getText();

            for (String email : emails) {

                Mail mail = new Mail(email,subject,message);
                Thread ob = new Thread(mail);
                ob.setDaemon(true);
                progress.progressProperty().bind(mail.progressProperty());
                progress.visibleProperty().bind(mail.runningProperty());
                ob.start();
            }
            System.out.println("end");
        }else {
            new Alert(Alert.AlertType.WARNING,"Please fill the message!").show();
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
                    JFXButton button = new JFXButton("edit",new ImageView("F:\\1st semester final project\\moods salon\\src\\main\\resources\\img\\edit-97@30x.png"));
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

package lk.ijse.moods_salon.controller;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import lk.ijse.moods_salon.dto.AppointmentInventoryDTO;
import lk.ijse.moods_salon.dto.tm.AppointmentCartTM;
import lk.ijse.moods_salon.dto.tm.AppointmentTM;
import lk.ijse.moods_salon.mail.Mail;
import lk.ijse.moods_salon.model.*;
import lk.ijse.moods_salon.util.RegExPatterns;
import lk.ijse.moods_salon.util.SystemAlert;
import lk.ijse.moods_salon.util.TxtColours;

public class AppointmentFormController implements Initializable {

    @FXML
    private AnchorPane pane;

    @FXML
    private JFXDatePicker appointtmentDate;

    @FXML
    private JFXTimePicker appointmentTime;

    @FXML
    private Label lblCustomerName;

    @FXML
    private JFXComboBox<String> cmbCustomer;

    @FXML
    private JFXComboBox<String> cmbService;

    @FXML
    private JFXComboBox<String> cmbEmployee;

    @FXML
    private JFXComboBox<String> cmbInventory;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private TableView<AppointmentCartTM> tblCart;

    @FXML
    private TableColumn<?, ?> colService;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colEmployee;

    @FXML
    private TableColumn<?, ?> colInventory;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private Label lblAppointmentId;

    @FXML
    private TableView<AppointmentTM> tblAppointment;

    @FXML
    private TableColumn<?, ?> colAppointmentId;

    @FXML
    private TableColumn<?, ?> colCustomerId;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colTime;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colActions;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXButton searchCancelBtn;

    ObservableList<AppointmentCartTM> cart = FXCollections.observableArrayList();

    ObservableList<AppointmentTM> appointment = FXCollections.observableArrayList();

    ObservableList<String> services = FXCollections.observableArrayList();

    ObservableList<String> employees = FXCollections.observableArrayList();

    ObservableList<String> inventories = FXCollections.observableArrayList();

    @FXML
    void addCustomerBtnOnAction(ActionEvent event) throws IOException {
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/customerForm.fxml")));
    }

    @FXML
    void cmbCustomerOnAction(ActionEvent event) {
        String id = cmbCustomer.getSelectionModel().getSelectedItem();
        try {
            String name = CustomerModel.getCustomerName(id);
            lblCustomerName.setText(name);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    @FXML
    void addRequirementOnAction(ActionEvent event) {
        if (!(cmbService.getSelectionModel().getSelectedItem() == null || cmbEmployee.getSelectionModel().getSelectedItem() == null || cmbInventory.getSelectionModel().getSelectedItem() == null || txtQty.getText().isEmpty())) {
            if (RegExPatterns.getIntPattern().matcher(txtQty.getText()).matches()) {
                TxtColours.setDefaultColours(txtQty);

                String service = cmbService.getSelectionModel().getSelectedItem();
                services.remove(service);
                String employee = cmbEmployee.getSelectionModel().getSelectedItem();
                employees.remove(employee);
                String inventory = cmbInventory.getSelectionModel().getSelectedItem();
                inventories.remove(inventory);
                int qty = Integer.parseInt(txtQty.getText());
                double price = 0;
                try {
                    price = ServiceModel.findPrice(service);
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
                JFXButton button = new JFXButton("delete", new ImageView("img/trash-can@1.5x.png"));
                button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                button.getStyleClass().add("infoBtn");
                setRemoveBtnOnAction(button);
                cart.add(new AppointmentCartTM(service, price, employee, inventory, qty, button));
                tblCart.setItems(cart);
                clearComponents();
            }else {
                new SystemAlert(Alert.AlertType.WARNING,"Warning","Please enter correct qty",ButtonType.OK).show();
                TxtColours.setErrorColours(txtQty);
            }
        }else {
            new SystemAlert(Alert.AlertType.WARNING,"Warning","Please select all the requirements!",ButtonType.OK).show();
        }
    }

    @FXML
    void placeAppointmentOnAction(ActionEvent event) {
        if (tblCart.getItems().size() != 0) {
            if (!(lblAppointmentId.getText().isEmpty() || appointtmentDate.getValue() == null || appointmentTime.getValue() == null || cmbCustomer.getSelectionModel().getSelectedItem() == null)) {
                if (!appointtmentDate.getValue().isBefore(LocalDate.now())) {
                    String appointmentId = lblAppointmentId.getText();
                    Date date = Date.from(appointtmentDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    String time = appointmentTime.getValue().toString();
                    String customerId = cmbCustomer.getSelectionModel().getSelectedItem();

                    List<String> services = new ArrayList<>();
                    List<String> employees = new ArrayList<>();
                    List<AppointmentInventoryDTO> inventoryList = new ArrayList<>();

                    for (int i = 0; i < tblCart.getItems().size(); i++) {
                        AppointmentCartTM cartTM = tblCart.getItems().get(i);
                        try {
                            String serviceId = ServiceModel.getServiceId(cartTM.getService());
                            services.add(serviceId);

                            String employeeId = EmployeeModel.getEmployeeId(cartTM.getEmployee());
                            employees.add(employeeId);

                            String inventoryId = InventoryModel.getInventoryId(cartTM.getInventory());
                            inventoryList.add(new AppointmentInventoryDTO(appointmentId, inventoryId, cartTM.getQty()));
                        } catch (SQLException e) {
                            e.printStackTrace();
                            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                        }
                    }

                    try {
                        boolean isPlaced = PlaceAppointmentModel.placeAppointment(appointmentId, date, time, customerId, services, employees, inventoryList);
                        if (isPlaced) {
                            new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Appointment has placed!",ButtonType.OK).show();
                            sendMail(customerId, date, time);
                            clearAppointment();
                            generateAppointmentId();
                            populateAppointmentTable();
                            cart.clear();
                            setServices();
                            setEmployees();
                            setInventory();
                        } else {
                            new SystemAlert(Alert.AlertType.WARNING,"Warning","Appointment has not placed!",ButtonType.OK).show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
                    }
                }else {
                    new SystemAlert(Alert.AlertType.WARNING,"Warning","Please select correct date!",ButtonType.OK).show();
                }
            }else {
                new SystemAlert(Alert.AlertType.WARNING,"Warning","Please fill all details of appointment.",ButtonType.OK).show();
            }
        }else {
            new SystemAlert(Alert.AlertType.WARNING,"Warning","Please select requirements for appointment.",ButtonType.OK).show();
        }
    }

    @FXML
    void searchCancelBtnOnAction(ActionEvent event) {
        txtSearch.setText("");
        searchCancelBtn.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCustomers();
        setServices();
        setEmployees();
        setInventory();
        generateAppointmentId();
        setCellValue();
        populateAppointmentTable();
        searchFilter();
    }

    private void sendMail(String customerId, Date date, String time) {
        String day = appointtmentDate.getValue().toString();
        String email = null;
        try {
            email = CustomerModel.getEmail(customerId);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
        String subject = "Appointment placed 😊";
        String message = "Appointment has placed on <b>"+day+"</b> at <b>"+time+"</b>. You can cancel appointment by contact us.";

        Mail mail = new Mail(email,subject,message);
        Thread thread = new Thread(mail);

        mail.valueProperty().addListener((a, oldValue, newValue) -> {
            if (newValue){
                new SystemAlert(Alert.AlertType.INFORMATION,"Email","Mail sent successfully",ButtonType.OK).show();
            }else {
                new SystemAlert(Alert.AlertType.NONE,"Connection Error","Connection Error!",ButtonType.OK).show();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void searchFilter() {
        FilteredList<AppointmentTM> filteredData = new FilteredList<>(appointment, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(AppointmentTM -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    searchCancelBtn.setVisible(false);
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                if (AppointmentTM.getAppointmentId().toLowerCase().contains(searchKeyword)){
                    searchCancelBtn.setVisible(true);
                    return true;
                }else if(AppointmentTM.getCustomerId().toLowerCase().contains(searchKeyword)) {
                    searchCancelBtn.setVisible(true);
                    return true;
                }else if(AppointmentTM.getStatus().toLowerCase().contains(searchKeyword)){
                    searchCancelBtn.setVisible(true);
                    return true;
                }else
                    searchCancelBtn.setVisible(true);
                return false;

            });
        });

        SortedList<AppointmentTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblAppointment.comparatorProperty());
        tblAppointment.setItems(sortedData);
    }

    private void populateAppointmentTable() {
        try {
            appointment.clear();
            ResultSet rs = AppointmentModel.getAll();
            while (rs.next()){
                if (rs.getString(5).equals("Canceled") || rs.getString(5).equals("Completed")){
                    appointment.add(new AppointmentTM(rs.getString(1),rs.getString(2),rs.getDate(3),rs.getString(4),rs.getString(5),new JFXButton()));
                }else {
                    JFXButton button = new JFXButton("cancel", new ImageView("img/cancel@1.5x.png"));
                    button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    button.getStyleClass().add("infoBtn");
                    setCancelBtnOnAction(button);
                    appointment.add(new AppointmentTM(rs.getString(1), rs.getString(2), rs.getDate(3), rs.getString(4), rs.getString(5), button));
                }
            }
            tblAppointment.setItems(appointment);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void setCancelBtnOnAction(JFXButton button) {
        button.setOnAction((e) -> {
            int index = -1;
            for (int i=0 ; i<tblAppointment.getItems().size(); i++){
                if (colActions.getCellData(i).equals(button)){
                    index = i;
                }
            }
            ButtonType yes = new ButtonType("Yes",ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No",ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new SystemAlert(Alert.AlertType.INFORMATION,"Information","Do you want to cancel '"+colAppointmentId.getCellData(index)+"' appointment?",yes,no).showAndWait();
            if (result.orElse(no) == yes){
                String id = String.valueOf(colAppointmentId.getCellData(index));
                try {
                    boolean isCanceled = AppointmentModel.cancelAppointment(id);
                    if (isCanceled){
                        new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Appointment '"+id+"' was canceled",ButtonType.OK).show();
                        populateAppointmentTable();
                        searchFilter();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
                }
            }
        });
    }

    private void clearAppointment() {
        cmbCustomer.setValue("");
        appointtmentDate.setValue(null);
        appointmentTime.setValue(null);
        clearComponents();
    }

    private void setRemoveBtnOnAction(JFXButton button) {
        button.setOnAction((e) -> {
            int index = -1;
            for (int i=0; i<tblCart.getItems().size(); i++){
                if (colAction.getCellData(i).equals(button)){
                    index = i;
                }
            }
            ButtonType yes = new ButtonType("Yes",ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No",ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new SystemAlert(Alert.AlertType.INFORMATION,"Information","Are you sure to remove '"+colService.getCellData(index)+"' service?",yes,no).showAndWait();

            if (result.orElse(no) == yes) {
                services.add(String.valueOf(colService.getCellData(index)));
                employees.add(String.valueOf(colEmployee.getCellData(index)));
                inventories.add(String.valueOf(colInventory.getCellData(index)));
                tblCart.getItems().remove(index);
                tblCart.refresh();
            }
        });
    }

    private void clearComponents() {
        cmbInventory.setValue("");
        cmbEmployee.setValue("");
        cmbService.setValue("");
        txtQty.setText("");
    }

    private void setCellValue() {
        //set Appointment cart table
        colService.setCellValueFactory(new PropertyValueFactory<>("service"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("employee"));
        colInventory.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("button"));

        //set Appointment details table
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colActions.setCellValueFactory(new PropertyValueFactory<>("button"));
    }

    private void generateAppointmentId() {
        try {
            String id = AppointmentModel.generateId();
            lblAppointmentId.setText(id);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void setInventory() {
        try {
            inventories = InventoryModel.getInventoryNames();
            cmbInventory.setItems(inventories);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong");
        }
    }

    private void setEmployees() {
        try {
            employees = EmployeeModel.getEmployeeNames();
            cmbEmployee.setItems(employees);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void setServices() {
        try {
            services = ServiceModel.getServiceNames();
            cmbService.setItems(services);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void setCustomers() {
        try {
            ObservableList<String> customer = CustomerModel.getAllCustomerId();
            cmbCustomer.setItems(customer);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }

    }
}

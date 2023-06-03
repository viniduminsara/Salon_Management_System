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
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import lk.ijse.moods_salon.bo.custom.ServiceBO;
import lk.ijse.moods_salon.bo.custom.impl.ServiceBOImpl;
import lk.ijse.moods_salon.dto.ServiceDTO;
import lk.ijse.moods_salon.dto.tm.ServiceTM;
import lk.ijse.moods_salon.util.RegExPatterns;
import lk.ijse.moods_salon.util.SystemAlert;
import lk.ijse.moods_salon.util.TxtColours;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


public class ServiceFormController implements Initializable {
    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtserviceid;

    @FXML
    private JFXComboBox<String> cmbCategory;

    @FXML
    private TableView<ServiceTM> tblService;

    @FXML
    private TableColumn<?, ?> colServiceId;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private JFXButton searchCancelBtn;

    @FXML
    private Label lblError;

    ObservableList<ServiceTM> data = FXCollections.observableArrayList();

    ServiceBO serviceBO = new ServiceBOImpl();

    @FXML
    void addBtnOnAction(ActionEvent event) {
        boolean isExists = false;
        try {
            isExists = serviceBO.serviceExists(txtserviceid.getText());
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
        if (!(txtserviceid.getText().isEmpty() || txtDescription.getText().isEmpty() || txtPrice.getText().isEmpty() || cmbCategory.getSelectionModel().getSelectedItem() == null)) {
            if (RegExPatterns.getServiceId().matcher(txtserviceid.getText()).matches()) {
                TxtColours.setDefaultColours(txtserviceid);
                TxtColours.setDefaultColours(txtDescription);
                if (RegExPatterns.getDoublePattern().matcher(txtPrice.getText()).matches()) {
                    TxtColours.setDefaultColours(txtPrice);
                    cmbCategory.setFocusColor(Paint.valueOf("#4059a9"));
                    cmbCategory.setUnFocusColor(Paint.valueOf("#4d4d4d"));
                    if (!isExists) {
                        TxtColours.setDefaultColours(txtserviceid);
                        lblError.setText("");

                        String id = txtserviceid.getText();
                        String description = txtDescription.getText();
                        double price = Double.parseDouble(txtPrice.getText());
                        String category = cmbCategory.getSelectionModel().getSelectedItem();

                        try {
                            boolean isSaved = serviceBO.addService(new ServiceDTO(id, description, price, category));
                            if (isSaved) {
                                new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Service Saved!",ButtonType.OK).show();
                                clearTextFields();
                                populateServiceTable();
                                searchFilter();
                            } else {
                                new SystemAlert(Alert.AlertType.WARNING,"Warning","Service not saved!",ButtonType.OK).show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
                        }
                    } else {
                        lblError.setText("Service Id already exists!");
                        TxtColours.setErrorColours(txtserviceid);
                    }
                }else {
                    lblError.setText("Please enter double value for price.");
                    TxtColours.setErrorColours(txtPrice);
                }
            }else {
                lblError.setText("Invalid serviceId.");
                TxtColours.setErrorColours(txtserviceid);
            }
        }else {
            lblError.setText("Please fill all details");
            if (txtserviceid.getText().isEmpty()){
                TxtColours.setErrorColours(txtserviceid);
            }
            if (txtDescription.getText().isEmpty()){
                TxtColours.setErrorColours(txtDescription);
            }
            if (txtPrice.getText().isEmpty()){
                TxtColours.setErrorColours(txtPrice);
            }
            if (cmbCategory.getSelectionModel().getSelectedItem() == null){
                cmbCategory.setFocusColor(Paint.valueOf("Red"));
                cmbCategory.setUnFocusColor(Paint.valueOf("Red"));
            }
        }
    }

    private void clearTextFields() {
        txtserviceid.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        cmbCategory.setValue(null);
    }

    @FXML
    void deleteBtnOAction(ActionEvent event) {
        if (!txtserviceid.getText().isEmpty()) {
            if (RegExPatterns.getServiceId().matcher(txtserviceid.getText()).matches()) {
                boolean isExists = false;
                try {
                    isExists = serviceBO.serviceExists(txtserviceid.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
                }
                if (isExists) {
                    TxtColours.setDefaultColours(txtserviceid);
                    lblError.setText("");

                    ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> result = new SystemAlert(Alert.AlertType.INFORMATION, "Information", "Are you sure to delete?", yes, no).showAndWait();

                    if (result.orElse(no) == yes) {
                        String id = txtserviceid.getText();

                        try {
                            boolean isDeleted = serviceBO.deleteService(id);
                            if (isDeleted) {
                                new SystemAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Service deleted!", ButtonType.OK).show();
                                clearTextFields();
                                populateServiceTable();
                                searchFilter();
                            } else {
                                new SystemAlert(Alert.AlertType.WARNING, "Warning", "Service not deleted!", ButtonType.OK).show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            new SystemAlert(Alert.AlertType.ERROR, "Error", "Something went wrong!", ButtonType.OK).show();
                        }
                    }
                }else {
                    lblError.setText("No Service Found");
                    TxtColours.setErrorColours(txtserviceid);
                }
            }else {
                lblError.setText("Invalid service Id");
                TxtColours.setErrorColours(txtserviceid);
            }
        }else {
            lblError.setText("Please enter serviceId.");
            TxtColours.setErrorColours(txtserviceid);
        }
    }

    @FXML
    void updateBtnOnAction(ActionEvent event) {
        if (!(txtserviceid.getText().isEmpty() || txtDescription.getText().isEmpty() || txtPrice.getText().isEmpty() || cmbCategory.getSelectionModel().getSelectedItem() == null)) {
            if (RegExPatterns.getServiceId().matcher(txtserviceid.getText()).matches()) {
                TxtColours.setDefaultColours(txtserviceid);
                TxtColours.setDefaultColours(txtDescription);
                if (RegExPatterns.getDoublePattern().matcher(txtPrice.getText()).matches()) {
                    TxtColours.setDefaultColours(txtPrice);
                    cmbCategory.setFocusColor(Paint.valueOf("#4059a9"));
                    cmbCategory.setUnFocusColor(Paint.valueOf("#4d4d4d"));
                    lblError.setText("");

                    String id = txtserviceid.getText();
                    String description = txtDescription.getText();
                    double price = Double.parseDouble(txtPrice.getText());
                    String category = cmbCategory.getSelectionModel().getSelectedItem();

                    try {
                        boolean isUpdated = serviceBO.updateService(new ServiceDTO(id, description, price, category));
                        if (isUpdated) {
                            new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Service updated!",ButtonType.OK).show();
                            clearTextFields();
                            populateServiceTable();
                            searchFilter();
                        } else {
                            new SystemAlert(Alert.AlertType.WARNING,"Warning","Service has not updated!",ButtonType.OK).show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
                    }
                }else {
                    lblError.setText("Please enter double value for price.");
                    TxtColours.setErrorColours(txtPrice);
                }
            }else {
                lblError.setText("Invalid serviceId.");
                TxtColours.setErrorColours(txtserviceid);
            }
        }else {
            lblError.setText("Please fill all details");
            if (txtserviceid.getText().isEmpty()){
                TxtColours.setErrorColours(txtserviceid);
            }
            if (txtDescription.getText().isEmpty()){
                TxtColours.setErrorColours(txtDescription);
            }
            if (txtPrice.getText().isEmpty()){
                TxtColours.setErrorColours(txtPrice);
            }
            if (cmbCategory.getSelectionModel().getSelectedItem() == null){
                cmbCategory.setFocusColor(Paint.valueOf("Red"));
                cmbCategory.setUnFocusColor(Paint.valueOf("Red"));
            }
        }
    }

    @FXML
    void searchCancelBtnOnAction(ActionEvent event) {
        txtSearch.setText("");
        searchCancelBtn.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValue();
        populateServiceTable();
        searchFilter();
        setComboBox();
    }

    private void setComboBox() {
        String[] items = {"Hair","Nail","Dressing","Facial"};
        cmbCategory.setItems(FXCollections.observableArrayList(items));
    }

    private void searchFilter() {
        FilteredList<ServiceTM> filteredData = new FilteredList<>(data, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(ServiceTM -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    searchCancelBtn.setVisible(false);
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                if (ServiceTM.getServiceId().toLowerCase().contains(searchKeyword)){
                    searchCancelBtn.setVisible(true);
                    return true;
                }else if(ServiceTM.getDescription().toLowerCase().contains(searchKeyword)){
                    searchCancelBtn.setVisible(true);
                    return true;
                }else
                    searchCancelBtn.setVisible(true);
                return false;

            });
        });

        SortedList<ServiceTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblService.comparatorProperty());
        tblService.setItems(sortedData);
    }

    private void setCellValue() {
        colServiceId.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("button"));
    }

    private void populateServiceTable() {
        try {
            ArrayList<ServiceDTO> allServices = serviceBO.getAllServices();
            data.clear();
            for (ServiceDTO service : allServices){
                JFXButton button = new JFXButton("edit",new ImageView("img/edit-97@30x.png"));
                button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                button.getStyleClass().add("infoBtn");
                setEditBtnOnAction(button);
                data.add(new ServiceTM(service.getServiceId(),service.getDescription(),service.getPrice(),service.getCategory(),button));
            }
            if (data != null){
                tblService.setItems(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }

    private void setEditBtnOnAction(JFXButton button) {
        button.setOnAction((e) -> {
            int index = -1;
            for (int i=0; i<tblService.getItems().size(); i++){
                if (colAction.getCellData(i).equals(button)){
                    index = i;
                }
            }
            ServiceTM service = tblService.getItems().get(index);
            txtserviceid.setText(service.getServiceId());
            txtDescription.setText(service.getDescription());
            txtPrice.setText(String.valueOf(service.getPrice()));
            cmbCategory.setValue(service.getCategory());
        });
    }
}

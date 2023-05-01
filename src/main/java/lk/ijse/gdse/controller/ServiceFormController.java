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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import lk.ijse.gdse.dto.Service;
import lk.ijse.gdse.dto.tm.ServiceTM;
import lk.ijse.gdse.model.ServiceModel;
import lk.ijse.gdse.util.RegExPatterns;
import lk.ijse.gdse.util.TxtColours;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    ObservableList<ServiceTM> data = FXCollections.observableArrayList();

    @FXML
    void addBtnOnAction(ActionEvent event) {
        boolean isExists = false;
        try {
            isExists = ServiceModel.isExists(txtserviceid.getText());
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
        if (!(txtserviceid.getText().isEmpty() || txtDescription.getText().isEmpty() || txtPrice.getText().isEmpty() || cmbCategory.getSelectionModel().getSelectedItem() == null)) {
            if (!isExists) {
                TxtColours.setDefaultColours(txtserviceid);
                if (RegExPatterns.getServiceId().matcher(txtserviceid.getText()).matches()) {
                    TxtColours.setDefaultColours(txtserviceid);
                    if (RegExPatterns.getDoublePattern().matcher(txtPrice.getText()).matches()) {
                        TxtColours.setDefaultColours(txtPrice);

                        String id = txtserviceid.getText();
                        String description = txtDescription.getText();
                        double price = Double.parseDouble(txtPrice.getText());
                        String category = cmbCategory.getSelectionModel().getSelectedItem();

                        try {
                            boolean isSaved = ServiceModel.addService(new Service(id, description, price, category));
                            if (isSaved) {
                                new Alert(Alert.AlertType.CONFIRMATION, "Service Saved!").show();
                                clearTextFields();
                                populateServiceTable();
                                searchFilter();
                            } else {
                                new Alert(Alert.AlertType.WARNING, "Service not saved!").show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                        }
                    }else {
                        new Alert(Alert.AlertType.WARNING,"Please enter double value for price.").show();
                        TxtColours.setErrorColours(txtPrice);
                    }
                }else {
                    new Alert(Alert.AlertType.WARNING,"Invalid serviceId.").show();
                    TxtColours.setErrorColours(txtserviceid);
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Service Id already exists!").show();
                TxtColours.setErrorColours(txtserviceid);
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Please fill all details").show();
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
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {
                String id = txtserviceid.getText();
                try {
                    boolean isDeleted = ServiceModel.deleteService(id);
                    if (isDeleted) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Service has deleted!").show();
                        clearTextFields();
                        populateServiceTable();
                        searchFilter();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Service has not deleted").show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Please enter serviceId.").show();
        }
    }

    @FXML
    void updateBtnOnAction(ActionEvent event) {
        if (!(txtserviceid.getText().isEmpty() || txtDescription.getText().isEmpty() || txtPrice.getText().isEmpty() || cmbCategory.getSelectionModel().getSelectedItem() == null)) {
            if (RegExPatterns.getServiceId().matcher(txtserviceid.getText()).matches()) {
                TxtColours.setDefaultColours(txtserviceid);
                if (RegExPatterns.getDoublePattern().matcher(txtPrice.getText()).matches()) {
                    TxtColours.setDefaultColours(txtPrice);

                    String id = txtserviceid.getText();
                    String description = txtDescription.getText();
                    double price = Double.parseDouble(txtPrice.getText());
                    String category = cmbCategory.getSelectionModel().getSelectedItem();

                    try {
                        boolean isUpdated = ServiceModel.updateService(new Service(id, description, price, category));
                        if (isUpdated) {
                            new Alert(Alert.AlertType.CONFIRMATION, "Service has updated!").show();
                            clearTextFields();
                            populateServiceTable();
                            searchFilter();
                        } else {
                            new Alert(Alert.AlertType.WARNING, "Service has not updated!").show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                    }
                }else {
                    new Alert(Alert.AlertType.WARNING,"Please enter double value for price.").show();
                    TxtColours.setErrorColours(txtPrice);
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"Invalid serviceId.").show();
                TxtColours.setErrorColours(txtserviceid);
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Please fill all the details.").show();
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
            ResultSet rs = ServiceModel.getAll();
            data.clear();
            while (rs.next()){
                JFXButton button = new JFXButton("edit",new ImageView("F:\\1st semester final project\\moods salon\\src\\main\\resources\\img\\edit-97@30x.png"));
                button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                button.getStyleClass().add("infoBtn");
                setEditBtnOnAction(button);
                data.add(new ServiceTM(rs.getString(1),rs.getString(2),rs.getDouble(3),rs.getString(4),button));
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

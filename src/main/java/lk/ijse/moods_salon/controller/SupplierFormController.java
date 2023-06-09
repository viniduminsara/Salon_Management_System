package lk.ijse.moods_salon.controller;

import com.jfoenix.controls.JFXButton;
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
import lk.ijse.moods_salon.bo.custom.SupplierBO;
import lk.ijse.moods_salon.bo.factory.BOFactory;
import lk.ijse.moods_salon.bo.factory.BOTypes;
import lk.ijse.moods_salon.dto.SupplierDTO;
import lk.ijse.moods_salon.dto.tm.SupplierTM;
import lk.ijse.moods_salon.util.RegExPatterns;
import lk.ijse.moods_salon.util.SystemAlert;
import lk.ijse.moods_salon.util.TxtColours;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierFormController implements Initializable {

    @FXML
    private JFXButton searchCancelBtn;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtSupplierId;

    @FXML
    private TableView<SupplierTM> tblSupplier;

    @FXML
    private TableColumn<?, ?> colSupplierId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private Label lblError;

    ObservableList<SupplierTM> data = FXCollections.observableArrayList();

    SupplierBO supplierBO = (SupplierBO) BOFactory.getBoFactory().getBO(BOTypes.SUPPLIER);

    @FXML
    void addSupplierBtnOnAction(ActionEvent event) {
        boolean isExists = false;
        try {
            isExists = supplierBO.existsSupplier(txtSupplierId.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!(txtSupplierId.getText().isEmpty() || txtName.getText().isEmpty() || txtContact.getText().isEmpty() || txtAddress.getText().isEmpty())) {
            if (RegExPatterns.getSupplierId().matcher(txtSupplierId.getText()).matches()) {
                TxtColours.setDefaultColours(txtSupplierId);
                TxtColours.setDefaultColours(txtName);
                if (RegExPatterns.getContactPattern().matcher(txtContact.getText()).matches()) {
                    TxtColours.setDefaultColours(txtContact);
                    TxtColours.setDefaultColours(txtAddress);
                    if (!isExists) {
                        TxtColours.setDefaultColours(txtSupplierId);
                        lblError.setText("");

                        String id = txtSupplierId.getText();
                        String name = txtName.getText();
                        String contact = txtContact.getText();
                        String address = txtAddress.getText();

                        try {
                            boolean isSaved = supplierBO.addSupplier(new SupplierDTO(id, name, contact, address));
                            if (isSaved) {
                                new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Supplier has saved!",ButtonType.OK).show();
                                clearTextfields();
                                populateSupplierTable();
                                searchFilter();
                            } else {
                                new SystemAlert(Alert.AlertType.WARNING,"Warning","Supplier not saved!",ButtonType.OK).show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
                        }
                    } else {
                            lblError.setText("Supplier Id is already exists");
                            TxtColours.setErrorColours(txtSupplierId);
                    }
                }else {
                    lblError.setText("Invalid contact number");
                    TxtColours.setErrorColours(txtContact);
                }
            }else {
                lblError.setText("Invalid supplierId");
                TxtColours.setErrorColours(txtSupplierId);
            }
        }else {
            lblError.setText("Please fill all the details.");
            if (txtSupplierId.getText().isEmpty()){
                TxtColours.setErrorColours(txtSupplierId);
            }
            if (txtName.getText().isEmpty()){
                TxtColours.setErrorColours(txtName);
            }
            if (txtContact.getText().isEmpty()){
                TxtColours.setErrorColours(txtContact);
            }
            if (txtAddress.getText().isEmpty()){
                TxtColours.setErrorColours(txtAddress);
            }
        }
    }

    private void clearTextfields() {
        txtSupplierId.setText("");
        txtName.setText("");
        txtContact.setText("");
        txtAddress.setText("");
    }

    @FXML
    void deleteBtnOnAction(ActionEvent event) {
        if (!txtSupplierId.getText().isEmpty()) {
            if (RegExPatterns.getSupplierId().matcher(txtSupplierId.getText()).matches()) {
                boolean isExists = false;
                try {
                    isExists = supplierBO.existsSupplier(txtSupplierId.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (isExists) {
                    ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> result = new SystemAlert(Alert.AlertType.INFORMATION, "Information", "Are tou sure to delete?", yes, no).showAndWait();
                    if (result.orElse(no) == yes) {
                        String id = txtSupplierId.getText();
                        TxtColours.setDefaultColours(txtSupplierId);
                        lblError.setText("");

                        try {
                            boolean isDeleted = supplierBO.deleteSupplier(id);
                            if (isDeleted) {
                                new SystemAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Supplier has deleted!", ButtonType.OK).show();
                                clearTextfields();
                                populateSupplierTable();
                                searchFilter();
                            } else {
                                new SystemAlert(Alert.AlertType.WARNING, "Warning", "Supplier not deleted!", ButtonType.OK).show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            new SystemAlert(Alert.AlertType.ERROR, "Error", "Something went wrong!", ButtonType.OK).show();
                        }
                    }
                }else {
                    lblError.setText("No Supplier Found");
                    TxtColours.setErrorColours(txtSupplierId);
                }
            }else {
                lblError.setText("Invalid Supplier Id");
                TxtColours.setErrorColours(txtSupplierId);
            }
        }else {
            lblError.setText("Please enter the supplierId.");
            TxtColours.setErrorColours(txtSupplierId);
        }
    }

    @FXML
    void updateSupplierBtnOnAction(ActionEvent event) {
        if (!(txtSupplierId.getText().isEmpty() || txtName.getText().isEmpty() || txtContact.getText().isEmpty() || txtAddress.getText().isEmpty())) {
            if (RegExPatterns.getSupplierId().matcher(txtSupplierId.getText()).matches()) {
                TxtColours.setDefaultColours(txtSupplierId);
                TxtColours.setDefaultColours(txtName);
                if (RegExPatterns.getContactPattern().matcher(txtContact.getText()).matches()) {
                    TxtColours.setDefaultColours(txtContact);
                    TxtColours.setDefaultColours(txtAddress);
                    lblError.setText("");

                    String id = txtSupplierId.getText();
                    String name = txtName.getText();
                    String contact = txtContact.getText();
                    String address = txtAddress.getText();

                    try {
                        boolean isUpdated = supplierBO.updateSupplier(new SupplierDTO(id, name, contact, address));
                        if (isUpdated) {
                            new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Supplier has updated!",ButtonType.OK).show();
                            clearTextfields();
                            populateSupplierTable();
                            searchFilter();
                        } else {
                            new SystemAlert(Alert.AlertType.WARNING,"Warning","Supplier not updated!",ButtonType.OK).show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
                    }
                }else {
                    lblError.setText("Invalid contact number");
                    TxtColours.setErrorColours(txtContact);
                }
            }else {
                lblError.setText("Invalid supplierId");
                TxtColours.setErrorColours(txtSupplierId);
            }
        }else {
            lblError.setText("Please fill all the details.");
            if (txtSupplierId.getText().isEmpty()){
                TxtColours.setErrorColours(txtSupplierId);
            }
            if (txtName.getText().isEmpty()){
                TxtColours.setErrorColours(txtName);
            }
            if (txtContact.getText().isEmpty()){
                TxtColours.setErrorColours(txtContact);
            }
            if (txtAddress.getText().isEmpty()){
                TxtColours.setErrorColours(txtAddress);
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
        setCellValues();
        populateSupplierTable();
        searchFilter();
    }

    private void searchFilter() {
        FilteredList<SupplierTM> filteredData = new FilteredList<>(data, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(SupplierTM -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    searchCancelBtn.setVisible(false);
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                if (SupplierTM.getSupplierId().toLowerCase().contains(searchKeyword)){
                    searchCancelBtn.setVisible(true);
                    return true;
                }else if(SupplierTM.getName().toLowerCase().contains(searchKeyword)){
                    searchCancelBtn.setVisible(true);
                    return true;
                }else
                    searchCancelBtn.setVisible(true);
                return false;

            });
        });

        SortedList<SupplierTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblSupplier.comparatorProperty());
        tblSupplier.setItems(sortedData);
    }

    private void setCellValues() {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("button"));
    }

    private void populateSupplierTable() {
        try {
            ArrayList<SupplierDTO> allSuppliers = supplierBO.getAllSuppliers();
            data.clear();
            for (SupplierDTO dto : allSuppliers){
                JFXButton button = new JFXButton("edit",new ImageView("img/edit-97@30x.png"));
                button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                button.getStyleClass().add("infoBtn");
                setEditBtnOnAction(button);
                data.add(new SupplierTM(dto.getSupplierId(),dto.getName(),dto.getContact(),dto.getAddress(),button));
            }
            if (!data.isEmpty()){
                tblSupplier.setItems(data);
            }else{
                new Alert(Alert.AlertType.WARNING,"No suppliers found!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }

    private void setEditBtnOnAction(JFXButton button) {
        button.setOnAction((e) -> {
            int index = -1;
            for (int i=0; i<tblSupplier.getItems().size(); i++){
                if (colAction.getCellData(i).equals(button)){
                    index = i;
                }
            }
            SupplierTM supplier = tblSupplier.getItems().get(index);
            txtSupplierId.setText(supplier.getSupplierId());
            txtName.setText(supplier.getName());
            txtContact.setText(supplier.getContact());
            txtAddress.setText(supplier.getAddress());
        });
    }
}

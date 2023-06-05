package lk.ijse.moods_salon.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
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
import lk.ijse.moods_salon.bo.custom.InventoryBO;
import lk.ijse.moods_salon.bo.custom.impl.InventoryBOImpl;
import lk.ijse.moods_salon.db.DBConnection;
import lk.ijse.moods_salon.dto.InventoryDTO;
import lk.ijse.moods_salon.dto.InventoryOrderDTO;
import lk.ijse.moods_salon.dto.InventoryOrderDetailDTO;
import lk.ijse.moods_salon.dto.UserDTO;
import lk.ijse.moods_salon.dto.tm.InventoryOrderTM;
import lk.ijse.moods_salon.dto.tm.InventoryTM;
import lk.ijse.moods_salon.dto.tm.OrderCartTM;
import lk.ijse.moods_salon.util.RegExPatterns;
import lk.ijse.moods_salon.util.SystemAlert;
import lk.ijse.moods_salon.util.TxtColours;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;


public class InventoryFormController implements Initializable {

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXButton searchCancelBtn;

    @FXML
    private JFXTextField txtQtyOnHand;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    private JFXTextField txtInventory;

    @FXML
    private TableView<InventoryTM> tblinventory;

    @FXML
    private TableColumn<?, ?> colInventoryId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colQtyOnHand;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private Label lblSupplierName;

    @FXML
    private JFXComboBox<String> cmbInventoryId;

    @FXML
    private Label lblInventoryName;

    @FXML
    private Label lblQtyOnHand;

    @FXML
    private Label lblUnitPrice;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXDatePicker OrderDate;

    @FXML
    private Label lblOrderId;

    @FXML
    private TableView<OrderCartTM> tblCart;

    @FXML
    private TableColumn<?, ?> colSupplierId;

    @FXML
    private TableColumn<?, ?> colInventoryCartId;

    @FXML
    private TableColumn<?, ?> colInventoryName;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colRemove;

    @FXML
    private JFXComboBox<String> cmbSupplierId;

    private UserDTO user;

    @FXML
    private TableView<InventoryOrderTM> tblOrders;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colSupplier;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colUser;

    @FXML
    private JFXTextField txtOrderSearch;

    @FXML
    private JFXButton cancelSearchBtn;

    @FXML
    private Label lblError;

    ObservableList<InventoryTM> data = FXCollections.observableArrayList();

    ObservableList<OrderCartTM> cart = FXCollections.observableArrayList();

    ObservableList<InventoryOrderTM> orders = FXCollections.observableArrayList();

    InventoryBO inventoryBO = new InventoryBOImpl();

    @FXML
    void addBtnOnAction(ActionEvent event) {
        boolean isExists = false;
        try {
            isExists = inventoryBO.existsInventory(txtInventory.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!(txtInventory.getText().isEmpty() || txtName.getText().isEmpty() || txtQtyOnHand.getText().isEmpty() || txtUnitPrice.getText().isEmpty())) {
            if (RegExPatterns.getInventoryId().matcher(txtInventory.getText()).matches()) {
                TxtColours.setDefaultColours(txtInventory);
                TxtColours.setDefaultColours(txtName);
                if (RegExPatterns.getIntPattern().matcher(txtQtyOnHand.getText()).matches()) {
                    TxtColours.setDefaultColours(txtQtyOnHand);
                    if (RegExPatterns.getDoublePattern().matcher(txtUnitPrice.getText()).matches()) {
                        TxtColours.setDefaultColours(txtUnitPrice);
                        if (!isExists) {
                            TxtColours.setDefaultColours(txtInventory);
                            lblError.setText("");

                            String id = txtInventory.getText();
                            String name = txtName.getText();
                            Integer qtyOnHand = Integer.valueOf(txtQtyOnHand.getText());
                            Double unitPrice = Double.valueOf(txtUnitPrice.getText());

                            try {
                                boolean isSaved = inventoryBO.addInventory(new InventoryDTO(id, name, qtyOnHand, unitPrice));
                                if (isSaved) {
                                    new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Inventory item saved!",ButtonType.OK).show();
                                    populateInventoryTable();
                                    searchFilter();
                                    clearTextFields();
                                } else {
                                    new SystemAlert(Alert.AlertType.WARNING,"Warning","Inventory item not saved!",ButtonType.OK).show();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
                            }
                        }else{
                            lblError.setText("Inventory Id already exists!");
                            TxtColours.setErrorColours(txtInventory);
                        }
                    }else {
                        lblError.setText("Please enter double value for unit price");
                        TxtColours.setErrorColours(txtUnitPrice);
                    }
                }else {
                    lblError.setText("Please enter integer value for qtyOnHand.");
                    TxtColours.setErrorColours(txtQtyOnHand);
                }
            }else {
                lblError.setText("Invalid inventoryId");
                TxtColours.setErrorColours(txtInventory);
            }
        }else {
            lblError.setText("Please fill all details.");
            if (txtInventory.getText().isEmpty()){
                TxtColours.setErrorColours(txtInventory);
            }
            if (txtName.getText().isEmpty()){
                TxtColours.setErrorColours(txtName);
            }
            if (txtQtyOnHand.getText().isEmpty()){
                TxtColours.setErrorColours(txtQtyOnHand);
            }
            if (txtUnitPrice.getText().isEmpty()){
                TxtColours.setErrorColours(txtUnitPrice);
            }
        }
    }

    private void clearTextFields() {
        txtInventory.setText("");
        txtName.setText("");
        txtQtyOnHand.setText("");
        txtUnitPrice.setText("");
    }

    @FXML
    void deleteBtnOnAction(ActionEvent event) {
        if (!txtInventory.getText().isEmpty()) {
            if (RegExPatterns.getInventoryId().matcher(txtInventory.getText()).matches()) {
                boolean isExists = false;
                try {
                    isExists = inventoryBO.existsInventory(txtInventory.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if(isExists) {
                    TxtColours.setDefaultColours(txtInventory);
                    ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> result = new SystemAlert(Alert.AlertType.INFORMATION, "Information", "Are you sure to delete ?", yes, no).showAndWait();

                    if (result.orElse(no) == yes) {
                        String id = txtInventory.getText();
                        lblError.setText("");

                        try {
                            boolean isDeleted = inventoryBO.deleteInventory(id);
                            if (isDeleted) {
                                new SystemAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Inventory item has deleted!", ButtonType.OK).show();
                                populateInventoryTable();
                                searchFilter();
                                clearTextFields();
                            } else {
                                new SystemAlert(Alert.AlertType.WARNING, "Warning", "Inventory item not deleted!", ButtonType.OK).show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            new SystemAlert(Alert.AlertType.ERROR, "Error", "Something went wrong!", ButtonType.OK).show();
                        }
                    }
                }else {
                    lblError.setText("No inventory Found");
                    TxtColours.setErrorColours(txtInventory);
                }
            }else {
                lblError.setText("Invalid inventory Id");
                TxtColours.setErrorColours(txtInventory);
            }
        }else {
            lblError.setText("Please enter the inventoryId.");
            TxtColours.setErrorColours(txtInventory);
        }
    }

    @FXML
    void searchCancelBtnOnAction(ActionEvent event) {
        txtSearch.setText("");
        searchCancelBtn.setVisible(false);
    }

    @FXML
    void updateBtnOnAction(ActionEvent event) {
        if (!(txtInventory.getText().isEmpty() || txtName.getText().isEmpty() || txtQtyOnHand.getText().isEmpty() || txtUnitPrice.getText().isEmpty())) {
            if (RegExPatterns.getInventoryId().matcher(txtInventory.getText()).matches()) {
                TxtColours.setDefaultColours(txtInventory);
                TxtColours.setDefaultColours(txtName);
                if (RegExPatterns.getIntPattern().matcher(txtQtyOnHand.getText()).matches()) {
                    TxtColours.setDefaultColours(txtQtyOnHand);
                    if (RegExPatterns.getDoublePattern().matcher(txtUnitPrice.getText()).matches()) {
                        TxtColours.setDefaultColours(txtUnitPrice);
                        lblError.setText("");

                        String id = txtInventory.getText();
                        String name = txtName.getText();
                        Integer qtyOnHand = Integer.valueOf(txtQtyOnHand.getText());
                        Double unitPrice = Double.valueOf(txtUnitPrice.getText());

                        try {
                            boolean isUpdated = inventoryBO.updateInventory(new InventoryDTO(id, name, qtyOnHand, unitPrice));
                            if (isUpdated) {
                                new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Inventory item has updated!",ButtonType.OK).show();
                                clearTextFields();
                                populateInventoryTable();
                                searchFilter();
                            } else {
                                new SystemAlert(Alert.AlertType.WARNING,"Warning","Inventory item not updated!",ButtonType.OK).show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
                        }
                    }else {
                        lblError.setText("Please enter double value for unit price");
                        TxtColours.setErrorColours(txtUnitPrice);
                    }
                }else {
                    lblError.setText("Please enter integer value for qtyOnHand.");
                    TxtColours.setErrorColours(txtQtyOnHand);
                }
            }else {
                lblError.setText("Invalid inventoryId");
                TxtColours.setErrorColours(txtInventory);
            }
        }else {
            lblError.setText("Please fill all details.");
            if (txtInventory.getText().isEmpty()){
                TxtColours.setErrorColours(txtInventory);
            }
            if (txtName.getText().isEmpty()){
                TxtColours.setErrorColours(txtName);
            }
            if (txtQtyOnHand.getText().isEmpty()){
                TxtColours.setErrorColours(txtQtyOnHand);
            }
            if (txtUnitPrice.getText().isEmpty()){
                TxtColours.setErrorColours(txtUnitPrice);
            }
        }
    }

    @FXML
    void addToCartBtnOnAction(ActionEvent event) {
        if (!(cmbSupplierId.getSelectionModel().getSelectedItem() == null || cmbInventoryId.getSelectionModel().getSelectedItem() == null || txtQty.getText().isEmpty())) {
            if (RegExPatterns.getIntPattern().matcher(txtQty.getText()).matches()) {
                String supplierId = cmbSupplierId.getSelectionModel().getSelectedItem();
                String inventoryId = cmbInventoryId.getSelectionModel().getSelectedItem();
                String name = lblInventoryName.getText();
                double unitPrice = Double.parseDouble(lblUnitPrice.getText());
                int qty = Integer.parseInt(txtQty.getText());
                JFXButton button = new JFXButton("delete", new ImageView("img/trash-can@1.5x.png"));
                button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                button.getStyleClass().add("infoBtn");
                setRemoveBtnOnAction(button);

                if (!cart.isEmpty()) {
                    for (int i = 0; i < tblCart.getItems().size(); i++) {
                        if (colInventoryCartId.getCellData(i).equals(inventoryId)) {
                            qty += (int) colQty.getCellData(i);
                            cart.get(i).setQty(qty);
                            tblCart.refresh();
                            return;
                        }
                    }
                }
                cart.add(new OrderCartTM(supplierId, inventoryId, name, unitPrice, qty, button));
                tblCart.setItems(cart);
                cmbInventoryId.setValue(null);
                lblInventoryName.setText("");
                lblUnitPrice.setText("");
                lblQtyOnHand.setText("");
                txtQty.setText("");
            }else {
                new SystemAlert(Alert.AlertType.WARNING,"Warning","Please enter integer value for qty.",ButtonType.OK).show();
            }
        }else {
            new SystemAlert(Alert.AlertType.WARNING,"Warning","Fill all requirements",ButtonType.OK).show();
        }
    }

    @FXML
    void cmbInventoryIdOnAction(ActionEvent event) {
        String id = cmbInventoryId.getSelectionModel().getSelectedItem();
        try {
            InventoryDTO item = inventoryBO.getInventory(id);
            if (item != null){
                lblInventoryName.setText(item.getName());
                lblQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
                lblUnitPrice.setText(String.valueOf(item.getUnitPrice()));
                txtQty.requestFocus();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    @FXML
    void cmbSupplierIdOnAction(ActionEvent event) {
        String supplier = cmbSupplierId.getSelectionModel().getSelectedItem();
        try {
            String name = inventoryBO.getSupplierName(supplier);
            if (name != null){
                lblSupplierName.setText(name);
                cmbSupplierId.setDisable(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    @FXML
    void txtQtyOnAction(ActionEvent event) {
        addToCartBtnOnAction(event);
    }

    @FXML
    void placeOrderBtnOnAction(ActionEvent event) {
        if (!(cmbSupplierId.getSelectionModel().getSelectedItem() == null || OrderDate.getValue() == null)) {
            if (tblCart.getItems().size() != 0) {
                String orderId = lblOrderId.getText();
                ArrayList<InventoryOrderDetailDTO> items = new ArrayList<>();
                for (OrderCartTM cartItem : cart) {
                    items.add(new InventoryOrderDetailDTO(orderId,cartItem.getInventoryId(),cartItem.getQty()));
                }
                Date date = Date.from(OrderDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                String supplierId = cmbSupplierId.getSelectionModel().getSelectedItem();
                String userId = user.getUserId();
                try {
                    boolean isPlaced = inventoryBO.placeInventoryOrder(new InventoryOrderDTO(orderId, date, supplierId, userId), items);
                    if (isPlaced) {
                        new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Inventoy order placed!",ButtonType.OK).show();
                        clearComponents();
                        cart.clear();
                        generateOrderId();
                        populateOrderTable();
                        cmbSupplierId.setDisable(false);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"Add items for the order!").show();
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Fill all the required fields!").show();
        }
    }

    @FXML
    void cancelSearchBtnOnAction(ActionEvent event) {
        txtOrderSearch.setText("");
        cancelSearchBtn.setVisible(false);
    }

    @FXML
    void reportBtnOnction(ActionEvent event) {
        new Thread(() -> {
            try {
                JasperDesign design = JRXmlLoader.load(new File("src/main/java/lk/ijse/moods_salon/report/inventory.jrxml"));
                JasperReport report = JasperCompileManager.compileReport(design);
                JasperPrint jasperPrint = JasperFillManager.fillReport(report, null, DBConnection.getInstance().getConnection());
                //            JasperPrintManager.printReport(jasperPrint, true);
                JasperViewer.viewReport(jasperPrint,false);

            } catch (JRException | SQLException e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setValueFactory();
        populateInventoryTable();
        searchFilter();
        setSupplierId();
        setInventoryId();
        generateOrderId();
        populateOrderTable();
        orderSearchFilter();
    }

    private void orderSearchFilter() {
        FilteredList<InventoryOrderTM> filteredData = new FilteredList<>(orders, b -> true);
        txtOrderSearch.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(InventoryOrderTM -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    cancelSearchBtn.setVisible(false);
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                if (InventoryOrderTM.getOrderId().toLowerCase().contains(searchKeyword)){
                    cancelSearchBtn.setVisible(true);
                    return true;
                }else if(InventoryOrderTM.getSupplier().toLowerCase().contains(searchKeyword)){
                    cancelSearchBtn.setVisible(true);
                    return true;
                }else if (InventoryOrderTM.getUser().toLowerCase().contains(searchKeyword)){
                    cancelSearchBtn.setVisible(true);
                    return true;
                } else
                    cancelSearchBtn.setVisible(true);
                return false;

            });
        });

        SortedList<InventoryOrderTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblOrders.comparatorProperty());
        tblOrders.setItems(sortedData);
    }

    private void populateOrderTable() {
        try {
            orders = inventoryBO.getAllOrders();
            tblOrders.setItems(orders);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setUser(UserDTO user){
        this.user = user;
    }

    private void generateOrderId() {
        try {
            String id = inventoryBO.generateOrderId();
            lblOrderId.setText(id);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void setRemoveBtnOnAction(JFXButton button) {
        button.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes",ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No",ButtonBar.ButtonData.CANCEL_CLOSE);
            int index = -1;
            for (int i=0; i<tblCart.getItems().size(); i++){
                if (colRemove.getCellData(i).equals(button)){
                    index = i;
                }
            }

            Optional<ButtonType> result = new SystemAlert(Alert.AlertType.INFORMATION,"Information","Do you want remove '"+colInventoryName.getCellData(index)+"' item ?",yes,no).showAndWait();
            if (result.orElse(no) == yes) {
                tblCart.getItems().remove(index);
                tblCart.refresh();
            }
        });
    }

    private void clearComponents() {
        cmbInventoryId.setValue(null);
        cmbSupplierId.setValue(null);
        lblSupplierName.setText("");
        lblInventoryName.setText("");
        lblUnitPrice.setText("");
        lblQtyOnHand.setText("");
        txtQty.setText("");
        OrderDate.setValue(null);
    }

    private void setInventoryId() {
        try {
            ObservableList<String> inventories = inventoryBO.getInventoryIds();
            cmbInventoryId.setItems(inventories);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void setSupplierId() {
        try {
            ObservableList<String> suppliers = inventoryBO.getSupplierIds();
            cmbSupplierId.setItems(suppliers);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void setValueFactory() {
        //Inventory details table
        colInventoryId.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("button"));

        //order cart
        colInventoryCartId.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colInventoryName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colRemove.setCellValueFactory(new PropertyValueFactory<>("button"));

        //order details table
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("user"));
    }

    private void searchFilter() {
        FilteredList<InventoryTM> filteredData = new FilteredList<>(data, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(InventoryTM -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    searchCancelBtn.setVisible(false);
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                if (InventoryTM.getInventoryId().toLowerCase().contains(searchKeyword)){
                    searchCancelBtn.setVisible(true);
                    return true;
                }else if(InventoryTM.getName().toLowerCase().contains(searchKeyword)){
                    searchCancelBtn.setVisible(true);
                    return true;
                }else
                    searchCancelBtn.setVisible(true);
                return false;

            });
        });

        SortedList<InventoryTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblinventory.comparatorProperty());
        tblinventory.setItems(sortedData);
    }

    private void populateInventoryTable() {
        try {
            ArrayList<InventoryDTO> allInventory = inventoryBO.getAllInventory();
            data.clear();
            for (InventoryDTO item : allInventory){
                JFXButton button = new JFXButton("edit",new ImageView("img/edit-97@30x.png"));
                button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                button.getStyleClass().add("infoBtn");
                setEditBtnOnAction(button);
                data.add(new InventoryTM(item.getInventoryId(),item.getName(),item.getQtyOnHand(),item.getUnitPrice(),button));
            }
            tblinventory.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void setEditBtnOnAction(JFXButton button) {
        button.setOnAction((e) -> {
            int index = -1;
            for (int i=0; i<tblinventory.getItems().size(); i++){
                if (colAction.getCellData(i).equals(button)){
                    index = i;
                }
            }
            InventoryTM item = tblinventory.getItems().get(index);
            txtInventory.setText(item.getInventoryId());
            txtName.setText(item.getName());
            txtQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
            txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
        });
    }
}

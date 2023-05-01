package lk.ijse.gdse.controller;

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
import lk.ijse.gdse.db.DBConnection;
import lk.ijse.gdse.dto.Inventory;
import lk.ijse.gdse.dto.OrderItemDetail;
import lk.ijse.gdse.dto.User;
import lk.ijse.gdse.dto.tm.InventoryOrderTM;
import lk.ijse.gdse.dto.tm.InventoryTM;
import lk.ijse.gdse.dto.tm.OrderCartTM;
import lk.ijse.gdse.model.InventoryModel;
import lk.ijse.gdse.model.InventoryOrderModel;
import lk.ijse.gdse.model.PlaceInventoryOrderModel;
import lk.ijse.gdse.model.SupplierModel;
import lk.ijse.gdse.util.RegExPatterns;
import lk.ijse.gdse.util.TxtColours;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
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

    private User user;

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

    ObservableList<InventoryTM> data = FXCollections.observableArrayList();

    ObservableList<OrderCartTM> cart = FXCollections.observableArrayList();

    ObservableList<InventoryOrderTM> orders = FXCollections.observableArrayList();


    @FXML
    void addBtnOnAction(ActionEvent event) {
        boolean isExists = false;
        try {
            isExists = InventoryModel.isExists(txtInventory.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!isExists) {
            TxtColours.setDefaultColours(txtInventory);
            if (!(txtInventory.getText().isEmpty() || txtName.getText().isEmpty() || txtQtyOnHand.getText().isEmpty() || txtUnitPrice.getText().isEmpty())) {
                if (RegExPatterns.getInventoryId().matcher(txtInventory.getText()).matches()) {
                    TxtColours.setDefaultColours(txtInventory);
                    if (RegExPatterns.getIntPattern().matcher(txtQtyOnHand.getText()).matches()) {
                        TxtColours.setDefaultColours(txtQtyOnHand);
                        if (RegExPatterns.getDoublePattern().matcher(txtUnitPrice.getText()).matches()) {
                            TxtColours.setDefaultColours(txtUnitPrice);

                            String id = txtInventory.getText();
                            String name = txtName.getText();
                            Integer qtyOnHand = Integer.valueOf(txtQtyOnHand.getText());
                            Double unitPrice = Double.valueOf(txtUnitPrice.getText());

                            try {
                                boolean isSaved = InventoryModel.addInventory(new Inventory(id, name, qtyOnHand, unitPrice));
                                if (isSaved) {
                                    new Alert(Alert.AlertType.CONFIRMATION, "Inventory item saved!").show();
                                    populateInventoryTable();
                                    searchFilter();
                                    clearTextFields();
                                } else {
                                    new Alert(Alert.AlertType.WARNING, "Inventory item not saved!").show();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                            }
                        }else {
                            new Alert(Alert.AlertType.WARNING,"Please enter double value for unit price").show();
                            TxtColours.setErrorColours(txtUnitPrice);
                        }
                    }else {
                        new Alert(Alert.AlertType.WARNING,"Please enter integer value for qtyOnHand.").show();
                        TxtColours.setErrorColours(txtQtyOnHand);
                    }
                }else {
                    new Alert(Alert.AlertType.WARNING,"Invalid inventoryId").show();
                    TxtColours.setErrorColours(txtInventory);
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"Please fill all details.").show();
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Inventory Id already exists!").show();
            TxtColours.setErrorColours(txtInventory);
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
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION,"Are you sure to delete ?",yes,no).showAndWait();

        if (result.orElse(no) == yes) {
                String id = txtInventory.getText();
                try {
                    boolean isDeleted = InventoryModel.deleteInventory(id);
                    if (isDeleted) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Inventory item has deleted!").show();
                        populateInventoryTable();
                        searchFilter();
                        clearTextFields();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Inventory item not deleted!").show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Please enter the inventoryId.").show();
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
                if (RegExPatterns.getIntPattern().matcher(txtQtyOnHand.getText()).matches()) {
                    TxtColours.setDefaultColours(txtQtyOnHand);
                    if (RegExPatterns.getDoublePattern().matcher(txtUnitPrice.getText()).matches()) {
                        TxtColours.setDefaultColours(txtUnitPrice);

                        String id = txtInventory.getText();
                        String name = txtName.getText();
                        Integer qtyOnHand = Integer.valueOf(txtQtyOnHand.getText());
                        Double unitPrice = Double.valueOf(txtUnitPrice.getText());

                        try {
                            boolean isUpdated = InventoryModel.updateInventory(new Inventory(id, name, qtyOnHand, unitPrice));
                            if (isUpdated) {
                                new Alert(Alert.AlertType.CONFIRMATION, "Inventory item has updated!").show();
                                clearTextFields();
                                populateInventoryTable();
                                searchFilter();
                            } else {
                                new Alert(Alert.AlertType.WARNING, "Inventory item has not updated!").show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                        }
                    }else {
                        new Alert(Alert.AlertType.WARNING,"Please enter double value for unit price").show();
                        TxtColours.setErrorColours(txtUnitPrice);
                    }
                }else {
                    new Alert(Alert.AlertType.WARNING,"Please enter integer value for qtyOnHand.").show();
                    TxtColours.setErrorColours(txtQtyOnHand);
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"Invalid inventoryId").show();
                TxtColours.setErrorColours(txtInventory);
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Please fill all the details.").show();
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
                JFXButton button = new JFXButton("delete", new ImageView("F:\\1st semester final project\\moods salon\\src\\main\\resources\\img\\trash-can@1.5x.png"));
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
                new Alert(Alert.AlertType.WARNING,"Please enter integer value for qty.").show();
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Fill all requirements").show();
        }
    }

    @FXML
    void cmbInventoryIdOnAction(ActionEvent event) {
        String id = cmbInventoryId.getSelectionModel().getSelectedItem();
        try {
            Inventory item = InventoryModel.getInventoryDetails(id);
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
            String name = SupplierModel.getSupplierName(supplier);
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
                ArrayList<OrderItemDetail> items = new ArrayList<>();
                for (OrderCartTM cartItem : cart) {
                    items.add(new OrderItemDetail(cartItem.getInventoryId(), cartItem.getQty()));
                }
                String orderId = lblOrderId.getText();
                Date date = Date.from(OrderDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                String supplierId = cmbSupplierId.getSelectionModel().getSelectedItem();
                String userId = user.getUserId();
                try {
                    boolean isPlaced = PlaceInventoryOrderModel.placeOrder(orderId, date, supplierId, userId, items);
                    if (isPlaced) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Inventory order placed!").show();
                        clearComponents();
                        cart.clear();
                        generateOrderId();
                        populateOrderTable();
                        cmbSupplierId.setDisable(false);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
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
        try {
            JasperDesign design = JRXmlLoader.load(new File("F:\\1st semester final project\\moods salon\\src\\main\\java\\lk\\ijse\\gdse\\report\\inventory.jrxml"));
            JasperReport report = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, null, DBConnection.getInstance().getConnection());
//            JasperPrintManager.printReport(jasperPrint, true);
            JasperViewer.viewReport(jasperPrint,false);

        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
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
            orders = InventoryOrderModel.getAll();
            tblOrders.setItems(orders);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user){
        this.user = user;
    }

    private void generateOrderId() {
        try {
            String id = InventoryOrderModel.generateId();
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

            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION,"Do you want remove '"+colInventoryName.getCellData(index)+"' item from cart?",yes,no).showAndWait();
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
            ObservableList<String> inventories = InventoryModel.getInventoryIds();
            cmbInventoryId.setItems(inventories);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void setSupplierId() {
        try {
            ObservableList<String> suppliers = SupplierModel.getSupplierIds();
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
            ResultSet rs = InventoryModel.getAllInventory();
            data.clear();
            while (rs.next()){
                JFXButton button = new JFXButton("edit",new ImageView("F:\\1st semester final project\\moods salon\\src\\main\\resources\\img\\edit-97@30x.png"));
                button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                button.getStyleClass().add("infoBtn");
                setEditBtnOnAction(button);
                data.add(new InventoryTM(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getDouble(4),button));
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

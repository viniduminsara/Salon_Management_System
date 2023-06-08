package lk.ijse.moods_salon.bo.custom;

import javafx.collections.ObservableList;
import lk.ijse.moods_salon.bo.SuperBO;
import lk.ijse.moods_salon.dto.InventoryDTO;
import lk.ijse.moods_salon.dto.InventoryOrderDTO;
import lk.ijse.moods_salon.dto.InventoryOrderDetailDTO;
import lk.ijse.moods_salon.dto.tm.InventoryOrderTM;

import java.sql.SQLException;
import java.util.ArrayList;

public interface InventoryBO extends SuperBO {

    boolean existsInventory(String id) throws SQLException;

    boolean addInventory(InventoryDTO dto) throws SQLException;

    boolean deleteInventory(String id) throws SQLException;

    boolean updateInventory(InventoryDTO dto) throws SQLException;

    InventoryDTO getInventory(String id) throws SQLException;

    String getSupplierName(String id) throws SQLException;

    ObservableList<InventoryOrderTM> getAllOrders() throws SQLException;

    String generateOrderId() throws SQLException;

    ObservableList<String> getInventoryIds() throws SQLException;

    ObservableList<String> getSupplierIds() throws SQLException;

    ArrayList<InventoryDTO> getAllInventory() throws SQLException;

    boolean placeInventoryOrder(InventoryOrderDTO dto, ArrayList<InventoryOrderDetailDTO> items) throws SQLException;

}

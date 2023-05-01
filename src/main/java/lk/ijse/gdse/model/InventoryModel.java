package lk.ijse.gdse.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.gdse.dto.AppointmentInventory;
import lk.ijse.gdse.dto.Inventory;
import lk.ijse.gdse.dto.OrderItemDetail;
import lk.ijse.gdse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryModel {

    public static ResultSet getAllInventory() throws SQLException {
        String query = "SELECT inventoryId,name,qtyOnHand,unitPrice FROM inventory";
        return CrudUtil.execute(query);
    }

    public static ResultSet searhinvrntory(String name) throws SQLException {
        String query = "SELECT inventoryId,name,qtyOnHand,unitPrice FROM inventory WHERE name = ?";
        return CrudUtil.execute(query,name);
    }

    public static boolean addInventory(Inventory inventory) throws SQLException {
        String query = "INSERT INTO inventory VALUES (?,?,?,?)";
        return CrudUtil.execute(query,inventory.getInventoryId(),inventory.getName(),inventory.getQtyOnHand(),inventory.getUnitPrice());
    }

    public static boolean deleteInventory(String id) throws SQLException {
        String query = "DELETE FROM inventory WHERE inventoryId = ?";
        return CrudUtil.execute(query,id);
    }

    public static boolean updateInventory(Inventory inventory) throws SQLException {
        String query = "UPDATE inventory SET name = ?,qtyOnHand = ?,unitPrice = ? WHERE inventoryId = ?";
        return CrudUtil.execute(query,inventory.getName(),inventory.getQtyOnHand(),inventory.getUnitPrice(),inventory.getInventoryId());
    }

    public static ObservableList<String> getInventoryNames() throws SQLException {
        String query = "SELECT name FROM inventory ORDER BY inventoryId";
        ResultSet rs = CrudUtil.execute(query);
        ObservableList<String> inventory = FXCollections.observableArrayList();
        while (rs.next()){
            inventory.add(rs.getString(1));
        }
        return inventory;
    }

    public static String getInventoryId(String name) throws SQLException {
        String query = "SELECT inventoryId FROM inventory WHERE name = ?";
        ResultSet rs = CrudUtil.execute(query,name);
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    public static boolean updateQty(List<AppointmentInventory> inventoryList) throws SQLException {
        for (AppointmentInventory item : inventoryList) {
            if (!update(item)){
                return false;
            }
        }
        return true;
    }

    private static boolean update(AppointmentInventory item) throws SQLException {
        String query = "UPDATE inventory SET qtyOnHand = (qtyOnHand - ?) WHERE inventoryId = ?";
        return CrudUtil.execute(query,item.getUsedQty(),item.getInventoryId());
    }

    public static boolean isExists(String id) throws SQLException {
        String query = "SELECT inventoryId FROM inventory WHERE inventoryId = ?";
        ResultSet rs = CrudUtil.execute(query,id);
        if (rs.next()){
            return true;
        }
        return false;
    }

    public static ObservableList<String> getInventoryIds() throws SQLException {
        String query = "SELECT inventoryId FROM inventory ORDER BY inventoryId";
        ResultSet rs = CrudUtil.execute(query);
        ObservableList<String> inventories = FXCollections.observableArrayList();
        while (rs.next()){
            inventories.add(rs.getString(1));
        }
        return inventories;
    }

    public static Inventory getInventoryDetails(String id) throws SQLException {
        String query = "SELECT * FROM inventory WHERE inventoryId = ?";
        ResultSet rs = CrudUtil.execute(query,id);
        if (rs.next()){
            return new Inventory(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getDouble(4));
        }
        return null;
    }

    public static boolean updateOrderQty(ArrayList<OrderItemDetail> items) throws SQLException {
        for (OrderItemDetail inventory : items) {
            if (!updateQty(inventory)){
                return false;
            }
        }
        return true;
    }

    private static boolean updateQty(OrderItemDetail inventory) throws SQLException {
        String query = "UPDATE inventory SET qtyOnHand = (qtyOnHand + ?) WHERE inventoryId = ?";
        return CrudUtil.execute(query,inventory.getQty(),inventory.getInventoryId());
    }
}

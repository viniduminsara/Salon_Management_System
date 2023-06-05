package lk.ijse.moods_salon.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.InventoryDAO;
import lk.ijse.moods_salon.entity.Inventory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryDAOImpl implements InventoryDAO {
    @Override
    public boolean add(Inventory entity) throws SQLException {
        String query = "INSERT INTO inventory VALUES (?,?,?,?)";
        return SQLUtil.execute(query,entity.getInventoryId(),entity.getName(),entity.getQtyOnHand(),entity.getUnitPrice());
    }

    @Override
    public boolean delete(String s) throws SQLException {
        String query = "DELETE FROM inventory WHERE inventoryId = ?";
        return SQLUtil.execute(query,s);
    }

    @Override
    public boolean update(Inventory entity) throws SQLException {
        String query = "UPDATE inventory SET name = ?,qtyOnHand = ?,unitPrice = ? WHERE inventoryId = ?";
        return SQLUtil.execute(query,entity.getName(),entity.getQtyOnHand(),entity.getUnitPrice(),entity.getInventoryId());
    }

    @Override
    public boolean exists(String s) throws SQLException {
        String query = "SELECT inventoryId FROM inventory WHERE inventoryId = ?";
        ResultSet rs = SQLUtil.execute(query,s);
        return rs.next();
    }

    @Override
    public ArrayList<Inventory> getAll() throws SQLException {
        String query = "SELECT inventoryId,name,qtyOnHand,unitPrice FROM inventory";
        ResultSet rs= SQLUtil.execute(query);
        ArrayList<Inventory> inventories = new ArrayList<>();
        while (rs.next()){
            inventories.add(new Inventory(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getDouble(4)));
        }
        return inventories;
    }

    @Override
    public Inventory get(String id) throws SQLException {
        String query = "SELECT * FROM inventory WHERE inventoryId = ?";
        ResultSet rs = SQLUtil.execute(query,id);
        if (rs.next()){
            return new Inventory(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getDouble(4));
        }
        return null;
    }

    @Override
    public ObservableList<String> getIds() throws SQLException {
        String query = "SELECT inventoryId FROM inventory ORDER BY inventoryId";
        ResultSet rs = SQLUtil.execute(query);
        ObservableList<String> inventories = FXCollections.observableArrayList();
        while (rs.next()){
            inventories.add(rs.getString(1));
        }
        return inventories;
    }

    @Override
    public boolean updateQty(String id, Integer qty) throws SQLException {
        String query = "UPDATE inventory SET qtyOnHand = (qtyOnHand + ?) WHERE inventoryId = ?";
        return SQLUtil.execute(query,qty,id);
    }
}

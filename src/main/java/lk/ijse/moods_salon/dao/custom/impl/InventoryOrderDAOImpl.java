package lk.ijse.moods_salon.dao.custom.impl;

import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.InventoryOrderDAO;
import lk.ijse.moods_salon.entity.InventoryOrder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryOrderDAOImpl implements InventoryOrderDAO {

    @Override
    public boolean add(InventoryOrder entity) throws SQLException {
        String query = "INSERT INTO inventory_order VALUES (?,?,?,?)";
        return SQLUtil.execute(query,entity.getOrderId(),entity.getDate(),entity.getSupplierId(),entity.getUserId());
    }

    @Override
    public boolean delete(String s) throws SQLException {
        return false;
    }

    @Override
    public boolean update(InventoryOrder entity) throws SQLException {
        return false;
    }

    @Override
    public boolean exists(String s) throws SQLException {
        return false;
    }

    @Override
    public ArrayList<InventoryOrder> getAll() throws SQLException {
        return null;
    }

    @Override
    public String generateId() throws SQLException {
        String query = "SELECT orderId FROM inventory_order ORDER BY orderId DESC LIMIT 1";
        ResultSet rs = SQLUtil.execute(query);
        return rs.next() ? String.format("O-%03d", (Integer.parseInt(rs.getString(1).replace("O-", "")) + 1)) : "O-001";
    }
}

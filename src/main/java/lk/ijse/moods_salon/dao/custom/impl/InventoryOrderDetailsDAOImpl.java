package lk.ijse.moods_salon.dao.custom.impl;

import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.InventoryOrderDetailsDAO;
import lk.ijse.moods_salon.entity.InventoryOrderDetails;
import lk.ijse.moods_salon.util.CrudUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryOrderDetailsDAOImpl implements InventoryOrderDetailsDAO {
    @Override
    public boolean add(InventoryOrderDetails entity) throws SQLException {
        String query = "INSERT INTO inventory_order_details VALUES (?,?,?)";
        return SQLUtil.execute(query,entity.getOrderId(),entity.getInventoryId(),entity.getQty());
    }

    @Override
    public boolean delete(String s) throws SQLException {
        return false;
    }

    @Override
    public boolean update(InventoryOrderDetails entity) throws SQLException {
        return false;
    }

    @Override
    public boolean exists(String s) throws SQLException {
        return false;
    }

    @Override
    public ArrayList<InventoryOrderDetails> getAll() throws SQLException {
        return null;
    }
}

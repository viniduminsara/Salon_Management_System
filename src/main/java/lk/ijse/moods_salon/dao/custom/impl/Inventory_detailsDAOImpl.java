package lk.ijse.moods_salon.dao.custom.impl;

import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.Inventory_detailsDAO;
import lk.ijse.moods_salon.entity.Inventory_details;

import java.sql.SQLException;
import java.util.ArrayList;

public class Inventory_detailsDAOImpl implements Inventory_detailsDAO {
    @Override
    public boolean add(Inventory_details entity) throws SQLException {
        String query = "INSERT INTO inventory_details VALUES (?,?,?)";
        return SQLUtil.execute(query,entity.getAppointmentId(),entity.getInventoryId(),entity.getUsedQty());
    }

    @Override
    public boolean delete(String s) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Inventory_details entity) throws SQLException {
        return false;
    }

    @Override
    public boolean exists(String s) throws SQLException {
        return false;
    }

    @Override
    public ArrayList<Inventory_details> getAll() throws SQLException {
        return null;
    }
}

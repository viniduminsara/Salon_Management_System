package lk.ijse.moods_salon.dao.custom.impl;

import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.Service_detailsDAO;
import lk.ijse.moods_salon.entity.Service_details;

import java.sql.SQLException;
import java.util.ArrayList;

public class Service_detailsDAOImpl implements Service_detailsDAO {
    @Override
    public boolean add(Service_details entity) throws SQLException {
        String query = "INSERT INTO service_details VALUES (?,?)";
        return SQLUtil.execute(query,entity.getAppointmentId(),entity.getServiceId());
    }

    @Override
    public boolean delete(String s) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Service_details entity) throws SQLException {
        return false;
    }

    @Override
    public boolean exists(String s) throws SQLException {
        return false;
    }

    @Override
    public ArrayList<Service_details> getAll() throws SQLException {
        return null;
    }
}

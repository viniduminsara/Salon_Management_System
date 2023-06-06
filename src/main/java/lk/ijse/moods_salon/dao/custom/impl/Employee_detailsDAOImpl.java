package lk.ijse.moods_salon.dao.custom.impl;

import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.Employee_detailsDAO;
import lk.ijse.moods_salon.entity.Employee_details;

import java.sql.SQLException;
import java.util.ArrayList;

public class Employee_detailsDAOImpl implements Employee_detailsDAO {
    @Override
    public boolean add(Employee_details entity) throws SQLException {
        String query = "INSERT INTO employee_details VALUES (?,?)";
        return SQLUtil.execute(query,entity.getAppointmentId(),entity.getEmployeeId());
    }

    @Override
    public boolean delete(String s) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Employee_details entity) throws SQLException {
        return false;
    }

    @Override
    public boolean exists(String s) throws SQLException {
        return false;
    }

    @Override
    public ArrayList<Employee_details> getAll() throws SQLException {
        return null;
    }
}

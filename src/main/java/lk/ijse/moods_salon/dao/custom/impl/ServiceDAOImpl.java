package lk.ijse.moods_salon.dao.custom.impl;

import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.ServiceDAO;
import lk.ijse.moods_salon.entity.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceDAOImpl implements ServiceDAO {
    @Override
    public boolean add(Service entity) throws SQLException {
        String query = "INSERT INTO service VALUES (?,?,?,?)";
        return SQLUtil.execute(query, entity.getServiceId(), entity.getDescription(), entity.getPrice(), entity.getCategory());
    }

    @Override
    public boolean delete(String s) throws SQLException {
        String query = "DELETE FROM service WHERE serviceId = ?";
        return SQLUtil.execute(query,s);
    }

    @Override
    public boolean update(Service entity) throws SQLException {
        String query = "UPDATE service SET description = ?,price = ?,category = ? WHERE serviceId = ?";
        return SQLUtil.execute(query,entity.getDescription(),entity.getPrice(),entity.getCategory(),entity.getServiceId());
    }

    @Override
    public boolean exists(String s) throws SQLException {
        String query = "SELECT serviceId FROM service WHERE serviceId = ?";
        ResultSet rs = SQLUtil.execute(query,s);
        return rs.next();
    }

    @Override
    public ArrayList<Service> getAll() throws SQLException {
        String query = "SELECT serviceId,description,price,category FROM service";
        ResultSet rs = SQLUtil.execute(query);
        ArrayList<Service> services = new ArrayList<>();
        while (rs.next()){
            services.add(new Service(rs.getString(1),rs.getString(2),rs.getDouble(3),rs.getString(4)));
        }
        return services;
    }
}

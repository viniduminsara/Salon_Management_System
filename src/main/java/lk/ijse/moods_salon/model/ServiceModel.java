package lk.ijse.moods_salon.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dto.ServiceDTO;
import lk.ijse.moods_salon.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceModel {

    public static ResultSet getAll() throws SQLException {
        String query = "SELECT serviceId,description,price,category FROM service";
        return CrudUtil.execute(query);
    }

    public static boolean addService(ServiceDTO service) throws SQLException {
        String query = "INSERT INTO service VALUES (?,?,?,?)";
        return CrudUtil.execute(query, service.getServiceId(), service.getDescription(), service.getPrice(), service.getCategory());
    }

    public static boolean deleteService(String id) throws SQLException {
        String query = "DELETE FROM service WHERE serviceId = ?";
        return CrudUtil.execute(query,id);
    }

    public static boolean updateService(ServiceDTO service) throws SQLException {
        String query = "UPDATE service SET description = ?,price = ?,category = ? WHERE serviceId = ?";
        return CrudUtil.execute(query,service.getDescription(),service.getPrice(),service.getCategory(),service.getServiceId());
    }

    public static ObservableList<String> getServiceNames() throws SQLException {
        String query = "SELECT description FROM service ORDER BY serviceId";
        ResultSet rs = CrudUtil.execute(query);
        ObservableList<String> service = FXCollections.observableArrayList();
        while (rs.next()){
            service.add(rs.getString(1));
        }
        return service;
    }

    public static double findPrice(String service) throws SQLException {
        String query = "SELECT price FROM service WHERE description = ?";
        ResultSet rs = CrudUtil.execute(query,service);
        if (rs.next()){
            return Double.parseDouble(rs.getString(1));
        }
        return 0.0;
    }

    public static String getServiceId(String name) throws SQLException {
        String query = "SELECT serviceId FROM service WHERE description = ?";
        ResultSet rs = CrudUtil.execute(query,name);
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    public static boolean isExists(String id) throws SQLException {
        String query = "SELECT serviceId FROM service WHERE serviceId = ?";
        ResultSet rs = CrudUtil.execute(query,id);
        if (rs.next()){
            return true;
        }
        return false;
    }
}

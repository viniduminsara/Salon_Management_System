package lk.ijse.moods_salon.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dto.CustomerDTO;
import lk.ijse.moods_salon.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {

    public static int findTotalCustomers() throws SQLException {
        String query = "SELECT COUNT(customerId) FROM customer";
        ResultSet resultSet = CrudUtil.execute(query);
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static ResultSet getAllCustomers() throws SQLException {
        String query = "SELECT customerId,name,address,contact,gmail FROM customer";
        return CrudUtil.execute(query);
    }

    public static boolean addCustomer(CustomerDTO customer) throws SQLException {
        String query = "INSERT INTO customer VALUES (?,?,?,?,?)";
        return CrudUtil.execute(query,customer.getCustomerId(),customer.getName(),customer.getGmail(),customer.getContact(),customer.getAddress());
    }

    public static boolean deleteCustomer(String id) throws SQLException {
        String query ="DELETE FROM customer WHERE customerId = ?";
        return CrudUtil.execute(query,id);
    }

    public static boolean updateCustomer(CustomerDTO customer) throws SQLException {
        String query = "UPDATE customer SET name = ?, gmail = ?, contact = ?, address = ? WHERE customerId = ?";
        return CrudUtil.execute(query,customer.getName(),customer.getGmail(),customer.getContact(),customer.getAddress(),customer.getCustomerId());
    }

        public static ObservableList<String> getAllCustomerId() throws SQLException {
        String query = "SELECT customerId FROM customer ORDER BY customerId";
        ResultSet rs = CrudUtil.execute(query);
        ObservableList<String> customer = FXCollections.observableArrayList();
        while (rs.next()){
            customer.add(rs.getString(1));
        }
        return customer;
    }

    public static String getCustomerName(String id) throws SQLException {
        String query = "SELECT name FROM customer WHERE customerId = ?";
        ResultSet rs = CrudUtil.execute(query,id);
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    public static boolean findExists(String id) throws SQLException {
        String query = "SELECT customerId FROM customer";
        ResultSet rs = CrudUtil.execute(query);
        while (rs.next()){
            if (rs.getString(1).equals(id)){
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> getEmails() throws SQLException {
        String query = "SELECT gmail FROM customer ORDER BY customerId";
        ResultSet rs = CrudUtil.execute(query);
        ArrayList<String> email = new ArrayList<>();
        while (rs.next()){
            email.add(rs.getString(1));
        }
        return email;
    }

    public static String getEmail(String customerId) throws SQLException {
        String query = "SELECT gmail FROM customer WHERE customerId = ?";
        ResultSet rs = CrudUtil.execute(query,customerId);
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }
}

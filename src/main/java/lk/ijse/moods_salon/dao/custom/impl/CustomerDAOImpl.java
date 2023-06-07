package lk.ijse.moods_salon.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.CustomerDAO;
import lk.ijse.moods_salon.entity.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public boolean add(Customer entity) throws SQLException {
        String query = "INSERT INTO customer VALUES (?,?,?,?,?)";
        return SQLUtil.execute(query,entity.getCustomerId(),entity.getName(),entity.getGmail(),entity.getContact(),entity.getAddress());
    }

    @Override
    public boolean delete(String s) throws SQLException {
        String query ="DELETE FROM customer WHERE customerId = ?";
        return SQLUtil.execute(query,s);
    }

    @Override
    public boolean update(Customer entity) throws SQLException {
        String query = "UPDATE customer SET name = ?, gmail = ?, contact = ?, address = ? WHERE customerId = ?";
        return SQLUtil.execute(query,entity.getName(),entity.getGmail(),entity.getContact(),entity.getAddress(),entity.getCustomerId());
    }

    @Override
    public boolean exists(String s) throws SQLException {
        String query = "SELECT customerId FROM customer WHERE customerId = ?";
        ResultSet rs = SQLUtil.execute(query,s);
        return rs.next();
    }

    @Override
    public ArrayList<Customer> getAll() throws SQLException {
        String query = "SELECT customerId,name,address,contact,gmail FROM customer";
        ResultSet rs =  SQLUtil.execute(query);
        ArrayList<Customer> customers = new ArrayList<>();
        while (rs.next()){
            customers.add(new Customer(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));
        }
        return customers;
    }

    @Override
    public ArrayList<String> getEmails() throws SQLException {
        String query = "SELECT gmail FROM customer ORDER BY customerId";
        ResultSet rs = SQLUtil.execute(query);
        ArrayList<String> email = new ArrayList<>();
        while (rs.next()){
            email.add(rs.getString(1));
        }
        return email;
    }

    @Override
    public String getName(String id) throws SQLException {
        String query = "SELECT name FROM customer WHERE customerId = ?";
        ResultSet rs = SQLUtil.execute(query,id);
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    @Override
    public String getEmail(String id) throws SQLException {
        String query = "SELECT gmail FROM customer WHERE customerId = ?";
        ResultSet rs = SQLUtil.execute(query,id);
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    @Override
    public ObservableList<String> getIds() throws SQLException {
        String query = "SELECT customerId FROM customer ORDER BY customerId";
        ResultSet rs = SQLUtil.execute(query);
        ObservableList<String> customer = FXCollections.observableArrayList();
        while (rs.next()){
            customer.add(rs.getString(1));
        }
        return customer;
    }

    @Override
    public String getTotal() throws SQLException {
        String query = "SELECT COUNT(customerId) FROM customer";
        ResultSet resultSet = SQLUtil.execute(query);
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }
}

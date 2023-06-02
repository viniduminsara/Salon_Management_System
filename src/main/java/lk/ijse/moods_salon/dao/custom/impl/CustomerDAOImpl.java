package lk.ijse.moods_salon.dao.custom.impl;

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
}

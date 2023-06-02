package lk.ijse.moods_salon.bo.custom.impl;

import lk.ijse.moods_salon.bo.custom.CustomerBO;
import lk.ijse.moods_salon.dao.custom.CustomerDAO;
import lk.ijse.moods_salon.dao.custom.impl.CustomerDAOImpl;
import lk.ijse.moods_salon.dto.CustomerDTO;
import lk.ijse.moods_salon.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBOImpl implements CustomerBO {

    CustomerDAO customerDAO = new CustomerDAOImpl();

    @Override
    public boolean existsCustomer(String id) throws SQLException {
        return customerDAO.exists(id);
    }

    @Override
    public boolean addCustomer(CustomerDTO dto) throws SQLException {
        return customerDAO.add(new Customer(dto.getCustomerId(),dto.getName(),dto.getGmail(),dto.getContact(),dto.getAddress()));
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException {
        return customerDAO.delete(id);
    }

    @Override
    public boolean updateCustomer(CustomerDTO dto) throws SQLException {
        return customerDAO.update(new Customer(dto.getCustomerId(),dto.getName(),dto.getGmail(),dto.getContact(),dto.getAddress()));
    }

    @Override
    public ArrayList<String> getEmails() throws SQLException {
        return customerDAO.getEmails();
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException {
        ArrayList<Customer> all = customerDAO.getAll();
        ArrayList<CustomerDTO> customers = new ArrayList<>();
        for (Customer customer : all) {
            customers.add(new CustomerDTO(customer.getCustomerId(),customer.getName(),customer.getGmail(),customer.getContact(),customer.getAddress()));
        }
        return customers;
    }
}

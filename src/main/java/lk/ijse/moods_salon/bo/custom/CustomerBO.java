package lk.ijse.moods_salon.bo.custom;

import lk.ijse.moods_salon.bo.SuperBO;
import lk.ijse.moods_salon.dto.CustomerDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerBO extends SuperBO {

    boolean existsCustomer(String id) throws SQLException;

    boolean addCustomer(CustomerDTO dto) throws SQLException;

    boolean deleteCustomer(String id) throws SQLException;

    boolean updateCustomer(CustomerDTO dto) throws SQLException;

    ArrayList<String> getEmails() throws SQLException;

    ArrayList<CustomerDTO> getAllCustomers() throws SQLException;

}

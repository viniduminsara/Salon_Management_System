package lk.ijse.moods_salon.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dao.CrudDAO;
import lk.ijse.moods_salon.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerDAO extends CrudDAO<Customer,String> {

    ArrayList<String> getEmails() throws SQLException;

    String getName(String id) throws SQLException;

    String getEmail(String id) throws SQLException;

    ObservableList<String> getIds() throws SQLException;

}

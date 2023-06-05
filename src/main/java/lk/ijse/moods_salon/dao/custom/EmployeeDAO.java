package lk.ijse.moods_salon.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dao.CrudDAO;
import lk.ijse.moods_salon.entity.Employee;
import java.sql.SQLException;

public interface EmployeeDAO extends CrudDAO<Employee,String> {

    String getId(String name) throws SQLException;

    String getName(String id) throws SQLException;

    ObservableList<String> getAllNames() throws SQLException;

}

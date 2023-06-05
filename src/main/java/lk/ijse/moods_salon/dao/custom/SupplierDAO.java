package lk.ijse.moods_salon.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dao.CrudDAO;
import lk.ijse.moods_salon.entity.Supplier;

import java.sql.SQLException;

public interface SupplierDAO extends CrudDAO<Supplier,String> {

    String getName(String id) throws SQLException;

    ObservableList<String> getIds() throws SQLException;

}

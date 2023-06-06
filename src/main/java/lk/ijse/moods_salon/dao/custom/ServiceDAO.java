package lk.ijse.moods_salon.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dao.CrudDAO;
import lk.ijse.moods_salon.entity.Service;

import java.sql.SQLException;

public interface ServiceDAO extends CrudDAO<Service,String> {

    Double getPrice(String name) throws SQLException;

    String getId(String name) throws SQLException;

    ObservableList<String> getNames() throws SQLException;

}

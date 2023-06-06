package lk.ijse.moods_salon.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dao.CrudDAO;
import lk.ijse.moods_salon.entity.Inventory;

import java.sql.SQLException;

public interface InventoryDAO extends CrudDAO<Inventory,String> {

    Inventory get(String id) throws SQLException;

    ObservableList<String> getIds() throws SQLException;

    boolean updateQty(String id,Integer qty) throws SQLException;

    String getId(String name) throws SQLException;

    boolean updateUsedQty(String id,Integer qty) throws SQLException;

    ObservableList<String> getNames() throws SQLException;

}

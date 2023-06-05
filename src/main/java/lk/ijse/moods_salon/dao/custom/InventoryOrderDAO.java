package lk.ijse.moods_salon.dao.custom;

import lk.ijse.moods_salon.dao.CrudDAO;
import lk.ijse.moods_salon.entity.InventoryOrder;

import java.sql.SQLException;

public interface InventoryOrderDAO extends CrudDAO<InventoryOrder,String> {

    String generateId() throws SQLException;

}

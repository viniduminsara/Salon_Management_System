package lk.ijse.moods_salon.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dto.tm.AttendanceTM;
import lk.ijse.moods_salon.dto.tm.InventoryOrderTM;

import java.sql.SQLException;

public interface QueryDAO {

    ObservableList<AttendanceTM> get(String date) throws SQLException;

    ObservableList<InventoryOrderTM> getAllOrders() throws SQLException;

}

package lk.ijse.gdse.model;

import lk.ijse.gdse.dto.AppointmentInventory;
import lk.ijse.gdse.util.CrudUtil;

import java.sql.SQLException;
import java.util.List;

public class InventoryDetailsModel {
    public static boolean saveInventoryDetails(String appointmentId, List<AppointmentInventory> inventoryList) throws SQLException {
        for (AppointmentInventory item : inventoryList) {
            if (!save(appointmentId,item)){
                return false;
            }
        }
        return true;
    }

    private static boolean save(String appointmentId, AppointmentInventory item) throws SQLException {
        String query = "INSERT INTO inventory_details VALUES (?,?,?)";
        return CrudUtil.execute(query,appointmentId,item.getInventoryId(),item.getUsedQty());
    }
}

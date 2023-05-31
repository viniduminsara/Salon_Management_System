package lk.ijse.moods_salon.model;

import lk.ijse.moods_salon.dto.AppointmentInventoryDTO;
import lk.ijse.moods_salon.util.CrudUtil;

import java.sql.SQLException;
import java.util.List;

public class InventoryDetailsModel {
    public static boolean saveInventoryDetails(String appointmentId, List<AppointmentInventoryDTO> inventoryList) throws SQLException {
        for (AppointmentInventoryDTO item : inventoryList) {
            if (!save(appointmentId,item)){
                return false;
            }
        }
        return true;
    }

    private static boolean save(String appointmentId, AppointmentInventoryDTO item) throws SQLException {
        String query = "INSERT INTO inventory_details VALUES (?,?,?)";
        return CrudUtil.execute(query,appointmentId,item.getInventoryId(),item.getUsedQty());
    }
}

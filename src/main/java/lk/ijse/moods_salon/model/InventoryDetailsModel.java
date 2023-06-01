package lk.ijse.moods_salon.model;

import lk.ijse.moods_salon.dto.Inventory_detailsDTO;
import lk.ijse.moods_salon.util.CrudUtil;

import java.sql.SQLException;
import java.util.List;

public class InventoryDetailsModel {
    public static boolean saveInventoryDetails(String appointmentId, List<Inventory_detailsDTO> inventoryList) throws SQLException {
        for (Inventory_detailsDTO item : inventoryList) {
            if (!save(appointmentId,item)){
                return false;
            }
        }
        return true;
    }

    private static boolean save(String appointmentId, Inventory_detailsDTO item) throws SQLException {
        String query = "INSERT INTO inventory_details VALUES (?,?,?)";
        return CrudUtil.execute(query,appointmentId,item.getInventoryId(),item.getUsedQty());
    }
}
